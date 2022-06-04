package pt.isel.model.gps.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * UnprocessedGPSData entity.
 */
@Entity
@Table(name = "unprocessed_gps_data")
public class UnprocessedGpsData extends RetrievedGpsData {

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
