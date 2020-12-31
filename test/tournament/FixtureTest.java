package tournament;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the tests for the Fixture class.
 */
public class FixtureTest {

    /*
     * This class tests the following methods:
     * 
     * generate()
     *   - even number of teams
     *   - odd number of teams
     *   
     * writeToDatabase()
     */
        
    // covers even number of teams
    public void testFixtureManual1() {
        List<String> teams = new ArrayList<>(List.of("a", "b", "c", "d"));
        Fixture fixture = Fixture.generate(teams);
        System.out.println(fixture);
    }
    
    // covers odd number of teams
    public void testFixtureManual2() {
        List<String> teams = new ArrayList<>(List.of("a", "b", "c", "d", "e"));
        Fixture fixture = Fixture.generate(teams);
        System.out.println(fixture);
    }
    
    // covers writeToDatabase
    public void testDatabaseManual() throws SQLException {
        // First, make sure from psql that no table named matches exists in database fixturemaker
        List<String> teams = new ArrayList<>(List.of("a", "b", "c", "d"));
        Fixture fixture = Fixture.generate(teams);
        DatabaseConnection database = new DatabaseConnection();
        database.createSchema("tournament");
        fixture.writeToDatabase("tournament", database);
        // Then, check the tournament.matches table in the database
        // When done, drop the schema from the database using
        // DROP SCHEMA tournament CASCADE;
    }
   
}
