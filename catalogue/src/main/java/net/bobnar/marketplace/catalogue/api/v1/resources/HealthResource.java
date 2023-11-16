//package net.bobnar.marketplace.catalogue.api.v1.resources;
//
//import org.eclipse.microprofile.openapi.annotations.Operation;
//import org.eclipse.microprofile.openapi.annotations.tags.Tag;
//
//import javax.faces.bean.ApplicationScoped;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//@ApplicationScoped
//@Path("/health")
//@Tag(name="Health", description = "Health check api")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
//public class HealthResource {
//
//    @Operation(summary = "Get health report")
//    @GET
//    public Response getHealth() {
//        int result = 0;
//
//        return Response.status(Response.Status.OK).entity(result).build();
//    }
//
//    @Operation(summary = "Check if service is able to accept requests.")
//    @GET
//    @Path("live")
//    public Response getLiveness() {
//        String result = "UP";
//
//        return Response.status(Response.Status.OK).entity(result).build();
//    }
//
//    @Operation(summary = "Checks if service is healthy and ready to process requests.")
//    @GET
//    @Path("ready")
//    public Response getReadiness() {
//        String result = "Ready.";
//
//        return Response.status(Response.Status.OK).entity(result).build();
//    }
//
//
//}
