package net.bobnar.marketplace.catalogue;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@OpenAPIDefinition(
        info = @Info(
                title = "Image catalog API", version = "v1",
                contact = @Contact(email = "rso@fri.uni-lj.si", name = "Ziga"),
                license = @License(name = "dev"),
                description = "API for managing image metadata.",
                termsOfService = "Non-production"
        ),
        servers = @Server(url = "http://localhost:8080/")
)
@ApplicationPath("/v1")
public class CatalogueApplication extends Application {
}
