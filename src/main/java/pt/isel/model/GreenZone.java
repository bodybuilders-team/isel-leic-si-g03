package pt.isel.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "lat")),
            @AttributeOverride(name = "y", column = @Column(name = "lon"))
    })
    private Point centerLocation;

    /**
     * The radius of the green zone.
     */
    @Column(name = "radius", nullable = false)
    private Double radius;

    /**
     * Creates a new instance of GreenZone.
     *
     * @param vehicle        the vehicle
     * @param centerLocation the center location
     * @param radius         the radius
     */
    public GreenZone(Vehicle vehicle, Point centerLocation, Double radius) {
        this.vehicle = vehicle;
        this.centerLocation = centerLocation;
        this.radius = radius;
    }

    // Needed for JPA...
    public GreenZone() {}

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
    public Point getCenterLocation() {
        return centerLocation;
    }

    /**
     * Sets the center of the green zone.
     *
     * @param centerLocation the center of the green zone.
     */
    public void setCenterLocation(Point centerLocation) {
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

    @Override
    public String toString() {
        return "GreenZone{" +
                "id=" + id +
                ", vehicle=" + vehicle +
                ", centerLocation='" + centerLocation + '\'' +
                ", radius=" + radius +
                '}';
    }
}
