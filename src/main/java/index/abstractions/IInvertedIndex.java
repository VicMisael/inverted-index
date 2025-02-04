package index.abstractions;

import index.csv.RowItem;

import java.util.List;

public interface IInvertedIndex {
    List<RowItem> find(final String term);
}
