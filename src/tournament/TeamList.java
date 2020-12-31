package tournament;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class representing a list of teams (for the tournament).
 */
public class TeamList {
    
	private final Set<String> teamSet;
	
	/*
	 * AF: f(teams) -> A list of teams where teams contains the teams in the list.
	 */
	
	TeamList() {
	    this.teamSet = new HashSet<>();
	}
	
	TeamList(List<String> teams) {
	    this.teamSet = new HashSet<>(teams);
	}
	
	/**
	 * Add a team. Assumes unique name.
	 * @param teamName unique
	 */
	void add(String teamName) {
		teamSet.add(teamName);
	}
	
	/**
	 * Remove a team.
	 * @param teamName
	 */
	void remove(String teamName) {
		teamSet.remove(teamName);
	}
	
	/**
	 * Returns list of teams.
	 * @return teams
	 */
	List<String> getList() {
		return new ArrayList<String>(teamSet);
	}
	
	/**
	 * Insert the team list to the given database. Creates the teams table.
	 * @param database
	 * @throws SQLException 
	 */
	void writeToDatabase(String tournament, DatabaseConnection database) throws SQLException {
		final String tableName = Tournament.teamsTableName(tournament);
	    createTable(tableName, database);
		for (String team: teamSet) {
		    insertTeamToDatabase(tableName, team, database);
		}
	}
	
	/**
	 * Create a table to insert the team list in.
	 * @param database
	 * @throws SQLException
	 */
	private void createTable(String tableName, DatabaseConnection database) throws SQLException {
	    final List<String> columns = List.of(
	            "id BIGSERIAL NOT NULL PRIMARY KEY", 
	            "name VARCHAR(50) NOT NULL", 
	            "wins INT NOT NULL", 
	            "draws INT NOT NULL", 
	            "losses INT NOT NULL", 
	            "scores_for INT NOT NULL", 
	            "scores_against INT NOT NULL", 
	            "score_difference INT NOT NULL", 
	            "points INT NOT NULL"
	            );
	    database.createTable(tableName, columns);;
	}
	
	/**
	 * Insert a team in the table. Assumes the table exists.
	 * @param team
	 * @param database
	 * @throws SQLException 
	 */
	private void insertTeamToDatabase(String tableName, String team, DatabaseConnection database) 
	        throws SQLException {
	    final List<String> columns = List.of("name", "wins", "draws", "losses", "scores_for", 
	            "scores_against", "score_difference", "points");
	    final List<Object> values = List.of(team, 0, 0, 0, 0, 0, 0, 0);
	    database.insert(tableName, columns, values);
	}
	
}
