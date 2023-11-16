package net.bobnar.marketplace.catalogue.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.annotation.Annotation;

@Entity
@Table(name="ads")
@NamedQuery(
        name="Ads.findAll",
        query="SELECT e FROM AdEntity e"
)
public class AdEntity implements Serializable, Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String source;
    private String title;
    private String originalUri;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalUri() {
        return originalUri;
    }

    public void setOriginalUri(String orginalUri) {
        this.originalUri = orginalUri;
    }

    @Override
    public String name() {
        return "AdEntity";
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
