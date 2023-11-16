package net.bobnar.marketplace.loaderAgent.services;

import net.bobnar.marketplace.loaderAgent.AvtoNetListItemResult;
import net.bobnar.marketplace.loaderAgent.BolhaListItemResult;
import net.bobnar.marketplace.loaderAgent.ProcessResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class BolhaServiceTest {

    @Test
    void processList() throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("src/test/resources/bolha-avto-oglasi.html");
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);
        String data = reader.lines().collect(Collectors.joining(System.lineSeparator()));

        ProcessResult<List<BolhaListItemResult>> result = new BolhaService().processList(data);

        assertNotNull(result);
        assertEquals("", result.errors);
        assertEquals("ok", result.status);
        assertEquals(31, result.resultItem.size()); // We actually only get 99 ads in the top100
    }

//    @Test
//    void processListing() throws FileNotFoundException {
//        FileInputStream fis = new FileInputStream("src/test/resources/avtonet-ad-details-18980973.html");
//        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
//        BufferedReader reader = new BufferedReader(isr);
//        String data = reader.lines().collect(Collectors.joining(System.lineSeparator()));
//
//        var result = new AvtoNetService().processListing(data);
//
//        assertNotNull(result);
//    }

    @Test
    void  processListItem() throws Exception {
        FileInputStream fis = new FileInputStream("src/test/resources/bolha-avto-oglasi-row.html");
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);
        String data = reader.lines().collect(Collectors.joining(System.lineSeparator()));

        Document item = Jsoup.parse(data);

        BolhaListItemResult result = new BolhaService().processListItem(item);

        assertNotNull(result);

        assertFalse(result.isExposed);
        assertEquals("VW Arteon 2.0 TDI 110kW-MODEL 2020-ODLIČEN-MENJAM-UGODNO", result.title);
        assertEquals(11911746, result.id);
        assertEquals("https://static.bolha.com/dist/ceb7cc1a7b.png", result.photoPath);
        assertEquals("Rabljeno vozilo", result.age);
        assertEquals(1, result.drivenDistanceKm);
        assertEquals(2019, result.manufacturingYear);
        assertEquals("Sevnica, Sevnica", result.location);
        assertEquals("2023-11-16T19:05:17+01:00", result.publishDate);
        assertEquals("24.500 €", result.price);
    }
}
