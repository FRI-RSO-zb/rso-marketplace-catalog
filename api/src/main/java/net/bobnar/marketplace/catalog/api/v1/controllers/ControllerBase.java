package net.bobnar.marketplace.catalog.api.v1.controllers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public abstract class ControllerBase {
    protected Response respondBadRequestWithError(String error) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(error)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }

    protected Response respondNotFound() {
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Item not found.")
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }

    protected Response respondOk(Object value) {
        return Response.ok(value)
                .build();
    }

    protected Response respondOkList(List<?> items, Integer itemsCount) {
        Response.ResponseBuilder builder = Response.ok(items);
        if (itemsCount != null) {
            builder.header("X-Total-Count", itemsCount);
        }

        return builder.build();
    }

}
