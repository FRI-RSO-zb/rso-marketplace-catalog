package net.bobnar.marketplace.data.converters;

import net.bobnar.marketplace.data.entities.EntityBase;

public abstract class ConverterBase<TEntity extends EntityBase, TDto> {
    public abstract TDto toDto(TEntity entity);

    public abstract TEntity toEntity(TDto item);
}
