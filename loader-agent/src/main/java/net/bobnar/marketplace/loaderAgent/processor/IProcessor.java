package net.bobnar.marketplace.loaderAgent.processor;

public interface IProcessor<TItem, TListItem> {
    ProcessItemResult<TItem> processItem(String data);
    ProcessListResult<TListItem> processItemList(String data);
    ProcessItemResult<TListItem> processListItem(String data);
}
