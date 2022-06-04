package pt.isel.model.clients;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Private client entity.
 */
@Entity
@Table(name = "private_clients")
public class PrivateClient extends Client {

    /**
     * Creates a new instance of PrivateClient.
     *
     * @param name              the client name
     * @param phoneNumber       the client phone number
     * @param nif               the client nif
     * @param address           the client address
     * @param active            true if the client is active, false otherwise
     * @param citizenCardNumber the client citizen card number
     */
    public PrivateClient(
            String name,
            String phoneNumber,
            String nif,
            String address,
            boolean active,
            String citizenCardNumber
    ) {
        super(name, phoneNumber, nif, address, active);
        this.citizenCardNumber = citizenCardNumber;
    }

    // Needed for JPA...
    public PrivateClient() {}

    /**
     * The client citizen card number.
     */
    @Column(name = "citizen_card_number", nullable = false)
    private String citizenCardNumber;


    /**
     * Gets the client citizen card number.
     *
     * @return the client citizen card number
     */
    public String getCitizenCardNumber() {
        return citizenCardNumber;
    }

    /**
     * Sets the client citizen card number.
     *
     * @param citizenCardNumber the client citizen card number
     */
    public void setCitizenCardNumber(String citizenCardNumber) {
        this.citizenCardNumber = citizenCardNumber;
    }

    @Override
    public String toString() {
        return "PrivateClient{" +
                "id=" + getId() +
                ", referral=" + referral +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", nif='" + nif + '\'' +
                ", address='" + address + '\'' +
                ", active=" + active +
                ", citizenCardNumber='" + citizenCardNumber + '\'' +
                '}';
    }
}
