import java.sql.*;

/*
 *
 * Load SQL driver (JDBC/ODBC)
 * - add to build path
 *
 * Set up our database (script)
 * (-create database)
 * - create tables
 * - populate with starting data
 *
 * connect to the database
 *
 * insert/modify/delete data
 *
 * query data
 *
 * disconnect from the database
 *
 */
public class DataBaseLayer {

    private String CalebUrl;
    private String SethUrl;
    private String url;

    private Connection connection;

    public Database(int mode) {
        if(mode == 0 ) url = CalebUrl;
        else if ( mode == 1 ) url = SethUrl;
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(url);
    }

    public void disconnect() throws SQLException {
        connection.close();
    }

    public ResultSet runQuery(String query) throws SQLException {
        // Create a statment preparing for an SQL query
        PreparedStatement stmt = connection.prepareStatement(query);
        // Execute the prepared statement
        ResultSet results = stmt.executeQuery();

        return results;
    }

    public ResultSet employeeLookup(String ssn) throws SQLException {
//        String query = "SELECT * FROM Employee WHERE SSN = %s";
//        String.format(query, ssn); //Then prepare statement and execute query. no need for stmt.setString
        String query = "SELECT * FROM Employee WHERE SSN = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, ssn);
        ResultSet results = stmt.executeQuery();

        return results;
    }

    public int insertEmployee(Employee e) throws  SQLException {
        String sql = "INSERT INTO Employee(SSN, Salary, FirstName, MiddleName, LastName) VALUES ( ?, ?, ?, ?, ? )";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, e.getSsn());
        stmt.setDouble(2, e.getSalary());
        stmt.setString(3, e.getFirstName());
        stmt.setString(4, e.getMiddleName());
        stmt.setString(5, e.getLastName());

        int numRowsAffected = stmt.executeUpdate();
        return numRowsAffected;
    }


}
