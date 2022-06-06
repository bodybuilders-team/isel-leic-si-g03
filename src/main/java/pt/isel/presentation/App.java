package pt.isel.presentation;

import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import pt.isel.dal.PersistenceManager;
import pt.isel.dal.Repository;
import pt.isel.dal.implementations.AlarmDataRepository;
import pt.isel.dal.implementations.VehicleRepository;
import pt.isel.dal.implementations.clients.PrivateClientRepository;
import pt.isel.dataProcessors.GpsDataCleaner;
import pt.isel.dataProcessors.GpsDataProcessor;
import pt.isel.model.AlarmData;
import pt.isel.model.GreenZone;
import pt.isel.model.Point;
import pt.isel.model.Vehicle;
import pt.isel.model.clients.Client;
import pt.isel.model.clients.PrivateClient;
import pt.isel.model.gps.device.GpsDevice;
import pt.isel.model.gps.device.GpsDeviceState;
import pt.isel.utils.Utils;


import static pt.isel.utils.ConsoleUI.requestBoolean;
import static pt.isel.utils.ConsoleUI.requestDouble;
import static pt.isel.utils.ConsoleUI.requestInteger;
import static pt.isel.utils.ConsoleUI.requestString;


/**
 * SI Project - 2021/22 Summer Semmester - LEIC41D - Group 03
 *
 * @author 48089 André Páscoa
 * @author 48280 André Jesus
 * @author 48287 Nyckollas Brandão
 */
public class App {
    static final int GPS_DATA_PROCESSOR_INTERVAL = 5 * 60 * 1000; // 5 minutes
    static final int INVALID_GPS_DATA_CLEANER_INTERVAL = 5 * 60 * 1000; // 5 minutes

