package net.bobnar.marketplace.catalog.services.repositories;

import com.kumuluz.ee.rest.beans.QueryParameters;
import net.bobnar.marketplace.common.dtos.catalog.v1.sellers.Seller;
import net.bobnar.marketplace.data.converters.SellerConverter;
import net.bobnar.marketplace.data.entities.SellerEntity;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class SellersRepository extends RepositoryBase<SellerEntity, Seller> {

    @Override
    public Long countQueriedItems(QueryParameters query) {
        return super.countQueriedItems(query);
    }

    @Override
    protected Class<SellerEntity> getEntityClass() {
        return SellerEntity.class;
    }

    @Override
    protected SellerConverter getConverter() {
        return SellerConverter.getInstance();
    }
}
