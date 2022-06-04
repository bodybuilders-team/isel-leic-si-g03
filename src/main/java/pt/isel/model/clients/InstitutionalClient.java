package pt.isel.model.clients;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Institutional client entity.
 */
@Entity
@Table(name = "institutional_clients")
public class InstitutionalClient extends Client {

    /**
     * Creates a new instance of InstitutionalClient.
     *
     * @param name        the client name
     * @param phoneNumber the client phone number
     * @param nif         the client nif
     * @param address     the client address
     * @param active      true if the client is active, false otherwise
     * @param contactName the client contact name
     */
    public InstitutionalClient(
            String name,
            String phoneNumber,
            String nif,
            String address,
            boolean active,
            String contactName
    ) {
        super(name, phoneNumber, nif, address, active);
        this.contactName = contactName;
    }

    // Needed for JPA...
    public InstitutionalClient() {}

    /**
     * The client contact name.
     */
    @Column(name = "contact_name", nullable = false)
    private String contactName;


    /**
     * Gets the client contact name.
     *
     * @return the client contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets the client contact name.
     *
     * @param contactName the client contact name
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }


    @Override
    public String toString() {
        return "InstitutionalClient{" +
                "id=" + getId() +
                ", referral=" + referral +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", nif='" + nif + '\'' +
                ", address='" + address + '\'' +
                ", active=" + active +
                ", contactName='" + contactName + '\'' +
                '}';
    }
}
