package net.bobnar.marketplace.data.converters;

import net.bobnar.marketplace.common.dtos.ItemBase;

public interface ToDtoConvertable<TDto extends ItemBase> {
    TDto toDto();
}
