package net.bobnar.marketplace.catalog.api.v1.controllers;

import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.rest.beans.QueryParameters;
import net.bobnar.marketplace.catalog.services.repositories.CarBrandsRepository;
import net.bobnar.marketplace.catalog.services.repositories.CarModelsRepository;
import net.bobnar.marketplace.common.dtos.catalog.v1.carBrands.CarBrand;
import net.bobnar.marketplace.common.dtos.catalog.v1.carModels.CarModel;
import net.bobnar.marketplace.common.dtos.catalog.v1.sellers.Seller;
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
@Path("brands")
@Tag(name="Car Brands", description = "Endpoints for managing car brands.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class CarBrandsController {

//    @Inject
//    @Metric(name="sellers_counter")
//    private ConcurrentGauge itemsCounter;
//
//    @Inject
//    @Metric(name="sellers_adding_meter")
//    private Meter itemsAddingMeter;

    @Inject
    CarBrandsRepository repo;

    @Inject
    CarModelsRepository modelsRepo;


    @GET
    @Operation(
            summary = "Get filtered car brands list",
            description = "Get list of all car brands that match the specified filter."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Resulting list of car brands.",
                    content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = CarBrand.class)),
                    headers = {@Header(name = "X-Total-Count", schema = @Schema(type = SchemaType.INTEGER), description = "Total count of elements matching the query")}
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Bad request. Malformed query."
            )
    })
//    @Timed(name="brands_get_timer")
    public Response getBrands(
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
                    example = "primaryIdentifier:eq:vw"
            )
            String where,
            @Context UriInfo uriInfo
    ) {
        QueryParameters query = this.getRequestQuery(uriInfo);
        List<CarBrand> result = repo.findItems(query);
        Long allItemsCount = repo.countQueriedItems(query);

        return Response
                .ok(result)
                .header("X-Total-Count", allItemsCount)
                .build();
    }

    @GET
    @Path("{id}")
    @Operation(
            summary = "Get car brand by id",
            description = "Find the car brand with the specified identifier."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Resulting car brand item.",
                    content = @Content(schema = @Schema(implementation = Seller.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Car brand with specified id does not exist."
            )
    })
//    @Timed(name="sellers_get_seller_timer")
    public Response getBrand(@Parameter(description = "Car brand identifier.", required = true) @PathParam("id") Integer id) {
        CarBrand item = repo.getItem(id);

        return item != null ?
                Response.ok(item).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("count")
    @Operation(
            summary = "Get count of car brands that match specified filter",
            description = "Filter the list of all car brands and return the total count of all items."
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
//    @Timed(name="brands_count_timer")
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
                    example = "primaryIdentifier:eq:vw"
            )
            String where,
            @Context UriInfo uriInfo
    ) {
        Long count = repo.countQueriedItems(this.getRequestQuery(uriInfo));

        return Response.ok(count).build();
    }

    @POST
    @Operation(
            summary = "Create car brand",
            description = "Creates the car brand item using specified details."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Car brand item created.",
                    content = @Content(schema = @Schema(implementation = CarBrand.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid information specified."
            )
    })
//    @Timed(name="brands_create_timer")
//    @Metered(name="brands_create_meter")
    public Response createBrand(
            @RequestBody(description = "Car brand item", required = true, content = @Content(schema = @Schema(implementation = CarBrand.class)))
            CarBrand item) {

        if (item.getId() != null  || item.getName() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        item = repo.createItem(item);
//        this.sellersAddingMeter.mark();
//        this.sellersCounter.inc();

        return Response.status(Response.Status.CREATED)
                .entity(item)
                .build();
    }

    @PUT
    @Path("{id}/identifiers")
    @Operation(
            summary = "Update car brand identifiers",
            description = "Update the car brand item using specified details."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Car brand item updated.",
                    content = @Content(schema = @Schema(implementation = CarBrand.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid information specified."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Car brand with specified identifier not found."
            )
    })
//    @Timed(name="sellers_update_timer")
//    @Metered(name="sellers_update_meter")
    public Response updateBrandIdentifiers(@Parameter(description = "Car brand identifier.", required = true) @PathParam("id") Integer id, String identifiers) {
        CarBrand item = repo.getItem(id);
        if (item != null) {
            item.setIdentifiers(identifiers);
            item = repo.updateItem(id, item);
        }

        return item != null ?
                Response.ok(item).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{id}")
    @Operation(
            summary = "Remove car brand",
            description = "Removes the car brand with specified identifier."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "Car brand was deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Car brand with specified identifier not found."
            )
    })
//    @Timed(name="sellers_delete_timer")
//    @Metered(name="sellers_delete_meter")
    public Response deleteBrand(@Parameter(description = "Car brand identifier.", required = true) @PathParam("id") Integer id) {
        boolean result = false;
        CarBrand item = repo.getItem(id);
        if (item != null && item.getModelsCount() == 0) {
            result = repo.deleteItem(id);
        }

        if (result) {
//        sellersCounter.dec();
        }

        return result ?
                Response.noContent().build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("{id}/models")
    @Operation(
            summary = "Get models of specified brand",
            description = "Find models that belong to the brand with the specified identifier."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Resulting car models.",
                    content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = CarModel.class)),
                    headers = {@Header(name = "X-Total-Count", schema = @Schema(type = SchemaType.INTEGER), description = "Total count of elements matching the query")}
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Car brand with specified id does not exist."
            )
    })
//    @Timed(name="sellers_get_seller_timer")
    public Response getBrandModels(@Parameter(description = "Brand identifier.", required = true) @PathParam("id") Integer id) {
        List<CarModel> items = modelsRepo.getModelsByBrand(id);

        return items != null ?
                Response.ok(items).build() :
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
