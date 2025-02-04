package index.csv;

import index.abstractions.IRowKey;

public class RowKey implements IRowKey {
    private final int key;

    public RowKey(int key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return Integer.toString(key);
    }

    public int getKeyAsInt() {
        return key;  // Allows direct access to the integer key when needed
    }
}
