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
import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;
import net.bobnar.marketplace.common.dtos.catalog.v1.carBrands.CarBrand;
import net.bobnar.marketplace.common.dtos.catalog.v1.carModels.CarModel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@GraphQLClass
@ApplicationScoped
public class CarModelsGraph {
    @Inject
    CarModelsRepository repo;
    @Inject
    AdsRepository adsRepo;
    @Inject
    CarBrandsRepository brandsRepo;

    @GraphQLQuery
    public PaginationWrapper<CarModel> getModels(@GraphQLArgument(name = "pagination") Pagination pagination,
                                                 @GraphQLArgument(name = "sort") Sort sort,
                                                 @GraphQLArgument(name = "filter") Filter filter,
                                                 @GraphQLEnvironment ResolutionEnvironment resolutionEnvironment) {
        return GraphQLUtils.process(repo.findItems(new QueryParameters()), pagination, sort, filter);
    }

    @GraphQLQuery
    public CarBrand getBrand(@GraphQLContext CarModel model) {
        return brandsRepo.getItem(model.getBrandId());
    }

    @GraphQLQuery
    public PaginationWrapper<Ad> getAds(@GraphQLContext CarModel model,
                                        @GraphQLArgument(name = "pagination") Pagination pagination,
                                        @GraphQLArgument(name = "sort") Sort sort,
                                        @GraphQLArgument(name = "filter") Filter filter,
                                        @GraphQLEnvironment ResolutionEnvironment resolutionEnvironment) {
        return GraphQLUtils.process(adsRepo.getAdsByModel(model.getId()), pagination, sort, filter);
    }

    @GraphQLQuery
    public CarModel getModel(@GraphQLArgument(name = "id") Integer id) {
        return repo.getItem(id);
    }

}
