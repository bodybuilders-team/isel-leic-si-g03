package pt.isel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

/**
 * AlarmData entity.
 * Represents the list_alarms view.
 */
@Entity
@Table(name = "list_alarms")
public class AlarmData {

    /**
     * The gps data associated with the alarm.
     */
    @Id
    @Column(name = "gps_data_id")
    private Integer gpsDataId;

    /**
     * The driver name.
     */
    @Column(name = "driver_name", nullable = false)
    private String driverName;

    /**
     * The vehicle plate.
     */
    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    /**
     * The location of the data.
     */
    @Column(nullable = false, columnDefinition = "POINT")
    private String location;

    /**
     * The timestamp of the data.
     */
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;


    /**
     * Gets the gps data associated with the alarm.
     *
     * @return the gps data associated with the alarm.
     */
    public Integer getGpsDataId() {
        return gpsDataId;
    }

    /**
     * Gets the driver name.
     *
     * @return the driver name.
     */
    public String getDriverName() {
        return driverName;
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
     * Gets the location of the data.
     *
     * @return the location of the data.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the timestamp of the data.
     *
     * @return the timestamp of the data.
     */
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "AlarmData{" +
                "gpsDataId=" + gpsDataId +
                ", driverName='" + driverName + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", location='" + location + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
