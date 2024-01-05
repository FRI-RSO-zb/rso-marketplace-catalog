package net.bobnar.marketplace.catalog.api.v1.controllers;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import net.bobnar.marketplace.common.dtos.catalog.v1.info.Info;
import net.bobnar.marketplace.common.dtos.catalog.v1.info.VersionInfo;
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
public class InfoController {

    private static VersionInfo versionInfo;

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
        ConfigurationUtil config = ConfigurationUtil.getInstance();

        Info info = new Info(
                config.get("kumuluzee.name").get(),
                config.get("kumuluzee.env.name").get(),
                config.get("kumuluzee.version").get());

        return Response.ok(info)
                .build();
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
        if (versionInfo == null) {
            initializeVersionInfo();
        }

        return Response.ok(versionInfo)
                .build();
    }

    private void initializeVersionInfo() {
        Properties prop = new Properties();
        try {
            prop.load(InfoController.class.getResourceAsStream("/META-INF/service.properties"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String version = prop.getProperty("service.version");
        VersionInfo info = new VersionInfo(
                version,
                version.split("\\.")[0],
                version.split("\\.")[1],
                version.split("\\.")[2].split("-")[0],
                prop.getProperty("git.branch"),
                prop.getProperty("git.commit.id"),
                prop.getProperty("git.commit.id.abbrev"),
                "true".equals(prop.getProperty("git.dirty")),
                prop.getProperty("git.build.time"));

        InfoController.versionInfo = info;
    }
}
