package net.bobnar.marketplace.catalog.services.repositories;

import com.kumuluz.ee.rest.beans.QueryParameters;
import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;
import net.bobnar.marketplace.common.dtos.catalog.v1.carModels.CarModel;
import net.bobnar.marketplace.data.converters.CarModelConverter;
import net.bobnar.marketplace.data.entities.AdEntity;
import net.bobnar.marketplace.data.entities.CarModelEntity;

import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class CarModelsRepository extends RepositoryBase<CarModelEntity, CarModel> {

    public List<CarModel> getModelsByBrand(Integer brandId) {
        TypedQuery<CarModelEntity> query = this.getEntityManager().createNamedQuery("CarModels.findByBrandId", CarModelEntity.class);
        query.setParameter("brandId", brandId);

        List<CarModel> results = query.getResultStream().map(this::toDto).collect(Collectors.toList());

        return results;
    }

    public Long countModelAds(Integer id) {
        CarModelEntity item = this.get(id);

        return (long) item.getAds().size();
    }

    @Override
    public Long countQueriedItems(QueryParameters query) {
        return super.countQueriedItems(query);
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
