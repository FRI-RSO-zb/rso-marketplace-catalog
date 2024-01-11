package net.bobnar.marketplace.catalog.api.v1.controllers;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import net.bobnar.marketplace.catalog.services.config.ServiceConfig;
import net.bobnar.marketplace.catalog.services.utils.DatabaseHelper;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.logging.Logger;


@Path("debug")
@RequestScoped
@Tag(name = "Debug", description = "Debugging interface")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(name="debug", allowOrigin = "*", exposedHeaders = "X-Total-Count", supportedMethods = "GET, HEAD, PUT, POST, OPTIONS, DELETE")
public class DebugController {
    private Logger log = Logger.getLogger(DebugController.class.getName());

    @Inject
    private ServiceConfig serviceConfig;

    @Inject
    private DatabaseHelper dbHelper;

    @POST
    @Path("break")
    @Operation(
            summary = "Set the state of this service instance as broken",
            description = "Set the state of this service instance as broken. Used to debug if the cluster will heal itself.",
            hidden = false // true
    )
    public Response debugBreakInstance() {
        serviceConfig.disable();

        return Response.ok()
                .build();
    }

    @POST
    @Path("db")
    @Operation(
            summary = "Execute SQL query",
            description = "Executes the query and returns result.",
            hidden = false
    )
    public Response debugExecuteDb(String query) {
        List<Object[]> items = this.dbHelper.executeQuery(query);

        return Response.ok(items)
                .build();
    }

    @GET
    @Path("db/{table}")
    @Operation(
            summary = "Obtain the database values",
            description = "Queries the entities from the database, so they can be used to replicate the environment.",
            hidden = false
    )
    public Response debugGetDb(@Parameter(description = "Table name.", required = true, example = "ads") @PathParam("table") String table) {
        List<Map<String, ?>> items = this.dbHelper.getTableItems(table);

        return Response.ok(items)
                .build();
    }

    @GET
    @Path("db/backup")
    @Operation(
            summary = "Create the database backup scripts",
            description = "Queries all the entities from the database, so they can be used to replicate the environment.",
            hidden = false
    )
    @Produces(MediaType.TEXT_PLAIN)
    public Response debugGetDbBackup() {
        String result = this.dbHelper.getTableContentInsertStatement("carbrands") + "\n" +
                this.dbHelper.getTableContentInsertStatement("carmodels") + "\n" +
                this.dbHelper.getTableContentInsertStatement("sellers") + "\n" +
                this.dbHelper.getTableContentInsertStatement("ads") + "\n";
        return Response.ok(result)
                .type("text/plain")
                .build();
    }
}
