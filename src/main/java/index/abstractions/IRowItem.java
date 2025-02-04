package index.abstractions;

public interface IRowItem<T extends IRowItem<T>> {
    String getIndexableString();

    IRowKey getRowKey();

     T getAsChild();
}
