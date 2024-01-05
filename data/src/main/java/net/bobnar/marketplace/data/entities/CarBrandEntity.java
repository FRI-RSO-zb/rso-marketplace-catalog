package net.bobnar.marketplace.data.entities;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="CarBrands")
@NamedQuery(name="CarBrands.findAll", query="SELECT e FROM CarBrandEntity e")
public class CarBrandEntity extends EntityBase {
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
}
