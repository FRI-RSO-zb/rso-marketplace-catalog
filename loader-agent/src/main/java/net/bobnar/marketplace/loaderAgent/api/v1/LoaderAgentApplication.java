package net.bobnar.marketplace.loaderAgent.api.v1;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(
    info = @Info(
            title="Marketplace Loader Agent API",
            version="v1",
            description="API for controlling the marketplace loader agent. Here you can trigger loading new data into marketplace"
    ),
    servers=@Server(
            description="Local development",
            url="http://localhost:8081"
    )
)
@ApplicationPath("v1")
public class LoaderAgentApplication extends Application {
}
