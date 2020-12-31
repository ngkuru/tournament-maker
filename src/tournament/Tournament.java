package tournament;

import java.sql.SQLException;
import java.util.List;

/**
 * Class containing the methods for interacting with the table and the fixtures.
 */
public class Tournament {

    private static final String MATCHES_TABLE = "matches";
    private static final String TEAMS_TABLE = "teams";
    private static final int POINTS_FOR_WIN = 3;
    private static final int POINTS_FOR_DRAW = 1;
    private static final int POINTS_FOR_LOSS = 0;

    private final DatabaseConnection database;

    /*
     * AF: f(database) -> Tournament helper which allows to create and edit tournaments, where 
     *   each tournament is represented as a schema with two tables which are controlled by 
     *   the database field.
     * 
     * All information is contained in the schema called name. There are two tables called teams 
     * and matches. teams contains information about the table, namely:
     *   - id
     *   - name
     *   - wins
     *   - draws
     *   - losses
     *   - scores for
     *   - scores against
     *   - points
     * matches contains information about the fixture, namely:
     *   - id
     *   - round
     *   - game
     *   - home
     *   - away
     *   - home score
     *   - away score
     */

    Tournament() throws SQLException {
        database = new DatabaseConnection();
    }
    
    /**
     * Return the matches table name for a tournament.
     * @param tournament
     * @return matches table name
     */
    static String matchesTableName(String tournament) {
        return tournament + "." + MATCHES_TABLE;
    }
    
    /**
     * Return the teams table name for a tournament.
     * @param tournament
     * @return teams table name
     */
    static String teamsTableName(String tournament) {
        return tournament + "." + TEAMS_TABLE;
    }
    
    /**
     * Create a new tournament with the given name, teams and options.
     * @param tournamentName
     * @throws SQLException 
     */
    void create(String tournamentName, List<String> teams) throws SQLException {
        // Generate the team list and the fixture
        TeamList teamList = new TeamList(teams);
        Fixture fixture = Fixture.generate(teamList.getList());
        // Create the tournament database
        database.createSchema(tournamentName);
        // Write the teams and the fixture to the database
        teamList.writeToDatabase(tournamentName, database);
        fixture.writeToDatabase(tournamentName, database);
    }
    
    /**
     * Remove an existing tournament.
     * @param tournamentName
     * @throws SQLException 
     */
    void delete(String tournamentName) throws SQLException {
        database.removeSchema(tournamentName);
    }

    /**
     * Enters a score for a game. Updates the table.
     * @param round
     * @param game
     * @param homeScore
     * @param awayScore
     * @throws SQLException 
     */
    void enterScore(String tournament, String round, int game, int homeScore, int awayScore) 
            throws SQLException {
        // Get the table names
        final String matchesTable = Tournament.matchesTableName(tournament);
        final String teamsTable = Tournament.teamsTableName(tournament);
        // Remove the previous score if it exists
        removeScoreIfExists(tournament, round, game);
        // Retrieve the match information
        final List<String> selectColumns = List.of("home", "away");
        final List<String> whereColumns = List.of("round", "game");
        final List<Object> whereValues = List.of(round, game);
        final SelectClause select = new SelectClause(selectColumns, matchesTable);
        final WhereClause where = new WhereClause(whereColumns, whereValues);
        final List<Object> information = database.select(select, where).get(0);
        final String home = (String) information.get(0);
        final String away = (String) information.get(1);
        // Set the match results
        MatchResult homeResult = MatchResult.result(homeScore, awayScore);
        MatchResult awayResult = MatchResult.result(awayScore, homeScore);
        // Update the table
        addResultToTable(teamsTable, home, homeResult, homeScore, awayScore);
        addResultToTable(teamsTable, away, awayResult, awayScore, homeScore);
        // Set the scores in the database
        final List<String> updateColumns = List.of("home_score", "away_score");
        final List<Object> updateValues = List.of(homeScore, awayScore);
        final UpdateClause update = new UpdateClause(matchesTable, updateColumns, updateValues);
        database.update(update, where);
    }
    
