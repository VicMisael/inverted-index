package index;



import index.abstractions.IInvertedIndex;
import index.abstractions.IRowCollection;
import index.abstractions.IRowItem;
import index.abstractions.IRowKey;
import index.csv.RowCollection;
import index.csv.RowItem;
import index.utils.Stemmer;

import java.util.*;

public class InvertedIndex implements IInvertedIndex {

    private final HashMap<String,List<IRowKey>> indexMap = new HashMap<>();

    // Keep a reference to the original collection so we can map keys back to items.
    private final RowCollection rowCollection;

    public InvertedIndex(final RowCollection rowItems){
        buildIndex(rowItems);
        rowCollection = rowItems;
    }
    private void buildIndex(RowCollection collection){
        for (RowItem item : collection) {
            List<String> terms = Stemmer.processText(item.getIndexableString());
            for (String term : terms) {
                indexMap.computeIfAbsent(term, k -> new ArrayList<>()).add(item.getRowKey());
            }
        }
    }

    @Override
    public List<RowItem> find(String term) {
        List<String> processedTerms = Stemmer.processText(term);

        // If no valid terms are extracted, return an empty result.
        if (processedTerms.isEmpty()) {
            return List.of();
        }

        // Use a set to store the intersection of keys matching all terms.
        Set<IRowKey> resultKeys = null;
        for (String processedTerm : processedTerms) {
            List<IRowKey> keysForTerm = indexMap.get(processedTerm);
            if (keysForTerm == null || keysForTerm.isEmpty()) {
                // If any term is not present, then no row can match all terms.
                return List.of();
            }
            if (resultKeys == null) {
                // For the first term, initialize the result set.
                resultKeys = new HashSet<>(keysForTerm);
            } else {
                // For subsequent terms, take the intersection.
                resultKeys.retainAll(keysForTerm);
            }
            // If the intersection is empty at any point, we can return early.
            if (resultKeys.isEmpty()) {
                return List.of();
            }
        }

        // Convert the resulting keys into IRowItem objects using the rowCollection lookup.
        List<RowItem> results = new ArrayList<>();
        for (IRowKey key : resultKeys) {
            RowItem item = rowCollection.findByKey(key);
            if (item != null) {
                results.add(item);
            }
        }
        return results;
    }
}
