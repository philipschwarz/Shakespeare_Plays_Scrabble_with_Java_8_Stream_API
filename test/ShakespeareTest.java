import org.junit.Test;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;

import static org.junit.Assert.assertEquals;

public class ShakespeareTest
{
    private static final int [] letterScores =
    {
        // a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q,  r, s, t, u, v, w, x, y, z 
           1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10
    };

    private IntUnaryOperator letterScore = letter -> letterScores[letter - 'a'];

    private int wordScore(String word)
    {
        return word.chars().map(letterScore).sum();
    }

    @Test public void test_letterScore_a() { assertEquals(1, letterScore.applyAsInt('a')); }

    @Test public void test_letterScore_j() { assertEquals(8, letterScore.applyAsInt('j')); }

    @Test public void test_letterScore_z() { assertEquals(10, letterScore.applyAsInt('z')); }

    @Test public void test_wordScore_meander() { assertEquals(10, wordScore("meander")); }

    @Test public void test_wordScore_pejorative() { assertEquals(22, wordScore("pejorative")); }

    @Test public void test_wordScore_quotient() { assertEquals(17, wordScore("quotient")); }

    @Test public void test_words_with_best_scores()
    {
        assertEquals(
            Arrays.asList("pejorative", "quotient", "meander"),
            highestScoringWordsIn(Arrays.asList("alas", "pejorative", "to", "be", "or", "quotient", "not", "meander")));
    }
}
