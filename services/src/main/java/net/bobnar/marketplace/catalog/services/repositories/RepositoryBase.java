package net.bobnar.marketplace.catalog.services.repositories;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import net.bobnar.marketplace.common.dtos.ItemBase;
import net.bobnar.marketplace.data.converters.ConverterBase;
import net.bobnar.marketplace.data.entities.EntityBase;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class RepositoryBase<T extends EntityBase<TDto>, TDto extends ItemBase> {

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

    public T get(Integer id) {
        T entity = this.em.find(this.getEntityClass(), id);

        return entity;
    }

    protected T queryGetSingleResultOrNull(TypedQuery<T> query) {
        List<T> results = query.setMaxResults(1).getResultList();

        if (!results.isEmpty()) {
            return results.get(0);
        }

        return null;
    }

    public T getByColumn(String columnName, String value) {
        TypedQuery<T> query = this.em.createQuery("SELECT e FROM " + this.getEntityClass().getName() + " e WHERE e." + columnName + "=:value", this.getEntityClass())
                .setParameter("value", value);

        return queryGetSingleResultOrNull(query);
    }

    public TDto getItem(Integer id) {
        TDto item = this.toDto(this.get(id));

        return item;
    }

    public List<T> getMultiple(List<Integer> ids) {
        TypedQuery<T> query = this.em.createQuery("SELECT e FROM " + this.getEntityClass().getName() + " e WHERE e.id in :ids", this.getEntityClass())
                .setParameter("ids", ids);

        return query.getResultList();
    }

    public List<T> getMultipleByColumn(String columnName, List<String> values) {
        TypedQuery<T> query = this.em.createQuery("SELECT e FROM " + this.getEntityClass().getName() + " e WHERE e." + columnName + " in :values", this.getEntityClass())
                .setParameter("values", values);

        return query.getResultList();
    }

    public List<TDto> getMultipleItems(List<Integer> ids) {
        List<T> result = this.getMultiple(ids);

        return toDtoList(result);
    }

    public List<T> find(QueryParameters query) {
        if (query == null) {
            query = new QueryParameters();
        }
        List<T> entities = JPAUtils.queryEntities(this.em, this.getEntityClass(), query);

        return entities;
    }

    public List<TDto> findItems(QueryParameters query) {
        List<T> result = this.find(query);

        return toDtoList(result);
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

    protected void persistAll(List<T> entities) {
        try {
            this.beginTransaction();
            for (T entity : entities) {
                this.persistNonTransactional(entity);
            }
            this.commitTransaction();
        } catch (Exception e) {
            this.rollbackTransaction();
            throw e;
        }
    }

    public void create(T entity) {
        this.persist(entity);

        if (entity.getId() == null) {
            throw new RuntimeException("Unable to save entity " + entity.getClass().getName());
        }
    }

    public void create(List<T> entities) {
        this.persistAll(entities);
    }

    public TDto createItem(TDto item) {
        T entity = this.toEntity(item);
        this.create(entity);

        return this.toDto(entity);
    }

    public List<TDto> createItems(List<TDto> items) {
        List<T> entities = this.toEntityList(items);
        this.create(entities);

        return this.toDtoList(entities);
    }

    public T update(Integer id, T updateEntity) {
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

    public boolean delete(Integer id) {
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

    public boolean deleteItem(TDto item) {
        if (item != null) {
            return this.delete(item.getId());
        }

        return false;
    }

    public int countQueriedItems(QueryParameters query) {
        if (query == null) {
            query = new QueryParameters();
        }
        return JPAUtils.queryEntitiesCount(this.em, this.getEntityClass(), query).intValue();
    }

    protected abstract Class<T> getEntityClass();

    protected abstract ConverterBase<T, TDto> getConverter();

    public TDto toDto(T entity) {
        return entity != null ?
                this.getConverter().toDto(entity) :
                null;
    }

    public List<TDto> toDtoList(List<T> entities) {
        return toDtoList(entities.stream());
    }

    public List<TDto> toDtoList(Stream<T> entitiesStream) {
        return entitiesStream.map(this::toDto).collect(Collectors.toList());
    }

    public T toEntity(TDto item) {
        return item != null ?
                this.getConverter().toEntity(item) :
                null;
    }

    public List<T> toEntityList(List<TDto> items) {
        return items.stream().map(this::toEntity).collect(Collectors.toList());
    }

    public EntityManager getEntityManager() {
        return this.em;
    }
}
