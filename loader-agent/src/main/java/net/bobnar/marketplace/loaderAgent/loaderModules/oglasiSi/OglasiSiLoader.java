package net.bobnar.marketplace.loaderAgent.loaderModules.oglasiSi;

import net.bobnar.marketplace.loaderAgent.loader.LoaderBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class OglasiSiLoader extends LoaderBase<Object> {
    public OglasiSiLoader() {
        super("https://oglasi.si/");
    }

    public Object loadLatestCarAds() throws IOException {
        Document result = this.loadDocumentFromUrl("avtomobili");

        System.out.println(result.html());

        return result.html();
    }
}
