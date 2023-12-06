package net.bobnar.marketplace.catalog.services.repositories;

import com.kumuluz.ee.rest.beans.QueryParameters;
import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;
import net.bobnar.marketplace.data.converters.AdConverter;
import net.bobnar.marketplace.data.entities.AdEntity;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class AdsRepository extends RepositoryBase<AdEntity, Ad> {
    public List<Ad> getFilteredAds(QueryParameters query) {
        List<AdEntity> result = this.find(query);

        return result.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<Ad> getAdsFromSeller(Integer sellerId) {
        TypedQuery<AdEntity> query = this.getEntityManager().createNamedQuery("Ads.findBySellerId", AdEntity.class);
        query.setParameter("sellerId", sellerId);

        List<Ad> results = query.getResultStream().map(this::toDto).collect(Collectors.toList());

        return results;
    }

    public Ad getAd(Integer id) {
        Ad item = this.toDto(this.get(id));

        return item;
    }

    public Ad createAd(Ad item) {
        AdEntity entity = this.toEntity(item);
        this.persist(entity);

        if (entity.getId() == null) {
            throw new RuntimeException("Unable to save ad");
        }

        return this.toDto(entity);
    }

    public Ad updateAd(Integer id, Ad item) {
        AdEntity updated = this.update(id, this.toEntity(item));

        return this.toDto(updated);
    }

    public boolean deleteAd(Integer id)  {
        return this.delete(id);
    }

    public Long countQueriedItems(QueryParameters query) {
        return super.countQueriedItems(query);
    }

    @Override
    protected Class<AdEntity> getEntityClass() {
        return AdEntity.class;
    }

    @Override
    protected AdConverter getConverter() {
        return AdConverter.getInstance();
    }
}
