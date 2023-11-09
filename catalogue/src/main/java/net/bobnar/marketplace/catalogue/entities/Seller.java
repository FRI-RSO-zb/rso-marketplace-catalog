package net.bobnar.marketplace.catalogue.entities;


import javax.persistence.*;

@Entity
@Table(name="sellers")
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    public Integer getId() {
        return this.id;
    }
}
