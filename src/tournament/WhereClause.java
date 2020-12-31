package tournament;

import java.util.List;

/**
 * This class represents the WHERE clause for SQL queries. Currently only supports when all 
 * conditions must be satisfied (i.e. all AND and no OR).
 */
public class WhereClause {

    private final List<String> columns;
    private final List<Object> values;
    
    /*
     * AF: f(columns, values) -> A WHERE part of a SQL query, where columns are the columns to 
     *   check the condition at and values are the values to check.
     */
    
    WhereClause(List<String> columns, List<Object> values) {
        this.columns = columns;
        this.values = values;
    };
    
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
        final StringBuilder where = new StringBuilder();
        if (size() > 0) {
            where.append("WHERE (");
            for (int i = 0; i < size(); i++) {
                if (i != 0) {
                    where.append(" AND ");
                }
                where.append(columns.get(i) + "=?");
            }
            where.append(")");
        }
        return where.toString();
    }
    
}
