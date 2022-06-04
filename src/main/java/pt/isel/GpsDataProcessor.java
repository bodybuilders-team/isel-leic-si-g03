package pt.isel;

import java.util.TimerTask;


import static pt.isel.dal.PersistenceManager.getEntityManager;

public class GpsDataProcessor extends TimerTask {

    @Override
    public void run() {
        getEntityManager()
                .createStoredProcedureQuery("process_gps_data")
                .execute();
    }
}
