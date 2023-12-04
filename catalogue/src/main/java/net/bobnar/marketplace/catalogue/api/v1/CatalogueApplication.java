package net.bobnar.marketplace.catalogue.api.v1;

import com.kumuluz.ee.health.HealthRegistry;
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
                description = "API for marketplace catalogue. Provides access to ad listings and sellers."
        ),
        servers = {
                @Server(
                        description = "Local development",
                        url = "http://localhost:8001/"
                ),
                @Server(
                        description = "Local kubernetes cluster deployment",
                        url = "http://catalogue.marketplace.local:8888/"
                ),
                @Server(
                        description = "Production deployment",
                        url = "https://catalogue.marketplace.bobnar.net/"
                ),
        }
)
@ApplicationPath("v1")
public class CatalogueApplication extends Application {
}
