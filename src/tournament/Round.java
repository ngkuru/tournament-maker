package tournament;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a round (in the fixture)
 */
public class Round {
	
	private final List<Match> matches = new ArrayList<>();
		
	/*
	 * AF: f(matches) -> Round where matches contains matches in the round.
	 */
	
	/**
	 * Add a match to the round.
	 * @param match
	 */
	void addMatch(Match match) {
		matches.add(match);
	}
	
	/**
	 * Return a list of matches.
	 * @return matches
	 */
	List<Match> getMatches() {
		return new ArrayList<Match>(matches);
	}
	
	/**
	 * Shuffle the ordering of the matches and home/away of every match.
	 */
	void deepShuffle() {
		for (Match match: matches) {
			match.shuffle();
		}
		this.shuffle();
	}
	
	/**
	 * Generate a mirror of this round. If a round is 
	 * 
	 * t1 - t2
	 * t3 - t4
	 * 
	 * then its mirror is defined by
	 * 
	 * t2 - t1
	 * t4 - t3
	 * 
	 * @return mirror
	 */
	Round generateMirror() {
		Round mirror = new Round();
		for (Match match: matches) {
			mirror.addMatch(match.generateMirror());
		}
		return mirror;
	}

	/**
	 * Shuffle the ordering of the matches.
	 */
	private void shuffle() {
	    Collections.shuffle(matches);
	}

	@Override
	public boolean equals(Object other) {
	    return (other instanceof Round) && sameValue((Round) other);
	}
	
	private boolean sameValue(Round other) {
	    return matches.equals(other.matches);
	}
	
	@Override
	public int hashCode() {
	    return matches.hashCode();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < matches.size(); i++) {
			if (i != 0) {
				sb.append("\n");
			}
			sb.append(matches.get(i).toString());
		}
		return sb.toString();
	}

}
