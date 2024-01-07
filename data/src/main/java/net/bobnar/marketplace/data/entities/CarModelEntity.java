package net.bobnar.marketplace.data.entities;

import net.bobnar.marketplace.common.dtos.catalog.v1.carModels.CarModel;
import net.bobnar.marketplace.data.converters.CarModelConverter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="CarModels", indexes={ @Index(columnList = "primaryIdentifier") })
@NamedQuery(name="CarModels.findAll", query="SELECT e FROM CarModelEntity e")
@NamedQuery(name="CarModels.findByBrandId", query="SELECT e FROM CarModelEntity e WHERE e.brandId=:brandId")
@NamedQuery(name="CarModels.findByPrimaryIdentifier", query="SELECT e FROM CarModelEntity e WHERE e.brandId=:brandId AND e.primaryIdentifier=:primaryIdentifier")
@NamedQuery(name="CarModels.findWithIdentifier", query="SELECT e FROM CarModelEntity e WHERE  e.brandId=:brandId AND e.identifiers LIKE :identifier")
public class CarModelEntity extends EntityBase<CarModel> {
    private String name;

    @ManyToOne
    @JoinColumn(name="brandid", insertable = false, updatable = false)
    private CarBrandEntity brand;
    private Integer brandId;

    private String primaryIdentifier;
    private String identifiers;

    @OneToMany(mappedBy = "model")
    private List<AdEntity> ads;

    public CarBrandEntity getBrand() {
        return brand;
    }

//    public void setBrand(CarBrandEntity brand) {
//        this.brand = brand;
//    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
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

    public List<AdEntity> getAds() {
        return ads;
    }

    public void setAds(List<AdEntity> ads) {
        this.ads = ads;
    }

    @Override
    public CarModel toDto() {
        return CarModelConverter.getInstance().toDto(this);
    }
}
