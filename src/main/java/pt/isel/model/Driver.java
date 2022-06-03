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
}
