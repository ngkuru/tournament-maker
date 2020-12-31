package tournament;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * This class contains the tests for the Tournament class.
 */
public class TournamentTest {

    /*
     * This class tests the following methods:
     * 
     * matchesTableName()
     * teamsTableName()
     * create()
     * delete()
     * enterScore()
     * removeScoreIfExists()
     *   - score exists
     *   - score doesn't exist
     *   
     * getTable()
     * getRoundNames()
     * getMatchesByRound()
     */
    
    // covers matches table name
    @Test
    public void testMatchesTableName() {
        assertEquals("test.matches", Tournament.matchesTableName("test"), "incorrect table name");
    }
    
    // covers teams table name
    @Test
    public void testTeamsTableName() {
        assertEquals("test.teams", Tournament.teamsTableName("test"), "incorrect table name");
    }
    
    // covers create
    // @Test
    public void testCreateManual() throws SQLException {
        // First check that no schema named test exists in fixturemaker
        final List<String> teams = List.of("a", "b", "c", "d");
        Tournament t = new Tournament();
        t.create("test", teams);
        // Now check and drop the schema;  
    }
    
    // covers delete
    // @Test
    public void testDeleteManual() throws SQLException {
        // First check that no schema named test exists in fixturemaker
        final List<String> teams = List.of("a", "b", "c", "d");
        Tournament t = new Tournament();
        t.create("test", teams);
        t.delete("test");
        // Now check
    }
    
    // covers enter
    // @Test
    public void testEnterManual() throws SQLException {
        // First check that no schema named test exists in fixturemaker
        final List<String> teams = List.of("a", "b", "c", "d");
        Tournament t = new Tournament();
        t.create("test", teams);
        t.enterScore("test", "Round 1", 1, 1, 0);
        // Now check the matches table and drop the schema
    }
    
    // covers remove score exists
    // @Test
    public void testRemoveManual1() throws SQLException {
        // First check that no schema named test exists in fixturemaker
        final List<String> teams = List.of("a", "b", "c", "d");
        Tournament t = new Tournament();
        t.create("test", teams);
        t.enterScore("test", "Round 1", 1, 1, 0);
        t.removeScoreIfExists("test", "Round 1", 1);
        // Now check the matches table and drop the schema
    }
    
    // covers remove score doesn't exist
    // @Test
    public void testRemoveManual2() throws SQLException {
        // First check that no schema named testremove exists in fixturemaker
        final List<String> teams = List.of("a", "b", "c", "d");
        Tournament t = new Tournament();
        t.create("test", teams);
        t.enterScore("test", "Round 1", 1, 2, 1);
        t.removeScoreIfExists("test", "Round 1", 2);
        // Now check the matches table and drop the schema
    } 
    
    // covers table
    // @Test 
    public void testTable() throws SQLException {
        // First check that no schema named test exists in fixturemaker
        final List<String> teams = List.of("a", "b", "c", "d");
        Tournament t = new Tournament();
        t.create("test", teams);
        t.enterScore("test", "Round 1", 1, 2, 1);
        System.out.println(t.getTable("test"));
        t.delete("test");
        // Now check
    }
    
    // covers round names
    // @Test
    public void testRoundNames() throws SQLException {
        // First check that no schema named test exists in fixturemaker
        final List<String> teams = List.of("a", "b", "c", "d");
        Tournament t = new Tournament();
        t.create("test", teams);
        t.enterScore("test", "Round 1", 1, 2, 1);
        System.out.println(t.getRoundNames("test"));
        t.delete("test");
        // Now check
    }
    
    // covers matches
    // @Test
    public void testMatches() throws SQLException {
        // First check that no schema named test exists in fixturemaker
        final List<String> teams = List.of("a", "b", "c", "d");
        Tournament t = new Tournament();
        t.create("test", teams);
        t.enterScore("test", "Round 1", 1, 2, 1);
        System.out.println(t.getMatchesByRound("test", "Round 1"));
        t.delete("test");
        // Now check
    }
}
