package net.bobnar.marketplace.catalog.api.v1.controllers;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import net.bobnar.marketplace.catalog.services.repositories.CarModelsRepository;
import net.bobnar.marketplace.catalog.services.repositories.RepositoryBase;
import net.bobnar.marketplace.common.dtos.catalog.v1.carModels.CarModel;
import net.bobnar.marketplace.data.entities.CarModelEntity;
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


@Log
@Path("models")
@Tag(name="Car Models", description = "Endpoints for managing car models.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@CrossOrigin(name="models", allowOrigin = "*", exposedHeaders = "X-Total-Count", supportedMethods = "GET, HEAD, PUT, POST, OPTIONS, DELETE")
public class CarModelsController extends CRUDControllerBase<CarModelEntity, CarModel> {
    private final String MetricsPrefix = "models_";
    @Inject @Metric(name=MetricsPrefix+MetricsCounterName)
    private ConcurrentGauge itemsCounter;
    @Inject @Metric(name=MetricsPrefix+MetricsAddingMeterName)
    private Meter addingMeter;
    @Inject @Metric(name=MetricsPrefix+MetricsRemovingMeterName)
    private Meter removingMeter;

    @Inject
    CarModelsRepository modelsRepo;

    @GET
    @Operation(
            summary = "Get filtered car models list",
            description = "Get list of all car models that match the specified filter."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Resulting list of car models.",
                    content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = CarModel.class)),
                    headers = {@Header(name = "X-Total-Count", schema = @Schema(type = SchemaType.INTEGER), description = "Total count of elements matching the query")}
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Bad request. Malformed query."
            )
    })
    @Timed(name=MetricsPrefix+MetricsGetListOperationName+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsGetListOperationName+MetricsMeterSuffix)
    public Response getModels(
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
                    example = "primaryIdentifier:eq:golf"
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
            summary = "Get car model by id",
            description = "Find the car model with the specified identifier."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Resulting car model item.",
                    content = @Content(schema = @Schema(implementation = CarModel.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Car model with specified id does not exist."
            )
    })
    @Timed(name=MetricsPrefix+MetricsGetItemOperationName+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsGetItemOperationName+MetricsMeterSuffix)
    public Response getModel(@Parameter(description = "Car model identifier.", required = true) @PathParam("id") Integer id) {
        return respondGetItemById(id);
    }

    @POST
    @Operation(
            summary = "Create car models",
            description = "Creates the car models using specified details."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Car model items created.",
                    content = @Content(schema = @Schema(implementation = CarModel.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid information specified."
            )
    })
    @Timed(name=MetricsPrefix+MetricsCreateItemsOperationName+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsCreateItemsOperationName+MetricsMeterSuffix)
    public Response createModels(
            @RequestBody(description = "Car model items", required = true, content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = CarModel.class)))
            List<CarModel> items) {
        for (CarModel item : items) {
            if (item.getName() == null) {
                return respondBadRequestWithError("Missing item name");
            }
            if (item.getBrandId() == null) {
                return respondBadRequestWithError("Missing item brand id");
            }
        }

        return respondCreateItems(items);
    }

    @PUT
    @Path("{id}")
    @Operation(
            summary = "Update car model",
            description = "Update the car model item using specified details."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Car model item updated.",
                    content = @Content(schema = @Schema(implementation = CarModel.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid information specified."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Car model with specified identifier not found."
            )
    })
    @Timed(name=MetricsPrefix+MetricsUpdateItemOperationName+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsUpdateItemOperationName+MetricsMeterSuffix)
    public Response updateModel(@Parameter(description = "Car model identifier.", required = true) @PathParam("id") Integer id, CarModel item) {
        return respondUpdateItem(id, item);
    }

    @DELETE
    @Path("{id}")
    @Operation(
            summary = "Remove car model",
            description = "Removes the car model with specified identifier."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "Car model was deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Car model with specified identifier not found."
            )
    })
    @Timed(name=MetricsPrefix+MetricsDeleteItemOperationName+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsDeleteItemOperationName+MetricsMeterSuffix)
    public Response deleteModel(@Parameter(description = "Car model identifier.", required = true) @PathParam("id") Integer id) {
        CarModelEntity entity = getMainRepository().get(id);
        if (entity != null) {
//            if (!entity.get().isEmpty()) {
                // TODO!
//                return respondBadRequestWithError("Car model contains ads");
//            }
        }

        return respondDeleteItemById(id);
    }

    @Override
    protected RepositoryBase<CarModelEntity, CarModel> getMainRepository() {
        return modelsRepo;
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
