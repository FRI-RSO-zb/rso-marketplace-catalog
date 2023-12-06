package net.bobnar.marketplace.data.entities;

import javax.persistence.*;

@Entity
@Table(name="sellers")
@NamedQuery(name="Sellers.findAll", query="SELECT e FROM SellerEntity e")
public class SellerEntity extends EntityBase {
    private String name;
    private String location;
    private String contact;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
