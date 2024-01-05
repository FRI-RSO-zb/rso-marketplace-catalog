package net.bobnar.marketplace.catalog.services.repositories;

import com.kumuluz.ee.rest.beans.QueryParameters;
import net.bobnar.marketplace.common.dtos.catalog.v1.carBrands.CarBrand;
import net.bobnar.marketplace.data.converters.CarBrandConverter;
import net.bobnar.marketplace.data.entities.CarBrandEntity;
import net.bobnar.marketplace.data.entities.CarModelEntity;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class CarBrandsRepository extends RepositoryBase<CarBrandEntity, CarBrand> {
    public Long countBrandModels(Integer id) {
        CarBrandEntity item = this.get(id);

        return (long) item.getModels().size();
    }

    public Long countBrandAds(Integer id) {
        CarBrandEntity item = this.get(id);

        return (long) item.getAds().size();
    }

    @Override
    public Long countQueriedItems(QueryParameters query) {
        return super.countQueriedItems(query);
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
