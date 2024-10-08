package pt.isel.dataProcessors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.RollbackException;
import org.eclipse.persistence.sessions.DatabaseLogin;
import pt.isel.dal.Mapper;
import pt.isel.model.gps.data.GpsData;
import pt.isel.model.gps.data.InvalidGpsData;
import pt.isel.model.gps.data.UnprocessedGpsData;
import pt.isel.model.gps.device.GpsDevice;

import java.util.List;
import java.util.Optional;
import java.util.TimerTask;

import static pt.isel.dal.PersistenceManager.executeWithIsolationLevel;

/**
 * Processes the GPS data, using optimistic locking.
 * Can be scheduled to run periodically.
 */
public class OptimisticGpsDataProcessor extends TimerTask {

    @Override
    public void run() {
        int nreps = 2; // Number of times to repeat the process before giving up

        int initialNreps = nreps;
        do {
            try {
                System.out.println("Thread " + Thread.currentThread().getName() +
                        " trying to process gps data, nreps executed = " + (initialNreps - nreps));
                --nreps;

                // Isolation level needed because of the gps_data trigger.
                executeWithIsolationLevel(DatabaseLogin.TRANSACTION_REPEATABLE_READ, this::processGpsData);
                nreps = 0;
            } catch (RollbackException | OptimisticLockException e) {
                if (e.getCause() instanceof OptimisticLockException || e instanceof OptimisticLockException) {
                    if (nreps == 0)
                        throw new RuntimeException("Gps Data processor concurrency error");
                } else
                    throw e;
            }
        } while (nreps > 0);
    }


    /**
     * Processes the GPS data.
     *
     * @param em the entity manager
     */
    void processGpsData(EntityManager em) {

        // Get unprocessed GPS data
        List<UnprocessedGpsData> unprocessedGpsDataList = em
                .createQuery("SELECT entity FROM UnprocessedGpsData entity", UnprocessedGpsData.class)
                .setLockMode(LockModeType.OPTIMISTIC)
                .getResultList();

        // Iterate over the unprocessed GPS data
        for (UnprocessedGpsData unprocessedGpsData : unprocessedGpsDataList) {
            Mapper<GpsDevice> gpsDeviceMapper = new Mapper<>(em) {};
            Optional<GpsDevice> oGpsDevice = gpsDeviceMapper.read(unprocessedGpsData.getGpsDeviceId());

            // If the GPS device is found, the date is valid
            if (oGpsDevice.isPresent()) {
                GpsData gpsData = new GpsData(
                        oGpsDevice.get(),
                        unprocessedGpsData.getTimestamp(),
                        unprocessedGpsData.getLocation()
                );

                Mapper<GpsData> mapper = new Mapper<>(em) {};
                mapper.create(gpsData);

                em.persist(gpsData);
            }
            // If the GPS device is not found, the date is invalid
            else {
                Mapper<InvalidGpsData> invalidGpsDataMapper = new Mapper<>(em) {};
                InvalidGpsData invalidGpsData = new InvalidGpsData(
                        unprocessedGpsData.getGpsDeviceId(),
                        unprocessedGpsData.getTimestamp(),
                        unprocessedGpsData.getLocation()
                );

                invalidGpsDataMapper.create(invalidGpsData);
            }

            em.remove(unprocessedGpsData);
        }
    }
}
