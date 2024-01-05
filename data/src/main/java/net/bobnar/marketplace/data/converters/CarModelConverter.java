package net.bobnar.marketplace.data.converters;

import net.bobnar.marketplace.common.dtos.catalog.v1.carBrands.CarBrand;
import net.bobnar.marketplace.common.dtos.catalog.v1.carModels.CarModel;
import net.bobnar.marketplace.data.entities.CarBrandEntity;
import net.bobnar.marketplace.data.entities.CarModelEntity;

public class CarModelConverter extends ConverterBase<CarModelEntity, CarModel> {
    private static CarModelConverter instance;

    public CarModel toDto(CarModelEntity entity) {
        return new CarModel(entity.getId(), entity.getName(), entity.getBrandId(), entity.getPrimaryIdentifier(), entity.getIdentifiers());
    }

    public CarModelEntity toEntity(CarModel item) {
        CarModelEntity entity = new CarModelEntity();
        entity.setId(item.getId());
        entity.setName(item.getName());
        entity.setBrandId(item.getBrandId());
        entity.setPrimaryIdentifier(item.getPrimaryIdentifier());
        entity.setIdentifiers(item.getIdentifiers());

        return entity;
    }

    public static CarModelConverter getInstance() {
        if (instance == null) {
            instance = new CarModelConverter();
        }

        return instance;
    }
}
