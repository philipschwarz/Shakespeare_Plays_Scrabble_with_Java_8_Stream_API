import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static org.junit.Assert.*;

public class ShakespeareTest
{
    private static final int [] letterScores =
    {
        // a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q,  r, s, t, u, v, w, x, y, z 
           1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10
    };

    private static final int [] scrabbleAvailableLetters =
    {
        // a, b, c, d,  e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z   
           9, 2, 2, 1, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1
    };

    private int alphabeticIndexOf(Integer character) { return character - 'a'; }

    private Supplier<TreeMap<Integer,List<String>>> reverseOrderMapFactory = () -> new TreeMap<Integer,List<String>>(Comparator.reverseOrder());

    private IntUnaryOperator letterScore = letter -> letterScores[alphabeticIndexOf(letter)];

    private Function<String,Integer> score = word -> word.chars().map(letterScore).sum();

    private Collector<String, ?, TreeMap<Integer,List<String>>> intoDescendingMapFromScoreToWordsWithScore =
        Collectors.groupingBy(
            score,
            reverseOrderMapFactory,
            toList()
        );

    private Function<List<String>, Stream<? extends String>> listOfWordsToStreamOfWords = List::stream;

    private Function<Map.Entry<Integer, List<String>>, List<String>> mapEntryForScoreToWordsWithScore = Map.Entry::getValue;

    private List<String> highestScoringWordsIn(List<String> words)
    {
        return words
            .stream()
            .collect(intoDescendingMapFromScoreToWordsWithScore)
            .entrySet()
            .stream()
            .limit(3)
            .map(mapEntryForScoreToWordsWithScore)
            .flatMap(listOfWordsToStreamOfWords)
            .collect(toList());
    }

    private Collector<Integer, ?, Map<Integer, Long>> intoMapFromLetterToNumberOfOccurrences =
        groupingBy(identity(), counting());

    private Map<Integer,Long> letterCountHistogramFor(String word)
    {
        return word
            .chars()
            .boxed()
            .collect(intoMapFromLetterToNumberOfOccurrences);
    }

    private Predicate<Map.Entry<Integer, Long>> letterCountIsLegal = keyValuePair ->
    {
        Integer letter = keyValuePair.getKey();
        Long occurrencesOfLetter = keyValuePair.getValue();
        return occurrencesOfLetter <= scrabbleAvailableLetters[alphabeticIndexOf(letter)];
    };

    private boolean canWrite(String word)
    {
        return letterCountHistogramFor(word)
                .entrySet()
                .stream()
                .allMatch(letterCountIsLegal);
    }

    @Test public void test_letterScore_a() { assertEquals(1, letterScore.applyAsInt('a')); }

    @Test public void test_letterScore_j() { assertEquals(8, letterScore.applyAsInt('j')); }

    @Test public void test_letterScore_z() { assertEquals(10, letterScore.applyAsInt('z')); }

    @Test public void test_wordScore_meander() { assertEquals(10, score.apply("meander").intValue()); }

    @Test public void test_wordScore_pejorative() { assertEquals(22, score.apply("pejorative").intValue()); }

    @Test public void test_wordScore_quotient() { assertEquals(17, score.apply("quotient").intValue()); }

    @Test public void test_words_with_best_scores()
    {
        assertEquals(
            asList("pejorative", "quotient", "meander"),
            highestScoringWordsIn(asList("alas", "pejorative", "to", "be", "or", "quotient", "not", "meander")));
    }

    @Test public void test_histogram_of_letter_count_in_word()
    {
        Map<Integer,Long> histogram = new HashMap();
        histogram.put((int)'t',1L);
        histogram.put((int)'r',1L);
        histogram.put((int)'a',3L);
        histogram.put((int)'l',2L);
        assertEquals(histogram, letterCountHistogramFor("tralala"));
    }

    @Test public void test_cannot_write()
    {
        assertFalse(canWrite("whizzing"));
    }

    @Test public void test_can_write()
    {
        assertTrue(canWrite("antidisestablishmenterianism"));
    }
}
