package net.bobnar.marketplace.catalogue.api.v1.resources;

import com.kumuluz.ee.logs.cdi.Log;
import net.bobnar.marketplace.catalogue.entities.SellerEntity;
import net.bobnar.marketplace.catalogue.services.SellerService;
import org.eclipse.microprofile.metrics.ConcurrentGauge;
import org.eclipse.microprofile.metrics.Meter;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Metric;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Log
@Path("sellers")
@Tag(name="Sellers", description = "Seller related endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class SellersResource {

    @Inject
    @Metric(name="seller_counter")
    private ConcurrentGauge sellerCounter;

    @Inject
    @Metric(name="seller_adding_meter")
    private Meter addMeter;

    @Inject
    SellerService sellerService;


    @GET
    @Operation(description="Get list of all sellers")
    @Timed(name="get-all-sellers-timer")
    public Response getSellers() {
        List<SellerEntity> sellers = sellerService.getSellers();

        return Response.ok(sellers).build();
    }

    @GET
    @Path("{id}")
    public Response getSeller(@PathParam("id") Integer id) {
        SellerEntity seller = sellerService.getSeller(id);

        return seller != null ?
                Response.ok(seller).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Timed(name="create-seller-timer")
    @Metered(name="seler-creating-meter")
    public Response createSeller(SellerEntity seller) {
        addMeter.mark();
        sellerCounter.inc();
        sellerService.saveSeller(seller);

        return Response.noContent().build();
    }

    @DELETE
    @Path("{id}")
    @Timed(name="delete-seller-timer")
    @Metered(name="seller-deleting-meter")
    public Response deleteSeller(@PathParam("id") Integer id) {
        sellerService.deleteSeller(id);
        sellerCounter.dec();

        return Response.noContent().build();
    }
}
