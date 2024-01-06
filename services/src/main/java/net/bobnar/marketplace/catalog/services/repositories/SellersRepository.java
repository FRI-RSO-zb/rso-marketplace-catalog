package net.bobnar.marketplace.catalog.services.repositories;

import net.bobnar.marketplace.common.dtos.catalog.v1.sellers.Seller;
import net.bobnar.marketplace.data.converters.SellerConverter;
import net.bobnar.marketplace.data.entities.SellerEntity;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SellersRepository extends RepositoryBase<SellerEntity, Seller> {
    @Override
    protected Class<SellerEntity> getEntityClass() {
        return SellerEntity.class;
    }

    @Override
    protected SellerConverter getConverter() {
        return SellerConverter.getInstance();
    }
}
