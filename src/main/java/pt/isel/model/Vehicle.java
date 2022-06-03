package pt.isel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import pt.isel.model.clients.Client;
import pt.isel.model.gps.GpsDevice;

/**
 * Vehicle entity.
 */
@Entity
@Table(name = "vehicles")
public class Vehicle {

    /**
     * The vehicle id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The gps device associated with the vehicle.
     */
    @OneToOne
    @JoinColumn(name = "gps_device_id", nullable = false)
    private GpsDevice gpsDevice;

    /**
     * The client associated with the vehicle.
     */
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    /**
     * The vehicle plate.
     */
    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    /**
     * Number of alarms of the vehicle.
     */
    @Column(name = "num_alarms", nullable = false)
    private Integer numAlarms;


    /**
     * Gets the vehicle id.
     *
     * @return the vehicle id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the vehicle id.
     *
     * @param id the vehicle id.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the gps device associated with the vehicle.
     *
     * @return the gps device associated with the vehicle.
     */
    public GpsDevice getGpsDevice() {
        return gpsDevice;
    }

    /**
     * Sets the gps device associated with the vehicle.
     *
     * @param gpsDevice the gps device associated with the vehicle.
     */
    public void setGpsDevice(GpsDevice gpsDevice) {
        this.gpsDevice = gpsDevice;
    }

    /**
     * Gets the client associated with the vehicle.
     *
     * @return the client associated with the vehicle.
     */
    public Client getClient() {
        return client;
    }

    /**
     * Sets the client associated with the vehicle.
     *
     * @param client the client associated with the vehicle.
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Gets the vehicle plate.
     *
     * @return the vehicle plate.
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * Sets the vehicle plate.
     *
     * @param licensePlate the vehicle plate.
     */
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    /**
     * Gets the number of alarms of the vehicle.
     *
     * @return the number of alarms of the vehicle.
     */
    public Integer getNumAlarms() {
        return numAlarms;
    }

    /**
     * Sets the number of alarms of the vehicle.
     *
     * @param numAlarms the number of alarms of the vehicle.
     */
    public void setNumAlarms(Integer numAlarms) {
        this.numAlarms = numAlarms;
    }
}
