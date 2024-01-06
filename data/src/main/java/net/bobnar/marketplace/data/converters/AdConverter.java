package net.bobnar.marketplace.data.converters;

import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;
import net.bobnar.marketplace.data.entities.AdEntity;

public class AdConverter extends ConverterBase<AdEntity, Ad> {
    private static AdConverter instance;

    public Ad toDto(AdEntity entity) {
        return new Ad(entity.getId(), entity.getTitle(), entity.getSource(), entity.getSourceId(), entity.getOriginalUri(), entity.getPhotoUri(), entity.getSellerId(), entity.getModelId(), entity.getOtherData());
    }

    public AdEntity toEntity(Ad item) {
        AdEntity entity = new AdEntity();
        entity.setId(item.getId());
        entity.setTitle(item.getTitle());
        entity.setSource(item.getSource());
        entity.setSourceId(item.getSourceId());
        entity.setOriginalUri(item.getOriginalUri());
        entity.setPhotoUri(item.getPhotoUri());
        entity.setSellerId(item.getSellerId());
        entity.setModelId(item.getModelId());
        entity.setOtherData(item.getOtherData());

        return entity;
    }

    public static AdConverter getInstance() {
        if (instance == null) {
            instance = new AdConverter();
        }

        return instance;
    }
}
