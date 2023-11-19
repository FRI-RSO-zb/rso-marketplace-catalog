package net.bobnar.marketplace.loaderAgent.loaderModules.doberAvto;

import net.bobnar.marketplace.loaderAgent.processor.ProcessItemResult;
import net.bobnar.marketplace.loaderAgent.processor.ProcessListResult;
import net.bobnar.marketplace.loaderAgent.processor.ProcessorBase;
import org.json.JSONArray;
import org.json.JSONObject;

public class DoberAvtoProcessor extends ProcessorBase<Object, DoberAvtoListItem> {
    @Override
    public ProcessItemResult<Object> processItem(String data) {
        return null;
    }

    @Override
    public ProcessListResult<DoberAvtoListItem> processItemList(String data) {
        ProcessListResult<DoberAvtoListItem> result = new ProcessListResult<>();
        result.success();

        JSONObject input = new JSONObject(data);
        JSONArray items = input.getJSONArray("results");

        for (Object item : items) {
            result.processedItems.add(null);
        }

//        input.get("");

        return result;
    }

    @Override
    public ProcessItemResult<DoberAvtoListItem> processListItem(String data) {
        var item = new JSONObject(data);

        return this.processListItem(item);
    }

    private ProcessItemResult<DoberAvtoListItem> processListItem(JSONObject item) {
        ProcessItemResult<DoberAvtoListItem> result = new ProcessItemResult<>();
        result.success();

        result.item = new DoberAvtoListItem();
        result.item.id = item.getString("postId");
        result.item.manufacturer = item.getString("manufacturer");
        result.item.title = item.getString("modelName");
        result.item.manufacturer = item.getString("odometer");
        result.item.manufacturer = item.getString("enginePower");
        result.item.engineDisplacementCcm = item.getString("engineDisplacement");
        result.item.manufacturer = item.getString("engineDisplacement");





        return result;
    }
}
