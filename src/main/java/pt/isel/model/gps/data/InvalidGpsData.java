package pt.isel.model.gps.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Date;

import pt.isel.model.Point;

/**
 * InvalidGPSData entity.
 */
@Entity
@Table(name = "invalid_gps_data")
public class InvalidGpsData extends RetrievedGpsData {

    public InvalidGpsData(Integer gpsDeviceId, Date timestamp, Point location) {
        super(gpsDeviceId, timestamp, location);
    }

    // Needed for JPA...
    public InvalidGpsData() {}

    @Override
    public String toString() {
        return "InvalidGpsData{" +
                "id=" + getId() +
                ", gpsDevice=" + gpsDeviceId +
                ", timestamp=" + timestamp +
                ", location=" + location +
                '}';
    }
}
