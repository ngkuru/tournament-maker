package tournament;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * This class contains the tests for the Order enum.
 */
public class OrderTest {

    /*
     * This class tests the following methods:
     * 
     * toString()
     *   - asc
     *   - desc
     */
    
    
    // covers asc
    @Test
    public void testAsc() {
        assertEquals("ASC", Order.ASC.toString(), "incorrect string");
    }
    
    // covers desc
    @Test
    public void testDesc() {
        assertEquals("DESC", Order.DESC.toString(), "incorrect string");
    }
    
}
