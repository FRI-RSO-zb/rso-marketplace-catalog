package net.bobnar.marketplace.data.converters;

import net.bobnar.marketplace.common.dtos.catalog.v1.sellers.Seller;
import net.bobnar.marketplace.data.entities.SellerEntity;

public class SellerConverter extends ConverterBase<SellerEntity, Seller> {
    private static SellerConverter instance;

    public Seller toDto(SellerEntity entity) {
        return new Seller(entity.getId(), entity.getName(), entity.getLocation(), entity.getContact());
    }

    public SellerEntity toEntity(Seller item) {
        SellerEntity entity = new SellerEntity();
        entity.setId(item.id());
        entity.setName(item.name());
        entity.setLocation(item.location());
        entity.setContact(item.contact());

        return entity;
    }

    public static SellerConverter getInstance() {
        if (instance == null) {
            instance = new SellerConverter();
        }

        return instance;
    }
}
