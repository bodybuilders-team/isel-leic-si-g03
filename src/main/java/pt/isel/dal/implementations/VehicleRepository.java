package pt.isel.dal.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;

import org.eclipse.persistence.sessions.DatabaseLogin;
import pt.isel.dal.Mapper;
import pt.isel.dal.PersistenceManager;
import pt.isel.dal.Repository;
import pt.isel.model.GreenZone;
import pt.isel.model.Vehicle;
import pt.isel.model.clients.Client;
import pt.isel.model.clients.InstitutionalClient;


/**
 * Repository for Vehicle.
 */
public class VehicleRepository extends Repository<Vehicle> {

    public VehicleRepository(EntityManager em) {
        super(em);
    }

    /**
     * Creates a vehicle with the respective associated equipment information,
     * and associates it with a customer.
     * <p>
     * If data is passed to the creation of a green zone, creates it and
     * associates the vehicle with that zone.
     *
     * @param v  Vehicle to be created
     * @param gz GreenZone to be created
     */
    public void createVehicle(Vehicle v, GreenZone gz) {
        PersistenceManager.executeWithIsolationLevel(DatabaseLogin.TRANSACTION_SERIALIZABLE, (em) -> {

            Query query = em
                    .createNativeQuery("CALL create_vehicle(?::INTEGER,?::INTEGER,?,?::INTEGER,?::POINT,?::DOUBLE)");

            query.setParameter(1, v.getGpsDevice().getId());
            query.setParameter(2, v.getClient().getId());
            query.setParameter(3, v.getLicensePlate());
            query.setParameter(4, v.getNumAlarms());
            query.setParameter(5, gz.getCenterLocation().getX());
            query.setParameter(6, gz.getCenterLocation().getY());
            query.setParameter(7, gz.getRadius());

            query.executeUpdate();
        });
    }

    /**
     * Creates a vehicle with the respective associated equipment information,
     * and associates it with a customer.
     *
     * @param v Vehicle to be created
     */
    public void createVehicle(Vehicle v) {
        PersistenceManager.executeWithIsolationLevel(DatabaseLogin.TRANSACTION_SERIALIZABLE, (em) -> {

            Query query = em
                    .createNativeQuery("CALL create_vehicle(?::INTEGER,?::INTEGER,?,?::INTEGER,?::POINT,?::DOUBLE)");

            query.setParameter(1, v.getGpsDevice().getId());
            query.setParameter(2, v.getClient().getId());
            query.setParameter(3, v.getLicensePlate());
            query.setParameter(4, v.getNumAlarms());
            query.setParameter(5, null);
            query.setParameter(6, null);
            query.setParameter(7, null);

            query.executeUpdate();
        });
    }


    /**
     * Creates a vehicle with the respective associated equipment information,
     * and associates it with a customer.
     * <p>
     * Creates a green zone with the given data, and associates the vehicle with.
     *
     * @param v  Vehicle to be created
     * @param gz GreenZone to be created (optional)
     */
    public void nativeCreateVehicle(Vehicle v, GreenZone gz) {
        em.lock(v.getClient(), LockModeType.PESSIMISTIC_READ);

        // Get number of client cars
        int clientCarsCount = v.getClient().getVehicles().size();

        // If the client is institutional or has less than 3 cars, create the vehicle and the green zone
        if (clientCarsCount < Client.maxVehicles || v.getClient() instanceof InstitutionalClient) {
            Mapper<Vehicle> vehicleMapper = new Mapper<>(em) {};
            vehicleMapper.create(v);

            Mapper<GreenZone> greenZoneMapper = new Mapper<>(em) {};
            greenZoneMapper.create(gz);
        }
    }

    /**
     * Creates a vehicle with the respective associated equipment information,
     * and associates it with a customer.
     *
     * @param v Vehicle to be created
     */
    public void nativeCreateVehicle(Vehicle v) {
        em.lock(v.getClient(), LockModeType.PESSIMISTIC_READ);

        // Get number of client cars
        int clientCarsCount = v.getClient().getVehicles().size();

        // If the client is institutional or has less than 3 cars, create the vehicle and the green zone
        if (clientCarsCount < Client.maxVehicles || v.getClient() instanceof InstitutionalClient) {
            Mapper<Vehicle> vehicleMapper = new Mapper<>(em) {};
            vehicleMapper.create(v);
        }
    }

    /**
     * Returns the total number of alarms for all vehicles of the given year.
     *
     * @param year Year of the alarms
     * @return Total number of alarms
     */
    public int getAlarmsCount(int year) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("get_alarms_count");
        query.registerStoredProcedureParameter(1, Integer.class, jakarta.persistence.ParameterMode.IN);

        query.setParameter(1, year);

        return (int) query.getSingleResult();
    }
}
