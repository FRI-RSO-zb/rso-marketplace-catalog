package net.bobnar.marketplace.loaderAgent.services;

import net.bobnar.marketplace.loaderAgent.AvtoNetListItemResult;
import net.bobnar.marketplace.loaderAgent.ProcessResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.platform.commons.util.ExceptionUtils;

import javax.enterprise.context.RequestScoped;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestScoped
public class AvtoNetService {
    final String avtonetUrl = "https://www.avto.net/";

    public Object loadAvtonetTop100List() throws IOException {
        Document result = Jsoup.connect(this.getAvtonetUrl("Ads/results_100.asp?oglasrubrika=1&prodajalec=2"))
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

    public ProcessResult<List<AvtoNetListItemResult>> processList(String data) {
        Document page = Jsoup.parse(data);

        ProcessResult<List<AvtoNetListItemResult>> result = new ProcessResult<List<AvtoNetListItemResult>>();
        result.status = "fail";
        result.resultItem = new ArrayList<AvtoNetListItemResult>();

        StringBuilder exceptionsBuilder = new StringBuilder();

        Elements resultRows = page.getElementsByClass("GO-Results-Row");
        for (Element resultRow : resultRows) {
            try {
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
        // TODO:
        return null;
    }

    public AvtoNetListItemResult processListItem(Element itemRow) throws Exception {
        AvtoNetListItemResult result = new AvtoNetListItemResult();

        String adLink = itemRow.getElementsByTag("a").first().attr("href");
        Matcher matcher = Pattern.compile("id=([0-9]+)&display=(.*)", Pattern.CASE_INSENSITIVE).matcher(adLink);
        matcher.find();
        result.id = Integer.parseInt(matcher.group(1));
        result.shortTitle = matcher.group(2).replace("%20", " ");

        result.title = itemRow.getElementsByClass("GO-Results-Naziv").first().getElementsByTag("span").first().text();

        result.photoPath = itemRow.getElementsByClass("GO-Results-Photo").first().getElementsByTag("img").first().attr("SRC");

        Elements dataRows = itemRow.getElementsByClass("GO-Results-Data-Top").first().getElementsByTag("tr");
        for (Element dataRow : dataRows) {
            Elements items = dataRow.getElementsByTag("td");
            String property = items.first().text();
            String value = "";
            if (items.size() > 1) {
                value = items.get(1).text();
            }

            if (property.equals("1.registracija")) {
                result.firstRegistrationYear = Integer.parseInt(value);
            } else if (property.equals("Prevoženih")) {
                result.drivenDistanceKm = Integer.parseInt(value.replace(" km", ""));
            } else if (property.equals("Gorivo")) {
                result.engineType = value;
            } else if (property.equals("Menjalnik")) {
                result.transmissionType = value;
            } else if (property.equals("Motor")) {
                result.engineParameters = value;
            } else if (property.equals("Starost")) {
                result.age = value;
            } else if (property.equals("Baterija") || property.endsWith("baterija")) {
                result.otherParameters.put(property, value);
            } else {
                throw new InvalidParameterException("Unknown parameter in ad listing: " + property + ", value: " + value);
            }
        }

        Element sellerNotesEl = itemRow.getElementsByClass("GO-bg-graylight").first();
        if (sellerNotesEl != null) {
            result.sellerNotes = sellerNotesEl.text();
        }


        Element priceEl = itemRow.getElementsByClass("GO-Results-PriceLogo").first();
        Element sellerEl = priceEl.getElementsByClass("GO-Results-Logo").first();
        if (sellerEl != null) {
            result.isDealer = true;
            Element sellerLinkEl = sellerEl.getElementsByTag("a").first();
            if (sellerLinkEl != null) {
                String sellerLink = sellerLinkEl.attr("HREF");
                Matcher matcher1 = Pattern.compile("broker=([0-9]+)&", Pattern.CASE_INSENSITIVE).matcher(sellerLink);
                matcher1.find();
                result.dealerId = Integer.parseInt(matcher1.group(1));
            } else {
                // Case of blank image as seller graphics - cannot determine dealer id.
            }
        }

        boolean priceProcessed = false;
        Element regularPriceEl = priceEl.getElementsByClass("Go-Results-Price-TXT-Regular").first();
        if (regularPriceEl != null) {
            result.regularPrice = regularPriceEl.text();
            priceProcessed = true;
        }
        Element actionPriceTextEl = priceEl.getElementsByClass("Go-Results-Price-Akcija-TXT").first();
        Element actionOldPriceEl = priceEl.getElementsByClass("Go-Results-Price-TXT-StaraCena").first();
        Element actionNewPriceEl = priceEl.getElementsByClass("Go-Results-Price-TXT-AkcijaCena").first();
        if (actionNewPriceEl != null) {
            //result.actionPrice = actionNewPriceEl.text();
            priceProcessed = true;
        }

        if (!priceProcessed)
            throw new Exception("Price element does not exist");

        return result;
    }

    private String getAvtonetUrl(String path) {
        return this.avtonetUrl + path;
    }
}
