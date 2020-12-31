package tournament;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Class containing the methods for interacting with the database.
 */
public class DatabaseConnection {

    private final Connection connection;

    private static final String URL = "jdbc:postgresql:";
    private static final String USERNAME = "fixturemaker";
    private static final String PASSWORD = "java";

    /*
     * AF: f(connection) -> Database connection handling communication between methods to create
     *   and update tournaments and the database. connection field is the object enabling this to 
     *   interact with the database. 
     */
    
    DatabaseConnection() throws SQLException {
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    /**
     * Closes the connection.
     * @throws SQLException
     */
    void close() throws SQLException {
        connection.close();
    }

    /**
     * Creates a new schema.
     * @param schema
     * @throws SQLException 
     */
    void createSchema(String schema) throws SQLException {
        // Build the statement and execute
        final PreparedStatement statement = connection.prepareStatement("CREATE SCHEMA " + schema);
        statement.executeUpdate();
        statement.close();
    }

    /**
     * Removes a schema.
     * @param schema
     * @throws SQLException 
     */
    void removeSchema(String schema) throws SQLException {
        // Build the statement and execute
        final PreparedStatement statement = connection.prepareStatement("DROP SCHEMA " + schema 
                + " CASCADE");
        statement.executeUpdate();
        statement.close();
    }
    
    /**
     * Creates a new table.
     * @param tableName
     * @param columns
     * @throws SQLException 
     */
    void createTable(String tableName, List<String> columns) throws SQLException {
        // Build the statement and execute
        final PreparedStatement statement = connection.prepareStatement("CREATE TABLE " + tableName
                + "(" + String.join(", ", columns) + ")");
        statement.executeUpdate();
        statement.close();
    }

    /**
     * Drops a table.
     * @param tableName
     * @throws SQLException
     */
    void dropTable(String tableName) throws SQLException {
        // Build the statement and execute
        final PreparedStatement statement = connection.prepareStatement("DROP TABLE " + tableName);
        statement.executeUpdate();
        statement.close();
    }

    /**
     * Inserts a row to a table.
     * @param tableName
     * @param columns assumes size > 0
     * @param values assumes same size as columns
     * @throws SQLException
     */
    void insert(String tableName, List<String> columns, List<Object> values) 
            throws SQLException {
        // Build the statement string
        final StringBuilder statementString = new StringBuilder();
        statementString.append("INSERT INTO " + tableName + "(" + String.join(",", columns) + ") "
                + "VALUES (");
        // Add question marks for values
        for (int i = 0; i < columns.size(); i++) {
            if (i != 0) {
                statementString.append(",");
            }
            statementString.append("?");
        }
        statementString.append(")");
        // Build the statement
        final PreparedStatement statement = connection.prepareStatement(
                statementString.toString());
        // Set the values
        for (int i = 1; i <= columns.size(); i++) {
            statement.setObject(i, values.get(i-1));
        }
        // Execute the statement
        statement.executeUpdate();
        statement.close();
    }
    
    /**
     * Selects information from a table from columns from rows satisfying the where condition. 
     * Currently only supports when all conditions must be satisfied (i.e. all AND and no OR).
     * @param tableName
     * @param columns
     * @param where
     * @return results
     * @throws SQLException
     */
    List<List<Object>> select(SelectClause select, WhereClause where) throws SQLException {
        return select(prepareSelect(select, where));
    }

    /**
     * Selects information from a table from columns with rows ordered by the order condition.
     * @param select
     * @param order
     * @return results
     * @throws SQLException 
     */
    List<List<Object>> select(SelectClause select, OrderClause order) throws SQLException {
        return select(prepareSelect(select, order));
    }
    
    /**
     * Selects information from a table from columns from rows satisfying the where condition
     * ordered by the order condition. Currently only supports when all conditions must be 
     * satisfied (i.e. all AND and no OR).
     * @param tableName
     * @param columns
     * @param where
     * @param order
     * @return results
     * @throws SQLException
     */
    List<List<Object>> select(SelectClause select, WhereClause where, OrderClause order) 
            throws SQLException {
        return select(prepareSelect(select, where, order));
    }
    
    /**
     * Updates information in a table in columns in rows satisfying the where conditions.
     * @param tableName
     * @param columns assumes size > 0
     * @param values assumes same size as columns
     * @param where
     * @throws SQLException 
     */
    void update(UpdateClause update, WhereClause where) throws SQLException {
        final PreparedStatement statement = prepareUpdate(update, where);
        statement.executeUpdate();
        statement.close();
    }

    /**
     * Returns whether the selected columns in rows satisfying the where conditions contain a null.
     * Currently only supports when all conditions must be satisfied (i.e. all AND and no OR), and 
     * looks only at the first row.
     * @param tableName
     * @param columns
     * @param where
     * @return whether there is a null
     * @throws SQLException 
     */
    boolean containsNull(SelectClause select, WhereClause where) throws SQLException {
        // Build and execute the statement
        final PreparedStatement statement = prepareSelect(select, where);
        final ResultSet results = statement.executeQuery();
        // Check whether we have a null
        while (results.next()) {
            for (int i = 1; i <= select.size(); i++) {
                results.getObject(i);
                if (results.wasNull()) {
                    return true;
                }
            }   
        }
        return false;
    }

    /**
     * Sets values to null in the table in given columns in rows satisfying the where conditions.
     * Currently only supports when all conditions must be satisfied (i.e. all AND and no OR).
     * @param tableName
     * @param columns
     * @param where
     * @throws SQLException 
     */
    void updateNull(UpdateClause update, WhereClause where) throws SQLException {
        final PreparedStatement statement = prepareNull(update, where);
        statement.executeUpdate();
        statement.close();
    }

    /**
     * Selects information from a table by executing a select query.
     * @param statement
     * @return results
     * @throws SQLException
     */
    private List<List<Object>> select(PreparedStatement statement) throws SQLException {
        final ResultSet results = statement.executeQuery();
        List<List<Object>> resultList = new ArrayList<>();
        while (results.next()) {
            final List<Object> row = new ArrayList<>();
            for (int i = 1; i <= results.getMetaData().getColumnCount(); i++) {
                row.add(results.getObject(i));
            }
            resultList.add(row);
        }
        statement.close();
        return resultList;
    }
    
    /**
     * Prepares a SELECT statement with ordering.
     * @param select
     * @param order
     * @return statement
     * @throws SQLException 
     */
    private PreparedStatement prepareSelect(SelectClause select, OrderClause order) 
            throws SQLException {
        return connection.prepareStatement(select + " " + order);
    }
    
    /**
     * Prepares a SELECT statement with where conditions. Currently only supports when all 
     * conditions must be satisfied (i.e. all AND and no OR).
     * @param select
     * @param where
     * @return statement
     * @throws SQLException
     */
    private PreparedStatement prepareSelect(SelectClause select, WhereClause where) 
            throws SQLException {
        final OrderClause empty = new OrderClause(List.of(), List.of());
        return prepareSelect(select, where, empty);
    }
    
    /**
     * Prepares a SELECT statement with where conditions and ordering. Currently only supports 
     * when all conditions must be satisfied (i.e. all AND and no OR).
     * @param select
     * @param where
     * @param order
     * @return statement
     * @throws SQLException
     */
    private PreparedStatement prepareSelect(SelectClause select, WhereClause where, 
            OrderClause order) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement(select + " " + where 
                + " " + order);
        for (int i = 1; i <= where.size(); i++) {
            statement.setObject(i, where.value(i-1));
        }
        return statement;
    }

