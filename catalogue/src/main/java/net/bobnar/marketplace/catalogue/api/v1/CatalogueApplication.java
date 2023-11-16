package net.bobnar.marketplace.catalogue.api.v1;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@OpenAPIDefinition(
        info = @Info(
                title = "Marketplace Catalogue API", version = "v1",
//                contact = @Contact(email = "rso@fri.uni-lj.si", name = "Ziga"),
                license = @License(name = ""),
                description = "API for marketplace catalogue. Provides access to ad listings and sellers."//,
//                termsOfService = "Non-production"
        ),
        servers = @Server(
                description = "Local development",
                url = "http://localhost:8080/"
        )
)
@ApplicationPath("v1")
public class CatalogueApplication extends Application {
}
