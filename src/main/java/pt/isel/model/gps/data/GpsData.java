package pt.isel.model.gps.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.awt.geom.Point2D;
import java.util.Date;

import pt.isel.model.gps.device.GpsDevice;
import pt.isel.utils.Utils;

/**
 * GPSData entity.
 */
@Entity
@Table(name = "gps_data")
public class GpsData {

    /**
     * Creates a new instance of GPSData.
     *
     * @param gpsDevice the gps device
     * @param timestamp the timestamp
     * @param location  the location
     */
    public GpsData(GpsDevice gpsDevice, Date timestamp, String location) {
        this.gpsDevice = gpsDevice;
        this.timestamp = timestamp;
        this.location = location;
    }

    // Needed for JPA...
    public GpsData() {
    }

    /**
     * The GPS data id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The GPS device associated with this data.
     */
    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private GpsDevice gpsDevice;

    /**
     * The timestamp of the data.
     */
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    /**
     * The location of the data.
     */
    @Column(nullable = false, columnDefinition = "POINT")
    private String location;


    /**
     * Gets the GPS data id.
     *
     * @return the GPS data id
     */
    @Id
    public Integer getId() {
        return id;
    }

    /**
     * Sets the GPS data id.
     *
     * @param id the GPS data id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the GPS device associated with this data.
     *
     * @return the GPS device associated with this data
     */
    public GpsDevice getGpsDevice() {
        return gpsDevice;
    }

    /**
     * Sets the GPS device associated with this data.
     *
     * @param gpsDevice the GPS device associated with this data
     */
    public void setGpsDevice(GpsDevice gpsDevice) {
        this.gpsDevice = gpsDevice;
    }

    /**
     * Gets the timestamp of the data.
     *
     * @return the timestamp of the data
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of the data.
     *
     * @param timestamp the timestamp of the data
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the location of the data.
     *
     * @return the location of the data
     */
    public Point2D.Float getLocation() {
        return Utils.parsePoint(location);
    }

    /**
     * Sets the location of the data.
     *
     * @param location the location of the data
     */
    public void setLocation(Point2D.Float location) {
        this.location = Utils.pointToString(location);
    }

    @Override
    public String toString() {
        return "GpsData{" +
                "id=" + id +
                ", gpsDevice=" + gpsDevice +
                ", timestamp=" + timestamp +
                ", location='" + location + '\'' +
                '}';
    }
}
