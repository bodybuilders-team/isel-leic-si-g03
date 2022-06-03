package pt.isel.presentation;

import java.util.Optional;

import pt.isel.dal.Mapper;
import pt.isel.dal.PersistenceManager;
import pt.isel.dal.clients.PrivateClientRepository;
import pt.isel.model.gps.GpsData;
import pt.isel.model.clients.InstitutionalClient;
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
        Mapper<InstitutionalClient> institutionalClientMapper = new Mapper<>() {
        };

        Mapper<PrivateClient> privateClientMapper = new Mapper<>() {
        };

        PrivateClientRepository privateClientRepository = new PrivateClientRepository();

        PersistenceManager.execute(() -> {
            PrivateClient privateClient = new PrivateClient(
                    "Joao",
                    "91234",
                    "123456789",
                    "Rua da casa",
                    true,
                    "123456789"
            );

            privateClientMapper.create(privateClient);

            privateClientRepository.deleteAll();
        });


        PersistenceManager.execute(() -> {

            //Generate an InstitutionalClient based on a hospital
            InstitutionalClient institutionalClient = new InstitutionalClient(
                    "Hospital",
                    "91234",
                    "123456789",
                    "Rua da casa",
                    true,
                    "Joao"
            );


            institutionalClientMapper.create(institutionalClient);

            Optional<PrivateClient> opc = privateClientRepository.get(privateClient -> privateClient.getName().equals("Joao"));
            if (opc.isPresent()) {
                PrivateClient pc = opc.get();
                institutionalClient.setReferral(pc);
            } else
                System.out.println("Not found");
            institutionalClientMapper.update(institutionalClient);

            InstitutionalClient ic = institutionalClientMapper.read(institutionalClient.getId());
            System.out.println(ic);
            privateClientRepository.getAll().forEach(System.out::println);

            GpsData data = (GpsData) PersistenceManager.
                    getEntityManager()
                    .createQuery("SELECT gpsData FROM GpsData gpsData")
                    .getSingleResult();
            System.out.println(data);
        });


        PersistenceManager.closeEntityManagerFactory();
    }
}
