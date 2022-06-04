package pt.isel.model.gps.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

/**
 * UnprocessedGPSData entity.
 */
@Entity
@Table(name = "unprocessed_gps_data")
public class UnprocessedGpsData extends RetrievedGpsData {

    /**
     * The version of the entity.
     */
    @Version
    private Integer version;

    @Override
    public String toString() {
        return "UnprocessedGpsData{" +
                "id=" + getId() +
                ", gpsDevice=" + gpsDeviceId +
                ", timestamp=" + timestamp +
                ", location=" + location +
                '}';
    }
}
