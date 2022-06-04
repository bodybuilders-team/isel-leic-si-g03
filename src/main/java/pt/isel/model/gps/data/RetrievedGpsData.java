package pt.isel.model.gps.data;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.awt.geom.Point2D;
import java.util.Date;
import pt.isel.model.Point;
import pt.isel.model.gps.device.GpsDevice;
import pt.isel.utils.Utils;

/**
 * RetrievedGpsData entity.
 */
@MappedSuperclass
public abstract class RetrievedGpsData {

    /**
     * The gps device that retrieved the gps data.
     */
    @Column(name = "device_id")
    protected Integer gpsDeviceId;
    /**
     * The timestamp of the gps data.
     */
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date timestamp;

    /**
     * Location of the gps data.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "lat")),
            @AttributeOverride(name = "y", column = @Column(name = "lon"))
    })
    protected Point location;
    /**
     * The id of the retrieved gps data.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Creates a new instance of RetrievedGpsData.
     *
     * @param gpsDeviceId the gps device
     * @param timestamp the timestamp
     * @param location  the location
     */
    public RetrievedGpsData(Integer gpsDeviceId, Date timestamp, Point location) {
        this.gpsDeviceId = gpsDeviceId;
        this.timestamp = timestamp;
        this.location = location;
    }

    // Needed for JPA...
    public RetrievedGpsData() {}

    /**
     * Gets the id of the retrieved gps data.
     *
     * @return the id of the retrieved gps data
     */
    @Id
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the retrieved gps data.
     *
     * @param id the id of the retrieved gps data
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the gps device that retrieved the gps data.
     *
     * @return the gps device that retrieved the gps data
     */
    public Integer getGpsDeviceId() {
        return gpsDeviceId;
    }

    /**
     * Sets the gps device that retrieved the gps data.
     *
     * @param gpsDeviceId the gps device that retrieved the gps data
     */
    public void setGpsDevice(Integer gpsDeviceId) {
        this.gpsDeviceId = gpsDeviceId;
    }

    /**
     * Gets the timestamp of the gps data.
     *
     * @return the timestamp of the gps data
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of the gps data.
     *
     * @param timestamp the timestamp of the gps data
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the location of the gps data.
     *
     * @return the location of the gps data
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Sets the location of the gps data.
     *
     * @param location the location of the gps data
     */
    public void setLocation(Point location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "RetrievedGpsData{" +
                "id=" + id +
                ", gpsDevice=" + gpsDeviceId +
                ", timestamp=" + timestamp +
                ", location='" + location + '\'' +
                '}';
    }
}
