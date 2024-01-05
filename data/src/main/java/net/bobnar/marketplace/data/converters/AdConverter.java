package net.bobnar.marketplace.data.converters;

import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;
import net.bobnar.marketplace.data.entities.AdEntity;

public class AdConverter extends ConverterBase<AdEntity, Ad> {
    private static AdConverter instance;

    public Ad toDto(AdEntity entity) {
        return new Ad(entity.getId(), entity.getTitle(), entity.getSource(), entity.getSellerId(), entity.getBrandId(), entity.getModelId());
    }

    public AdEntity toEntity(Ad item) {
        AdEntity entity = new AdEntity();
        entity.setId(item.getId());
        entity.setTitle(item.getTitle());
        entity.setSource(item.getSource());
        entity.setSellerId(item.getSellerId());
        entity.setBrandId(item.getBrandId());
        entity.setModelId(item.getModelId());

        return entity;
    }

    public static AdConverter getInstance() {
        if (instance == null) {
            instance = new AdConverter();
        }

        return instance;
    }
}
