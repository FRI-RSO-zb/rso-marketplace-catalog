package net.bobnar.marketplace.catalog.api.v1.controllers;

import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.rest.beans.QueryParameters;
import net.bobnar.marketplace.catalog.services.repositories.SellersRepository;
import net.bobnar.marketplace.common.dtos.catalog.v1.sellers.Seller;
//import org.eclipse.microprofile.metrics.ConcurrentGauge;
//import org.eclipse.microprofile.metrics.Meter;
//import org.eclipse.microprofile.metrics.annotation.Metered;
//import org.eclipse.microprofile.metrics.annotation.Metric;
//import org.eclipse.microprofile.metrics.annotation.Timed;
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
@Path("sellers")
@Tag(name="Sellers", description = "Endpoints for managing sellers.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class SellersController {

//    @Inject
//    @Metric(name="sellers_counter")
//    private ConcurrentGauge sellersCounter;
//
//    @Inject
//    @Metric(name="sellers_adding_meter")
//    private Meter sellersAddingMeter;

    @Inject
    SellersRepository sellers;


    @GET
    @Operation(
            summary = "Get filtered sellers list",
            description = "Get list of all sellers that match the specified filter."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Resulting list of sellers.",
                    content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = Seller.class)),
                    headers = {@Header(name = "X-Total-Count", schema = @Schema(type = SchemaType.INTEGER), description = "Total count of elements matching the query")}
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Bad request. Malformed query."
            )
    })
//    @Timed(name="sellers_get_timer")
    public Response getSellers(
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
                    example = "id:eq:1"
            )
            String where,
            @Context UriInfo uriInfo
    ) {
        QueryParameters query = this.getRequestQuery(uriInfo);
        List<Seller> result = sellers.getSellers(query);
        Long allItemsCount = sellers.countQueriedItems(query);

        return Response
                .ok(result)
                .header("X-Total-Count", allItemsCount)
                .build();
    }

    @GET
    @Path("count")
    @Operation(
            summary = "Get count of sellers that match specified filter",
            description = "Filter the list of all sellers and return the total count of all items."
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
//    @Timed(name="sellers_count_timer")
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
        Long count = sellers.countQueriedItems(this.getRequestQuery(uriInfo));

        return Response.ok(count).build();
    }

    @GET
    @Path("{id}")
    @Operation(
            summary = "Get seller by id",
            description = "Find the seller with the specified identifier."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Resulting seller item.",
                    content = @Content(schema = @Schema(implementation = Seller.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Seller with specified id does not exist."
            )
    })
//    @Timed(name="sellers_get_seller_timer")
    public Response getSeller(@Parameter(description = "Seller identifier.", required = true) @PathParam("id") Integer id) {
        Seller seller = sellers.getSeller(id);

        return seller != null ?
                Response.ok(seller).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Operation(
            summary = "Create seller",
            description = "Creates the seller item using specified details."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Seller item created.",
                    content = @Content(schema = @Schema(implementation = Seller.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid information specified."
            )
    })
//    @Timed(name="sellers_create_timer")
//    @Metered(name="sellers_create_meter")
    public Response createSeller(
            @RequestBody(description = "Seller item", required = true, content = @Content(schema = @Schema(implementation = Seller.class)))
            Seller item) {

        if (item.id() != null  || item.name() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        item = sellers.createSeller(item);
//        this.sellersAddingMeter.mark();
//        this.sellersCounter.inc();

        return Response.status(Response.Status.CREATED)
                .entity(item)
                .build();
    }

    @PUT
    @Path("{id}")
    @Operation(
            summary = "Update seller",
            description = "Update the seller item using specified details."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Ad item updated.",
                    content = @Content(schema = @Schema(implementation = Seller.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid information specified."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Seller with specified identifier not found."
            )
    })
//    @Timed(name="sellers_update_timer")
//    @Metered(name="sellers_update_meter")
    public Response updateSeller(@Parameter(description = "Seller identifier.", required = true) @PathParam("id") Integer id, Seller item) {
        item = sellers.updateSeller(id, item);

        return item != null ?
                Response.ok(item).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{id}")
    @Operation(
            summary = "Remove seller",
            description = "Removes the seller with specified identifier."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "Seller was deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Seller with specified identifier not found."
            )
    })
//    @Timed(name="sellers_delete_timer")
//    @Metered(name="sellers_delete_meter")
    public Response deleteSeller(@Parameter(description = "Seller identifier.", required = true) @PathParam("id") Integer id) {
        boolean result = sellers.deleteSeller(id);

//        sellersCounter.dec();

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
