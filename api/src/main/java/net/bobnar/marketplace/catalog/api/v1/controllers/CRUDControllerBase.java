package net.bobnar.marketplace.catalog.api.v1.controllers;

import com.kumuluz.ee.rest.beans.QueryParameters;
import net.bobnar.marketplace.catalog.services.repositories.RepositoryBase;
import net.bobnar.marketplace.common.controllers.ControllerBase;
import net.bobnar.marketplace.common.dtos.ItemBase;
import net.bobnar.marketplace.data.entities.EntityBase;
import org.eclipse.microprofile.metrics.ConcurrentGauge;
import org.eclipse.microprofile.metrics.Meter;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

public abstract class CRUDControllerBase<T extends EntityBase<TDto>, TDto extends ItemBase> extends ControllerBase {
    protected final String MetricsCounterName = "counter";
    protected final String MetricsAddingMeterName = "adding_meter";
    protected final String MetricsRemovingMeterName = "removing_meter";
    protected final String MetricsTimerSuffix = "_timer";
    protected final String MetricsMeterSuffix = "_meter";
    protected final String MetricsGetListOperationName = "get_list";
    protected final String MetricsGetItemOperationName = "get_item";
    protected final String MetricsCreateItemsOperationName = "create_items";
    protected final String MetricsUpdateItemOperationName = "update_item";
    protected final String MetricsDeleteItemOperationName = "delete_item";

    protected Response respondGetQueryItemsResponse(Integer limit, Integer offset, String where, String order, @Context UriInfo uriInfo) {
        QueryParameters query = this.getRequestQuery(uriInfo);
        List<TDto> items = getMainRepository().findItems(query);
        int allItemsCount = getMainRepository().countQueriedItems(query);

        return respondWithItemList(items, allItemsCount);
    }

    protected Response respondGetItemById(Integer id) {
        TDto result = getMainRepository().getItem(id);

        return respondWithItemOrNotFound(result);
    }

    protected Response respondGetItemsByIds(List<Integer> ids) {
        List<TDto> result = getMainRepository().getMultipleItems(ids);

        return respondWithItemList(result, result.size());
    }

    protected Response respondCreateItem(TDto item) {
        if (item.getId() != null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        item = getMainRepository().createItem(item);

        if (getMetricsAddingMeter() != null) {
            getMetricsAddingMeter().mark();
        }
        if (getMetricsItemsCounterGauge() != null) {
            getMetricsItemsCounterGauge().inc();
        }

        return Response.status(Response.Status.CREATED)
                .entity(item)
                .build();
    }

    protected Response respondCreateItems(List<TDto> items) {
        for (TDto item : items) {
            if (item.getId() != null) {
                return respondBadRequestWithError("Item id is not empty");
            }
        }

        items = getMainRepository().createItems(items);

        if (getMetricsAddingMeter() != null) {
            getMetricsAddingMeter().mark(items.size());
        }
        if (getMetricsItemsCounterGauge() != null) {
            for (TDto i : items) {
                getMetricsItemsCounterGauge().inc();
            }
        }

        return Response.status(Response.Status.CREATED)
                .entity(items)
                .build();
    }

    protected Response respondUpdateItem(Integer id, TDto item) {
        item = getMainRepository().updateItem(id, item);

        return respondWithItemOrNotFound(item);
    }

    protected Response respondDeleteItemById(Integer id) {
        boolean result = getMainRepository().delete(id);

        if (getMetricsRemovingMeter() != null) {
            getMetricsRemovingMeter().mark();
        }
        if (getMetricsItemsCounterGauge() != null) {
            getMetricsItemsCounterGauge().dec();
        }

        return result ?
                Response.noContent().build() :
                respondBadRequestWithError("Unable to delete item");
    }

    protected Response respondWithItemOrNotFound(TDto item) {
        return item != null ?
                respondOk(item) :
                respondNotFound();
    }

    protected Response respondWithItemOrNotFound(T entity) {
        return respondWithItemOrNotFound(getMainRepository().toDto(entity));
    }

    protected Response respondWithItemList(List<TDto> items, Integer totalCount) {
        return respondOkList(items, totalCount);
    }

    protected <TOtherDto extends ItemBase> Response respondWithAnyItemList(List<TOtherDto> items, Integer totalCount) {
        return respondOkList(items, totalCount);
    }

    protected abstract RepositoryBase<T, TDto> getMainRepository();

    protected QueryParameters getRequestQuery(UriInfo uriInfo) {
        return QueryParameters.query(uriInfo.getRequestUri().getQuery())
                .maxLimit(50)
                .defaultLimit(10)
                .defaultOffset(0)
                .build();
    }


    protected Meter getMetricsAddingMeter() {
        return null;
    }

    protected Meter getMetricsRemovingMeter() {
        return null;
    }

    protected ConcurrentGauge getMetricsItemsCounterGauge() {
        return null;
    }
}
