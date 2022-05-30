package pt.isel.model.clients;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "clients")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Client implements Serializable {

    public Client(String name, String phoneNumber, String nif, String address, boolean active) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.nif = nif;
        this.address = address;
        this.active = active;
    }

    //Needed for JPA...
    public Client() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "referral")
    protected Client referral;

    @Column(nullable = false)
    protected String name;

    @Column(name = "phone_number", nullable = false)
    protected String phoneNumber;

    @Column(nullable = false)
    protected String nif;

    @Column(nullable = false)
    protected String address;

    @Column(nullable = false)
    protected Boolean active;


    public Client getReferral() {
        return referral;
    }

    public void setReferral(Client referral) {
        this.referral = referral;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

