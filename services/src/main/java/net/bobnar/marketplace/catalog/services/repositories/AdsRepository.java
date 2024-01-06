package net.bobnar.marketplace.catalog.services.repositories;

import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;
import net.bobnar.marketplace.data.converters.AdConverter;
import net.bobnar.marketplace.data.entities.AdEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.util.List;

@ApplicationScoped
public class AdsRepository extends RepositoryBase<AdEntity, Ad> {
    public List<AdEntity> getAdsBySourceIds(List<String> sourceIds) {
        TypedQuery<AdEntity> query = this.getEntityManager().createQuery("SELECT e FROM " + this.getEntityClass().getName() + " e WHERE e.sourceId in :sourceIds", this.getEntityClass())
                .setParameter("sourceIds", sourceIds);

        return query.getResultList();
    }

    public List<AdEntity> getAdsFromSeller(Integer sellerId) {
        TypedQuery<AdEntity> query = this.getEntityManager().createNamedQuery("Ads.findBySellerId", AdEntity.class);
        query.setParameter("sellerId", sellerId);

        return query.getResultList();
    }

    public List<AdEntity> getAdsByBrand(Integer brandId) {
        TypedQuery<AdEntity> query = this.getEntityManager().createNamedQuery("Ads.findByBrandId", AdEntity.class);
        query.setParameter("brandId", brandId);

        return query.getResultList();
    }

    public List<AdEntity> getAdsByModel(Integer modelId) {
        TypedQuery<AdEntity> query = this.getEntityManager().createNamedQuery("Ads.findByModelId", AdEntity.class);
        query.setParameter("modelId", modelId);

        return query.getResultList();
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
