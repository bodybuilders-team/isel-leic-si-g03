package pt.isel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Driver entity.
 */
@Entity
@Table(name = "drivers")
public class Driver {

    /**
     * The vehicle that the driver drives.
     */
    @Id
    @OneToOne
    private Vehicle vehicle;

    /**
     * The driver's name.
     */
    @Column(nullable = false)
    private String name;

    /**
     * The driver's phone number.
     */
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    /**
     * Creates a new instance of Driver.
     *
     * @param vehicle     the vehicle
     * @param name        the name
     * @param phoneNumber the phone number
     */
    public Driver(Vehicle vehicle, String name, String phoneNumber) {
        this.vehicle = vehicle;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    // Needed for JPA...
    public Driver() {}

    /**
     * Gets the vehicle that the driver drives.
     *
     * @return the vehicle that the driver drives
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * Sets the vehicle that the driver drives.
     *
     * @param vehicle the vehicle that the driver drives
     */
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * Gets the driver's name.
     *
     * @return the driver's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the driver's name.
     *
     * @param name the driver's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the driver's phone number.
     *
     * @return the driver's phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the driver's phone number.
     *
     * @param phoneNumber the driver's phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "vehicle=" + vehicle +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
