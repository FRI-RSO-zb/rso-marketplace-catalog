package net.bobnar.marketplace.catalogue.entities;

import javax.persistence.*;

@Entity
@Table(name="ads")
@NamedQuery(name="Ads.findAll", query="SELECT a FROM Ad a")
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String source;

    public Integer getId() {
        return this.id;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
