package pt.isel.dal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import pt.isel.Utils;
import pt.isel.model.GreenZone;
import pt.isel.model.Vehicle;
import pt.isel.model.clients.PrivateClient;

import static pt.isel.dal.PersistenceManager.getEntityManager;

/**
 * Repository for Vehicle.
 */
public class VehicleRepository extends Repository<PrivateClient> {

    /**
     * Creates a vehicle with the respective associated equipment information,
     * and associates it with a customer.
     * <p>
     * If data is passed to the creation of a green zone, creates it and
     * associates the vehicle with that zone.
     *
     * @param v  Vehicle to be created
     * @param gz GreenZone to be created (optional)
     */
    public void createVehicle(Vehicle v, GreenZone gz) {
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery(
                "CALL create_vehicle(?::INTEGER,?::INTEGER,?,?::INTEGER,?::POINT,?::DOUBLE)");

        query.setParameter(1, v.getGpsDevice().getId());
        query.setParameter(2, v.getClient().getId());
        query.setParameter(3, v.getLicensePlate());
        query.setParameter(4, v.getNumAlarms());
        query.setParameter(5, Utils.parsePoint(gz.getCenterLocation()));
        query.setParameter(6, gz.getRadius());

        query.executeUpdate();
    }
}
