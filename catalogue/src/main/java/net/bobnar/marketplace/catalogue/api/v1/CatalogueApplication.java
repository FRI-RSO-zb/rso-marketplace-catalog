package net.bobnar.marketplace.catalogue.api.v1;

//import com.kumuluz.ee.cors.annotations.CrossOrigin;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@SecurityScheme(
        securitySchemeName = "openid-connect",
        type = SecuritySchemeType.OPENIDCONNECT,
        openIdConnectUrl = "https://id.bobnar.net/.well-known/openid-configuration"
)
//@CrossOrigin
@OpenAPIDefinition(
        info = @Info(
                title = "Marketplace Catalogue API",
                version = "v1",
                description = "Marketplace Catalogue API. Exposes access to ad listings and sellers.",
                contact = @Contact(),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/license/mit/"
                ),
                termsOfService = "https://example.com/terms"
        ),
        security = @SecurityRequirement(name = "openid-connect"),
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
@DeclareRoles({"user", "admin"})
@ApplicationPath("v1")
public class CatalogueApplication extends Application {
}
