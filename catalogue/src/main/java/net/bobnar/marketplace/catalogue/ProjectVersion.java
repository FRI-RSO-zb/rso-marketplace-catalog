package net.bobnar.marketplace.catalogue;

import java.io.IOException;
import java.util.Properties;

public class ProjectVersion {
    public static void getVersion() {
        Properties prop = new Properties();
        try {
            prop.load(ProjectVersion.class.getResourceAsStream("/META-INF/version.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String version = prop.getProperty("version");
    }
}
