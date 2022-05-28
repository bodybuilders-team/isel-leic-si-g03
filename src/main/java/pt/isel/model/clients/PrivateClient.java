package pt.isel.model.clients;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "private_clients")
public class PrivateClient extends Client {
    public PrivateClient(String name, String phoneNumber, String nif, String address, boolean active, String citizenCardNumber) {
        super(name, phoneNumber, nif, address, active);
        this.citizenCardNumber = citizenCardNumber;
    }

    //Needed for JPA...
    public PrivateClient() {
    }

    @Column(name = "citizen_card_number", nullable = false)
    private String citizenCardNumber;


    public String getCitizenCardNumber() {
        return citizenCardNumber;
    }

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
