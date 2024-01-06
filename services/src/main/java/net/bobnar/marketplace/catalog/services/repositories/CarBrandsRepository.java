package net.bobnar.marketplace.catalog.services.repositories;

import com.kumuluz.ee.rest.beans.QueryParameters;
import net.bobnar.marketplace.common.dtos.catalog.v1.carBrands.CarBrand;
import net.bobnar.marketplace.data.converters.CarBrandConverter;
import net.bobnar.marketplace.data.entities.CarBrandEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;


@ApplicationScoped
public class CarBrandsRepository extends RepositoryBase<CarBrandEntity, CarBrand> {
    public List<CarBrandEntity> findByPrimaryIdentifier(String identifier) {
        TypedQuery<CarBrandEntity> query = this.getEntityManager().createNamedQuery("CarBrands.findByPrimaryIdentifier", CarBrandEntity.class);
        query.setParameter("primaryIdentifier", identifier);

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
