package net.bobnar.marketplace.loaderAgent.services;

import net.bobnar.marketplace.loaderAgent.AvtoNetListItemResult;
import net.bobnar.marketplace.loaderAgent.ProcessResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class AvtoNetServiceTest {

    @Test
    void processTop100List() throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("src/test/resources/avtonet-top100-list.html");
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);
        String data = reader.lines().collect(Collectors.joining(System.lineSeparator()));

        ProcessResult<List<AvtoNetListItemResult>> result = new AvtoNetService().processList(data);

        assertNotNull(result);
        assertEquals("", result.errors);
        assertEquals("ok", result.status);
        assertEquals(99, result.resultItem.size()); // We actually only get 99 ads in the top100
    }

    @Test
    void processListing() throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("src/test/resources/avtonet-ad-details-18980973.html");
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);
        String data = reader.lines().collect(Collectors.joining(System.lineSeparator()));

        var result = new AvtoNetService().processListing(data);

        assertNotNull(result);
    }

    @Test
    void  processListItem() throws Exception {
        FileInputStream fis = new FileInputStream("src/test/resources/avtonet-top100-list-row.html");
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);
        String data = reader.lines().collect(Collectors.joining(System.lineSeparator()));

        Document item = Jsoup.parse(data);

        AvtoNetListItemResult result = new AvtoNetService().processListItem(item);

        assertNotNull(result);

        assertEquals("Renault Scenic Dynamique dCi 110 NAVI-ALU-TEMP-USNJE-BREZ POLOGA", result.title);
        assertEquals(18972791, result.id);
        assertEquals("Renault Scenic", result.shortTitle);
        assertEquals("https://images.avto.net/photo/18972791/1056238_160.jpg", result.photoPath);
        assertEquals(2012, result.firstRegistrationYear);
        assertEquals(1500000, result.drivenDistanceKm);
        assertEquals("diesel motor", result.engineType);
        assertEquals("ročni menjalnik", result.transmissionType);
        assertEquals("1461 ccm, 81 kW / 110 KM", result.engineParameters);
        assertEquals("DELNO USNJE LED NAVIGACIJA TEMPOMAT OPRAVLJEN VELIK SERVIS FINANCIRANJE BREZ POLOGA TUDI ZA TUJCE", result.sellerNotes);
        assertEquals(16101, result.dealerId);
        assertEquals("5.990 €", result.regularPrice);
    }
}
