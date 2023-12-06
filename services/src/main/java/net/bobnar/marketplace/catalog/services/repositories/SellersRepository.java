package net.bobnar.marketplace.catalog.services.repositories;

import com.kumuluz.ee.rest.beans.QueryParameters;
import net.bobnar.marketplace.common.dtos.catalog.v1.sellers.Seller;
import net.bobnar.marketplace.data.converters.SellerConverter;
import net.bobnar.marketplace.data.entities.SellerEntity;

import javax.enterprise.context.RequestScoped;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class SellersRepository extends RepositoryBase<SellerEntity, Seller> {

    public List<Seller> getSellers(QueryParameters query) {
        List<SellerEntity> result = this.find(query);

        return result.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Seller getSeller(Integer id) {
        Seller item = this.toDto(this.get(id));

        return item;
    }

    public Seller createSeller(Seller item) {
        SellerEntity entity = this.toEntity(item);
        this.persist(entity);

        if (entity.getId() == null) {
            throw new RuntimeException("Unable to save seller");
        }

        return this.toDto(entity);
    }

    public Seller updateSeller(Integer id, Seller item) {
        SellerEntity updated = this.update(id, this.toEntity(item));

        return this.toDto(updated);
    }

    public boolean deleteSeller(Integer id) {
        return this.delete(id);
    }

    public Long countQueriedItems(QueryParameters query) {
        return super.countQueriedItems(query);
    }

    @Override
    protected Class<SellerEntity> getEntityClass() {
        return SellerEntity.class;
    }

    @Override
    protected SellerConverter getConverter() {
        return SellerConverter.getInstance();
    }
}
