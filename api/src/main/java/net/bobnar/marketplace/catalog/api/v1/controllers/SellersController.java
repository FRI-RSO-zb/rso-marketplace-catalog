package net.bobnar.marketplace.catalog.api.v1.controllers;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import net.bobnar.marketplace.catalog.services.repositories.RepositoryBase;
import net.bobnar.marketplace.catalog.services.repositories.SellersRepository;
import net.bobnar.marketplace.common.dtos.catalog.v1.sellers.Seller;
//import org.eclipse.microprofile.metrics.ConcurrentGauge;
//import org.eclipse.microprofile.metrics.Meter;
//import org.eclipse.microprofile.metrics.annotation.Metered;
//import org.eclipse.microprofile.metrics.annotation.Metric;
//import org.eclipse.microprofile.metrics.annotation.Timed;
import net.bobnar.marketplace.data.entities.SellerEntity;
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
@Path("sellers")
@Tag(name="Sellers", description = "Endpoints for managing sellers.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@CrossOrigin(name="sellers", allowOrigin = "*", exposedHeaders = "X-Total-Count", supportedMethods = "GET, HEAD, PUT, POST, OPTIONS, DELETE")
public class SellersController extends CRUDControllerBase<SellerEntity, Seller> {
    private Logger log = Logger.getLogger(SellersController.class.getName());

    private final String MetricsPrefix = "sellers_";
    @Inject @Metric(name=MetricsPrefix+MetricsCounterName)
    private ConcurrentGauge itemsCounter;
    @Inject @Metric(name=MetricsPrefix+MetricsAddingMeterName)
    private Meter addingMeter;
    @Inject @Metric(name=MetricsPrefix+MetricsRemovingMeterName)
    private Meter removingMeter;

    @Inject
    SellersRepository sellersRepo;

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
    @Timed(name=MetricsPrefix+MetricsGetListOperationName+"_by_seller"+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsGetListOperationName+"_by_seller"+MetricsMeterSuffix)
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
                    examples = { @ExampleObject(name="Empty", value=""), @ExampleObject(name="Filter by id", value="id:eq:1") }
            )
            String where,
            @QueryParam("order")
            @Parameter(
                    name = "order",
                    in = ParameterIn.QUERY,
                    description = "Sorting strategy",
                    examples = { @ExampleObject(name="Empty", value=""), @ExampleObject(name="Sort by brand id", value="brandId ASC") }
            )
            String order,
            @QueryParam("ids")
            @Parameter(
                    name = "ids",
                    in = ParameterIn.QUERY,
                    description = "List of ids to query. Exclusive parameter that overrides all other."
            )
            List<Integer> ids,
            @Context UriInfo uriInfo
    ) {
        if (!ids.isEmpty()) {
            return respondGetItemsByIds(ids);
        }

        return respondGetQueryItemsResponse(limit, offset, where, order, uriInfo);
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
    @Timed(name=MetricsPrefix+MetricsGetItemOperationName+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsGetItemOperationName+MetricsMeterSuffix)
    public Response getSeller(@Parameter(description = "Seller identifier.", required = true) @PathParam("id") Integer id) {
        return respondGetItemById(id);
    }

    @POST
    @Operation(
            summary = "Create sellers",
            description = "Creates the seller items using specified details."
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
    @Timed(name=MetricsPrefix+MetricsCreateItemsOperationName+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsCreateItemsOperationName+MetricsMeterSuffix)
    public Response createSellers(
            @RequestBody(description = "Seller items", required = true, content = @Content(schema = @Schema(type = SchemaType.ARRAY,implementation = Seller.class)))
            List<Seller> items) {
        for (Seller item : items) {
            if (item.getName() == null) {
                return respondBadRequestWithError("Missing item name");
            }
        }

        return respondCreateItems(items);
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
    @Timed(name=MetricsPrefix+MetricsUpdateItemOperationName+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsUpdateItemOperationName+MetricsMeterSuffix)
    public Response updateSeller(@Parameter(description = "Seller identifier.", required = true) @PathParam("id") Integer id, Seller item) {
        return respondUpdateItem(id, item);
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
    @Timed(name=MetricsPrefix+MetricsDeleteItemOperationName+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsDeleteItemOperationName+MetricsMeterSuffix)
    public Response deleteSeller(@Parameter(description = "Seller identifier.", required = true) @PathParam("id") Integer id) {
        return respondDeleteItemById(id);
    }

    @Override
    protected RepositoryBase<SellerEntity, Seller> getMainRepository() {
        return sellersRepo;
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
