package tournament;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * This class contains the tests for the Round class.
 */
public class RoundTest {

    /*
     * This class tests the following methods:
     * 
     * equals()
     *   - equal rounds
     *   - equal rounds with different match constructors
     *   - different game
     *   - different ordering
     * 
     * addMatch()
     * getMatches()
     * generateMirror()
     */
    
    /*
     * Tests for equals()
     */
    
    // covers equal rounds
    @Test
    public void testEquals1() {
        Match match1 = new Match("a", "b");
        Match match2 = new Match("c", "d");
        Round round1 = new Round();
        round1.addMatch(match1);
        round1.addMatch(match2);
        Round round2 = new Round();
        round2.addMatch(match1);
        round2.addMatch(match2);
        assertTrue(round1.equals(round2), "incorrect equality");
    }
    
    // covers equal rounds with different match constructors
    @Test
    public void testEquals2() {
        Match match1 = new Match("a", "b");
        Match match2 = new Match("c", "d");
        Match match3 = new Match("c", "d");
        Round round1 = new Round();
        round1.addMatch(match1);
        round1.addMatch(match2);
        Round round2 = new Round();
        round2.addMatch(match1);
        round2.addMatch(match3);
        assertTrue(round1.equals(round2), "incorrect equality");
    }
    
    // covers different game
    @Test
    public void testEquals3() {
        Match match1 = new Match("a", "b");
        Match match2 = new Match("c", "d");
        Match match3 = new Match("e", "f");
        Round round1 = new Round();
        round1.addMatch(match1);
        round1.addMatch(match2);
        Round round2 = new Round();
        round2.addMatch(match1);
        round2.addMatch(match3);
        assertTrue(!round1.equals(round2), "incorrect equality");
    }
    
    // covers different ordering
    @Test
    public void testEquals4() {
        Match match1 = new Match("a", "b");
        Match match2 = new Match("c", "d");
        Round round1 = new Round();
        round1.addMatch(match1);
        round1.addMatch(match2);
        Round round2 = new Round();
        round2.addMatch(match2);
        round2.addMatch(match1);
        assertTrue(!round1.equals(round2), "incorrect equality");
    }
   
    /*
     * Tests for addMatch(), getMatches() and generateMirror()
     */
    
    // covers addMatch and getMatches
    @Test
    public void testMatches() {
        Match match1 = new Match("a", "b");
        Match match2 = new Match("c", "d");
        Round round = new Round();
        round.addMatch(match1);
        round.addMatch(match2);
        List<Match> getMatches = round.getMatches();
        assertEquals(match1, getMatches.get(0), "incorrect matches");
        assertEquals(match2, getMatches.get(1), "incorrect matches");
    }
    
    // covers generateMirror
    @Test
    public void testMirror() {
        // Generate the matches and their mirrors
        Match match1 = new Match("a", "b");
        Match match2 = new Match("c", "d");
        Match mirror1 = match1.generateMirror();
        Match mirror2 = match2.generateMirror();
        // Create the main round
        Round round1 = new Round();
        round1.addMatch(match1);
        round1.addMatch(match2);
        // Create the expected mirror
        Round round2 = new Round();
        round2.addMatch(mirror1);
        round2.addMatch(mirror2);
        // Generate the mirror and check
        Round mirror = round1.generateMirror();
        assertEquals(round2, mirror, "incorrect mirror");
    }
    
}
