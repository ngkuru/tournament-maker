package tournament;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * This class contains the tests for the SelectClause class.
 */
public class SelectClauseTest {

    /*
     * This class tests the following methods:
     * 
     * size()
     * toString()
     */
    
    // covers size
    @Test
    public void testSize() {
        final SelectClause select = new SelectClause(List.of("a", "b"), "table");
        assertEquals(2, select.size(), "incorrect size");
    }
    
    // covers string
    @Test
    public void testString() {
        final SelectClause select = new SelectClause(List.of("a", "b"), "table");
        final String expected = "SELECT a, b FROM table";
        assertEquals(expected, select.toString(), "incorrect string");
    }
    
}
