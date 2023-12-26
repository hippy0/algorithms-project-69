package hexlet.code;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SearchEngine {

    public static List<String> search(List<Map<String, String>> docs, String sentence) {
        if (docs.isEmpty()) {
            return List.of();
        }

        Map<String, Double> approachMap = new HashMap<>();

        String sentenceTerm = Pattern.compile("\\w+")
                .matcher(sentence)
                .results()
                .map(MatchResult::group)
                .collect(Collectors.joining(" "));

        for (Map<String, String> doc : docs) {
            String text = doc.get("text");
            text = text.replace("'", "");

            String textTerm = Pattern.compile("\\w+")
                    .matcher(text)
                    .results()
                    .map(MatchResult::group)
                    .collect(Collectors.joining(" "));

            for (String key : sentenceTerm.split(" ")) {
                for (String word : textTerm.split(" ")) {
                    if (word.contentEquals(key)) {
//                        if (approachMap.containsKey(doc.get("id"))) {
//                            approachMap.put(doc.get("id"), approachMap.get(doc.get("id")) + 1);
//                        } else {
//                            approachMap.put(doc.get("id"), 1);
//                        }
//
//                        break;
                        approachMap.put(doc.get("id"), getTFxIDF(doc, docs, key));
                    }
                }
            }
        }

        if (approachMap.isEmpty()) {
            return List.of();
        }

        List<Map.Entry<String, Double>> sortedListOfEntries = approachMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Double::compareTo))
                .toList()
                .reversed();

        return sortedListOfEntries.stream()
                .map((Map.Entry::getKey))
                .toList();
    }

    private static double getIDF(List<Map<String, String>> docs, String term) {
        double idf = 0;

        for (Map<String, String> doc : docs) {
            String text = doc.get("text");
            text = text.replace("'", "");

            String textTerm = Pattern.compile("\\w+")
                    .matcher(text)
                    .results()
                    .map(MatchResult::group)
                    .collect(Collectors.joining(" "));

            for (String word : textTerm.split(" ")) {
                if (word.equalsIgnoreCase(term)) {
                    idf++;
                    break;
                }
            }
        }

        return Math.log(docs.size() / idf);
    }

    private static double getTF(Map<String, String> doc, String term) {
        double tf = 0;

        String text = doc.get("text");
        text = text.replace("'", "");

        String textTerm = Pattern.compile("\\w+")
                .matcher(text)
                .results()
                .map(MatchResult::group)
                .collect(Collectors.joining(" "));

        for (String word : textTerm.split(" ")) {
            if (word.equals(term)) {
                tf++;
            }
        }

        return tf / textTerm.split(" ").length;
    }

    private static double getTFxIDF(Map<String, String> doc, List<Map<String, String>> docs, String term) {
        return getTF(doc, term) * getIDF(docs, term);
    }
}
