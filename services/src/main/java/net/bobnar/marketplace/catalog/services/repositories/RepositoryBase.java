package net.bobnar.marketplace.catalog.services.repositories;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import net.bobnar.marketplace.common.dtos.ItemBase;
import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;
import net.bobnar.marketplace.data.converters.ConverterBase;
import net.bobnar.marketplace.data.entities.AdEntity;
import net.bobnar.marketplace.data.entities.EntityBase;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public abstract class RepositoryBase<T extends EntityBase, TDto extends ItemBase> {

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

    public T getEntity(Integer id) {
        return this.get(id);
    }

    public TDto getItem(Integer id) {
        TDto item = this.toDto(this.get(id));

        return item;
    }

    protected List<T> find(QueryParameters query) {
        List<T> entities = JPAUtils.queryEntities(this.em, this.getEntityClass(), query);

        return entities;
    }

    public List<T> findEntities(QueryParameters query) {
        return this.find(query);
    }

    public List<TDto> findItems(QueryParameters query) {
        List<T> result = this.find(query);

        return result.stream().map(this::toDto).collect(Collectors.toList());
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

    public TDto createItem(TDto item) {
        T entity = this.toEntity(item);
        this.persist(entity);

        if (entity.getId() == null) {
            throw new RuntimeException("Unable to save item " + item.getClass().getName());
        }

        return this.toDto(entity);
    }

    protected T update(Integer id, T updateEntity) {
        T entity = this.get(id);
        if (entity == null) {
            return null;
        }

        this.merge(entity, updateEntity);

        return updateEntity;
    }

    public TDto updateItem(Integer id, TDto item) {
        T updated = this.update(id, this.toEntity(item));

        return this.toDto(updated);
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

    public boolean deleteItem(Integer id) {
        return this.delete(id);
    }

    public boolean deleteItem(TDto item) {
        if (item != null) {
            return this.delete(item.getId());
        }

        return false;
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

    public EntityManager getEntityManager() {
        return this.em;
    }
}
