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
import net.bobnar.marketplace.catalog.services.repositories.SellersRepository;
import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;
import net.bobnar.marketplace.common.dtos.catalog.v1.sellers.Seller;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@GraphQLClass
@ApplicationScoped
public class SellersGraph {
    @Inject
    private SellersRepository sellersRepo;
    @Inject
    AdsRepository adsRepo;

    @GraphQLQuery
    public PaginationWrapper<Seller> getSellers(@GraphQLArgument(name = "pagination") Pagination pagination,
                                                @GraphQLArgument(name = "sort") Sort sort,
                                                @GraphQLArgument(name = "filter") Filter filter,
                                                @GraphQLEnvironment ResolutionEnvironment resolutionEnvironment) {
        return GraphQLUtils.process(sellersRepo.findItems(new QueryParameters()), pagination, sort, filter);
    }

    @GraphQLQuery
    public PaginationWrapper<Ad> getAds(@GraphQLContext Seller seller,
                                                @GraphQLArgument(name = "pagination") Pagination pagination,
                                                @GraphQLArgument(name = "sort") Sort sort,
                                                @GraphQLArgument(name = "filter") Filter filter,
                                                @GraphQLEnvironment ResolutionEnvironment resolutionEnvironment) {
        return GraphQLUtils.process(adsRepo.toDtoList(adsRepo.getAdsFromSeller(seller.getId())), pagination, sort, filter);
    }

    @GraphQLQuery
    public Seller getSeller(@GraphQLArgument(name = "id") Integer id) {
        return sellersRepo.getItem(id);
    }

}
