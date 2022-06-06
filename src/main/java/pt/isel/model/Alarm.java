package pt.isel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.Objects;

import pt.isel.model.gps.data.GpsData;

/**
 * Alarm entity.
 */
@Entity
@Table(name = "alarms")
public class Alarm {

    /**
     * Creates a new instance of Alarm.
     *
     * @param gpsData    the gps data
     * @param driverName the driver name
     */
    public Alarm(GpsData gpsData, String driverName) {
        this.gpsData = gpsData;
        this.driverName = driverName;
    }

    // Needed for JPA...
    public Alarm() {}

    /**
     * The gps data associated with the alarm.
     */
    @Id
    @OneToOne
    @JoinColumn(name = "gps_data_id", nullable = false)
    private GpsData gpsData;

    /**
     * The driver name.
     */
    @Column(name = "driver_name", nullable = false)
    private String driverName;

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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Alarm alarm = (Alarm) o;
        return Objects.equals(gpsData.getId(), alarm.gpsData.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(gpsData.getId());
    }
}