    /**
     * Application entry point.
     */
    public static void main(String[] args) throws SQLException, IOException {
        Utils.createDatabase(true);

        Timer processGpsDataTimer = new Timer("Process GPS Data");
        TimerTask processGpsDataTask = new GpsDataProcessor();
        processGpsDataTimer.schedule(processGpsDataTask, 0, GPS_DATA_PROCESSOR_INTERVAL);

        Timer cleanGpsDataTimer = new Timer("Clean Invalid GPS Data");
        TimerTask cleanGpsDataTask = new GpsDataCleaner();
        cleanGpsDataTimer.schedule(cleanGpsDataTask, 1000, INVALID_GPS_DATA_CLEANER_INTERVAL);


        // App main loop
        do {
            Operation userInput = displayMenu();
            if (userInput == Operation.Exit) {
                System.out.println("Exiting...");
                break;
            }

            if (userInput != null)
                PersistenceManager.execute((em) -> {executeOperation(userInput, em);});
            else
                System.out.println("Invalid Operation.");

            System.out.print("\nPress ENTER to continue...");
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
        System.out.print("\n> ");

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
     * Requests the user to enter client information, and creates it
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
     * Requests the user to enter a client NIF and, if a client with that NIF exists, updates it,
     * requesting the user for new data.
     *
     * @param em entity manager
     */
    private static void updatePrivateClient(EntityManager em) {
        System.out.println("\n<------------------ Update Private Client ------------------>\n");

        PrivateClientRepository privateClientRepository = new PrivateClientRepository(em);

        String clientNif = requestString("Client NIF: ");
        Optional<PrivateClient> privateClient = privateClientRepository.get((client) -> client.getNif().equals(clientNif));

        if (privateClient.isPresent()) {
            System.out.println("Introduce the new data for the client:");
            PrivateClient updateClient = requestPrivateClient();
            updateClient.setId(privateClient.get().getId());

            privateClientRepository.update(updateClient);
        } else
            System.out.println("Client not found.");
    }

    /**
     * Requests the user to enter a client NIF and, if a client with that NIF exists, deletes it.
     *
     * @param em entity manager
     */
    private static void deletePrivateClient(EntityManager em) {
        System.out.println("\n<------------------ Delete Private Client ------------------>\n");

        PrivateClientRepository privateClientRepository = new PrivateClientRepository(em);

        String clientNif = requestString("Client NIF: ");
        Optional<PrivateClient> privateClient = privateClientRepository.get((client) -> client.getNif().equals(clientNif));

        if (privateClient.isPresent())
            privateClientRepository.remove(privateClient.get());
        else
            System.out.println("Client not found.");
    }

    /**
     * Requests the user to enter a client NIF and, if a client with that NIF exists, desactivates it.
     *
     * @param em entity manager
     */
    private static void disableClient(EntityManager em) {
        System.out.println("\n<------------------ Disable Client ------------------>\n");

        Repository<Client> clientRepository = new Repository<>(em) {};

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

    /**
     * Requests the user to insert the data for a new vehicle and, optionally, for a new green zone.
     *
     * @param em entity manager
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private static void createVehicle(EntityManager em) {
        System.out.println("\n<------------------ Create Vehicle ------------------>\n");

        Repository<GpsDevice> gpsDeviceRepository = new Repository<>(em) {};
        Repository<GpsDeviceState> gpsDeviceStateRepository = new Repository<>(em) {};

        GpsDeviceState activeState = gpsDeviceStateRepository.get((state) -> state.getStatus().equals("Active")).get();
        GpsDevice gpsDevice = new GpsDevice(activeState);

        gpsDeviceRepository.add(gpsDevice);

        String clientNif = requestString("Client NIF: ");
        Repository<Client> clientRepository = new Repository<>(em) {};
        Optional<Client> owner = clientRepository.get((client) -> client.getNif().equals(clientNif));

        if (owner.isEmpty()) {
            System.out.println("Client not found.");
            return;
        }

        String licensePlate = requestString("License Plate: ");

        Vehicle vehicle = new Vehicle(gpsDevice, owner.get(), licensePlate, 0);

        VehicleRepository vehicleRepository = new VehicleRepository(em);

        if (requestBoolean("Create green zone?(true/false): ")) {
            String centerLocation = requestString("Center Location: ");
            Double radius = requestDouble("Radius: ");

            GreenZone greenZone = new GreenZone(vehicle, Point.parsePoint(centerLocation), radius);

            boolean created = vehicleRepository.nativeCreateVehicle(vehicle, greenZone);
            if (!created)
                System.out.println("Number of vehicles exceeded");
        } else {
            boolean created = vehicleRepository.nativeCreateVehicle(vehicle);
            if (!created)
                System.out.println("Number of vehicles exceeded");
        }
    }

    /**
     * Requests the user to insert the year of the alarms and, optionally, the license plate of a vehicle,
     * and prints the total number of alarms for that vehicle in that year.
     * <p>
     * If no license plate is provided, the method prints the total number of alarms for all vehicles in that year.
     *
     * @param em entity manager
     */
    private static void getAlarmsCount(EntityManager em) {
        System.out.println("\n<------------------ Get Alarms Count ------------------>\n");

        int year = requestInteger("Year: ");
        String licensePlate = requestString("License Plate: ");

        VehicleRepository vehicleRepository = new VehicleRepository(em);
        if (licensePlate == null || licensePlate.isEmpty())
            System.out.println("Total number of alarms:" + vehicleRepository.getAlarmsCount(year));
        else {
            Optional<Vehicle> vehicle = vehicleRepository.get((v) -> v.getLicensePlate().equals(licensePlate));
            if (vehicle.isPresent())
                System.out.println("Total number of alarms:" + vehicle.get().getAlarmsCount(em, year));
            else
                System.out.println("Vehicle not found.");
        }
    }

    /**
     * Lists the alarms' data.
     *
     * @param em entity manager
     */
    private static void listAlarmData(EntityManager em) {
        System.out.println("\n<------------------ List Alarm Data ------------------>\n");

        Repository<AlarmData> alarmDataRepository = new Repository<>(em) {};
        List<AlarmData> alarmDataList = alarmDataRepository.getAll();
        alarmDataList.forEach(System.out::println);
    }

    /**
     * Requests the user to insert new alarm data.
     *
     * @param em entity manager
     */
    private static void insertAlarmData(EntityManager em) {
        System.out.println("\n<------------------ Insert Alarm Data ------------------>\n");

        AlarmDataRepository alarmDataRepository = new AlarmDataRepository(em);

        Optional<AlarmData> alarmData = requestAlarmData();

        alarmData.ifPresent(alarmDataRepository::add);
    }

    private static final String alarm_date_format = "yyyy-MM-dd HH:mm:ss";

    private static Optional<AlarmData> requestAlarmData() {
        String licensePlate = requestString("License Plate: ");
        String alarmLocation = requestString("Alarm location (format: (x,y)): ");

        String alarmTime = requestString("Alarm Time: (format: " + alarm_date_format + "):");

        Date alarmDate;
        try {
            alarmDate = new SimpleDateFormat(alarm_date_format).parse(alarmTime);
        } catch (ParseException e) {
            System.out.println("Invalid alarm time format.");
            return Optional.empty();
        }

        return Optional.of(new AlarmData(licensePlate, Point.parsePoint(alarmLocation), alarmDate));
    }

}
