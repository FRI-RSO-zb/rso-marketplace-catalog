package net.bobnar.marketplace.loaderAgent.api.v1.controllers;

import net.bobnar.marketplace.loaderAgent.services.AvtoNetService;
import net.bobnar.marketplace.loaderAgent.services.BolhaService;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("loader")
@Tag(name="Loader", description="Endpoints for starting and reviewing the data loading jobs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoaderController {

    @GET
    public Response getLoaderJobs() {
        return Response.ok("0").build();
    }


    @POST
    @Path("load/{site}/{page}")
    public Response loadPage(@PathParam("site") String site, @PathParam("page") String page) throws IOException {
        if (site.equals("avtonet")) {
            if (page.equals("top100")) {
                return Response.ok(new AvtoNetService().loadAvtonetTop100List()).build();
            }
        } else if (site.equals("bolha")) {
            if (page.equals("latestCars")) {
                return Response.ok(new BolhaService().loadLatestCarAds()).build();
            }
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
