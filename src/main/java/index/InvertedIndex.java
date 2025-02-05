package index;



import index.abstractions.IInvertedIndex;
import index.abstractions.IRowKey;
import index.csv.RowCollection;
import index.csv.RowItem;
import index.utils.Stemmer;

import java.util.*;
import java.util.stream.Collectors;

public class InvertedIndex implements IInvertedIndex {

    private final HashMap<String,Map<IRowKey, Long>> indexMap = new HashMap<>();
    private final Map<String, Long> documentFrequencies = new HashMap<>();


    private final RowCollection rowCollection;

    public InvertedIndex(final RowCollection rowItems){
        buildIndex(rowItems);
        rowCollection = rowItems;
    }
    private void buildIndex(RowCollection collection){
        long docCount = collection.size();
        for (RowItem item : collection) {
            Set<String> uniqueTerms = new HashSet<>();
            List<String> terms = Stemmer.processText(item.getIndexableString());
            terms.forEach(term -> {
                indexMap.computeIfAbsent(term, k -> new HashMap<>()).merge(item.getRowKey(), 1L, Long::sum);
                uniqueTerms.add(term);
            });
            uniqueTerms.forEach(term -> documentFrequencies.merge(term, 1L, Long::sum));
        }
        documentFrequencies.forEach((term, count) -> documentFrequencies.put(term, (long) Math.log(docCount / (double) count)));
    }

    @Override
    public List<RowItem> find(String term) {

        List<String> processedTerms = Arrays.stream(term.split(";"))
                .flatMap(part -> Stemmer.processText(part).stream())
                .toList();

        if (processedTerms.isEmpty()) {
            return List.of();
        }

        Map<IRowKey, Double> scores = new HashMap<>();
        for (String processedTerm : processedTerms) {
            Map<IRowKey, Long> termDocs = indexMap.get(processedTerm);
            if (termDocs != null) {
                double idf = documentFrequencies.getOrDefault(processedTerm, 0L);
                termDocs.forEach((key, tf) -> scores.merge(key, tf * idf, Double::sum));
            }
        }

        return scores.entrySet().stream()
                .sorted(Map.Entry.<IRowKey, Double>comparingByValue().reversed())
                .map(entry -> rowCollection.findByKey(entry.getKey()))
                .filter(Objects::nonNull)
                .limit(20).collect(Collectors.toList());
    }
}
