package pt.isel.model.gps.data.dataProcessors;

import java.util.TimerTask;


import static pt.isel.dal.PersistenceManager.execute;

/**
 * Processes the GPS data.
 * Can be scheduled to run periodically.
 */
public class GpsDataProcessor extends TimerTask {

    @Override
    public void run() {
        execute((em) ->
                em.createNativeQuery("call process_gps_data()")
                        .executeUpdate()
        );
    }
}
