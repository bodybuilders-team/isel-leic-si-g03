package pt.isel.model.gps.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * InvalidGPSData entity.
 */
@Entity
@Table(name = "invalid_gps_data")
public class InvalidGpsData extends RetrievedGpsData {

    @Override
    public String toString() {
        return "InvalidGpsData{" +
                "id=" + getId() +
                ", gpsDevice=" + gpsDevice +
                ", timestamp=" + timestamp +
                ", location=" + location +
                '}';
    }
}
