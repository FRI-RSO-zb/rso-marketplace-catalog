package net.bobnar.marketplace.catalogue.api.v1.resources;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Properties;

@Path("info")
@Tag(name = "Info", description = "Deployment instance information")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class InfoResource {

    static class Info {
        public String name;
        public String environment;
        public String version;
    }

    static class VersionInfo {
        public String version;
        public String major;
        public String minor;
        public String patch;
        public String branchName;
        public String commitId;
        public String shortCommitId;
        public boolean isDirty;
        public String buildTime;
    }

    private static VersionInfo versionInfo;

    @GET
    @PermitAll
    @Operation(
            summary = "Get information about this service",
            description = "Returns the general information about the running instance"
    )
    public Response getInfo() {
        Info info = new Info();

        ConfigurationUtil config = ConfigurationUtil.getInstance();
        info.name = config.get("kumuluzee.name").get();
        info.environment = config.get("kumuluzee.env.name").get();
        info.version = config.get("kumuluzee.version").get();

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
                    description = "Version information.",
                    content = @Content(
                            schema = @Schema(
                                    description = "Version information.",
                                    implementation = VersionInfo.class
                            )
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

    private static void initializeVersionInfo() {
        VersionInfo info = new VersionInfo();

        Properties prop = new Properties();
        try {
            prop.load(InfoResource.class.getResourceAsStream("/META-INF/catalogue.properties"));
        } catch (Exception e) {
//            return Response.serverError()
//                    .build();
            throw new RuntimeException(e);
        }
        info.version = prop.getProperty("catalogue.version");
        info.major = info.version.split("\\.")[0];
        info.minor = info.version.split("\\.")[1];
        info.patch = info.version.split("\\.")[2].split("-")[0];

        prop = new Properties();
        try {
            prop.load(InfoResource.class.getResourceAsStream("/META-INF/git.properties"));
        } catch (Exception e) {
//            return Response.serverError()
//                    .build();
            throw new RuntimeException(e);
        }
        info.branchName =prop.getProperty("git.branch");
        info.commitId = prop.getProperty("git.commit.id");
        info.shortCommitId = prop.getProperty("git.commit.id.abbrev");
        info.isDirty = "true".equals(prop.getProperty("git.dirty"));
        info.buildTime = prop.getProperty("git.build.time");

        InfoResource.versionInfo = info;
    }
}