    /**
     * Prepares an UPDATE statement. Currently only supports when all conditions must be satisfied 
     * (i.e. all AND and no OR).
     * @param update
     * @param where
     * @return statement
     * @throws SQLException 
     */
    private PreparedStatement prepareUpdate(UpdateClause update, WhereClause where) 
            throws SQLException {
        final PreparedStatement statement = connection.prepareStatement(update + " " + where);
        for (int i = 1; i <= update.size(); i++) {
            statement.setObject(i, update.value(i-1));
        }
        for (int i = update.size() + 1; i <= update.size() + where.size(); i++) {
            statement.setObject(i, where.value(i-1 - update.size()));
        }
        return statement;
    }

    /**
     * Prepares an UPDATE statement with values set to null.
     * @param update
     * @param where
     * @return statement
     * @throws SQLException
     */
    private PreparedStatement prepareNull(UpdateClause update, WhereClause where) 
            throws SQLException {
        final PreparedStatement statement = connection.prepareStatement(update + " " + where);
        for (int i = 1; i <= update.size(); i++) {
            statement.setNull(i, Types.NULL);
        }
        for (int i = update.size() + 1; i <= update.size() + where.size(); i++) {
            statement.setObject(i, where.value(i-1 - update.size()));
        }
        return statement;
    }

}
