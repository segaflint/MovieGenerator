import java.sql.*;
import Constants.DataTables.*;

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
public class DatabaseLayer {

    final private int CalebMode = 0;
    final private int SethMode = 1;
    private int userMode = 1;

    private String CalebUrl = "jdbc:sqlite:/Users/caleb/Desktop/MovieGenerator.db";
    private String SethUrl = "jdbc:sqlite:/Users/sethrasmusson/Documents/CS364_DB/Databases/MovieGenerator.db";

    private String url;

    private Connection connection;

    public DatabaseLayer() {
        if(userMode == CalebMode ) url = CalebUrl;
        else if ( userMode == SethMode ) url = SethUrl;
    }

    // sets the user context and refreshes the connection; returns true on successful connection, false otherwise
    public boolean setUserMode(int mode) {
        userMode = mode;
        if(userMode == CalebMode ) url = CalebUrl;
        else if ( userMode == SethMode ) url = SethUrl;
        try {
            refreshConnection();
        } catch ( SQLException e ) {
            return false;
        }
        return true;
    }

    // Reconnects to the url; should be called immediately after a context switch
    private void refreshConnection() throws SQLException {
        if(connection != null) connection.close();
        connection = DriverManager.getConnection(url);
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(url);
    }

    public void disconnect() throws SQLException {
        connection.close();
    }

    // Returns a Result set from any given query script given
    public ResultSet runQuery(String query) throws SQLException {
        // Create a statement preparing for an SQL query
        PreparedStatement stmt = connection.prepareStatement(query);
        // Execute the prepared statement
        ResultSet results = stmt.executeQuery();

        return results;
    }

    public int getUser(String username, String password) {
        String query = "SELECT * FROM " + UserTable.TABLE_NAME + " WHERE " + UserTable.USER_NAME_COLUMN_NAME + " = '"
        +  username + "' AND " + UserTable.PASSWORD_COLUMN_NAME + " = '" + password + "'";

        ResultSet results;
        int userId;

        //if user with 'username' and 'password' exists, return the userId of that user
        //else return -1 on no user or error.
        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            results = stmt.executeQuery();
            if(!results.next()) { // no results
                return -1;
            }

            userId = results.getInt(UserTable.USER_ID_COLUMN_NAME);
        } catch (SQLException e) { // error
            return -1;
        }

        return userId;
    }

    // Returns True if a user was successfully created, false, if user already exists or insert failed
    public int createNewUser(String username, String password) {
        //CalebTODO



        // check if user with 'username' exists in the database
            // if it does, return -1
            // else do an insert into users with username and password and return the userId
        // upon any error, this should return -1. Make sure to surround database queries with try/catch so you can return
        // -1 in the catch block on any errors
        return -1;
    }



//    public ResultSet employeeLookup(String ssn) throws SQLException {
////        String query = "SELECT * FROM Employee WHERE SSN = %s";
////        String.format(query, ssn); //Then prepare statement and execute query. no need for stmt.setString
//        String query = "SELECT * FROM Employee WHERE SSN = ?";
//        PreparedStatement stmt = connection.prepareStatement(query);
//        stmt.setString(1, ssn);
//        ResultSet results = stmt.executeQuery();
//
//        return results;
//    }

//    public int insertEmployee(Employee e) throws  SQLException {
//        String sql = "INSERT INTO Employee(SSN, Salary, FirstName, MiddleName, LastName) VALUES ( ?, ?, ?, ?, ? )";
//
//        PreparedStatement stmt = connection.prepareStatement(sql);
//        stmt.setString(1, e.getSsn());
//        stmt.setDouble(2, e.getSalary());
//        stmt.setString(3, e.getFirstName());
//        stmt.setString(4, e.getMiddleName());
//        stmt.setString(5, e.getLastName());
//
//        int numRowsAffected = stmt.executeUpdate();
//        return numRowsAffected;
//    }

}
