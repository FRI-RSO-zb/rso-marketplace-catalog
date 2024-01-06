package net.bobnar.marketplace.catalog.api.v1.controllers;

import com.kumuluz.ee.logs.cdi.Log;
import net.bobnar.marketplace.catalog.services.repositories.CarBrandsRepository;
import net.bobnar.marketplace.catalog.services.repositories.CarModelsRepository;
import net.bobnar.marketplace.catalog.services.repositories.RepositoryBase;
import net.bobnar.marketplace.common.dtos.catalog.v1.carBrands.CarBrand;
import net.bobnar.marketplace.common.dtos.catalog.v1.carModels.CarModel;
import net.bobnar.marketplace.data.entities.CarBrandEntity;
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
@Path("brands")
@Tag(name="Car Brands", description = "Endpoints for managing car brands.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class CarBrandsController extends CRUDControllerBase<CarBrandEntity, CarBrand> {
    private final String MetricsPrefix = "brands_";
    @Inject @Metric(name=MetricsPrefix+MetricsCounterName)
    private ConcurrentGauge itemsCounter;
    @Inject @Metric(name=MetricsPrefix+MetricsAddingMeterName)
    private Meter addingMeter;
    @Inject @Metric(name=MetricsPrefix+MetricsRemovingMeterName)
    private Meter removingMeter;

    @Inject
    CarBrandsRepository brandsRepo;

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
    @Timed(name=MetricsPrefix+MetricsGetListOperationName+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsGetListOperationName+MetricsMeterSuffix)
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

        return respondGetQueryItemsResponse(limit, offset, where, uriInfo);
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
                    content = @Content(schema = @Schema(implementation = CarBrand.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Car brand with specified id does not exist."
            )
    })
    @Timed(name=MetricsPrefix+MetricsGetItemOperationName+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsGetItemOperationName+MetricsMeterSuffix)
    public Response getBrand(@Parameter(description = "Car brand identifier.", required = true) @PathParam("id") Integer id) {
        return respondGetItemById(id);
    }

    @POST
    @Operation(
            summary = "Create car brands",
            description = "Creates the car brands using specified details."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Car brand items created.",
                    content = @Content(schema = @Schema(implementation = CarBrand.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid information specified."
            )
    })
    @Timed(name=MetricsPrefix+MetricsCreateItemsOperationName+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsCreateItemsOperationName+MetricsMeterSuffix)
    public Response createBrands(
            @RequestBody(description = "Car brand items", required = true, content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = CarBrand.class)))
            List<CarBrand> items) {
        for (CarBrand item : items) {
            if (item.getName() == null) {
                return respondBadRequestWithError("Missing item name");
            }
        }

        return respondCreateItems(items);
    }

    @PUT
    @Path("{id}")
    @Operation(
            summary = "Update car brand",
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
    @Timed(name=MetricsPrefix+MetricsUpdateItemOperationName+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsUpdateItemOperationName+MetricsMeterSuffix)
    public Response updateBrand(@Parameter(description = "Car brand identifier.", required = true) @PathParam("id") Integer id, CarBrand item) {
        return respondUpdateItem(id, item);
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
    @Timed(name=MetricsPrefix+MetricsDeleteItemOperationName+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsDeleteItemOperationName+MetricsMeterSuffix)
    public Response deleteBrand(@Parameter(description = "Car brand identifier.", required = true) @PathParam("id") Integer id) {
        CarBrandEntity entity = getMainRepository().get(id);
        if (entity != null) {
            if (!entity.getModels().isEmpty()) {
                return respondBadRequestWithError("Car brand contains models");
            }
        }

        return respondDeleteItemById(id);
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
    @Timed(name=MetricsPrefix+MetricsGetItemOperationName+"_models"+MetricsTimerSuffix)
    @Metered(name=MetricsPrefix+MetricsGetItemOperationName+"_models"+MetricsMeterSuffix)
    public Response getBrandModels(@Parameter(description = "Brand identifier.", required = true) @PathParam("id") Integer id) {
        List<CarModel> items = modelsRepo.toDtoList(modelsRepo.getModelsByBrand(id));

        return respondWithAnyItemList(items, items.size());
    }

    @Override
    protected RepositoryBase<CarBrandEntity, CarBrand> getMainRepository() {
        return brandsRepo;
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
