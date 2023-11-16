package net.bobnar.marketplace.loaderAgent.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class OglasiSiService {
    final String oglasiSiUrl = "https://oglasi.si/";

    public Object loadLatestCarAds() throws IOException {
        var result = Jsoup.connect(this.getOglasiSiUrl("avtomobili"))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/119.0")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.5")
                .header("Accept-Encoding", "gzip, deflate")
                .header("DNT", "1")
                .header("Connection", "keep-alive")
                .get();

        System.out.println(result.html());

        return result.html();
    }

    public Object processList(String data) {
        return null;
    }

    public Object processListing(String data) {
        return null;
    }

    private String getOglasiSiUrl(String path) {
        return this.oglasiSiUrl + path;
    }
}
