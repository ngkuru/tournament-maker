package tournament;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing the fixture.
 */
public class Fixture {
    
    private final List<Round> rounds = new ArrayList<>();

    /*
     * AF: f(rounds) -> Fixture in the following format:
     * 
     * fixture ::= [round1, round2, ..., roundN]
     * round ::= [game1, game2, ..., gameN]
     * game ::= [homeTeam, awayTeam]
     */

    /**
     * Generate a random round robin with the given teams where every team plays every other team 
     * twice.
     * @param teams
     * @return fixture
     */
    static Fixture generate(List<String> teams) {
        // Get the team list and shuffle
        Collections.shuffle(teams);
        // Add a dummy team if necessary
        final int n = (teams.size() % 2 == 1) ? teams.size() + 1: teams.size();
        // Generate the first half
        final Fixture fixture = new Fixture();
        // There are n-1 rounds in the first half
        for (int i = 0; i < n-1; i++) {
            final Round round = new Round();
            // If not the dummy, team n-1 goes around playing everyone one by one
            if (teams.size() % 2 == 0) {
                round.addMatch(new Match(teams.get(n-1), teams.get(i)));    
            }
            // The rest divide into two halves and mirroring pairs play each other
            for (int j = 1; j < n/2; j++) {
                final int home = (i+j) % (n-1);
                final int away = (i-j + n-1) % (n-1);
                round.addMatch(new Match(teams.get(home), teams.get(away)));
            }
            // Shuffle the matches
            round.deepShuffle();
            // Add the round to the fixture
            fixture.addRound(round);
        }
        // Shuffle the first half
        fixture.shuffle();
        // Generate the second half
        fixture.generateSecondHalf();
        // Return the fixture
        return fixture;
    }

    /**
     * Insert the fixture to the given database. Creates the matches table.
     * @param database
     * @throws SQLException 
     */
    void writeToDatabase(String tournament, DatabaseConnection database) throws SQLException {
        final String tableName = Tournament.matchesTableName(tournament);
        createTable(tableName, database);
        for (int i = 0; i < rounds.size(); i++) {
            final String roundName = "Round " + (i+1);
            insertRoundToDatabase(tableName, rounds.get(i), roundName, database);
        }
    }
    
    /**
     * Add a round to the fixture.
     * @param round
     */
    private void addRound(Round round) {
        rounds.add(round);
    }
    
    /**
     * Shuffle the ordering of the matches.
     */
    private void shuffle() {
        Collections.shuffle(rounds);
    }

    /**
     * Add a mirror of the first half to the fixture.
     */
    private void generateSecondHalf() {
        final List<Round> roundsToAdd = new ArrayList<>();
        for (Round round: rounds) {
            roundsToAdd.add(round.generateMirror());
        }
        for (Round round: roundsToAdd) {
            addRound(round);
        }
    }
    
    /**
     * Create a table to insert the fixture in.
     * @param database
     * @throws SQLException
     */
    private void createTable(String tableName, DatabaseConnection database) throws SQLException {
        final List<String> columns = List.of(
                "id BIGSERIAL NOT NULL PRIMARY KEY", 
                "round VARCHAR(50) NOT NULL", 
                "game INT NOT NULL", 
                "home VARCHAR(50) NOT NULL", 
                "away VARCHAR(50) NULL", 
                "home_score INT", 
                "away_score INT"
                );
        database.createTable(tableName, columns);
    }
    
    /**
     * Insert a round to the database with the given name. Assumes the table TABLE_NAME exists.
     * @param round
     * @param roundName
     * @param database
     * @throws SQLException 
     */
    private void insertRoundToDatabase(String tableName, Round round, String roundName, 
            DatabaseConnection database) throws SQLException {
        // Initialize the columns we are going to set values for
        final List<String> columns = List.of("round", "game", "home", "away");
        // Get the matches from the round
        final List<Match> matches = round.getMatches();
        for (int i = 0; i < matches.size(); i++) {
            // Get the match information
            final int gameNumber = i+1;
            final String home = matches.get(i).getHomeTeam();
            final String away = matches.get(i).getAwayTeam();
            // Initialize the values and insert the row
            final List<Object> values = List.of(roundName, gameNumber, home, away);
            database.insert(tableName, columns, values);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rounds.size(); i++) {
            if (i != 0) {
                sb.append("\n\n");
            }
            sb.append("Round " + Integer.toString(i+1) + "\n");
            sb.append(rounds.get(i).toString());
        }
        return sb.toString();
    }
}
