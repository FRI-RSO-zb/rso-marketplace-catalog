package net.bobnar.marketplace.data.converters;

import net.bobnar.marketplace.common.dtos.catalog.v1.carBrands.CarBrand;
import net.bobnar.marketplace.data.entities.CarBrandEntity;

public class CarBrandConverter extends ConverterBase<CarBrandEntity, CarBrand> {
    private static CarBrandConverter instance;

    public CarBrand toDto(CarBrandEntity entity) {
        return new CarBrand(entity.getId(), entity.getName(), entity.getPrimaryIdentifier(), entity.getIdentifiers(), (long) entity.getModels().size());
    }

    public CarBrandEntity toEntity(CarBrand item) {
        CarBrandEntity entity = new CarBrandEntity();
        entity.setId(item.getId());
        entity.setName(item.getName());
        entity.setPrimaryIdentifier(item.getPrimaryIdentifier());
        entity.setIdentifiers(item.getIdentifiers());

        return entity;
    }

    public static CarBrandConverter getInstance() {
        if (instance == null) {
            instance = new CarBrandConverter();
        }

        return instance;
    }
}
