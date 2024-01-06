package net.bobnar.marketplace.catalog.api.graphql;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import com.kumuluz.ee.graphql.classes.Filter;
import com.kumuluz.ee.graphql.classes.Pagination;
import com.kumuluz.ee.graphql.classes.PaginationWrapper;
import com.kumuluz.ee.graphql.classes.Sort;
import com.kumuluz.ee.graphql.utils.GraphQLUtils;
import com.kumuluz.ee.rest.beans.QueryParameters;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import net.bobnar.marketplace.catalog.services.repositories.AdsRepository;
import net.bobnar.marketplace.catalog.services.repositories.CarBrandsRepository;
import net.bobnar.marketplace.catalog.services.repositories.CarModelsRepository;
import net.bobnar.marketplace.catalog.services.repositories.SellersRepository;
import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;
import net.bobnar.marketplace.common.dtos.catalog.v1.carBrands.CarBrand;
import net.bobnar.marketplace.common.dtos.catalog.v1.carModels.CarModel;
import net.bobnar.marketplace.common.dtos.catalog.v1.sellers.Seller;
import net.bobnar.marketplace.data.entities.AdEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@GraphQLClass
@ApplicationScoped
public class AdsGraph {
    @Inject
    private AdsRepository repo;
    @Inject
    private SellersRepository sellersRepo;
    @Inject
    private CarBrandsRepository brandsRepo;
    @Inject
    private CarModelsRepository modelsRepo;

    @GraphQLQuery
    public PaginationWrapper<Ad> getAds(@GraphQLArgument(name = "pagination") Pagination pagination,
                                              @GraphQLArgument(name = "sort") Sort sort,
                                              @GraphQLArgument(name = "filter") Filter filter) {

        return GraphQLUtils.process(repo.findItems(new QueryParameters()), pagination, sort, filter);
    }

    @GraphQLQuery
    public Ad getAd(@GraphQLArgument(name = "id") Integer id) {
        return repo.getItem(id);
    }

    @GraphQLQuery
    public Seller getSeller(@GraphQLContext Ad ad) {
        if (ad.getSellerId() == null) {
            return null;
        }

        return sellersRepo.getItem(ad.getSellerId());
    }

    @GraphQLQuery
    public CarModel getModel(@GraphQLContext Ad ad) {
        if (ad.getModelId() == null) {
            return null;
        }

        return modelsRepo.getItem(ad.getModelId());
    }

    @GraphQLMutation
    public Ad createAd(@GraphQLArgument(name="ad") Ad item) {
        return repo.createItem(item);
    }

    @GraphQLMutation
    public Boolean deleteAd(@GraphQLArgument(name="id") Integer id) {
        return repo.delete(id);
    }

}
