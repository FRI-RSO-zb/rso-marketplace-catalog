package net.bobnar.marketplace.catalogue.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.faces.bean.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/sellers")
@Tag(name="Seller", description = "Seller related endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SellersResource {
    @Operation(description="Get list of all sellers")
    @GET
    public Response getSellers() {
        String[] sellers = new String[] {"Seller1", "Seller2"};

        return Response.status(Response.Status.OK).entity(sellers).build();
    }
}
