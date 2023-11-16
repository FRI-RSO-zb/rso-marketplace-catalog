package net.bobnar.marketplace.catalogue.api.v1.resources;

import net.bobnar.marketplace.catalogue.entities.AdEntity;
import net.bobnar.marketplace.catalogue.services.AdService;
import org.eclipse.microprofile.metrics.ConcurrentGauge;
import org.eclipse.microprofile.metrics.Meter;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Metric;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("ads")
@Tag(name="Ads", description = "Ad related endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class AdsResource {

    @Inject
    @Metric(name="ad_counter")
    private ConcurrentGauge adCounter;

    @Inject
    @Metric(name="ad_adding_meter")
    private Meter addMeter;

    @Inject
    private AdService adService;

    @GET
    @Operation(summary = "Get all ads", description = "Obtain the list of all ads")
    @Timed(name="get-all-ads-timer")
    public Response getAllAds() {
        List<AdEntity> ads = adService.getAds();

        return Response.ok(ads).build();
    }

    @GET
    @Path("{id}")
    public Response getAd(@PathParam("id") Integer id) {
        AdEntity ad = adService.getAd(id);

        return ad != null ?
                Response.ok(ad).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Timed(name="create-ad-timer")
    @Metered(name="ad-creating-meter")
    public Response createAd(AdEntity ad) {
        addMeter.mark();
        adCounter.inc();
        adService.saveAd(ad);

        return Response.noContent().build();
    }

    @DELETE
    @Path("{id}")
    @Timed(name="delete-ad-timer")
    @Metered(name="ad-deleting-meter")
    public Response deleteAd(@PathParam("id") Integer id) {
        adService.deleteAd(id);
        adCounter.dec();

        return Response.noContent().build();
    }
}
