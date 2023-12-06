package net.bobnar.marketplace.data.converters;

import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;
import net.bobnar.marketplace.data.entities.AdEntity;

public class AdConverter extends ConverterBase<AdEntity, Ad> {
    private static AdConverter instance;

    public Ad toDto(AdEntity entity) {
        return new Ad(entity.getId(), entity.getTitle(), entity.getSource(), entity.getSellerId());
    }

    public AdEntity toEntity(Ad item) {
        AdEntity entity = new AdEntity();
        entity.setId(item.id());
        entity.setTitle(item.title());
        entity.setSource(item.source());
        entity.setSellerId(item.sellerId());

        return entity;
    }

    public static AdConverter getInstance() {
        if (instance == null) {
            instance = new AdConverter();
        }

        return instance;
    }
}
