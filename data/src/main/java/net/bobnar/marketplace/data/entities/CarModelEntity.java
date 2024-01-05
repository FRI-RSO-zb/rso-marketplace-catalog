package net.bobnar.marketplace.data.entities;

import javax.persistence.*;

@Entity
@Table(name="CarModels")
@NamedQuery(name="CarModels.findAll", query="SELECT e FROM CarModelEntity e")
public class CarModelEntity extends EntityBase {
    private String name;

    @ManyToOne
    @JoinColumn(name="brand_id")
    private CarBrandEntity brand;
    private String primaryIdentifier;
    private String identifiers;

    public CarBrandEntity getBrand() {
        return brand;
    }

    public void setBrand(CarBrandEntity brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryIdentifier() {
        return primaryIdentifier;
    }

    public void setPrimaryIdentifier(String primaryIdentifier) {
        this.primaryIdentifier = primaryIdentifier;
    }

    public String getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(String identifiers) {
        this.identifiers = identifiers;
    }
}
