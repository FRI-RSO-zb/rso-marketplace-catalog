package net.bobnar.marketplace.data.entities;

import net.bobnar.marketplace.common.dtos.ItemBase;
import net.bobnar.marketplace.data.converters.ToDtoConvertable;

import javax.persistence.*;

@MappedSuperclass
public abstract class EntityBase<TDto extends ItemBase> implements ToDtoConvertable<TDto> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
