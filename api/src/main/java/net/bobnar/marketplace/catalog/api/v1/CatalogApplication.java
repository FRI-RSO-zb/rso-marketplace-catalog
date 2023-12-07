package net.bobnar.marketplace.catalog.api.v1;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.servers.ServerVariable;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


//@SecurityScheme(
//        securitySchemeName = "openid-connect",
//        type = SecuritySchemeType.OPENIDCONNECT,
//        openIdConnectUrl = "https://id.marketplace.bobnar.net/.well-known/openid-configuration"
//)
@OpenAPIDefinition(
        info = @Info(
                title = "Marketplace Catalog API",
                version = "v1",
                description = "Marketplace Catalog API. Exposes access to ad listings and sellers.",
                contact = @Contact(),
                license = @License(name = "MIT License", url = "https://opensource.org/license/mit/"),
                termsOfService = "https://example.com/terms"
        ),
//        security = {
//                @SecurityRequirement(name = "openid-connect", scopes = {"a"})
//        },
        servers = {
                @Server(
                        description = "Local development",
                        url = "http://localhost:8801/"
                ),
                @Server(
                        description = "Deployment on different url",
                        url = "{schema}://{url}/",
                        variables = {
                                @ServerVariable(
                                        name = "schema",
                                        description = "Schema that is used to connect to server",
                                        defaultValue = "https",
                                        enumeration = {
                                                "http",
                                                "https"
                                        }
                                ),
                                @ServerVariable(
                                        name = "url",
                                        description = "Url that is used to connect to server",
                                        defaultValue = "catalog.marketplace.bobnar.net"
                                )
                        }
                ),
                @Server(
                        description = "Local kubernetes cluster deployment",
                        url = "http://catalog.marketplace.local:8888/"
                ),
                @Server(
                        description = "Production deployment",
                        url = "https://catalog.marketplace.bobnar.net/"
                )
        }
)
//@DeclareRoles({"user", "admin"})
@ApplicationPath("v1")
public class CatalogApplication extends Application {
}