    /**
     * Removes a score for a game if it exists. Updates the table.
     * @param round
     * @param game
     * @throws SQLException 
     */
    void removeScoreIfExists(String tournament, String round, int game) throws SQLException {
        // Get the table name
        final String tableName = Tournament.matchesTableName(tournament);
        // Check if the score exists
        final List<String> selectColumns = List.of("home_score", "away_score");
        final List<String> whereColumns = List.of("round", "game");
        final List<Object> whereValues = List.of(round, game);
        final SelectClause select = new SelectClause(selectColumns, tableName);
        final WhereClause where = new WhereClause(whereColumns, whereValues);
        boolean scoreExists = !database.containsNull(select, where);
        // If score exists then remove score
        if (scoreExists) {
            removeScore(tournament, round, game);
        }
    }
    
    /**
     * Return the table in the following format:
     * table ::= row1 \n row2 \n  ... rowN
     * row ::= name, w, d, l, sf, sa, sd, p
     * @return table
     * @throws SQLException 
     */
    String getTable(String tournament) throws SQLException {
        // Get the table name
        final String tableName = Tournament.teamsTableName(tournament);
        // Choose the columns to select
        final List<String> selectColumns = List.of("name", "wins", "draws", "losses", "scores_for",
                "scores_against", "score_difference", "points");
        // Choose the columns and directions to order
        final List<String> orderColumns = List.of("points", "score_difference", "scores_for");
        final List<Order> orderValues = List.of(Order.DESC, Order.DESC, Order.DESC);
        // Create the clauses
        final SelectClause select = new SelectClause(selectColumns, tableName);
        final OrderClause order = new OrderClause(orderColumns, orderValues);
        // Get the information
        List<List<Object>> information = database.select(select, order);
        // Build the string
        StringBuilder table = new StringBuilder();
        for (int i = 0; i < information.size(); i++) {
            if (i != 0) {
                table.append("\n");
            }
            for (int j = 0; j < information.get(i).size(); j++) {
                if (j != 0) {
                    table.append(",");
                }
                table.append(information.get(i).get(j));
            }
        }
        return table.toString();
    }
    
    /**
     * Returns the round names from a tournament in the following format:
     * 
     * roundNames ::= round1 "," ... "," roundN
     * 
     * @param tournament
     * @return round names
     * @throws SQLException 
     */
    String getRoundNames(String tournament) throws SQLException {
        // Get the table name
        final String tableName = Tournament.matchesTableName(tournament);
        // Choose the columns and the order
        final SelectClause select = new SelectClause(List.of("DISTINCT round"), tableName);
        final OrderClause order = new OrderClause(List.of("round"), List.of(Order.ASC));
        final List<List<Object>> information = database.select(select, order);
        // Build the list
        StringBuilder rounds = new StringBuilder();
        for (int i = 0; i < information.size(); i++) {
            if (i != 0) {
                rounds.append(",");
            }
            rounds.append(information.get(i).get(0));
        }
        return rounds.toString();
    }
    
    /**
     * Returns the match information from a round in the following format:
     * 
     * information ::= match1 "\n" ... "\n" matchN
     * match ::= home "," home_score "," away_score "," away
     * 
     * @param tournament
     * @param roundName
     * @return match information from a round
     * @throws SQLException
     */
    String getMatchesByRound(String tournament, String roundName) throws SQLException {
        // Get the table name
        final String tableName = Tournament.matchesTableName(tournament);
        // Choose the columns and the conditions
        final List<String> selectColumns = List.of("home", "home_score", "away_score", "away", "id");
        final SelectClause select = new SelectClause(selectColumns, tableName);
        final WhereClause where = new WhereClause(List.of("round"), List.of(roundName));
        final OrderClause order = new OrderClause(List.of("id"), List.of(Order.ASC));
        // Execute and return
        final List<List<Object>> information = database.select(select, where, order);
        // Build the string
        final StringBuilder matches = new StringBuilder();
        for (int i = 0; i < information.size(); i++) {
            if (i != 0) {
                matches.append("\n");
            }
            // Don't count the id, hence the size - 1, id is just meant to order
            for (int j = 0; j < information.get(i).size() - 1; j++) {
                if (j != 0) {
                    matches.append(",");
                }
                matches.append(information.get(i).get(j));
            }
        }
        return matches.toString();
        
    }
    
