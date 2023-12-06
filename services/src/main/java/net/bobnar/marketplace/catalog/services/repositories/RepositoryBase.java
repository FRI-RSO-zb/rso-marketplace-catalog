package net.bobnar.marketplace.catalog.services.repositories;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import net.bobnar.marketplace.data.converters.ConverterBase;
import net.bobnar.marketplace.data.entities.AdEntity;
import net.bobnar.marketplace.data.entities.EntityBase;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

public abstract class RepositoryBase<T extends EntityBase, TDto> {

    @Inject
    private EntityManager em;

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

    protected T get(Integer id) {
        var entity = this.em.find(this.getEntityClass(), id);

        return entity;
    }

    protected List<T> find(QueryParameters query) {
        List<T> entities = JPAUtils.queryEntities(this.em, this.getEntityClass(), query);

        return entities;
    }

    protected void persistNonTransactional(T entity) {
        this.em.persist(entity);
    }

    protected void persist(T entity) {
        try {
            this.beginTransaction();
            this.persistNonTransactional(entity);
            this.commitTransaction();
        } catch (Exception e) {
            this.rollbackTransaction();
            throw e;
        }
    }

    protected T update(Integer id, T updateEntity) {
        T entity = this.get(id);
        if (entity == null) {
            return null;
        }

        this.merge(entity, updateEntity);

        return updateEntity;
    }

    protected void merge(T sourceEntity, T updateEntity) {
        try {
            this.beginTransaction();

            updateEntity.setId(sourceEntity.getId());
            this.em.merge(updateEntity);

            this.commitTransaction();
        }
        catch (Exception e) {
            this.rollbackTransaction();
            throw e;
        }
    }

    protected boolean delete(Integer id) {
        T entity = this.get(id);
        if (entity == null) {
            return false;
        }

        try {
            this.beginTransaction();
            this.em.remove(entity);
            this.commitTransaction();
        } catch (Exception e) {
            this.rollbackTransaction();
            throw e;
        }

        return true;
    }

    protected Long countQueriedItems(QueryParameters query) {
        return JPAUtils.queryEntitiesCount(this.em, this.getEntityClass(), query);
    }

    protected abstract Class<T> getEntityClass();

    protected abstract ConverterBase<T, TDto> getConverter();

    protected TDto toDto(T entity) {
        return entity != null ?
                this.getConverter().toDto(entity) :
                null;
    }

    protected T toEntity(TDto item) {
        return item != null ?
                this.getConverter().toEntity(item) :
                null;
    }

    protected EntityManager getEntityManager() {
        return this.em;
    }
}
