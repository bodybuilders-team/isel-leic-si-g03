package pt.isel.model.gps;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.awt.geom.Point2D;
import java.util.Date;
import pt.isel.Utils;

@MappedSuperclass
public abstract class RetrievedGpsData {

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    protected GpsDevice gpsDevice;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date timestamp;

    protected String location;

    public GpsDevice getGpsDevice() {
        return gpsDevice;
    }

    public void setGpsDevice(GpsDevice gpsDevice) {
        this.gpsDevice = gpsDevice;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Point2D.Float getLocation() {
        return Utils.parsePoint(location);
    }

    public void setLocation(Point2D.Float location) {
        this.location = Utils.pointToString(location);
    }
}
