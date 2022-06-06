package pt.isel.dal.implementations;

import jakarta.persistence.EntityManager;
import pt.isel.dal.Repository;
import pt.isel.model.AlarmData;

public class AlarmDataRepository extends Repository<AlarmData> {

    public AlarmDataRepository(EntityManager em) {
        super(em);
    }

    @Override
    public void add(AlarmData entity) {
        em.createNativeQuery("INSERT INTO list_alarms (license_plate, lat, lon, timestamp) VALUES(?,?,?,?)")
                .setParameter(1, entity.getLicensePlate())
                .setParameter(2, entity.getLocation().getX())
                .setParameter(3, entity.getLocation().getY())
                .setParameter(4, entity.getTimestamp())
                .executeUpdate();
    }
}
