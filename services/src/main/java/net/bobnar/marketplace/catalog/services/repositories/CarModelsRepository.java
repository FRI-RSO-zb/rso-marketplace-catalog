package net.bobnar.marketplace.catalog.services.repositories;

import com.kumuluz.ee.rest.beans.QueryParameters;
import net.bobnar.marketplace.common.dtos.catalog.v1.carModels.CarModel;
import net.bobnar.marketplace.data.converters.CarModelConverter;
import net.bobnar.marketplace.data.entities.CarModelEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CarModelsRepository extends RepositoryBase<CarModelEntity, CarModel> {

    public List<CarModelEntity> findByPrimaryIdentifier(String identifier, Integer brandId) {
        TypedQuery<CarModelEntity> query = this.getEntityManager().createNamedQuery("CarModels.findByPrimaryIdentifier", CarModelEntity.class);
        query.setParameter("primaryIdentifier", identifier);
        query.setParameter("brandId", brandId);

        return query.getResultList();
    }

    public List<CarModelEntity> getModelsByBrand(Integer brandId) {
        TypedQuery<CarModelEntity> query = this.getEntityManager().createNamedQuery("CarModels.findByBrandId", CarModelEntity.class);
        query.setParameter("brandId", brandId);

        return query.getResultList();
    }

    public Long countModelAds(Integer id) {
        CarModelEntity item = this.get(id);

        return (long) item.getAds().size();
    }

    @Override
    protected Class<CarModelEntity> getEntityClass() {
        return CarModelEntity.class;
    }

    @Override
    protected CarModelConverter getConverter() {
        return CarModelConverter.getInstance();
    }
}
