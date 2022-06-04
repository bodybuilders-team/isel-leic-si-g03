package pt.isel.model.gps.data;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.awt.geom.Point2D;
import java.util.Date;

import pt.isel.model.gps.device.GpsDevice;
import pt.isel.utils.Utils;

/**
 * RetrievedGpsData entity.
 */
@MappedSuperclass
public abstract class RetrievedGpsData {

    /**
     * Creates a new instance of RetrievedGpsData.
     *
     * @param gpsDevice the gps device
     * @param timestamp the timestamp
     * @param location  the location
     */
    public RetrievedGpsData(GpsDevice gpsDevice, Date timestamp, String location) {
        this.gpsDevice = gpsDevice;
        this.timestamp = timestamp;
        this.location = location;
    }

    // Needed for JPA...
    public RetrievedGpsData() {}

    /**
     * The id of the retrieved gps data.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The gps device that retrieved the gps data.
     */
    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    protected GpsDevice gpsDevice;

    /**
     * The timestamp of the gps data.
     */
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date timestamp;

    /**
     * Location of the gps data.
     */
    @Column(nullable = false, columnDefinition = "POINT")
    protected String location;


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
    public GpsDevice getGpsDevice() {
        return gpsDevice;
    }

    /**
     * Sets the gps device that retrieved the gps data.
     *
     * @param gpsDevice the gps device that retrieved the gps data
     */
    public void setGpsDevice(GpsDevice gpsDevice) {
        this.gpsDevice = gpsDevice;
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
    public Point2D.Float getLocation() {
        return Utils.parsePoint(location);
    }

    /**
     * Sets the location of the gps data.
     *
     * @param location the location of the gps data
     */
    public void setLocation(Point2D.Float location) {
        this.location = Utils.pointToString(location);
    }

    @Override
    public String toString() {
        return "RetrievedGpsData{" +
                "id=" + id +
                ", gpsDevice=" + gpsDevice +
                ", timestamp=" + timestamp +
                ", location='" + location + '\'' +
                '}';
    }
}
