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
import io.leangen.graphql.annotations.GraphQLEnvironment;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.execution.ResolutionEnvironment;
import net.bobnar.marketplace.catalog.services.repositories.AdsRepository;
import net.bobnar.marketplace.catalog.services.repositories.CarBrandsRepository;
import net.bobnar.marketplace.catalog.services.repositories.CarModelsRepository;
import net.bobnar.marketplace.catalog.services.repositories.SellersRepository;
import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;
import net.bobnar.marketplace.common.dtos.catalog.v1.carBrands.CarBrand;
import net.bobnar.marketplace.common.dtos.catalog.v1.carModels.CarModel;
import net.bobnar.marketplace.common.dtos.catalog.v1.sellers.Seller;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@GraphQLClass
@ApplicationScoped
public class CarBrandsGraph {
    @Inject
    CarBrandsRepository repo;
    @Inject
    AdsRepository adsRepo;
    @Inject
    CarModelsRepository modelsRepo;

    @GraphQLQuery
    public PaginationWrapper<CarBrand> getBrands(@GraphQLArgument(name = "pagination") Pagination pagination,
                                                 @GraphQLArgument(name = "sort") Sort sort,
                                                 @GraphQLArgument(name = "filter") Filter filter,
                                                 @GraphQLEnvironment ResolutionEnvironment resolutionEnvironment) {
        return GraphQLUtils.process(repo.findItems(new QueryParameters()), pagination, sort, filter);
    }

    @GraphQLQuery
    public PaginationWrapper<CarModel> getModels(@GraphQLContext CarBrand brand,
                                                 @GraphQLArgument(name = "pagination") Pagination pagination,
                                                 @GraphQLArgument(name = "sort") Sort sort,
                                                 @GraphQLArgument(name = "filter") Filter filter,
                                                 @GraphQLEnvironment ResolutionEnvironment resolutionEnvironment) {
        return GraphQLUtils.process(modelsRepo.getModelsByBrand(brand.getId()), pagination, sort, filter);
    }

    @GraphQLQuery
    public PaginationWrapper<Ad> getAds(@GraphQLContext CarBrand brand,
                                        @GraphQLArgument(name = "pagination") Pagination pagination,
                                        @GraphQLArgument(name = "sort") Sort sort,
                                        @GraphQLArgument(name = "filter") Filter filter,
                                        @GraphQLEnvironment ResolutionEnvironment resolutionEnvironment) {
        return GraphQLUtils.process(adsRepo.getAdsByBrand(brand.getId()), pagination, sort, filter);
    }

    @GraphQLQuery
    public CarBrand getBrand(@GraphQLArgument(name = "id") Integer id) {
        return repo.getItem(id);
    }

}
