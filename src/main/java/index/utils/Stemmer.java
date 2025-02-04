package index.utils;

import opennlp.tools.stemmer.PorterStemmer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Stemmer {
    private static final Set<String> stopwords = new HashSet<>(Arrays.asList(
            "e", "o", "a", "os", "as", "de", "do", "da", "dos", "das", "um", "uma", "uns", "umas", "em", "no", "na", "nos", "nas",
            "por", "pelo", "pela", "pelos", "pelas", "para", "com", "se", "não", "ao", "aos", "à", "às", "que", "como", "mas", "ou", "seu", "sua"
    ));

    public static List<String> processText(String text) {
        PorterStemmer stemmer = new PorterStemmer();
        List<String> words = Arrays.asList(text.toLowerCase().split("\\s+"));
        // Apply stemming using Porter stemmer as a placeholder
        return words.stream()
                .filter(word -> !stopwords.contains(word))  // Remove stopwords
                .map(stemmer::stem)
                .collect(Collectors.toList());
    }
}
