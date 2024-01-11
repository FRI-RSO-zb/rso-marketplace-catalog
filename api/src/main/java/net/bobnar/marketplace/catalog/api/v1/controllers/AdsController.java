package net.bobnar.marketplace.catalog.api.v1.controllers;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import net.bobnar.marketplace.catalog.services.repositories.AdsRepository;
import net.bobnar.marketplace.catalog.services.repositories.RepositoryBase;
import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;
import net.bobnar.marketplace.data.entities.AdEntity;
import org.eclipse.microprofile.metrics.ConcurrentGauge;
import org.eclipse.microprofile.metrics.Meter;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Metric;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@Log
@Path("ads")
@Tag(name="Ads", description = "Endpoints for managing ad items.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@CrossOrigin(name="ads", allowOrigin = "*", exposedHeaders = "X-Total-Count", supportedMethods = "GET, HEAD, PUT, POST, OPTIONS, DELETE")
public class AdsController extends CRUDControllerBase<AdEntity, Ad> {
    private Logger log = Logger.getLogger(AdsController.class.getName());

    private final String MetricsPrefix = "ads_";
    @Inject @Metric(name=MetricsPrefix+MetricsCounterName)
    private ConcurrentGauge itemsCounter;
    @Inject @Metric(name=MetricsPrefix+MetricsAddingMeterName)
    private Meter addingMeter;
    @Inject @Metric(name=MetricsPrefix+MetricsRemovingMeterName)
    private Meter removingMeter;

    @Inject
    private AdsRepository adsRepo;

    @GET
    @Operation(
            summary = "Get filtered list of ads",
            description = "Filter the list of all ads with specified filter and return the results."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Resulting list of ads.",
                    content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = Ad.class)),
                    headers = {@Header(name = "X-Total-Count", schema = @Schema(type = SchemaType.INTEGER), description = "Total count of elements matching the query")}
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Bad request. Malformed query."
            )
    })
    @Timed(name=MetricsPrefix+MetricsGetListOperationName+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsGetListOperationName+MetricsMeterSuffix)
    public Response getAds(
            @QueryParam("limit")
            @Parameter(name = "limit",
                    description = "Limit the number of returned results",
                    in = ParameterIn.QUERY,
                    example = "10")
            Integer limit,
            @QueryParam("offset")
            @Parameter(
                    name = "offset",
                    in = ParameterIn.QUERY,
                    description = "Offset for filtering and pagination"
            )
            Integer offset,
            @QueryParam("where")
            @Parameter(
                    name = "where",
                    in = ParameterIn.QUERY,
                    description = "Where filter",
                    examples = { @ExampleObject(name="Empty", value=""), @ExampleObject(name="Filter by seller", value="sellerId:eq:1") }
            )
            String where,
            @QueryParam("order")
            @Parameter(
                    name = "order",
                    in = ParameterIn.QUERY,
                    description = "Sorting strategy",
                    examples = { @ExampleObject(name="Empty", value=""), @ExampleObject(name="Sort by brand id", value="brandId ASC"), @ExampleObject(name="Sort by brand id", value="model.brand.name ASC")  }
            )
            String order,
            @QueryParam("ids")
            @Parameter(
                    name = "ids",
                    in = ParameterIn.QUERY,
                    description = "List of ids to query. Exclusive parameter that overrides all other."
            )
            List<Integer> ids,
            @QueryParam("sourceIds")
            @Parameter(
                    name = "sourceIds",
                    in = ParameterIn.QUERY,
                    description = "List of source ad ids to query. Exclusive parameter that overrides all other, except sources."
            )
            List<String> sourceIds,
            @QueryParam("sources")
            @Parameter(
                    name = "sources",
                    in = ParameterIn.QUERY,
                    description = "List of ads with correct source to query. Exclusive parameter that must be used in combination with sourceIds",
                    examples = { @ExampleObject(name="Empty", value=""), @ExampleObject(name="Avtonet", value="avtonet"), @ExampleObject(name="Doberavto", value="doberavto") }
            )
            List<String> sources,
            @Context UriInfo uriInfo
    ) {
        if (!ids.isEmpty()) {
            return respondGetItemsByIds(ids);
        }

        if (!sourceIds.isEmpty()) {
            List<Ad> results = adsRepo.toDtoList(adsRepo.getAdsBySourceIds(sourceIds).stream().filter(e -> sources.contains(e.getSource())));
            return respondWithItemList(results, results.size());
        }

        if (!sources.isEmpty()) {
            return respondBadRequestWithError("Sources is set, but source ids is empty.");
        }

        return respondGetQueryItemsResponse(limit, offset, where, order, uriInfo);
    }

    @GET
    @Path("/by-seller/{sellerId}")
    @Operation(
            summary = "Get filtered list of ads from specified seller",
            description = "Filter the list of all ads from seller with specified filter and return the results."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Resulting list of ads.",
                    content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = Ad.class)),
                    headers = {@Header(name = "X-Total-Count", schema = @Schema(type = SchemaType.INTEGER), description = "Total count of elements matching the query")}
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Bad request. Malformed query."
            )
    })
    @Timed(name=MetricsPrefix+MetricsGetListOperationName+"_by_seller"+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsGetListOperationName+"_by_seller"+MetricsMeterSuffix)
    public Response getSellerAds(
            @Parameter(description = "Seller identifier", required = true) @PathParam("sellerId") Integer sellerId,
            @QueryParam("limit")
            @Parameter(name = "limit",
                    description = "Limit the number of returned results",
                    in = ParameterIn.QUERY,
                    example = "10")
            Integer limit
    ) {
        List<AdEntity> result = adsRepo.getAdsFromSeller(sellerId);

        return respondWithItemList(adsRepo.toDtoList(result), result.size());
    }

    @GET
    @Path("{id}")
    @Operation(
            summary = "Get ad by id",
            description = "Find the ad with the specified identifier."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Resulting ad item.",
                    content = @Content(schema = @Schema(implementation = Ad.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Ad with specified id does not exist."
            )
    })
    @Timed(name=MetricsPrefix+MetricsGetItemOperationName+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsGetItemOperationName+MetricsMeterSuffix)
    public Response getAd(@Parameter(description = "Ad identifier.", required = true) @PathParam("id") Integer id) {
        return respondGetItemById(id);
    }

    @POST
    @Operation(
            summary = "Create ads",
            description = "Creates the ads items using specified details."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Ad items created.",
                    content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = Ad.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid information specified."
            )
    })
    @Timed(name=MetricsPrefix+MetricsCreateItemsOperationName+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsCreateItemsOperationName+MetricsMeterSuffix)
    public Response createAds(
            @RequestBody(description = "Ad items", required = true, content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = Ad.class)))
            List<Ad> items) {
        for (Ad item : items) {
            if (item.getTitle() == null) {
                return respondBadRequestWithError("Missing item title");
            } else if (item.getSource() == null) {
                return respondBadRequestWithError("Missing item source");
//            } else if (item.getSellerId() == null) {
//                return respondBadRequestWithError("Missing item seller id");
            }
        }

        return respondCreateItems(items);
    }

    @PUT
    @Path("{id}")
    @Operation(
            summary = "Update ad",
            description = "Update the ad item using specified details."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Ad item updated.",
                    content = @Content(schema = @Schema(implementation = Ad.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid information specified."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Ad with specified identifier not found."
            )
    })
    @Timed(name=MetricsPrefix+MetricsUpdateItemOperationName+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsUpdateItemOperationName+MetricsMeterSuffix)
    public Response updateAd(@Parameter(description = "Ad identifier.", required = true) @PathParam("id") Integer id, Ad item) {
        return respondUpdateItem(id, item);
    }

    @DELETE
    @Path("{id}")
    @Operation(
            summary = "Remove ad",
            description = "Removes the ad with specified identifier."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "Ad was deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Ad with specified identifier not found."
            )
    })
    @Timed(name=MetricsPrefix+MetricsDeleteItemOperationName+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsDeleteItemOperationName+MetricsMeterSuffix)
    public Response deleteAd(@Parameter(description = "Ad identifier.", required = true) @PathParam("id") Integer id) {
        return respondDeleteItemById(id);
    }

    @Override
    protected RepositoryBase<AdEntity, Ad> getMainRepository() {
        return adsRepo;
    }

    @Override
    protected Meter getMetricsAddingMeter() {
        return addingMeter;
    }

    @Override
    protected Meter getMetricsRemovingMeter() {
        return removingMeter;
    }

    @Override
    protected ConcurrentGauge getMetricsItemsCounterGauge() {
        return itemsCounter;
    }
}
