import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShakespeareTest
{
    private static final int [] letterScores =
    {
        // a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q,  r, s, t, u, v, w, x, y, z 
           1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10
    };

    private int letterScore(char letter)
    {
        return letterScores[letter - 'a'];
    }

    @Test
    public void test_letterScore_a()
    {
        assertEquals(1, letterScore('a'));
    }
}
