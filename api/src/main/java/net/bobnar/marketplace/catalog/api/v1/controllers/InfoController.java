package net.bobnar.marketplace.catalog.api.v1.controllers;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import net.bobnar.marketplace.catalog.services.config.ServiceConfig;
import net.bobnar.marketplace.common.dtos.catalog.v1.info.Info;
import net.bobnar.marketplace.common.dtos.catalog.v1.info.VersionInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.config.ResultType;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
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

    @Inject
    private ServiceConfig serviceConfig;

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
//        InfoDto info = new InfoDto();
//        info.name = config.get("kumuluzee.name").get();
//        info.environment = config.get("kumuluzee.env.name").get();
//        info.version = config.get("kumuluzee.version").get();

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

    @POST
    @Path("debug/break")
    @Operation(
            summary = "Set the state of this service instance as broken",
            description = "Set the state of this service instance as broken. Used to debug if the cluster will heal itself.",
            hidden = true
    )
    public Response debugBreakInstance() {
        serviceConfig.disable();

        return Response.ok()
                .build();
    }

//
//    @GET
//    @Path("2")
//    @PermitAll
//    @Operation(
//            summary = "Get information about this service",
//            description = "Returns the general information about the running instance"
//    )
//    @APIResponses({
//            @APIResponse(
//                    responseCode = "200",
//                    content = @Content(schema = @Schema(implementation = Info.class))
//            ),
//            @APIResponse(
//                    responseCode = "503",
//                    description = "Server error. Possibly malformed service version information."
//            )
//    })
//    public Response getInfo2() {
//        ConfigurationUtil config = ConfigurationUtil.getInstance();
//
//        Info info = new Info();
//        info.name = config.get("kumuluzee.name").get();
//        info.environment = config.get("kumuluzee.env.name").get();
//        info.version = config.get("kumuluzee.version").get();
//
//        return Response.ok(info)
//                .build();
//    }

    @Inject
    private EntityManager em;

    @GET
    @Path("debug/db/{table}")
    @Operation(
            summary = "Obtain the database values",
            description = "Queries the entities from the database, so they can be used to replicate the environment.",
            hidden = false
    )
    public Response debugGetDb(@Parameter(description = "Table name.", required = true, example = "Ads") @PathParam("table") String table) {
        Query query = em.createNativeQuery("SELECT * FROM " + table)
                .setHint(QueryHints.RESULT_TYPE, ResultType.Map);
        List<Map<String, ?>> items = query.getResultList();

        return Response.ok(items)
                .build();
    }

    @POST
    @Path("debug/db")
    @Operation(
            summary = "Execute SQL query",
            description = "Executes the query and returns result.",
            hidden = false
    )
    public Response debugExecuteDb(String query) {
        Query q = em.createNativeQuery(query);
        List<Object[]> items = q.getResultList();

        return Response.ok(items)
                .build();
    }

    @GET
    @Path("debug/db/backup")
    @Operation(
            summary = "Create the database backup scripts",
            description = "Queries all the entities from the database, so they can be used to replicate the environment.",
            hidden = false
    )
    public Response debugGetDbBackup() {
        StringBuilder result = new StringBuilder();

        List<Map<String, ?>> carBrands = em.createNativeQuery("SELECT * FROM CarBrands")
                .setHint(QueryHints.RESULT_TYPE, ResultType.Map)
                .getResultList();
        if (!carBrands.isEmpty()) {
            Set<String> keys = carBrands.getFirst().keySet();
            result.append("INSERT INTO CarBrands(");
            result.append(")\n");

            result.append("VALUES\n");

            result.append(";\n");
        } else {
            result.append("-- CarBrands is empty.");
        }


        List<Map<String, ?>> carModels = em.createNativeQuery("SELECT * FROM CarModels")
                .setHint(QueryHints.RESULT_TYPE, ResultType.Map)
                .getResultList();

        List<Map<String, ?>> sellers = em.createNativeQuery("SELECT * FROM Sellers")
                .setHint(QueryHints.RESULT_TYPE, ResultType.Map)
                .getResultList();

        List<Map<String, ?>> ads = em.createNativeQuery("SELECT * FROM Ads")
                .setHint(QueryHints.RESULT_TYPE, ResultType.Map)
                .getResultList();
        if (!ads.isEmpty()) {
            String[] keys = Arrays.stream(ads.getFirst().keySet().toArray()).toArray(String[]::new);
//            Set<String> keys = ads.getFirst().keySet();
//            ads.getFirst().keySet().stream().findFirst().value.name
            result.append("INSERT INTO Ads(");
            result.append(String.join(", ", keys));
            result.append(")\n");

            result.append("VALUES\n");
            ArrayList<String> items = new ArrayList<String>();
            for (Map<String, ?> ad : ads) {
                ArrayList<String> values = new ArrayList<String>();
                for (String key : ad.keySet()) {
                    Object val = ad.get(key);

                    if (val instanceof String) {
                        values.add("\"" + val + "\"");
                    } else {
                        values.add(val.toString());
                    }
                }

                String item = "(" + String.join(", ", values) + ")";
                items.add(item);
            }
            result.append(String.join(", ", items));

            result.append(";\n");
        } else {
            result.append("-- Ads is empty.");
        }

        return Response.ok(result.toString())
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
