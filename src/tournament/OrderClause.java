package tournament;

import java.util.List;

public class OrderClause {
    
    private final List<String> columns;
    private final List<Order> directions;
    
    /*
     * AF: f(columns, directions) -> An ORDER BY part of a SQL query, where columns are the 
     *   columns to order by and the directions is the directions.
     */
    
    OrderClause(List<String> columns, List<Order> directions) {
        this.columns = columns;
        this.directions = directions;
    }
    
    @Override
    public String toString() {
        // Build the string
        final StringBuilder order = new StringBuilder();
        if (columns.size() > 0) {
            order.append("ORDER BY ");
            for (int i = 0; i < columns.size(); i++) {
                if (i != 0) {
                    order.append(", ");
                }
                order.append(columns.get(i) + " " + directions.get(i));
            }   
        }
        return order.toString();
    }
}
