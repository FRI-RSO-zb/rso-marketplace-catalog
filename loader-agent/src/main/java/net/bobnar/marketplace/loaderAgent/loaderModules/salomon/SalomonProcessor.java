package net.bobnar.marketplace.loaderAgent.loaderModules.salomon;

import net.bobnar.marketplace.loaderAgent.processor.ProcessItemResult;
import net.bobnar.marketplace.loaderAgent.processor.ProcessListResult;
import net.bobnar.marketplace.loaderAgent.processor.ProcessorBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SalomonProcessor extends ProcessorBase<Object, SalomonListItem> {

    @Override
    public ProcessItemResult<Object> processItem(String data) {
        return null;
    }

    @Override
    public ProcessListResult<SalomonListItem> processItemList(String data) {
        return null;
    }

    @Override
    public ProcessItemResult<SalomonListItem> processListItem(String data) {
        ProcessItemResult<SalomonListItem> result = new ProcessItemResult<>();
        result.success();

        Document row = Jsoup.parse(data);

        result.item = new SalomonListItem();

        return result;
    }
}
