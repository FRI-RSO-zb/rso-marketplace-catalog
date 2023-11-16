package net.bobnar.marketplace.loaderAgent.services;

import net.bobnar.marketplace.loaderAgent.AvtoNetListItemResult;
import net.bobnar.marketplace.loaderAgent.BolhaListItemResult;
import net.bobnar.marketplace.loaderAgent.ProcessResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.platform.commons.util.ExceptionUtils;

import javax.enterprise.context.RequestScoped;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestScoped
public class BolhaService {
    final String bolhaUrl = "https://www.bolha.com/";

    public Object loadLatestCarAds() throws IOException {
        Document result = Jsoup.connect(this.getBolhaUrl("avto-oglasi"))
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

    public ProcessResult<List<BolhaListItemResult>> processList(String data) {
        Document page = Jsoup.parse(data);

        ProcessResult<List<BolhaListItemResult>> result = new ProcessResult<>();
        result.status = "fail";
        result.resultItem = new ArrayList<>();

        StringBuilder exceptionsBuilder = new StringBuilder();

        Elements resultRows = page.getElementsByClass("content-main").first().getElementsByClass("EntityList-item");
        for (Element resultRow : resultRows) {
            try {
                if (resultRow.hasClass("EntityList-item--FeaturedStore") || resultRow.hasClass("BannerAlignment")) {
                    continue;
                }
                result.resultItem.add(this.processListItem(resultRow));
            } catch (Exception e) {
                exceptionsBuilder.append(e).append("\n");
                exceptionsBuilder.append(resultRow.html()).append("\n");
                exceptionsBuilder.append(ExceptionUtils.readStackTrace(e)).append("\n\n");
            }
        }

        String exceptions = exceptionsBuilder.toString();
        result.errors = exceptions;

        if (exceptions.isEmpty()) {
            result.status = "ok";
        }

        return result;
    }

    public Object processListing(String data) {
        return null;
    }


    public BolhaListItemResult processListItem(Element row) throws Exception {
        BolhaListItemResult result = new BolhaListItemResult();

        result.isExposed = row.hasClass("EntityList-item--VauVau");

        result.id = Integer.parseInt(row.getElementsByClass("entity-title").first().getElementsByTag("a").attr("name"));
        result.title = row.getElementsByClass("entity-title").first().text();
        result.url = row.attr("data-href");

        result.photoPath = row.getElementsByClass("entity-thumbnail").first().getElementsByTag("img").first().attr("src");

        String descriptionText = row.getElementsByClass("entity-description").first().text();

        Matcher matcher = Pattern.compile("(.+), ([0-9]+) km Leto izdelave: ([0-9]+). Lokacija vozila: ([A-zÀ-ž ]+), ([A-zÀ-ž ]+)", Pattern.CASE_INSENSITIVE).matcher(descriptionText);
        matcher.find();
        if (matcher.group(1).equals("Rabljeno vozilo")) {
            result.age = "Rabljeno vozilo";
        } else {
            throw new Exception("Invalid age" + descriptionText);
        }
        result.drivenDistanceKm = Integer.parseInt(matcher.group(2));
        result.manufacturingYear = Integer.parseInt(matcher.group(3));
        result.location = matcher.group(4) + ", " + matcher.group(5);

        result.publishDate = row.getElementsByClass("date").first().attr("datetime");

        result.price = row.getElementsByClass("entity-prices").first().text();


        return result;
    }

    private String getBolhaUrl(String path) {
        return this.bolhaUrl + path;
    }
}
