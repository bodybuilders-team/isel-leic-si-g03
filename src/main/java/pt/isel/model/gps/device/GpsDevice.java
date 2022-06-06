package pt.isel.model.gps.device;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;

/**
 * GPSDevice entity.
 */
@Entity
@Table(name = "gps_devices")
public class GpsDevice {

    /**
     * The id of the device.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    /**
     * The device status.
     */
    @ManyToOne
    @JoinColumn(name = "device_status", nullable = false)
    private GpsDeviceState deviceStatus;

    /**
     * Creates a new instance of GpsDevice.
     *
     * @param deviceStatus the device status
     */
    public GpsDevice(GpsDeviceState deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    // Needed for JPA...
    public GpsDevice() {}

    /**
     * Gets the id of the device.
     *
     * @return the id of the device
     */
    @Id
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the device.
     *
     * @param id the id of the device
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the device status.
     *
     * @return the device status
     */
    public GpsDeviceState getDeviceStatus() {
        return deviceStatus;
    }

    /**
     * Sets the device status.
     *
     * @param deviceStatus the device status
     */
    public void setDeviceStatus(GpsDeviceState deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    @Override
    public String toString() {
        return "GpsDevice{" +
                "id=" + id +
                ", deviceStatus=" + deviceStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GpsDevice gpsDevice = (GpsDevice) o;
        return Objects.equals(id, gpsDevice.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
