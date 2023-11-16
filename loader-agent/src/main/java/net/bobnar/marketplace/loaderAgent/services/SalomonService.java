package net.bobnar.marketplace.loaderAgent.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class SalomonService {
    final String salomonUrl = "http://oglasi.svet24.si/";

    public Object loadLatestCarAds() throws IOException {
        Document result = Jsoup.connect(this.getSalomonUrl("oglasi/motorna-vozila/avtomobili"))
                .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/119.0")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.5")
                .header("Accept-Encoding", "gzip, deflate, br")
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


    private String getSalomonUrl(String path) {
        return this.salomonUrl + path;
    }
}
