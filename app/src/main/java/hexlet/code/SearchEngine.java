package hexlet.code;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SearchEngine {

    public static List<String> search(List<Map<String, String>> docs, String sentence) {
        if (docs.isEmpty()) {
            return List.of();
        }

        Map<String, Integer> approachMap = new HashMap<>();

        String sentenceTerm = Pattern.compile("\\w+")
                .matcher(sentence)
                .results()
                .map(MatchResult::group)
                .collect(Collectors.joining(" "));

        for (Map<String, String> doc : docs) {
            String text = doc.get("text");
            String textTerm = Pattern.compile("\\w+")
                    .matcher(text)
                    .results()
                    .map(MatchResult::group)
                    .collect(Collectors.joining(" "));

            for (String key : sentenceTerm.split(" ")) {
                for (String word : textTerm.split(" ")) {
                    if (word.contentEquals(key)) {
                        if (approachMap.containsKey(doc.get("id"))) {
                            approachMap.put(doc.get("id"), approachMap.get(doc.get("id")) + 1);
                        } else {
                            approachMap.put(doc.get("id"), 1);
                        }

                        break;
                    }
                }
            }
        }

        if (approachMap.isEmpty()) {
            return List.of();
        }

        List<Map.Entry<String, Integer>> sortedListOfEntries = approachMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Integer::compareTo))
                .toList()
                .reversed();

        return sortedListOfEntries.stream()
                .map((Map.Entry::getKey))
                .toList();
    }
}
