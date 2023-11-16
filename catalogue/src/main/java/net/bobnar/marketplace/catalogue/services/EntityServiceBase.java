package net.bobnar.marketplace.catalogue.services;

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
}
