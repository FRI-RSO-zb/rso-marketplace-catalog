package net.bobnar.marketplace.loaderAgent.loaderModules.salomon;

import net.bobnar.marketplace.loaderAgent.loader.LoaderBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class SalomonLoader extends LoaderBase<Object> {
    public SalomonLoader() {
        super("http://oglasi.svet24.si/");
    }

    public Object loadLatestCarAds() throws IOException {
        Document result = this.loadDocumentFromUrl("oglasi/motorna-vozila/avtomobili");

        System.out.println(result.html());

        return result.html();
    }
}