    /**
     * Removes a score for a game. Assumes the score exists. Updates the table.
     * @param round
     * @param game
     * @throws SQLException 
     */
    private void removeScore(String tournament, String round, int game) throws SQLException {
        // Get the table names
        final String matchesTable = Tournament.matchesTableName(tournament);
        final String teamsTable = Tournament.teamsTableName(tournament);
        // Create the clauses to retrieve the information
        final List<String> selectColumns = List.of("home", "away", "home_score", "away_score");
        final List<String> whereColumns = List.of("round", "game");
        final List<Object> whereValues = List.of(round, game);
        final SelectClause select = new SelectClause(selectColumns, matchesTable);
        final WhereClause where = new WhereClause(whereColumns, whereValues);
        // Get the match information
        final List<Object> information = database.select(select, where).get(0);
        final String home = (String) information.get(0);
        final String away = (String) information.get(1);
        final int homeScore = (int) information.get(2);
        final int awayScore = (int) information.get(3);
        // Parse the match results
        MatchResult homeResult = MatchResult.result(homeScore, awayScore);
        MatchResult awayResult = MatchResult.result(awayScore, homeScore);
        // Update the table
        removeResultFromTable(teamsTable, home, homeResult, homeScore, awayScore);
        removeResultFromTable(teamsTable, away, awayResult, awayScore, homeScore);
        // Set the scores to null in the database
        final List<String> nullColumns = List.of("home_score", "away_score");
        final UpdateClause update = new UpdateClause(matchesTable, nullColumns);
        database.updateNull(update, where);
    }
    
    /**
     * Updates a team's information in the table by adding a game.
     * @param team
     * @param result
     * @param scoresFor
     * @param scoresAgainst
     * @throws SQLException 
     */
    private void addResultToTable(String tableName, String team, MatchResult result, 
            int scoresFor, int scoresAgainst) throws SQLException {
        updateTable(tableName, team, result, scoresFor, scoresAgainst, 1);
    }

    /**
     * Updates a team's information in the table by removing a game.
     * @param team
     * @param result
     * @param scoresFor
     * @param scoresAgainst
     * @throws SQLException 
     */
    private void removeResultFromTable(String tableName, String team, MatchResult result, 
            int scoresFor, int scoresAgainst) throws SQLException {
        updateTable(tableName, team, result, scoresFor, scoresAgainst, -1);
    }

    /**
     * Updates a team's information in the table with new game information.
     * @param team
     * @param result
     * @param scoresFor
     * @param scoresAgainst
     * @param direction whether the game is added or removed
     * @throws SQLException 
     */
    private void updateTable(String tableName, String team, MatchResult result, int scoresFor, 
            int scoresAgainst, int direction) throws SQLException {
        // Create the clauses to retrieve the information
        final List<String> selectColumns = List.of("wins", "draws", "losses", "scores_for", 
                "scores_against");
        final List<String> whereColumns = List.of("name");
        final List<Object> whereValues = List.of(team);
        final SelectClause select = new SelectClause(selectColumns, tableName);
        final WhereClause where = new WhereClause(whereColumns, whereValues);
        // Get the match information
        final List<Object> information = database.select(select, where).get(0);
        int wins = (int) information.get(0);
        int draws = (int) information.get(1);
        int losses = (int) information.get(2);
        int totalScoresFor = (int) information.get(3);
        int totalScoresAgainst = (int) information.get(4);
        // Increment the result
        switch (result) {
        case WIN:
            wins += direction;
            break;
        case DRAW:
            draws += direction;
            break;
        case LOSS:
            losses += direction;
            break;
        default:
            break;
        }
        // Calculate the new scoresFor and scoresAgainst
        totalScoresFor += scoresFor * direction;
        totalScoresAgainst += scoresAgainst * direction;
        // Calculate the new scoreDifference and points
        final int scoreDifference = totalScoresFor - totalScoresAgainst;
        final int points = wins * POINTS_FOR_WIN + draws * POINTS_FOR_DRAW + losses * POINTS_FOR_LOSS;
        // Set the results back
        final List<String> updateColumns = List.of("wins", "draws", "losses", "scores_for",
                "scores_against", "score_difference", "points");
        final List<Object> updateValues = List.of(wins, draws, losses, totalScoresFor, 
                totalScoresAgainst, scoreDifference, points);
        final UpdateClause update = new UpdateClause(tableName, updateColumns, updateValues);
        database.update(update, where);
    }

    public static void main(String[] args) throws SQLException {
        Tournament t = new Tournament();
        t.create("tname", List.of("a", "b", "c", "d"));
        System.out.println(t.getTable("tname"));
        t.enterScore("tname", "Round 1", 1, 4, 1);
//        t.removeScoreIfExists("tname", "Round 1", 1);
        System.out.println(t.getTable("tname"));
        System.out.println(t.getRoundNames("tname"));
        System.out.println(t.getMatchesByRound("tname", "Round 1"));
    }
}
