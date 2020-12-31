package tournament;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * This class contains the tests for the WhereClause enum.
 */
public class MatchResultTest {

    /*
     * This class tests the following methods:
     * 
     * result()
     *   - win
     *   - draw
     *   - loss
     */
    
    // covers win
    @Test
    public void testWin() {
        assertEquals(MatchResult.WIN, MatchResult.result(4, 3), "incorrect result");
    }
    
    // covers draw
    @Test
    public void testDraw() {
        assertEquals(MatchResult.DRAW, MatchResult.result(2, 2), "incorrect result");
    }
    
    // covers loss
    @Test
    public void testLoss() {
        assertEquals(MatchResult.LOSS, MatchResult.result(1, 3), "incorrect result");
    }
}
