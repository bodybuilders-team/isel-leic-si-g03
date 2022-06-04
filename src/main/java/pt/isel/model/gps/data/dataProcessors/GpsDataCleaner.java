package pt.isel.model.gps.data.dataProcessors;

import java.sql.Connection;
import java.util.TimerTask;
import org.eclipse.persistence.sessions.DatabaseLogin;


import static pt.isel.dal.PersistenceManager.executeWithIsolationLevel;

/**
 * Erases existing invalid gps data lasting longer than 15 days.
 * Can be scheduled to run periodically.
 */
public class GpsDataCleaner extends TimerTask {

    @Override
    public void run() {
        executeWithIsolationLevel(DatabaseLogin.TRANSACTION_REPEATABLE_READ,
                (em) -> em.createNativeQuery("call clear_invalid_gps_data()")
                        .executeUpdate());
    }
}
