package tournament;

import java.util.List;

/**
 * This class represents the UPDATE SET clause for SQL queries.
 */
public class UpdateClause {

    private final String table;
    private final List<String> columns;
    private final List<Object> values;
    
    /*
     * AF: f(table, columns, values) -> An UPDATE SET part of a SQL query, where table is the 
     *   table to update and columns and values are the columns to update and values the columns 
     *   are to be set to.
     */
    
    UpdateClause(String table, List<String> columns) {
        this.table = table;
        this.columns = columns;
        this.values = List.of();
    }
    
    UpdateClause(String table, List<String> columns, List<Object> values) {
        this.table = table;
        this.columns = columns;
        this.values = values;
    }
    
    /**
     * Return the value for the column at position.
     * @param position
     * @return value
     */
    Object value(int position) {
        return values.get(position);
    }
    
    /**
     * Return the size of the clause (number of conditions).
     * @return size
     */
    int size() {
        return columns.size();
    }
    
    @Override
    public String toString() {
        final StringBuilder set = new StringBuilder();
        set.append("SET ");
        for (int i = 0; i < columns.size(); i++) {
            if (i != 0) {
                set.append(", ");
            }
            set.append(columns.get(i) + "=?");
        }        
        return "UPDATE " + table + " " + set;
    }
    
}
