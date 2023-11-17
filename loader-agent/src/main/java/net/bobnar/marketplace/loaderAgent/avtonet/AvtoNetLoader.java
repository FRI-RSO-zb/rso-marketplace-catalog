package net.bobnar.marketplace.loaderAgent.avtonet;

import net.bobnar.marketplace.loaderAgent.loader.LoaderBase;
import org.jsoup.nodes.Document;

import javax.enterprise.context.RequestScoped;
import java.io.IOException;

@RequestScoped
public class AvtoNetLoader extends LoaderBase<Object> {
    public AvtoNetLoader() {
        super("https://www.avto.net/");
    }

    public Object loadAvtonetTop100List() throws IOException {
        Document result = this.loadDocumentFromUrl("Ads/results_100.asp?oglasrubrika=1&prodajalec=2");

        // TODO:
        System.out.println(result.html());

        return result.html();
    }
}