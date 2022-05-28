package pt.isel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import pt.isel.model.gps.GpsData;

@Entity
@Table(name = "alarms")
public class Alarm {

    @Id
    @OneToOne
    @JoinColumn(name = "gps_data_id")
    private GpsData gps_data;

    @Column(name = "driver_name", nullable = false)
    private String driverName;


    public GpsData getData() {
        return gps_data;
    }

    public void setData(GpsData data) {
        this.gps_data = data;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
}
