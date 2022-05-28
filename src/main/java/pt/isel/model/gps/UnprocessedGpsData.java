package pt.isel.model.gps;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "unprocessed_gps_data")
public class UnprocessedGpsData extends RetrievedGpsData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public String toString() {
        return "InvalidGpsData{" +
                "id=" + id +
                ", gpsDevice=" + gpsDevice +
                ", timestamp=" + timestamp +
                ", location=" + location +
                '}';
    }
}
