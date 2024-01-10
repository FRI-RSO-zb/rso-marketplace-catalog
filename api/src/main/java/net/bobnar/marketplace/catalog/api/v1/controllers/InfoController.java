package net.bobnar.marketplace.catalog.api.v1.controllers;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import net.bobnar.marketplace.common.controllers.InfoControllerBase;
import net.bobnar.marketplace.common.dtos.v1.info.Info;
import net.bobnar.marketplace.common.dtos.v1.info.VersionInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;


@Path("info")
@ApplicationScoped
@Tag(name = "Info", description = "Deployment instance information")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(name="info", allowOrigin = "*", exposedHeaders = "X-Total-Count", supportedMethods = "GET, HEAD, PUT, POST, OPTIONS, DELETE")
public class InfoController extends InfoControllerBase {
    @GET
    @PermitAll
    @Operation(
            summary = "Get information about this service",
            description = "Returns the general information about the running instance"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Info.class))
            ),
            @APIResponse(
                    responseCode = "503",
                    description = "Server error. Possibly malformed service version information."
            )
    })
    public Response getInfo() {
        return respondOk(getServiceInfo());
    }

    @GET
    @Path("version")
    @PermitAll
    @Operation(
            summary = "Get the version of this instance",
            description = "Returns the detailed version information of the running instance."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = VersionInfo.class))
            ),
            @APIResponse(
                    responseCode = "503",
                    description = "Server error. Possibly malformed service version information."
            )
    })
    public Response getVersion() {
        return respondOk(getVersionInfo());
    }

    @Override
    protected Properties getProperties() {
        return this.loadResourceProperties(this.getClass(), "/META-INF/service.properties");
    }
}
