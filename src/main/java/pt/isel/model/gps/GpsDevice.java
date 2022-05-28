package pt.isel.model.gps;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "gps_devices")
public class GpsDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "device_status", nullable = false)
    private GpsDeviceState deviceStatus;

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    public Integer getId() {
        return id;
    }


    public GpsDeviceState getDeviceStatus() {
        return deviceStatus;
    }

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
}
