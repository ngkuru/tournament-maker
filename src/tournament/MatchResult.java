package tournament;

/**
 * This enum represents possible match results.
 */
enum MatchResult {
    WIN, 
    DRAW, 
    LOSS, 
    BLANK;
    
    static MatchResult result(int scoresFor, int scoresAgainst) {
        if (scoresFor > scoresAgainst) {
            return WIN;
        } else if (scoresFor == scoresAgainst) {
            return DRAW;
        } else if (scoresFor < scoresAgainst) {
            return LOSS;
        } else {
            return BLANK;
        }
    }
}
