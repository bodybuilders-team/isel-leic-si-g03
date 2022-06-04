package pt.isel.presentation;

import java.util.Timer;
import java.util.TimerTask;
import pt.isel.GpsDataProcessor;
import pt.isel.dal.PersistenceManager;
import pt.isel.dal.implementations.clients.PrivateClientRepository;
import pt.isel.model.clients.PrivateClient;


/**
 * SI Project - 2021/22 Summer Semmester - LEIC41D - Group 03
 *
 * @author 48089 André Páscoa
 * @author 48280 André Jesus
 * @author 48287 Nyckollas Brandão
 */
public class App {

    /**
     * Application entry point.
     */
    public static void main(String[] args) {
        Timer processGpsDataTimer = new Timer("Process GPS Data");
        TimerTask processGpsDataTask = new GpsDataProcessor();
        processGpsDataTimer.schedule(processGpsDataTask, 0, 5*60*1000);

        PersistenceManager.execute((em) -> {
            PrivateClientRepository privateClientRepository = new PrivateClientRepository(em);

            privateClientRepository.deleteAll();

            PrivateClient privateClient = new PrivateClient(
                    "Joao",
                    "91234",
                    "123456789",
                    "Rua da casa",
                    true,
                    "123456789"
            );

            privateClientRepository.add(privateClient);

            PrivateClient client = privateClientRepository.getByName("Joao").get();

        });


    }
}
