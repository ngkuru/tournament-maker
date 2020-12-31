package tournament;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * This class contains the tests for the DatabaseConnection class.
 */
public class DatabaseConnectionTest {

    /*
     * This class tests the following methods:
     * 
     * createSchema()
     * removeSchema()
     * createTable()
     * dropTable()
     * insert()
     * select()
     *   - where single column
     *   - where multiple columns
     *   - order
     *   
     * update()
     * containsNull()
     * updateNull()
     */

    // covers createSchema
    // @Test
    public void testCreateSchemaManual() throws SQLException {
        // First, make sure from psql that no schema named dctest exists
        DatabaseConnection connection = new DatabaseConnection();
        connection.createSchema("dctest");
        connection.close();
        // Then, check the database
        // When done, drop the schema
    }

    // covers removeSchema
    // @Test
    public void testRemoveSchemaManual() throws SQLException {
        // First, make sure from psql that no schema named dctest exists
        DatabaseConnection connection = new DatabaseConnection();
        connection.createSchema("dctest");
        connection.removeSchema("dctest");
        connection.close();
        // Then, check the database
    }
    
    // covers createTable
    // @Test
    public void testCreateTableManual() throws SQLException {
        // First, make sure from psql that no table named dctest exists in database fixturemaker
        DatabaseConnection connection = new DatabaseConnection();
        connection.createTable("dctest", List.of("id BIGSERIAL NOT NULL PRIMARY KEY"));
        connection.close();
        // Then, check the database
        // When done, drop the database
    }

    // covers dropTable
    // @Test
    public void testDropTableManual() throws SQLException {
        // First, make sure from psql that no table named dctest exists in database fixturemaker
        DatabaseConnection connection = new DatabaseConnection();
        connection.createTable("droptest", List.of("id BIGSERIAL NOT NULL PRIMARY KEY"));
        connection.dropTable("droptest");
        connection.close();
        // Then, check the database
    }

    // covers insert
    // @Test
    public void testRowManual() throws SQLException {
        // First, make sure from psql that no table named dctest exists in database fixturemaker
        DatabaseConnection connection = new DatabaseConnection();
        connection.createTable("dctest", List.of("id BIGSERIAL NOT NULL PRIMARY KEY, "
                + "name VARCHAR(50) NOT NULL"));
        List<String> columns = List.of("name");
        List<Object> values = List.of("test");
        connection.insert("dctest", columns, values);
        connection.close();
        // Then, check the database
        // When done, drop the database
    }

