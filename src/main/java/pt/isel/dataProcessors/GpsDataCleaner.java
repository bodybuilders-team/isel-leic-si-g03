package pt.isel.dataProcessors;

import java.util.TimerTask;


import static pt.isel.dal.PersistenceManager.execute;

/**
 * Erases existing invalid gps data lasting longer than 15 days.
 * Can be scheduled to run periodically.
 */
public class GpsDataCleaner extends TimerTask {

    @Override
    public void run() {
        execute((em) -> {
                    em.createNativeQuery("call clear_invalid_gps_data()")
                            .executeUpdate();
                }
        );
    }
}
