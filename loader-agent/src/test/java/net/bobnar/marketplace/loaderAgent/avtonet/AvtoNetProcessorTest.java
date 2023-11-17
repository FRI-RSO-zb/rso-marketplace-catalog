package net.bobnar.marketplace.loaderAgent.avtonet;

import net.bobnar.marketplace.loaderAgent.processor.ProcessItemResult;
import net.bobnar.marketplace.loaderAgent.processor.ProcessListResult;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class AvtoNetProcessorTest {

    @Test
    void processTop100List() throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("src/test/resources/avtonet-top100-list.html");
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);
        String data = reader.lines().collect(Collectors.joining(System.lineSeparator()));

        ProcessListResult<AvtoNetListItem> result = new AvtoNetProcessor().processItemList(data);

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("", result.errors);
        assertEquals("ok", result.status);
        assertEquals(99, result.processedItems.size()); // We actually only get 99 ads in the top100
    }

//    @Test
//    void processListing() throws FileNotFoundException {
//        FileInputStream fis = new FileInputStream("src/test/resources/avtonet-ad-details-18980973.html");
//        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
//        BufferedReader reader = new BufferedReader(isr);
//        String data = reader.lines().collect(Collectors.joining(System.lineSeparator()));
//
//        var result = new AvtoNetLoader().processListing(data);
//
//        assertNotNull(result);
//    }

    @Test
    void  processListItem() throws Exception {
        FileInputStream fis = new FileInputStream("src/test/resources/avtonet-top100-list-row.html");
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);
        String data = reader.lines().collect(Collectors.joining(System.lineSeparator()));

        ProcessItemResult<AvtoNetListItem> result = new AvtoNetProcessor().processListItem(data);

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.item);
        assertEquals("Renault Scenic Dynamique dCi 110 NAVI-ALU-TEMP-USNJE-BREZ POLOGA", result.item.title);
        assertEquals(18972791, result.item.id);
        assertEquals("Renault Scenic", result.item.shortTitle);
        assertEquals("https://images.avto.net/photo/18972791/1056238_160.jpg", result.item.photoPath);
        assertEquals(2012, result.item.firstRegistrationYear);
        assertEquals(1500000, result.item.drivenDistanceKm);
        assertEquals("diesel motor", result.item.engineType);
        assertEquals("ročni menjalnik", result.item.transmissionType);
        assertEquals("1461 ccm, 81 kW / 110 KM", result.item.engineParameters);
        assertEquals("DELNO USNJE LED NAVIGACIJA TEMPOMAT OPRAVLJEN VELIK SERVIS FINANCIRANJE BREZ POLOGA TUDI ZA TUJCE", result.item.sellerNotes);
        assertEquals(16101, result.item.dealerId);
        assertEquals("5.990 €", result.item.regularPrice);
    }
}
