package pt.isel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import pt.isel.model.gps.data.GpsData;

/**
 * Alarm entity.
 */
@Entity
@Table(name = "alarms")
public class Alarm {

    /**
     * The gps data associated with the alarm.
     */
    @Id
    @OneToOne
    @JoinColumn(name = "gps_data_id")
    private GpsData gpsData;
    /**
     * The driver name.
     */
    @Column(name = "driver_name", nullable = false)
    private String driverName;

    public Alarm(GpsData gpsData, String driverName) {
        this.gpsData = gpsData;
        this.driverName = driverName;
    }

    // Needed for JPA...
    public Alarm() {}

    /**
     * Gets the gps data.
     *
     * @return the gps data
     */
    public GpsData getGpsData() {
        return gpsData;
    }

    /**
     * Sets the gps data.
     *
     * @param gpsData the new gps data
     */
    public void setGpsData(GpsData gpsData) {
        this.gpsData = gpsData;
    }

    /**
     * Gets the driver name.
     *
     * @return the driver name
     */
    public String getDriverName() {
        return driverName;
    }

    /**
     * Sets the driver name.
     *
     * @param driverName the new driver name
     */
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "gpsData=" + gpsData +
                ", driverName='" + driverName + '\'' +
                '}';
    }
}
