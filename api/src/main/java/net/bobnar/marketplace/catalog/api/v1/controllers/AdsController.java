package net.bobnar.marketplace.catalog.api.v1.controllers;

import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.rest.beans.QueryParameters;
import net.bobnar.marketplace.catalog.services.repositories.AdsRepository;
import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;
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


@Log
@Path("ads")
@Tag(name="Ads", description = "Endpoints for managing ad items.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AdsController {


    @Inject
    @Metric(name="ads_counter")
    private ConcurrentGauge adsCounter;

    @Inject
    @Metric(name="ad_adding_meter")
    private Meter adsAddingMeter;

    @Inject
    private AdsRepository ads;

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
    @Timed(name="ads_get_timer")
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
                    example = "sellerId:eq:1"
            )
            String where,
            @Context UriInfo uriInfo
    ) {
        QueryParameters query = this.getRequestQuery(uriInfo);
        List<Ad> result = ads.getFilteredAds(query);
        Long allItemsCount = ads.countQueriedItems(query);

        return Response
                .ok(result)
                .header("X-Total-Count", allItemsCount)
                .build();
    }

    @GET
    @Path("count")
    @Operation(
            summary = "Get count of ads that match specified filter",
            description = "Filter the list of all ads and return the total count of all items."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Number of items.",
                    content = @Content(schema = @Schema(type = SchemaType.INTEGER))
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Bad request. Malformed query."
            )
    })
    @Timed(name="ads_count_timer")
    public Response getCount(
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
                    example = "sellerId:eq:1"
            )
            String where,
            @Context UriInfo uriInfo
    ) {
        Long count = ads.countQueriedItems(this.getRequestQuery(uriInfo));

        return Response.ok(count).build();
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
    @Timed(name="ads_get_ads_by_seller_timer")
    @Metered(name="ads_get_ads_by_sellers_meter")
    public Response getSellerAds(
            @Parameter(description = "Seller identifier", required = true) @PathParam("sellerId") Integer sellerId,
            @QueryParam("limit")
            @Parameter(name = "limit",
                    description = "Limit the number of returned results",
                    in = ParameterIn.QUERY,
                    example = "10")
            Integer limit
    ) {
        var result = ads.getAdsFromSeller(sellerId);

        return Response
                .ok(result)
                .header("X-Total-Count", result.size())
                .build();
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
    @Timed(name="ads_get_ad_timer")
    public Response getAd(@Parameter(description = "Ad identifier.", required = true) @PathParam("id") Integer id) {
        Ad result = ads.getAd(id);

        return result != null ?
                Response.ok(result).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Operation(
            summary = "Create ad",
            description = "Creates the ad item using specified details."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Ad item created.",
                    content = @Content(schema = @Schema(implementation = Ad.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid information specified."
            )
    })
    @Timed(name="ads_create_timer")
    @Metered(name="ads_create_meter")
    public Response createAd(
            @RequestBody(description = "Ad item", required = true, content = @Content(schema = @Schema(implementation = Ad.class)))
            Ad item) {

        if (item.id() != null  || item.title() == null || item.source() == null || item.sellerId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        item = ads.createAd(item);
        this.adsAddingMeter.mark();
        this.adsCounter.inc();

        return Response.status(Response.Status.CREATED)
                .entity(item)
                .build();
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
    @Timed(name="ads_update_timer")
    @Metered(name="ads_update_meter")
    public Response updateAd(@Parameter(description = "Ad identifier.", required = true) @PathParam("id") Integer id, Ad item) {
        item = ads.updateAd(id, item);

        return item != null ?
                Response.ok(item).build() :
                Response.status(Response.Status.NOT_FOUND).build();
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
    @Timed(name="ad_delete_timer")
    @Metered(name="ads_delete_meter")
    public Response deleteAd(@Parameter(description = "Ad identifier.", required = true) @PathParam("id") Integer id) {
        boolean result = ads.deleteAd(id);

        adsCounter.dec();

        return result ?
                Response.noContent().build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    private QueryParameters getRequestQuery(UriInfo uriInfo) {
        return QueryParameters.query(uriInfo.getRequestUri().getQuery())
                .maxLimit(50)
                .defaultLimit(10)
                .defaultOffset(0)
                .build();
    }
}
