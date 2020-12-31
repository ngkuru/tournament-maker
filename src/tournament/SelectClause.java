package tournament;

import java.util.List;

/**
 * This class represents the SELECT FROM clause for SQL queries.
 */
public class SelectClause {

    private final List<String> columns;
    private final String table;
    
    /*
     * AF: f(columns, table) -> A SELECT FROM part of a SQL query, where table is the table to 
     *   select from and columns are the columns to select from the table.
     */

    SelectClause(List<String> columns, String table) {
        this.columns = columns;
        this.table = table;
    }
    
    /**
     * Return the size of the clause (number of columns).
     * @return size
     */
    int size() {
        return columns.size();
    }
    
    @Override
    public String toString() {
        return "SELECT " + String.join(", ", columns) + " FROM " + table;
    }
}
