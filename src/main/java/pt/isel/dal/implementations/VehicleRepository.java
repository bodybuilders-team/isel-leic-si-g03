package pt.isel.dal.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.ParameterMode;
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
    public boolean createVehicle(Vehicle v, GreenZone gz) {
        return PersistenceManager.executeWithIsolationLevel(DatabaseLogin.TRANSACTION_SERIALIZABLE, (em) -> {
            StoredProcedureQuery query = getCreateVehicleStoredProcedure(em);

            query.setParameter(1, v.getGpsDevice().getId());
            query.setParameter(2, v.getClient().getId());
            query.setParameter(3, v.getLicensePlate());
            query.setParameter(4, gz.getCenterLocation().getX());
            query.setParameter(5, gz.getCenterLocation().getY());
            query.setParameter(6, gz.getRadius());

            query.executeUpdate();

            return (boolean) query.getOutputParameterValue(7);
        });
    }

    private StoredProcedureQuery getCreateVehicleStoredProcedure(EntityManager em) {
        StoredProcedureQuery query = em
                .createStoredProcedureQuery("create_vehicle");
        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(4, Float.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(5, Float.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(6, Double.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(7, Boolean.class, ParameterMode.OUT);
        return query;
    }

    /**
     * Creates a vehicle with the respective associated equipment information,
     * and associates it with a customer.
     *
     * @param v Vehicle to be created
     */
    public boolean createVehicle(Vehicle v) {
        return PersistenceManager.executeWithIsolationLevel(DatabaseLogin.TRANSACTION_SERIALIZABLE, (em) -> {
            StoredProcedureQuery query = getCreateVehicleStoredProcedure(em);

            query.setParameter(1, v.getGpsDevice().getId());
            query.setParameter(2, v.getClient().getId());
            query.setParameter(3, v.getLicensePlate());
            query.setParameter(4, null);
            query.setParameter(5, null);
            query.setParameter(6, null);

            query.executeUpdate();
            return (boolean) query.getOutputParameterValue(7);
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
    public boolean nativeCreateVehicle(Vehicle v, GreenZone gz) {
        em.lock(v.getClient(), LockModeType.PESSIMISTIC_READ);

        // Get number of client cars
        int clientCarsCount = v.getClient().getVehicles().size();

        // If the client is institutional or has less than 3 cars, create the vehicle and the green zone
        if (clientCarsCount < Client.maxVehicles || v.getClient() instanceof InstitutionalClient) {
            Mapper<Vehicle> vehicleMapper = new Mapper<>(em) {};
            vehicleMapper.create(v);

            Mapper<GreenZone> greenZoneMapper = new Mapper<>(em) {};
            greenZoneMapper.create(gz);
            return true;
        }

        return false;
    }

    /**
     * Creates a vehicle with the respective associated equipment information,
     * and associates it with a customer.
     *
     * @param v Vehicle to be created
     */
    public boolean nativeCreateVehicle(Vehicle v) {
        em.lock(v.getClient(), LockModeType.PESSIMISTIC_READ);

        // Get number of client cars
        int clientCarsCount = v.getClient().getVehicles().size();

        // If the client is institutional or has less than 3 cars, create the vehicle and the green zone
        if (clientCarsCount < Client.maxVehicles || v.getClient() instanceof InstitutionalClient) {
            Mapper<Vehicle> vehicleMapper = new Mapper<>(em) {};
            vehicleMapper.create(v);
            return true;
        }

        return false;
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
        query.registerStoredProcedureParameter(2, Integer.class, jakarta.persistence.ParameterMode.OUT);

        query.setParameter(1, year);

        query.execute();

        return (int) query.getOutputParameterValue(2);
    }
}
