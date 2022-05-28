package pt.isel.model.clients;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "institutional_clients")
public class InstitutionalClient extends Client {

    public InstitutionalClient(String name, String phoneNumber, String nif, String address, boolean active, String contactName) {
        super(name, phoneNumber, nif, address, active);
        this.contactName = contactName;
    }

    //Needed for JPA...
    public InstitutionalClient() {

    }

    @Column(name = "contact_name", nullable = false)
    private String contactName;


    public String getContactName() {
        return contactName;
    }

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
