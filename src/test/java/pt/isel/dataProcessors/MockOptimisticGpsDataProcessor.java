package pt.isel.dataProcessors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.RollbackException;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.persistence.sessions.DatabaseLogin;
import pt.isel.dal.Mapper;
import pt.isel.model.gps.data.GpsData;
import pt.isel.model.gps.data.InvalidGpsData;
import pt.isel.model.gps.data.UnprocessedGpsData;
import pt.isel.model.gps.device.GpsDevice;


import static pt.isel.dal.PersistenceManager.executeWithIsolationLevel;

public class MockOptimisticGpsDataProcessor extends TimerTask {
    @Override
    public void run() {
        int nreps = 2;
        do {
            try {
                --nreps;
                // Isolation level needed because of the gps_data trigger.
                executeWithIsolationLevel(DatabaseLogin.TRANSACTION_REPEATABLE_READ,
                        this::processGpsData);
            } catch (RollbackException | OptimisticLockException e) {
                if (e.getCause() instanceof OptimisticLockException ||
                        e instanceof OptimisticLockException) {
                    if (nreps == 0)
                        throw new RuntimeException("Gps Data processor concurrency error");
                } else throw e;
            }
        } while (nreps > 0);
    }

    void processGpsData(EntityManager em) {
        List<UnprocessedGpsData> unprocessedGpsDataList = em
                .createQuery("SELECT entity FROM UnprocessedGpsData entity", UnprocessedGpsData.class)
                .setLockMode(LockModeType.OPTIMISTIC)
                .getResultList();

        for (UnprocessedGpsData unprocessedGpsData : unprocessedGpsDataList) {
            Mapper<GpsDevice> gpsDeviceMapper = new Mapper<>(em) {};

            Optional<GpsDevice> oGpsDevice = gpsDeviceMapper.read(unprocessedGpsData.getGpsDeviceId());

            if (oGpsDevice.isPresent()) {
                GpsData gpsData = new GpsData(oGpsDevice.get(), unprocessedGpsData.getTimestamp(),
                        unprocessedGpsData.getLocation());
                Mapper<GpsData> mapper = new Mapper<>(em) {};
                mapper.create(gpsData);

                em.persist(gpsData);
            } else {
                Mapper<InvalidGpsData> invalidGpsDataMapper = new Mapper<>(em) {};
                InvalidGpsData invalidGpsData = new InvalidGpsData(unprocessedGpsData.getGpsDeviceId(),
                        unprocessedGpsData.getTimestamp(), unprocessedGpsData.getLocation());

                invalidGpsDataMapper.create(invalidGpsData);
            }

            em.remove(unprocessedGpsData);
        }
    }

}