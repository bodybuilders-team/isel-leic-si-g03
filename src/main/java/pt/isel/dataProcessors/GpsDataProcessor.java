package pt.isel.dataProcessors;

import java.util.TimerTask;
import org.eclipse.persistence.sessions.DatabaseLogin;


import static pt.isel.dal.PersistenceManager.executeWithIsolationLevel;

/**
 * Processes the GPS data.
 * Can be scheduled to run periodically.
 */
public class GpsDataProcessor extends TimerTask {

    @Override
    public void run() {
        executeWithIsolationLevel(DatabaseLogin.TRANSACTION_REPEATABLE_READ, (em) -> {
            em.createNativeQuery("call process_gps_data()").executeUpdate();
        });
    }


}
