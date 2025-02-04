package index.csv;

import index.abstractions.IRowItem;
import index.abstractions.IRowKey;
import org.apache.commons.csv.CSVRecord;

public class RowItem implements IRowItem<RowItem> {
    private final CSVRecord record;
    private final RowKey rowKey;

    public RowItem(CSVRecord record, int rowIndex) {
        this.record = record;
        this.rowKey = new RowKey(rowIndex);
    }

    @Override
    public String getIndexableString() {
        return record.get("text_content_anonymous"); // Direct access by header name
    }

    @Override
    public IRowKey getRowKey() {
        return rowKey;
    }

    @Override
    public RowItem getAsChild() {
        return this;
    }

    @Override
    public String toString() {
       return rowKey.getKey() + " " + record.get("text_content_anonymous");
    }
}
