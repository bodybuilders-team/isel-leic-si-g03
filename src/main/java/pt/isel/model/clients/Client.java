package pt.isel.model.clients;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import pt.isel.model.Vehicle;


/**
 * Client entity.
 */
@Entity
@Table(name = "clients")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Client implements Serializable {

    /**
     * Creates a new instance of Client.
     *
     * @param name        the client name
     * @param phoneNumber the client phone number
     * @param nif         the client nif
     * @param address     the client address
     * @param active      true if the client is active, false otherwise
     */
    public Client(String name, String phoneNumber, String nif, String address, boolean active) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.nif = nif;
        this.address = address;
        this.active = active;
    }

    // Needed for JPA...
    public Client() {}

    /**
     * Maximum number of vehicles that a private client can own.
     */
    public static final int maxVehicles = 3;

    /**
     * The client referral.
     */
    @ManyToOne
    @JoinColumn(name = "referral")
    protected Client referral;

    /**
     * The client name.
     */
    @Column(nullable = false)
    protected String name;

    /**
     * The client phone number.
     */
    @Column(name = "phone_number", nullable = false)
    protected String phoneNumber;

    /**
     * The client nif.
     */
    @Column(nullable = false)
    protected String nif;

    /**
     * The client address.
     */
    @Column(nullable = false)
    protected String address;

    /**
     * True if the client is active, false otherwise.
     */
    @Column(nullable = false)
    protected Boolean active;

    /**
     * The client id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    /**
     * List of vehicles owned by the client.
     */
    @OneToMany(mappedBy = "client", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Vehicle> vehicles;

    /**
     * Client data type.
     */
    private String dtype;

    /**
     * Gets the client id.
     *
     * @return the client id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the client id.
     *
     * @param id the client id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the client referral.
     *
     * @return the client referral
     */
    public Client getReferral() {
        return referral;
    }

    /**
     * Sets the client referral.
     *
     * @param referral the client referral
     */
    public void setReferral(Client referral) {
        this.referral = referral;
    }

    /**
     * Gets the client name.
     *
     * @return the client name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the client name.
     *
     * @param name the client name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the client phone number.
     *
     * @return the client phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the client phone number.
     *
     * @param phoneNumber the client phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the client nif.
     *
     * @return the client nif
     */
    public String getNif() {
        return nif;
    }

    /**
     * Sets the client nif.
     *
     * @param nif the client nif
     */
    public void setNif(String nif) {
        this.nif = nif;
    }

    /**
     * Gets the client address.
     *
     * @return the client address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the client address.
     *
     * @param address the client address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the client active status.
     *
     * @return true if the client is active, false otherwise
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Sets the client active status.
     *
     * @param active true if the client is active, false otherwise
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * Gets the list of vehicles owned by the client.
     *
     * @return the list of vehicles owned by the client
     */
    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    /**
     * Adds a vehicle to the client list of vehicles.
     *
     * @param vehicle the vehicle to be added
     */
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicle.setClient(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
