package tournament;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * This class contains the tests for the TeamList class.
 */
public class TeamListTest {

    /*
     * This class tests the following methods:
     * 
     * add()
     * remove()
     * getList()
     *   - add
     *   - add and remove
     *   - add, remove and add back
     *   - add and remove but not right after
     *   
     * writeToDatabase()
     */
    
    // covers add
    @Test
    public void testAdd() {
        TeamList teamList = new TeamList();
        teamList.add("team1");
        teamList.add("team2");
        List<String> teams = teamList.getList();
        assertTrue(teams.contains("team1"), "failure to add or retrieve teams");
        assertTrue(teams.contains("team2"), "failure to add or retrieve teams");
    }
    
    // covers add and remove
    @Test
    public void testRemove1() {
        TeamList teamList = new TeamList();
        teamList.add("team1");
        teamList.remove("team1");
        List<String> teams = teamList.getList();
        assertEquals(0, teams.size(), "failure to remove teams");
    }
    
    // covers add, remove and add back
    @Test
    public void testRemove2() {
        TeamList teamList = new TeamList();
        teamList.add("team2");
        teamList.remove("team2");
        teamList.add("team2");
        List<String> teams = teamList.getList();
        assertEquals(1, teams.size(), "failure to remove teams");
        assertTrue(teams.contains("team2"), "failure to remove teams");
    }
    
    // covers add and remove but not right after
    @Test
    public void testRemove3() {
        TeamList teamList = new TeamList();
        teamList.add("team3");
        teamList.add("team4");
        teamList.remove("team3");
        teamList.remove("team4");
        teamList.add("team3");
        List<String> teams = teamList.getList();
        assertEquals(1, teams.size(), "failure to remove teams");
        assertTrue(teams.contains("team3"), "failure to remove teams");
    }
    
    // covers writeToDatabase
    public void testDatabaseManual() throws SQLException {
        // First, make sure from psql that no schema named tournament exists in database fixturemaker
        TeamList teamList = new TeamList();
        teamList.add("team1");
        teamList.add("team2");
        DatabaseConnection database = new DatabaseConnection();
        database.createSchema("tournament");
        teamList.writeToDatabase("tournament", database);
        // Then, check the tournament.teams table in the database
        // When done, drop the schema from the database using
        // DROP SCHEMA tournament CASCADE;
    }
}
