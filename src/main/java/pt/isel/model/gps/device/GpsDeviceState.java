package pt.isel.model.gps.device;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

/**
 * GPDeviceState entity.
 */
@Entity
@Table(name = "gps_device_states")
public class GpsDeviceState {

    /**
     * Creates a new instance of GpsDeviceState.
     *
     * @param status the status
     */
    public GpsDeviceState(String status) {
        this.status = status;
    }

    // Needed for JPA...
    public GpsDeviceState() {}

    /**
     * The status id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    /**
     * The status name.
     */
    @Column(nullable = false)
    private String status;

    /**
     * Gets the status id.
     *
     * @return the status id
     */
    @Id
    public Integer getId() {
        return id;
    }

    /**
     * Sets the status id.
     *
     * @param id the new status id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the status name.
     *
     * @return the status name
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status name.
     *
     * @param status the new status name
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "GpsDeviceState{" +
                "id=" + id +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        GpsDeviceState that = (GpsDeviceState) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