    // covers select where single column
    @Test
    public void testSelectWhereSingle() throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        connection.createTable("dctest", List.of("id BIGSERIAL NOT NULL PRIMARY KEY, "
                + "first_name VARCHAR(50) NOT NULL,"
                + "last_name VARCHAR(50) NOT NULL"));
        List<String> columns = List.of("first_name", "last_name");
        List<Object> person1 = List.of("first", "a");
        List<Object> person2 = List.of("second", "b");
        connection.insert("dctest", columns, person1);
        connection.insert("dctest", columns, person2);
        SelectClause select = new SelectClause(List.of("last_name"), "dctest");
        WhereClause where = new WhereClause(List.of("first_name"), List.of("second"));
        List<Object> results = connection.select(select, where).get(0);
        connection.dropTable("dctest");
        assertEquals(1, results.size(), "incorrect selection");
        assertEquals("b", results.get(0), "incorrect selection");
    }

    // covers select where multiple columns
    @Test
    public void testSelectWhereMulti() throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        connection.createTable("dctest", List.of("id BIGSERIAL NOT NULL PRIMARY KEY, "
                + "first_name VARCHAR(50) NOT NULL,"
                + "last_name VARCHAR(50) NOT NULL"));
        List<String> columns = List.of("first_name", "last_name");
        List<Object> person1 = List.of("first", "a");
        List<Object> person2 = List.of("second", "b");
        connection.insert("dctest", columns, person1);
        connection.insert("dctest", columns, person2);
        SelectClause select = new SelectClause(List.of("first_name", "last_name"), "dctest");
        WhereClause where = new WhereClause(List.of("first_name"), List.of("second"));
        List<Object> results = connection.select(select, where).get(0); 
        connection.dropTable("dctest");
        assertEquals(2, results.size(), "incorrect selection");
        assertEquals("second", results.get(0), "incorrect selection");
        assertEquals("b", results.get(1), "incorrect selection");
    }

    // covers select order
    @Test
    public void testSelectOrder() throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        connection.createTable("dctest", List.of("id BIGSERIAL NOT NULL PRIMARY KEY, "
                + "first_name VARCHAR(50) NOT NULL,"
                + "last_name VARCHAR(50) NOT NULL"));
        List<String> columns = List.of("first_name", "last_name");
        List<Object> person1 = List.of("first", "b");
        List<Object> person2 = List.of("second", "a");
        connection.insert("dctest", columns, person1);
        connection.insert("dctest", columns, person2);
        SelectClause select = new SelectClause(List.of("first_name", "last_name"), "dctest");
        OrderClause order = new OrderClause(List.of("last_name"), List.of(Order.ASC));
        List<Object> results = connection.select(select, order).get(0); 
        connection.dropTable("dctest");
        assertEquals(2, results.size(), "incorrect selection");
        assertEquals("second", results.get(0), "incorrect selection");
        assertEquals("a", results.get(1), "incorrect selection");
    }
    
    // covers update where
    @Test
    public void testUpdate() throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        connection.createTable("dctest", List.of("id BIGSERIAL NOT NULL PRIMARY KEY, "
                + "first_name VARCHAR(50) NOT NULL,"
                + "last_name VARCHAR(50) NOT NULL"));
        List<String> columns = List.of("first_name", "last_name");
        List<Object> person1 = List.of("first", "a");
        List<Object> person2 = List.of("second", "b");
        connection.insert("dctest", columns, person1);
        connection.insert("dctest", columns, person2);
        UpdateClause update = new UpdateClause("dctest", List.of("last_name"), List.of("c"));
        WhereClause where = new WhereClause(List.of("first_name"), List.of("second"));
        connection.update(update, where);
        SelectClause select = new SelectClause(List.of("last_name"), "dctest");
        List<Object> results = connection.select(select, where).get(0);
        connection.dropTable("dctest");
        assertEquals(1, results.size(), "incorrect update");
        assertEquals("c", results.get(0), "incorrect update");
    }

    // covers containsNull
    @Test
    public void testContainsNull() throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        connection.createTable("dctest", List.of("id BIGSERIAL NOT NULL PRIMARY KEY, "
                + "first_name VARCHAR(50) NOT NULL,"
                + "last_name VARCHAR(50)"));
        List<String> columns1 = List.of("first_name", "last_name");
        List<Object> person1 = List.of("first", "a");
        List<String> columns2 = List.of("first_name");
        List<Object> person2 = List.of("second");
        connection.insert("dctest", columns1, person1);
        connection.insert("dctest", columns2, person2);
        List<String> selectColumns = List.of("first_name", "last_name");
        List<String> whereColumns = List.of("first_name");
        List<Object> whereValues1 = List.of("first");
        List<Object> whereValues2 = List.of("second");
        final SelectClause select = new SelectClause(selectColumns, "dctest");
        final WhereClause where1 = new WhereClause(whereColumns, whereValues1);
        final WhereClause where2 = new WhereClause(whereColumns, whereValues2);
        boolean result1 = connection.containsNull(select, where1);
        boolean result2 = connection.containsNull(select, where2);
        connection.dropTable("dctest");
        assertEquals(false, result1, "incorrect null check");
        assertEquals(true, result2, "incorrect null check");
    }
    
    // covers updateNull
    @Test
    public void testUpdateNull() throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        connection.createTable("dctest", List.of("id BIGSERIAL NOT NULL PRIMARY KEY, "
                + "first_name VARCHAR(50),"
                + "last_name VARCHAR(50)"));
        List<String> columns = List.of("first_name", "last_name");
        List<Object> person1 = List.of("first", "a");
        List<Object> person2 = List.of("second", "b");
        connection.insert("dctest", columns, person1);
        connection.insert("dctest", columns, person2);
        List<String> nullColumns = List.of("last_name");
        List<String> whereColumns = List.of("first_name");
        List<Object> whereValues = List.of("second");
        final UpdateClause update = new UpdateClause("dctest", nullColumns);
        final WhereClause where = new WhereClause(whereColumns, whereValues);
        connection.updateNull(update, where);
        List<String> selectColumns = List.of("first_name", "last_name");
        final SelectClause select = new SelectClause(selectColumns, "dctest");
        boolean result = connection.containsNull(select, where);
        connection.dropTable("dctest");
        assertEquals(true, result, "incorrect null set");
    }
}
