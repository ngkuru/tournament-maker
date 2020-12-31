package tournament;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * This class contains the tests for the Server class.
 */
public class ServerTest {

    /*
     * This class tests the following methods:
     * 
     * start()
     * tableHandler()
     * roundsHandler()
     * matchesHandler()
     * enterHandler()
     * removeHandler()
     * createHandler()
     */
    
    // tests table
    // @Test
    public void testTableManual() throws SQLException, IOException {
        // First check that no schema named test exists in fixturemaker
        final List<String> teams = List.of("a", "b", "c", "d");
        Tournament t = new Tournament();
        t.create("test", teams);
        t.enterScore("test", "Round 1", 1, 2, 1);
        // Start the server
        // 0.0.0.0:8080/table?tournament=test
        // Check that the response is equal to the print
        System.out.println(t.getTable("test"));
        // Drop the schema afterwards
    }

    // tests rounds
    // @Test
    public void testRoundsManual() throws SQLException, IOException {
        // First check that no schema named test exists in fixturemaker
        final List<String> teams = List.of("a", "b", "c", "d");
        Tournament t = new Tournament();
        t.create("test", teams);
        t.enterScore("test", "Round 1", 1, 2, 1);
        // Start the server
        // 0.0.0.0:8080/rounds?tournament=test
        // Check that the response is equal to the print
        System.out.println(t.getRoundNames("test"));
        // Drop the schema afterwards
    }

    // tests matches
    // @Test
    public void testMatchesManual() throws SQLException, IOException {
        // First check that no schema named test exists in fixturemaker
        final List<String> teams = List.of("a", "b", "c", "d");
        Tournament t = new Tournament();
        t.create("test", teams);
        t.enterScore("test", "Round 1", 1, 2, 1);
        // Start the server
        // 0.0.0.0:8080/matches?tournament=test&round=Round 1
        // Check that the response is equal to the print
        System.out.println(t.getMatchesByRound("test", "Round 1"));
        // Drop the schema afterwards
    }
    
    // tests enter
    // @Test
    public void testEnterManual() throws SQLException, IOException {
        // First check that no schema named test exists in fixturemaker
        final List<String> teams = List.of("a", "b", "c", "d");
        Tournament t = new Tournament();
        t.create("test", teams);
        // Start the server
        // 0.0.0.0:8080/enter?tournament=test&round=Round 1&game=1&home=2&away=1
        // Check the matches table from the database and drop 
    }
    
    // tests remove
    // @Test
    public void testRemoveManual() throws SQLException, IOException {
        // First check that no schema named test exists in fixturemaker
        final List<String> teams = List.of("a", "b", "c", "d");
        Tournament t = new Tournament();
        t.create("test", teams);
        t.enterScore("test", "Round 1", 1, 2, 1);
        // Start the server
        // 0.0.0.0:8080/remove?tournament=test&round=Round 1&game=1
        // Check the matches table from the database and drop 
    }

    // tests create
    // @Test
    public void testCreateManual() throws SQLException, IOException {
        // First check that no schema named test exists in fixturemaker
        // Start the server
        // 0.0.0.0:8080/create?tournament=test&teams=a,b,c,d
        // Check the database and drop 
    }
}
