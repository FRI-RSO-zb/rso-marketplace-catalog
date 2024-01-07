package net.bobnar.marketplace.catalog.services.repositories;

import net.bobnar.marketplace.common.dtos.catalog.v1.carBrands.CarBrand;
import net.bobnar.marketplace.data.converters.CarBrandConverter;
import net.bobnar.marketplace.data.entities.CarBrandEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.util.List;


@ApplicationScoped
public class CarBrandsRepository extends RepositoryBase<CarBrandEntity, CarBrand> {
    public CarBrandEntity findByPrimaryIdentifier(String identifier) {
        return getByColumn("primaryIdentifier", identifier);
    }

    public List<CarBrandEntity> findByPrimaryIdentifiers(List<String> identifiers) {
        return this.getMultipleByColumn("primaryIdentifier", identifiers);
    }

    public List<CarBrandEntity> findItemsWithIdentifier(String identifier) {
        TypedQuery<CarBrandEntity> query = this.getEntityManager().createNamedQuery("CarBrands.findWithIdentifier", CarBrandEntity.class);
        query.setParameter("identifier", "%" + identifier + "%");

        return query.getResultList();
    }

    public Long countBrandModels(Integer id) {
        CarBrandEntity item = this.get(id);

        return (long) item.getModels().size();
    }

    @Override
    protected Class<CarBrandEntity> getEntityClass() {
        return CarBrandEntity.class;
    }

    @Override
    protected CarBrandConverter getConverter() {
        return CarBrandConverter.getInstance();
    }
}
