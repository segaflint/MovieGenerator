import java.sql.*;

import Constants.DataTables.*;
import Constants.Genres;
import Constants.Ratings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 *
 * Database Layer for all database interactions
 *
 */
public class DatabaseLayer {

    private final int CalebMode = 0;
    private final int SethMode = 1;
    private int userMode = 1;

    private String CalebUrl = "jdbc:sqlite:/Users/caleb/Desktop/MovieGenerator.db";
    private String SethUrl = "jdbc:sqlite:/Users/sethrasmusson/Documents/CS364_DB/Databases/MovieGenerator.db";

    private String url;

    private Connection connection;

    public DatabaseLayer() {
        if(userMode == CalebMode ) url = CalebUrl;
        else if ( userMode == SethMode ) url = SethUrl;
    }


    /*
     * PUBLIC METHODS
     */

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

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(url);
    }

    public void disconnect() throws SQLException {
        connection.close();
    }

    /*
     * DATABASE INTERACTIONS
     */


    /*
     * QUERIES
     */
    // Returns a Result set from any given query script given
    public ResultSet runQuery(String query) throws SQLException {
        // Create a statement preparing for an SQL query
        PreparedStatement stmt = connection.prepareStatement(query);
        // Execute the prepared statement
        ResultSet results = stmt.executeQuery();

        return results;
    }

    //if user with 'username' and 'password' exists, return the userId of that user
    //else return -1 on no user or error.
    public int getUser(String username, String password) {
        ResultSet results;
        int userId;

        // SELECT * FROM User WHERE UserName = 'username' AND Password = 'password'
        String query = "SELECT * FROM " + UserTable.TABLE_NAME + " WHERE " + UserTable.USER_NAME_COLUMN_NAME + " = '"
        +  username + "' AND " + UserTable.PASSWORD_COLUMN_NAME + " = '" + password + "'";

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

    // Return the count of movies watched by the user with userId. User will always only exist when this gets called.
    // Return 0 if none watched or error occurs.
    public int getMoviesWatchedCount(int userId) { // uses aggregate, Group by and having
        ResultSet results;
        int movieCount;

        //SELECT count(*) FROM User u JOIN Has_Watched hw ON u.UserId = hw.UserId GROUP BY u.UserId Having u.UserId = userId;
        String query = " SELECT count(*) FROM " + UserTable.TABLE_NAME + " u JOIN " + HasWatchedTable.TABLE_NAME
                + " hw ON u." + UserTable.USER_ID_COLUMN_NAME + " = hw." + HasWatchedTable.USER_ID_COLUMN_NAME + " GROUP BY u."
                + UserTable.USER_ID_COLUMN_NAME + " HAVING u." + UserTable.USER_ID_COLUMN_NAME + " = " + userId;

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            results = stmt.executeQuery();
            if(!results.next()) { // no results
                return 0;
            }

            movieCount = results.getInt(1);

        } catch (SQLException e) { // error
            return 0;
        }

        return movieCount;
    }

    // Return an array of Preference Configurations belonging to the user with userId
    public ArrayList<PreferenceConfiguration> getUserConfigurations(int userId) {
        ResultSet results;

        ArrayList<PreferenceConfiguration> configurationsList = new ArrayList<>();

        String sql = "SELECT * FROM " + HasTable.TABLE_NAME + " h NATURAL JOIN " +
                PreferenceConfigurationTable.TABLE_NAME + " WHERE h." + HasTable.USER_ID_COLUMN_NAME + " = " + userId;

        try {

            PreparedStatement stmt = connection.prepareStatement(sql);
            results = stmt.executeQuery();

            while (results.next()) {
                configurationsList.add(new PreferenceConfiguration(results));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return configurationsList;
    }

    // Generate a movie based on the given preference configuration
    public Movie generateMovie(int userId, PreferenceConfiguration configuration, boolean includeWatchedMovies) {
        //CalebTODO 3

        String sqlString = "SELECT * FROM " + MovieTable.TABLE_NAME +
                " WHERE " + MovieTable.TITLE_COLUMN_NAME + " = " + MovieTable.TITLE_COLUMN_NAME;

        if (configuration.getAnyYearFlag() == 'N') {
            sqlString = sqlString + " AND " + MovieTable.RELEASE_YEAR_COLUMN_NAME + " BETWEEN " + configuration.getReleaseYearFrom()
                    + " AND " + configuration.getReleaseYearTo();
        }

        if (!configuration.hasNoRatings()) {
            char[] ratings = configuration.getRatings();
            sqlString = sqlString + " AND " + MovieTable.RATING_COLUMN_NAME + " IN ( ";
            for (int i = 0; i < ratings.length; i++) { // iterate through ratingsflag array
                if (ratings[i] == 'Y') { // if this rating is flagged 'Y' Add it to the string
                    if (i == Ratings.R_INDEX) {
                        sqlString = sqlString + Ratings.R.toString() + ", ";
                    } else if (i == Ratings.PG13_INDEX) {
                        sqlString = sqlString + Ratings.PG13.toString() + ", ";
                    } else if (i == Ratings.PG_INDEX) {
                        sqlString = sqlString + Ratings.PG.toString() + ", ";
                    } else if (i == Ratings.G_INDEX) {
                        sqlString = sqlString + Ratings.G.toString() + ", ";
                    }
                }
            }
            sqlString = sqlString + "'' )";
        }

        if (!(configuration.getDirector().compareTo("") == 0)) { // if the string is not empty, you will query based on the director
            // AND Director LIKE 'director%String%'
            sqlString = sqlString + " AND Director LIKE " + configuration.getDirector().toString() + "% ";
        }

        if (!configuration.hasNoGenres()) {
            char[] genres = configuration.getGenres();
            sqlString = sqlString + " AND " + MovieTable.GENRE_COLUMN_NAME + " IN ( ";
            for (int i = 0; i < genres.length; i++) {
                if (genres[i] =='Y') {
                    if (i == Genres.ACTION_ADVENTURE_INDEX) {
                        sqlString = sqlString + Genres.ACTION_ADVENTURE.toString() + ", ";
                    } else if (i == Genres.HORROR_INDEX) {
                        sqlString = sqlString + Genres.HORROR.toString() + ", ";
                    } else if (i == Genres.KIDS_FAMILY_INDEX) {
                        sqlString = sqlString + Genres.KIDS_FAMILY.toString() + ", ";
                    } else if (i == Genres.DRAMA_INDEX) {
                        sqlString = sqlString + Genres.DRAMA.toString() + ", ";
                    } else if (i == Genres.COMEDY_INDEX) {
                        sqlString = sqlString + Genres.COMEDY.toString() + ", ";
                    } else if (i == Genres.SCIFI_FANTASY_INDEX) {
                        sqlString = sqlString + Genres.SCIFI_FANTASY.toString() + ", ";
                    }                                                                              
                }

            }
            sqlString = sqlString + "'' )";
        }

        if ( includeWatchedMovies == false ) {
            sqlString = sqlString + " EXCEPT SELECT " + MovieTable.MOVIE_ID_COLUMN_NAME + ", " + MovieTable.GENRE_COLUMN_NAME
                    + ", " + MovieTable.GENRE_COLUMN_NAME + ", " + MovieTable.RELEASE_YEAR_COLUMN_NAME + ", " +
                    MovieTable.RATING_COLUMN_NAME + ", " + MovieTable.DIRECTOR_COLUMN_NAME + " FROM " + MovieTable.TABLE_NAME
                    + " NATURAL JOIN " + HasWatchedTable.TABLE_NAME + " WHERE " + HasWatchedTable.USER_ID_COLUMN_NAME +
                    " = " + userId;
        }

        sqlString = sqlString + ";";
        
        ResultSet result;
        Movie movie;

        PreparedStatement stmt = null;

        try {

            stmt = connection.prepareStatement(sqlString);
            result = stmt.executeQuery();

            movie = (Movie) result;

            return movie;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }



    public Movie generateRandomMovie(){//query1 uses count(*) for count of Movies query2 USES OFFSET
        int totalMoviesCount = 0;
        int offset = 0;

        //SELECT count(*) FROM Movie
        String sqlMovieCount = "SELECT count(*) FROM " + MovieTable.TABLE_NAME;
        try {
            PreparedStatement movieCountStmt = connection.prepareStatement(sqlMovieCount);
            ResultSet countSet = movieCountStmt.executeQuery();

            if(countSet.next()) totalMoviesCount = countSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        offset = (int)(Math.random() * totalMoviesCount);

        ResultSet result;
        Movie movie = null;

        // generate totally random movie
        // QUERY2 then use that random number in a query where you limit 1 and offset by the random number.
        String sqlRandomMovie = "SELECT * FROM " + MovieTable.TABLE_NAME + " LIMIT 1 OFFSET " + offset;
        try {
            PreparedStatement randomMoviestmt = connection.prepareStatement(sqlRandomMovie);
            result = randomMoviestmt.executeQuery();
            if(result.next()) movie = new Movie(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movie;
    }


    // WAIT TO IMPLEMENT. Should be a final touch.
    // Return true if user with userId is an admin.
    public boolean isAdmin(int userId) {
        //TODO WAIT
        //query the database for user with userId. Create admin flag column in the user table and check if char is Y.
        // if is Y, return true. Otherwise false
        return false;
    }

    /*
     * INSERTS, DELETES, UPDATES
     */

    // Returns userId if a user was successfully created, -1, if user already exists or insert failed
    public int createNewUser(String username, String password) {
        //TODO WAIT

        // CALEB! see the getUser method for examples of how to put the table name, column names into a string from the
        // tables enums packages I created. These are located in "Constants>DataTables" If you'd like to look at them.
        // Do not directly insert hard coded strings for these areas in case we want to change table names, column names, etc.

        // check if user with 'username' exists in the database
            // if it does, return -1
            // else do an insert into users with username and password and return the userId
        // upon any error, this should return -1. Make sure to surround database queries with try/catch so you can return
        // -1 in the catch block on any errors
        return -1;
    }

    // returns the configurationId if successful insertion of configuration, -1 otherwise
    public int insertConfiguration(int userId, PreferenceConfiguration configuration) {
//        ResultSet results;
//        int configurationId = 0; // if configurationId is 0, there is no configuration in the database that corresponds
//        // has the IN operator and LIKE depending on the cases
//        String query = "SELECT " + PreferenceConfigurationTable.CONFIGURATION_ID_COLUMN_NAME + " FROM " + PreferenceConfigurationTable.TABLE_NAME +
//                whereEqualsInt(PreferenceConfigurationTable.RELEASE_YEAR_FROM_COLUMN_NAME, configuration.getReleaseYearFrom()) +
//                andEqualsInt(PreferenceConfigurationTable.RELEASE_YEAR_TO_COLUMN_NAME, configuration.getReleaseYearTo()) +
//                andEqualsString(PreferenceConfigurationTable.DIRECTOR_COLUMN_NAME, configuration.getDirector()) +
//                getGenresAndEqualsString(configuration) + getRatingsAndEqualsString(configuration) +
//                andEqualsString(PreferenceConfigurationTable.ANY_YEAR_FLAG_COLUMN_NAME, String.valueOf(configuration.getAnyYearFlag()));
//
//        String sql = "INSERT INTO " + PreferenceConfigurationTable.TABLE_NAME+"(" + PreferenceConfigurationTable.RELEASE_YEAR_FROM_COLUMN_NAME +
//                ", " + PreferenceConfigurationTable.RELEASE_YEAR_TO_COLUMN_NAME + ", " + PreferenceConfigurationTable.DIRECTOR_COLUMN_NAME +
//                ", " + PreferenceConfigurationTable.RATING_COLUMN_NAME + ", " + PreferenceConfigurationTable.GENRE_COLUMN_NAME +
//                ", " + PreferenceConfigurationTable.ANY_YEAR_FLAG_COLUMN_NAME + ") VALUES ( ?, ?, '?', '?', '?', '?' )";
//
//        String sql2 = "INSERT INTO " + HasTable.TABLE_NAME + "(" + HasTable.USER_ID_COLUMN_NAME+ ", " + HasTable.CONFIGURATION_NAME_COLUMN_NAME +
//                ", " + HasTable.CONFIGURATION_ID_COLUMN_NAME +") VALUES ( ?, '?', ? )";
//
//        PreparedStatement stmt = null;
//        PreparedStatement stmt2 = null;
//
//        try {
//
//            stmt = connection.prepareStatement(sql);
//            results = stmt.executeQuery();
//
//            stmt.setInt(1, configuration.getReleaseYearFrom());
//            stmt.setInt(2, configuration.getReleaseYearTo());
//            stmt.setString(3, configuration.getDirector());
//            stmt.setString(4, configuration.getRatings());
//            stmt.setString(5, configuration.getGenres());
//            stmt.setString(6, String.valueOf(configuration.getAnyYearFlag()));
//
//
//
//            stmt2 = connection.prepareStatement(sql2);
//
//            stmt2.setInt(1, userId);
//            stmt2.setString(2, configuration.getConfigurationName());
//            stmt2.setInt(3, configurationId);
//
//            return 0;
//
//        } catch (SQLException e) {
//            return -1;
//        }

        //First, insert the configuration from the object 'configuration' that was passed in by doing an insert
        //into preference configurations with 'configuration's and insert into 'has' with the configuration you just made
        return -1;
    }

    // Insert a new tuple into the user has watched column if the userid and movieId pairing doesnt already exist in the database
    public void insertUserHasWatched(int userId, int movieId) {

        //Check that this movie has not already been marked as watched by the user
        String sqlCheck = "SELECT count(*) FROM " + HasWatchedTable.TABLE_NAME + " WHERE " + HasWatchedTable.USER_ID_COLUMN_NAME +
                " = " + userId + " AND " + HasWatchedTable.MOVIE_ID_COLUMN_NAME + " = " + movieId;
        int checkCount = 0;
        try {
            PreparedStatement sqlCheckStmt = connection.prepareStatement(sqlCheck);
            ResultSet result = sqlCheckStmt.executeQuery();
            if(result.next()) {
                checkCount = result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(checkCount > 0) return; //If there is already, end

        // else insert into HasWatched

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = dateFormat.format(date);

        String sqlInsert = "INSERT INTO " + HasWatchedTable.TABLE_NAME + "(" + HasWatchedTable.USER_ID_COLUMN_NAME +
                ", " + HasWatchedTable.WATCHED_DATE_COLUMN_NAME + ", " + HasWatchedTable.MOVIE_ID_COLUMN_NAME +
                ") VALUES ( " + userId + ", '" + dateString + "', " + movieId +" )";
        try {
            PreparedStatement insertStmt = connection.prepareStatement(sqlInsert);

            insertStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /*
     * PRIVATE METHODS
     */

    // Reconnects to the url; should be called immediately after a context switch
    private void refreshConnection() throws SQLException {
        if(connection != null) connection.close();
        connection = DriverManager.getConnection(url);
    }

    /*
     * SCRIPT Stringing functions
     */

    // returns a string for an AND clause with leading and finishing spaces " AND columnName = 'value'"
    private String andEqualsString(String columnName, String value) {
        return " AND " + columnName + " = '" + value + "'";
    }

    // returns a string for an AND clause with leading and finishing spaces " AND columnName = value"
    private String andEqualsInt(String columnName, int value) {
        return " AND " + columnName + " = " + value;
    }

    // returns a string for a WHERE clause with leading and finishing spaces " WHERE columnName = 'value'"
    private String whereEqualsString(String columnName, String value) {
        return " WHERE " + columnName + " = '" + value + "'";
    }

    // returns a string for a WHERE clause with leading and finishing spaces " WHERE columnName = value"
    private String whereEqualsInt(String columnName, int value) {
        return " WHERE " + columnName + " = " + value;
    }

    private String getRatingsAndEqualsString(PreferenceConfiguration configuration) { //{ R, PG13, PG, G }
        char[] ratings = configuration.getRatings();
        return " AND " + PreferenceConfigurationTable.RATING_HAS_R_COLUMN_NAME + " = '" + ratings[0] + "'" +
               " AND " + PreferenceConfigurationTable.RATING_HAS_PG13_COLUMN_NAME + " = '" + ratings[1] + "'" +
               " AND " + PreferenceConfigurationTable.RATING_HAS_PG_COLUMN_NAME + " = '" + ratings[2] + "'" +
               " AND " + PreferenceConfigurationTable.RATING_HAS_G_COLUMN_NAME + " = '" + ratings[3] + "'";
    }

    private String getGenresAndEqualsString(PreferenceConfiguration configuration) { //{ ActionAdventure, Horror, KidsFamily, Drama, Comedy, SciFiFantasy}
        char[] genres = configuration.getGenres();
        return " AND " + PreferenceConfigurationTable.GENRE_HAS_ACTION_ADVENTURE_COLUMN_NAME + " = '" + genres[0] + "'" +
                " AND " + PreferenceConfigurationTable.GENRE_HAS_HORROR_COLUMN_NAME + " = '" + genres[1] + "'" +
                " AND " + PreferenceConfigurationTable.GENRE_HAS_KIDS_FAMILY_COLUMN_NAME + " = '" + genres[2] + "'" +
                " AND " + PreferenceConfigurationTable.GENRE_HAS_DRAMA_COLUMN_NAME + " = '" + genres[3] + "'" +
                " AND " + PreferenceConfigurationTable.GENRE_HAS_COMEDY_COLUMN_NAME + " = '" + genres[4] + "'" +
                " AND " + PreferenceConfigurationTable.GENRE_HAS_SCIFI_FANTASY_COLUMN_NAME + " = '" + genres[5] + "'";
    }

    private String getRatingsWhereAndEqualsString(PreferenceConfiguration configuration) { //{ R, PG13, PG, G }
        char[] ratings = configuration.getRatings();
        return " WHERE " + PreferenceConfigurationTable.RATING_HAS_R_COLUMN_NAME + " = '" + ratings[0] + "'" +
                " AND " + PreferenceConfigurationTable.RATING_HAS_PG13_COLUMN_NAME + " = '" + ratings[1] + "'" +
                " AND " + PreferenceConfigurationTable.RATING_HAS_PG_COLUMN_NAME + " = '" + ratings[2] + "'" +
                " AND " + PreferenceConfigurationTable.RATING_HAS_G_COLUMN_NAME + " = '" + ratings[3] + "'";
    }

    private String getGenresWhereAndEqualsString(PreferenceConfiguration configuration) { //{ ActionAdventure, Horror, KidsFamily, Drama, Comedy, SciFiFantasy}
        char[] genres = configuration.getGenres();
        return " WHERE " + PreferenceConfigurationTable.GENRE_HAS_ACTION_ADVENTURE_COLUMN_NAME + " = '" + genres[0] + "'" +
                " AND " + PreferenceConfigurationTable.GENRE_HAS_HORROR_COLUMN_NAME + " = '" + genres[1] + "'" +
                " AND " + PreferenceConfigurationTable.GENRE_HAS_KIDS_FAMILY_COLUMN_NAME + " = '" + genres[2] + "'" +
                " AND " + PreferenceConfigurationTable.GENRE_HAS_DRAMA_COLUMN_NAME + " = '" + genres[3] + "'" +
                " AND " + PreferenceConfigurationTable.GENRE_HAS_COMEDY_COLUMN_NAME + " = '" + genres[4] + "'" +
                " AND " + PreferenceConfigurationTable.GENRE_HAS_SCIFI_FANTASY_COLUMN_NAME + " = '" + genres[5] + "'";
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
