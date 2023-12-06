package net.bobnar.marketplace.data.entities;

import javax.persistence.*;

@MappedSuperclass
public abstract class EntityBase {
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
