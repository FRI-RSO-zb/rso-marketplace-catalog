package net.bobnar.marketplace.data.entities;

import net.bobnar.marketplace.common.dtos.catalog.v1.carBrands.CarBrand;
import net.bobnar.marketplace.data.converters.CarBrandConverter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="CarBrands", indexes = { @Index(columnList = "primaryIdentifier") })
@NamedQuery(name="CarBrands.findAll", query="SELECT e FROM CarBrandEntity e")
@NamedQuery(name="CarBrands.findByPrimaryIdentifier", query="SELECT e FROM CarBrandEntity e WHERE e.primaryIdentifier=:primaryIdentifier")
public class CarBrandEntity extends EntityBase<CarBrand> {
    private String name;
    private String primaryIdentifier;
    private String identifiers;
    @OneToMany(mappedBy = "brand")
    private List<CarModelEntity> models;

    public List<CarModelEntity> getModels() {
        return models;
    }

    public void setModels(List<CarModelEntity> models) {
        this.models = models;
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

    @Override
    public CarBrand toDto() {
        return CarBrandConverter.getInstance().toDto(this);
    }
}
