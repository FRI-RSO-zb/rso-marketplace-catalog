package net.bobnar.marketplace.data.converters;

import net.bobnar.marketplace.common.dtos.catalog.v1.sellers.Seller;
import net.bobnar.marketplace.data.entities.SellerEntity;

public class SellerConverter extends ConverterBase<SellerEntity, Seller> {
    private static SellerConverter instance;

    public Seller toDto(SellerEntity entity) {
        return new Seller(entity.getId(), entity.getName(), entity.getLocation(), entity.getContact(), entity.getSource(), entity.getSourceId());
    }

    public SellerEntity toEntity(Seller item) {
        SellerEntity entity = new SellerEntity();
        entity.setId(item.getId());
        entity.setName(item.getName());
        entity.setLocation(item.getLocation());
        entity.setContact(item.getContact());
        entity.setSource(item.getSource());
        entity.setSourceId(item.getSourceId());

        return entity;
    }

    public static SellerConverter getInstance() {
        if (instance == null) {
            instance = new SellerConverter();
        }

        return instance;
    }
}
