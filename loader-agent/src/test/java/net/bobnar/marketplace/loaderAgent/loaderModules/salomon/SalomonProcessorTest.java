package net.bobnar.marketplace.loaderAgent.loaderModules.salomon;

import net.bobnar.marketplace.loaderAgent.TestBase;
import net.bobnar.marketplace.loaderAgent.processor.ProcessItemResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SalomonProcessorTest extends TestBase {

    @Test
    void processListItem() throws Exception {
        String data = this.loadTestResource("salomon-avtomobili-row.html");

        ProcessItemResult<SalomonListItem> result = new SalomonProcessor().processListItem(data);

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isSuccess()),
                () -> assertNotNull(result.item)
        );
    }
}
