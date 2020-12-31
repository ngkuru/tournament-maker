package tournament;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * This class contains the tests for the OrderClause class.
 */
public class OrderClauseTest {
    
    /*
     * This class tests the following methods:
     * 
     * toString()
     */
    
    // covers string
    @Test
    public void testString() {
        final OrderClause order = new OrderClause(List.of("a", "b", "c"), List.of(Order.ASC, 
                Order.DESC, Order.ASC));
        final String expected = "ORDER BY a ASC, b DESC, c ASC";
        assertEquals(expected, order.toString(), "incorrect string");
    }

}
