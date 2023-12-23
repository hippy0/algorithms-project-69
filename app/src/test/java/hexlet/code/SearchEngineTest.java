package hexlet.code;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchEngineTest {

    private static List<Map<String, String>> docs;

    @BeforeAll
    static void prepareDocs() {
        String doc1 = "I can't shoot straight unless I've had a pint!";
        String doc2 = "Don't shoot shoot shoot that thing at me.";
        String doc3 = "I'm your shooter.";

        docs = List.of(
                Map.of("id", "doc1", "text", doc1),
                Map.of("id", "doc2", "text", doc2),
                Map.of("id", "doc3", "text", doc3)
        );
    }

    @Test
    void testSearchEngine() {
        List<String> actual = SearchEngine.search(docs, "had");
        List<String> expected = List.of("doc1");

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testSearchEngineWithMetrics() {
        List<String> actual = SearchEngine.search(docs, "shoot");
        List<String> expected = List.of("doc1", "doc2");

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testSearchEngineWithSticking() {
        List<String> actual = SearchEngine.search(docs, "shoot!!");
        List<String> expected = List.of("doc1", "doc2");

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testSearchEngineWithSentence() {
        List<String> actual = SearchEngine.search(docs, "shoot at me");
        List<String> expected = List.of("doc2", "doc1");

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testSearchEngineWithEmptyDocs() {
        List<String> actual = SearchEngine.search(new ArrayList<>(), "shoot");
        List<String> expected = List.of();

        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
