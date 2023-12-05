package net.bobnar.marketplace.catalogue.services;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import net.bobnar.marketplace.catalogue.entities.AdEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@RequestScoped
public abstract class EntityServiceBase {

    @Inject
    protected EntityManager em;

    protected void beginTransaction() {
        if (!this.em.getTransaction().isActive()) {
            this.em.getTransaction().begin();
        }
    }

    protected void commitTransaction() {
        if (this.em.getTransaction().isActive()) {
            this.em.getTransaction().commit();
        }
    }

    protected void rollbackTransaction() {
        if (this.em.getTransaction().isActive()) {
            this.em.getTransaction().rollback();
        }
    }

    public Long getQueryItemsCount(QueryParameters query, Class<?> itemClass) {
        return JPAUtils.queryEntitiesCount(this.em, itemClass, query);
    }
}
