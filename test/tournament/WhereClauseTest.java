package tournament;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * This class contains the tests for the WhereClause class.
 */
public class WhereClauseTest {

    /*
     * This class tests the following methods:
     * 
     * size()
     * value()
     * toString()
     */
    
    // covers size
    @Test
    public void testSize() {
        final WhereClause where = new WhereClause(List.of("a", "b"), List.of(1, 2));
        assertEquals(2, where.size(), "incorrect size");
    }
    
    // covers value
    @Test
    public void testValue() {
        final WhereClause where = new WhereClause(List.of("a", "b"), List.of(1, 2));
        assertEquals(1, where.value(0), "incorrect value");
        assertEquals(2, where.value(1), "incorrect value");
    }
    
    // covers string
    @Test
    public void testString() {
        final WhereClause where = new WhereClause(List.of("a", "b", "c"), List.of(1, 
                2, 3));
        final String expected = "WHERE (a=? AND b=? AND c=?)";
        assertEquals(expected, where.toString(), "incorrect string");
    }
}
