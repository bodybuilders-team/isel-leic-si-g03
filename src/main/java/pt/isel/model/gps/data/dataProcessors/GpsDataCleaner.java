package pt.isel.model.gps.data.dataProcessors;

import java.util.TimerTask;

import static pt.isel.dal.PersistenceManager.getEntityManager;

/**
 * Erases existing invalid gps data lasting longer than 15 days.
 * Can be scheduled to run periodically.
 */
public class GpsDataCleaner extends TimerTask {

    @Override
    public void run() {
        getEntityManager()
                .createStoredProcedureQuery("clear_invalid_gps_data")
                .execute();
    }
}
