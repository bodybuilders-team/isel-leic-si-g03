package pt.isel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * GreenZone entity.
 */
@Entity
@Table(name = "green_zones")
public class GreenZone {

    /**
     * The id of the green zone.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The vehicle associated with the green zone.
     */
    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    /**
     * The center of the green zone.
     */
    @Column(name = "center_location", nullable = false, columnDefinition = "POINT")
    private String centerLocation;

    /**
     * The radius of the green zone.
     */
    @Column(name = "radius", nullable = false)
    private Double radius;


    /**
     * Gets the id of the green zone.
     *
     * @return the id of the green zone.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the green zone.
     *
     * @param id the id of the green zone.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the vehicle associated with the green zone.
     *
     * @return the vehicle associated with the green zone.
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * Sets the vehicle associated with the green zone.
     *
     * @param vehicle the vehicle associated with the green zone.
     */
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * Gets the center of the green zone.
     *
     * @return the center of the green zone.
     */
    public String getCenterLocation() {
        return centerLocation;
    }

    /**
     * Sets the center of the green zone.
     *
     * @param centerLocation the center of the green zone.
     */
    public void setCenterLocation(String centerLocation) {
        this.centerLocation = centerLocation;
    }

    /**
     * Gets the radius of the green zone.
     *
     * @return the radius of the green zone.
     */
    public Double getRadius() {
        return radius;
    }

    /**
     * Sets the radius of the green zone.
     *
     * @param radius the radius of the green zone.
     */
    public void setRadius(Double radius) {
        this.radius = radius;
    }
}
