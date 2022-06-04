package pt.isel.presentation;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import pt.isel.dal.Mapper;
import pt.isel.dal.PersistenceManager;
import pt.isel.dal.Repository;
import pt.isel.dal.clients.PrivateClientRepository;
import pt.isel.model.AlarmData;
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

        Repository<AlarmData> ad = new Repository<>() {
        };

        ad.getAll().forEach(System.out::println);
    }
}
