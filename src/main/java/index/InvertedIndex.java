package index;



import index.abstractions.IInvertedIndex;
import index.abstractions.IRowKey;
import index.csv.RowCollection;
import index.csv.RowItem;
import index.utils.Stemmer;

import java.util.*;
import java.util.stream.Collectors;

public class InvertedIndex implements IInvertedIndex {

    private final HashMap<String,List<IRowKey>> indexMap = new HashMap<>();

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

        List<String> processedTerms  = Arrays.stream(term.split(";"))
                .flatMap(part -> Stemmer.processText(part).stream())
                .toList();

        if (processedTerms.isEmpty()) {
            return List.of();
        }

        return  processedTerms.stream().map(indexMap::get).filter(Objects::nonNull).
                flatMap(Collection::stream).distinct().map(rowCollection::findByKey).filter(Objects::nonNull).toList();

    }
}
