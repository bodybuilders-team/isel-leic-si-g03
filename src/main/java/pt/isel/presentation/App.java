package pt.isel.presentation;

import java.util.Optional;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import jakarta.persistence.EntityManager;
import pt.isel.dal.PersistenceManager;
import pt.isel.dal.Repository;
import pt.isel.dal.implementations.clients.PrivateClientRepository;
import pt.isel.model.clients.Client;
import pt.isel.model.clients.PrivateClient;
import pt.isel.model.gps.data.dataProcessors.GpsDataCleaner;
import pt.isel.model.gps.data.dataProcessors.GpsDataProcessor;


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
        processGpsDataTimer.schedule(processGpsDataTask, 0, 5 * 60 * 1000); // TODO: Magic constants?

        Timer cleanGpsDataTimer = new Timer("Clean Invalid GPS Data");
        TimerTask cleanGpsDataTask = new GpsDataCleaner();
        cleanGpsDataTimer.schedule(cleanGpsDataTask, 0, 5 * 60 * 1000); // TODO: Magic constants?

        // App main loop
        do {
            Operation userInput = displayMenu();
            if (userInput == Operation.Exit)
                break;

            if (userInput != null) {
                PersistenceManager.execute((em) -> {
                    executeOperation(userInput, em);
                });
            } else
                System.out.println("Invalid Operation.");

            System.out.println("Press ENTER to continue...");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();

        } while (true);
    }

    /**
     * Available app operations.
     */
    private enum Operation {
        Exit,
        CreatePrivateClient,
        UpdatePrivateClient,
        DeletePrivateClient,
        DisableClient,
        CreateVehicle,
        GetAlarmsCount,
        ListAlarmData,
        InsertAlarmData
    }

    /**
     * Displays the app menu and asks the user to select an operation.
     *
     * @return selected operation or null if its invalid
     */
    private static Operation displayMenu() {
        // Print menu
        System.out.println("<------------------ Available Operations ------------------>\n");
        System.out.println(" 1. Exit");
        System.out.println(" 2. Create Private Client");
        System.out.println(" 3. Update Private Client");
        System.out.println(" 4. Delete Private Client");
        System.out.println(" 5. Disable Client");
        System.out.println(" 6. Create Vehicle");
        System.out.println(" 7. Get Alarms Count");
        System.out.println(" 8. List Alarm Data");
        System.out.println(" 9. Insert Alarm Data");
        System.out.println("\n<------------------------------------------------------->\n");
        System.out.print("> ");

        // Get user input
        Scanner scanner = new Scanner(System.in);
        try {
            int result = scanner.nextInt();
            return Operation.values()[result - 1];
        } catch (Exception err) {
            return null;
        }
    }

    /**
     * Executes the selected operation.
     *
     * @param oper selected operation
     * @param em   entity manager
     */
    private static void executeOperation(Operation oper, EntityManager em) {
        switch (oper) {
            case CreatePrivateClient -> createPrivateClient(em);
            case UpdatePrivateClient -> updatePrivateClient(em);
            case DeletePrivateClient -> deletePrivateClient(em);
            case DisableClient -> disableClient(em);
            case CreateVehicle -> createVehicle(em);
            case GetAlarmsCount -> getAlarmsCount(em);
            case ListAlarmData -> listAlarmData(em);
            case InsertAlarmData -> insertAlarmData(em);
            default -> System.out.println("Invalid Operation.");
        }
    }

    /**
     * Creates a new private client.
     *
     * @param em entity manager
     */
    private static void createPrivateClient(EntityManager em) {
        System.out.println("\n<------------------ Create Private Client ------------------>\n");
        PrivateClient privateClient = requestPrivateClient();

        PrivateClientRepository privateClientRepository = new PrivateClientRepository(em);
        privateClientRepository.add(privateClient);
    }

    /**
     * Updates an existing private client.
     *
     * @param em entity manager
     */
    private static void updatePrivateClient(EntityManager em) {
        System.out.println("\n<------------------ Update Private Client ------------------>\n");

        PrivateClientRepository privateClientRepository = new PrivateClientRepository(em);

        String clientNif = requestString("Client NIF: ");
        Optional<PrivateClient> privateClient =
                privateClientRepository.get((client) -> client.getNif().equals(clientNif));

        if (privateClient.isPresent()) {
            System.out.println("Introduce the new data for the client:");
            PrivateClient updatedClient = requestPrivateClient();
            privateClientRepository.update(updatedClient);
        } else
            System.out.println("Client not found.");
    }

    /**
     * Deletes an existing private client.
     *
     * @param em entity manager
     */
    private static void deletePrivateClient(EntityManager em) {
        System.out.println("\n<------------------ Delete Private Client ------------------>\n");

        PrivateClientRepository privateClientRepository = new PrivateClientRepository(em);

        String clientNif = requestString("Client NIF: ");
        Optional<PrivateClient> privateClient =
                privateClientRepository.get((client) -> client.getNif().equals(clientNif));

        if (privateClient.isPresent())
            privateClientRepository.remove(privateClient.get());
        else
            System.out.println("Client not found.");
    }

    /**
     * Disables an existing client.
     *
     * @param em entity manager
     */
    private static void disableClient(EntityManager em) {
        System.out.println("\n<------------------ Disable Client ------------------>\n");

        Repository<Client> clientRepository = new Repository<>(em) {
        };

        String clientNif = requestString("Client NIF: ");
        Optional<Client> requestedClient = clientRepository.get((client) -> client.getNif().equals(clientNif));

        if (requestedClient.isPresent())
            clientRepository.remove(requestedClient.get());
        else
            System.out.println("Client not found.");
    }

    /**
     * Requests the user to insert the data for a new private client.
     *
     * @return the created private client
     */
    private static PrivateClient requestPrivateClient() {
        String name = requestString("Name: ");
        String phoneNumber = requestString("Phone Number: ");
        String nif = requestString("NIF: ");
        String address = requestString("Address: ");
        Boolean active = requestBoolean("Is active?(true/false): ");
        String citizenCardNumber = requestString("Citizen Card Number: ");

        return new PrivateClient(name, phoneNumber, nif, address, active, citizenCardNumber);
    }

    private static void createVehicle(EntityManager em) {
        System.out.println("\n<------------------ Create Vehicle ------------------>\n");
        Vehicle vehicle = requestVehicle();

        VehicleRepository vehicleRepository = new VehicleRepository(em);
        vehicleRepository.add(vehicle);
    }

    private static void getAlarmsCount(EntityManager em) {
        System.out.println("\n<------------------ Get Alarms Count ------------------>\n");

        VehicleRepository vehicleRepository = new VehicleRepository(em);

        String vehiclePlate = requestString("Vehicle Plate: ");
        Optional<Vehicle> vehicle =
                vehicleRepository.get((vehicle1) -> vehicle1.getPlate().equals(vehiclePlate));

        if (vehicle.isPresent()) {
            System.out.println("Alarms count: " + vehicle.get().getAlarms().size());
        } else
            System.out.println("Vehicle not found.");
    }

    private static void listAlarmData(EntityManager em) {
        System.out.println("\n<------------------ List Alarms ------------------>\n");

        VehicleRepository vehicleRepository = new VehicleRepository(em);

        String vehiclePlate = requestString("Vehicle Plate: ");
        Optional<Vehicle> vehicle =
                vehicleRepository.get((vehicle1) -> vehicle1.getPlate().equals(vehiclePlate));

        if (vehicle.isPresent()) {
            System.out.println("Alarms: ");
            for (Alarm alarm : vehicle.get().getAlarms()) {
                System.out.println("\t" + alarm.getId() + " - " + alarm.getDate());
            }
        } else
            System.out.println("Vehicle not found.");
    }

    private static void insertAlarmData(EntityManager em) {
        System.out.println("\n<------------------ Insert Alarm ------------------>\n");

        VehicleRepository vehicleRepository = new VehicleRepository(em);

        String vehiclePlate = requestString("Vehicle Plate: ");
        Optional<Vehicle> vehicle =
                vehicleRepository.get((vehicle1) -> vehicle1.getPlate().equals(vehiclePlate));

        if (vehicle.isPresent()) {
            Alarm alarm = new Alarm();
            alarm.setDate(new Date());
            alarm.setVehicle(vehicle.get());
            vehicle.get().getAlarms().add(alarm);
            vehicleRepository.update(vehicle.get());
        } else
            System.out.println("Vehicle not found.");
    }

    /**
     * Requests the user to insert an Integer value and returns it.
     *
     * @param message message to display
     * @return user input
     */
    private static Integer requestInteger(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        return scanner.nextInt();
    }

    /**
     * Requests the user to insert a String value and returns it.
     *
     * @param message message to display
     * @return user input
     */
    private static String requestString(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        return scanner.nextLine();
    }

    /**
     * Requests the user to insert a Boolean value and returns it.
     *
     * @param message message to display
     * @return user input
     */
    private static Boolean requestBoolean(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        return scanner.nextBoolean();
    }
}
