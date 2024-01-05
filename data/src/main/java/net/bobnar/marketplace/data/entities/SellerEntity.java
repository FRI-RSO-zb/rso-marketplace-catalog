package net.bobnar.marketplace.data.entities;

import net.bobnar.marketplace.common.dtos.catalog.v1.sellers.Seller;
import net.bobnar.marketplace.data.converters.SellerConverter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="sellers")
@NamedQuery(name="Sellers.findAll", query="SELECT e FROM SellerEntity e")
public class SellerEntity extends EntityBase<Seller> {
    private String name;
    private String location;
    private String contact;
    @OneToMany(mappedBy = "seller")
    private List<AdEntity> ads;

    public List<AdEntity> getAds() {
        return ads;
    }

    public void setAds(List<AdEntity> ads) {
        this.ads = ads;
    }

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

    @Override
    public Seller toDto() {
        return SellerConverter.getInstance().toDto(this);
    }
}
