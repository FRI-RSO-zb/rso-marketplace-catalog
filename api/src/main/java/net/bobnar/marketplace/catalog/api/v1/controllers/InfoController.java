package net.bobnar.marketplace.catalog.api.v1.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Properties;


@Path("info")
@ApplicationScoped
@Tag(name = "Info", description = "Deployment instance information")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InfoController {

    @Inject
    private RestProperties restProperties;

    @Schema(description = "Information about the instance.", example = """
            {
              "name": "catalogue",
              "environment": "prod",
              "version": "1.2.3-main-abcdef1"
            }""")
    public record Info(
        @JsonProperty("name")
        String name,
        @JsonProperty("environment")
        String environment,
        @JsonProperty("version")
        String version
    ){}

    @Schema(description = "Version information about the instance.",
            example = """
                    {
                      "version": "1.2.3-main-abcdef1",
                      "major": "1",
                      "minor": "2",
                      "patch": "3",
                      "branchName": "main",
                      "commitId": "abcdef1234567890abcdef1234567890abcdef12s",
                      "shortCommitId": "abcdef1",
                      "isDirty": false,
                      "buildTime": "2023-12-05T12:00:00+0100"
                    }""")
    public record VersionInfo(
        String version,
        String major,
        String minor,
        String patch,
        String branchName,
        String commitId,
        String shortCommitId,
        boolean isDirty,
        String buildTime) {}

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
                    content = @Content(
                            schema = @Schema(implementation = Info.class)
                    )
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
//                    responseCode = "200",
//                    description = "Version information.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = VersionInfo.class)
                    )
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

    @POST
    @Path("debug/break")
    public Response debugBreakInstance() {
        restProperties.setBroken(true);

        return Response.ok()
                .build();
    }

    private void initializeVersionInfo() {
        Properties prop = new Properties();
        try {
            prop.load(InfoController.class.getResourceAsStream("/META-INF/service.properties"));
        } catch (Exception e) {
            var log = Logger.getLogger(InfoController.class.getName());

            throw new RuntimeException(e);
        }
        String version = prop.getProperty("catalogue.version");

        prop = new Properties();
        try {
            prop.load(InfoController.class.getResourceAsStream("/META-INF/git.properties"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
