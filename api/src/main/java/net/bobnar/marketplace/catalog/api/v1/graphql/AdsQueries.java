package net.bobnar.marketplace.catalog.api.v1.graphql;

import com.kumuluz.ee.rest.beans.QueryParameters;
import net.bobnar.marketplace.catalog.services.repositories.AdsRepository;
import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import com.kumuluz.ee.graphql.classes.Filter;
import com.kumuluz.ee.graphql.classes.Pagination;
import com.kumuluz.ee.graphql.classes.PaginationWrapper;
import com.kumuluz.ee.graphql.classes.Sort;
import com.kumuluz.ee.graphql.utils.GraphQLUtils;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@GraphQLClass
@ApplicationScoped
public class AdsQueries {
    @Inject
    private AdsRepository ads;

    @GraphQLQuery
    public PaginationWrapper<Ad> allAds(@GraphQLArgument(name = "pagination") Pagination pagination,
                                                  @GraphQLArgument(name = "sort") Sort sort,
                                                  @GraphQLArgument(name = "filter") Filter filter) {

        return GraphQLUtils.process(ads.getFilteredAds(new QueryParameters()), pagination, sort, filter);
    }

    @GraphQLQuery
    public Ad getAd(@GraphQLArgument(name = "id") Integer id) {
        return ads.getAd(id);
    }

}
