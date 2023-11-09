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
@Path("/ads")
@Tag(name="Ads", description = "Ad related endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdsResource {

    @Operation(summary = "Get all ads", description = "Obtain the list of all ads")
    @GET
    public Response getAds() {
        //List<int> ads = new List<int>() {1, 2, 3, 4};
        int[] ads = new int[] {1, 2, 3, 4};


        return Response.status(Response.Status.OK).entity(ads).build();
    }
}
