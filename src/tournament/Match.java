package tournament;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a Match (in the fixture).
 */
public class Match {
	
	final List<String> teams;
	
	/*
	 * AF: f(teams) -> Match where teams.get(0) is the home team and teams.get(1) is the away team.
	 */
	
	Match(String homeTeam, String awayTeam) {
		this.teams = new ArrayList<>(List.of(homeTeam, awayTeam));
	}
	
	/**
	 * Returns the home team.
	 * @return the home team
	 */
	String getHomeTeam() {
	    return teams.get(0);
	}
	
	/**
	 * Returns the away team.
	 * @return the away team
	 */
	String getAwayTeam() {
	    return teams.get(1);
	}
	
	/**
	 * Generate a mirror of this match.
	 * @return mirror
	 */
	Match generateMirror() {
		return new Match(teams.get(1), teams.get(0));
	}
	
	/**
	 * Shuffle the home and away teams.
	 */
	void shuffle() {
		Collections.shuffle(teams);
	}
	
	@Override
	public boolean equals(Object other) {
	    return (other instanceof Match) && sameValue((Match) other);
	}
	
	private boolean sameValue(Match other) {
	    return teams.equals(other.teams);
	}
	
	@Override
	public int hashCode() {
	    return teams.hashCode();
	}
	
	@Override
	public String toString() {
		return teams.get(0) + " - " + teams.get(1);
	}

}
