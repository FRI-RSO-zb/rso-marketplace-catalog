package net.bobnar.marketplace.catalog.api.v1.controllers;

import com.kumuluz.ee.logs.cdi.Log;
import net.bobnar.marketplace.catalogue.entities.AdEntity;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Log
@Path("ads")
@Tag(name="Ads", description = "Endpoints for managing ad items.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class AdsController {

//    @Inject
//    @Metric(name="ad_counter")
//    private ConcurrentGauge adCounter;
//
//    @Inject
//    @Metric(name="ad_adding_meter")
//    private Meter addMeter;
//
//    @Inject
//    private AdService adService;

//    @Context
//    UriInfo uriInfo;

    @GET
//    @PermitAll
    @Operation(
            summary = "Get list of ads matching the filter criteria",
            description = "Filter the list of all ads with specified filter and return the paginated results."
    )
    @APIResponses({
            @APIResponse(
//                    responseCode = "200",
                    description = "Resulting list of entities.",
                    content = @Content(
                            schema = @Schema(
//                                    description = "List of ad elements",
//                                    type = SchemaType.ARRAY,
                                    implementation = AdEntity.class
                            )
                    ),
                    headers = {@Header(name = "X-Total-Count", schema = @Schema(type = SchemaType.INTEGER), description = "Total count of elements matching the query")}
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Bad request. Malformed query."
//                    content = @Content(mediaType = MediaType.APPLICATION_JSON)
            )
    })
//    @Timed(name="get-all-ads-timer")
    public Response getAllAds() {
//        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
//        QueryParameters query = new QueryParameters();
//        List<AdEntity> ads = adService.getAds(query);
//        Long allItemsCount = adService.getQueryItemsCount(query);

//        return Response.ok(ads)
//                .header("X-Total-Count", allItemsCount)
//                .build();
        return Response.ok("good").build();
    }

    @GET
//    @PermitAll
    @Path("{id}")
    public Response getAd(@PathParam("id") Integer id) {
//        AdEntity ad = adService.getAd(id);
//
//        return ad != null ?
//                Response.ok(ad).build() :
//                Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok().build();
    }

    @POST
    @RolesAllowed("user")
//    @Timed(name="create-ad-timer")
//    @Metered(name="ad-creating-meter")
    public Response createAd(/*AdEntity ad*/) {
//        addMeter.mark();
//        adCounter.inc();
//        adService.saveAd(ad);

//        return Response.noContent().build();

        return Response.ok().build();
    }

    @DELETE
//    @RolesAllowed("admin")
    @Path("{id}")
//    @Timed(name="delete-ad-timer")
//    @Metered(name="ad-deleting-meter")
    public Response deleteAd(@PathParam("id") Integer id) {
//        adService.deleteAd(id);
//        adCounter.dec();
//
//        return Response.noContent().build();

        return Response.ok().build();
    }
}
