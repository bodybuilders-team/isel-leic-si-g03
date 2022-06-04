package pt.isel.model.gps.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.awt.geom.Point2D;
import java.util.Date;
import pt.isel.model.Point;
import pt.isel.model.gps.device.GpsDevice;
import pt.isel.utils.Utils;

/**
 * InvalidGPSData entity.
 */
@Entity
@Table(name = "invalid_gps_data")
public class InvalidGpsData extends RetrievedGpsData {

    public InvalidGpsData(Integer gpsDeviceId, Date timestamp, Point location) {
        this.gpsDeviceId = gpsDeviceId;
        this.timestamp = timestamp;
        this.location = location;
    }

    public InvalidGpsData() {

    }

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
