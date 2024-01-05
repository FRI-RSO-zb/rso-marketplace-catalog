package net.bobnar.marketplace.catalog.services.repositories;

import com.kumuluz.ee.rest.beans.QueryParameters;
import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;
import net.bobnar.marketplace.data.converters.AdConverter;
import net.bobnar.marketplace.data.entities.AdEntity;

import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class AdsRepository extends RepositoryBase<AdEntity, Ad> {
    public List<Ad> getAdsFromSeller(Integer sellerId) {
        TypedQuery<AdEntity> query = this.getEntityManager().createNamedQuery("Ads.findBySellerId", AdEntity.class);
        query.setParameter("sellerId", sellerId);

        List<Ad> results = query.getResultStream().map(this::toDto).collect(Collectors.toList());

        return results;
    }

    public List<Ad> getAdsByBrand(Integer brandId) {
        TypedQuery<AdEntity> query = this.getEntityManager().createNamedQuery("Ads.findByBrandId", AdEntity.class);
        query.setParameter("brandId", brandId);

        List<Ad> results = query.getResultStream().map(this::toDto).collect(Collectors.toList());

        return results;
    }

    public List<Ad> getAdsByModel(Integer modelId) {
        TypedQuery<AdEntity> query = this.getEntityManager().createNamedQuery("Ads.findByModelId", AdEntity.class);
        query.setParameter("modelId", modelId);

        List<Ad> results = query.getResultStream().map(this::toDto).collect(Collectors.toList());

        return results;
    }

    @Override
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
