package tournament;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * This class contains the tests for the Match class.
 */
public class MatchTest {

    /*
     * This class tests the following methods:
     * 
     * equals()
     *   - equal matches
     *   - home team different
     *   - away team different
     *   - mirror matches
     *   
     * generateMirror()
     * getHomeTeam()
     * getAwayTeam()
     */
    
    /*
     * Tests for equals()
     */

    // covers equal matches
    @Test
    public void testEquals1() {
        Match match1 = new Match("a", "b");
        Match match2 = new Match("a", "b");
        assertTrue(match1.equals(match2), "incorrect equality");
    }
    
    // covers home team different
    @Test
    public void testEquals2() {
        Match match1 = new Match("a", "b");
        Match match2 = new Match("c", "b");
        assertTrue(!match1.equals(match2), "incorrect equality");
    }
    
    // covers away team different
    @Test
    public void testEquals3() {
        Match match1 = new Match("a", "b");
        Match match2 = new Match("a", "c");
        assertTrue(!match1.equals(match2), "incorrect equality");
    }
    
    // covers teams swapped
    @Test
    public void testEquals4() {
        Match match1 = new Match("a", "b");
        Match match2 = new Match("b", "a");
        assertTrue(!match1.equals(match2), "incorrect equality");
    }
    
    /*
     * Tests for generateMirror(), getHomeTeam() and getAwayTeam()
     */
    
    // covers generateMirror
    @Test
    public void testMirror() {
        Match match1 = new Match("a", "b");
        Match match2 = new Match("b", "a");
        assertEquals(match2, match1.generateMirror(), "incorrect mirror");
    }
    
    // covers getHomeTeam
    @Test
    public void testHome() {
        Match match = new Match("a", "b");
        assertEquals("a", match.getHomeTeam(), "incorrect home team");
    }
    
    // covers getAwayTeam
    @Test
    public void testAway() {
        Match match = new Match("a", "b");
        assertEquals("b", match.getAwayTeam(), "incorrect home team");
    }
    
}
