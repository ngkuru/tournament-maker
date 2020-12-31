package tournament;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * This class contains the tests for the WhereClause class.
 */
public class UpdateClauseTest {

    /*
     * This class tests the following methods:
     * 
     * size()
     * value()
     * toString()
     */
    
    // tests size
    @Test
    public void testSize() {
        final UpdateClause update = new UpdateClause("table", List.of("a", "b"), List.of(1, 2));
        assertEquals(2, update.size(), "incorrect size");
    }
    
    // tests value
    @Test
    public void testValue() {
        final UpdateClause update = new UpdateClause("table", List.of("a", "b"), List.of(1, 2));
        assertEquals(1, update.value(0), "incorrect value");
        assertEquals(2, update.value(1), "incorrect value");
    }
    
    // tests toString
    @Test
    public void testString() {
        final UpdateClause update = new UpdateClause("table", List.of("a", "b", "c"), List.of(1, 
                2, 3));
        final String expected = "UPDATE table SET a=?, b=?, c=?";
        assertEquals(expected, update.toString(), "incorrect string");
    }
    
}
