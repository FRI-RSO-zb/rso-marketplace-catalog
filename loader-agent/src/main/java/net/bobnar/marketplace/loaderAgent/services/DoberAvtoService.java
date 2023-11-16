package net.bobnar.marketplace.loaderAgent.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.enterprise.context.RequestScoped;
import java.io.IOException;

@RequestScoped
public class DoberAvtoService {
    final String doberAvtoUrl = "https://www.doberavto.si/";

    public Object loadLatestCarAds() throws IOException {
        String result = Jsoup.connect(this.getDoberAvtoUrl("internal-api/v1/marketplace/search?&&&&&&&&&&&&&&results=62&from=0&sort=published.down&includeSold=true"))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/119.0")
                .header("Accept", "*/*")
                .header("Accept-Language", "en-US,en;q=0.5")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("DNT", "1")
                .header("Connection", "keep-alive")
                .header("Referer", "https://www.doberavto.si/iskanje?sort=published.down&page=0")
                .ignoreContentType(true)
                .execute()
                .body();

        System.out.println(result);

        return result;
    }

    public Object processList(String data) {
        return null;
    }

    public Object processListing(String data) {
        return null;
    }


    private String getDoberAvtoUrl(String path) {
        return this.doberAvtoUrl + path;
    }
}
