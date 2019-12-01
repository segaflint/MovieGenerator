import Constants.DataTables.HasTable;
import Constants.DataTables.PreferenceConfigurationTable;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PreferenceConfiguration extends Component {

    private int configurationId;
    private String configurationName;
    private char anyYearFlag;
    private int releaseYearFrom;
    private int releaseYearTo;
    private String director;
    private char[] ratings; // 4 ratings exist; position in array should be { R, PG13, PG, G }
    private char[] genres; // 6 genres exist; position in array should be { ActionAdventure, Horror, KidsFamily, Drama, Comedy, SciFiFantasy}

    /*
     * CONSTRUCTORS
     */

    // Constructor based on each property
    public PreferenceConfiguration(int id, String name, char anyYear, int yearFrom, int yearTo, String director,
                                   char[] rating, char[] genre) {
        configurationId = id;
        configurationName = name;
        anyYearFlag = anyYear;
        releaseYearFrom = yearFrom;
        releaseYearTo = yearTo;
        this.director = director;
        this.ratings = rating;
        this.genres = genre;

    }

    // Constructor based on a result set
    public PreferenceConfiguration(ResultSet result) {
        try {
            configurationId = result.getInt(PreferenceConfigurationTable.CONFIGURATION_ID_COLUMN_NAME);
            configurationName = result.getString(HasTable.CONFIGURATION_NAME_COLUMN_NAME);
            anyYearFlag = result.getString(PreferenceConfigurationTable.ANY_YEAR_FLAG_COLUMN_NAME).charAt(0);
            releaseYearFrom = result.getInt(PreferenceConfigurationTable.RELEASE_YEAR_FROM_COLUMN_NAME);
            releaseYearTo = result.getInt(PreferenceConfigurationTable.RELEASE_YEAR_TO_COLUMN_NAME);
            director = result.getString(PreferenceConfigurationTable.DIRECTOR_COLUMN_NAME);
            ratings = setRatingsArray(result);
            genres = setGenresArray(result);
        } catch (SQLException e) {
            configurationName = "SQLerror";
        }
    }

    public PreferenceConfiguration(String configurationName) {
        this.configurationName = configurationName;
    }

    // return a ratings array based on a result set, if error in sql, return null
    public char[] setRatingsArray( ResultSet result ) { //{ R, PG13, PG, G }
        char[] array = new char[4];

        try {
            array[0] = result.getString(PreferenceConfigurationTable.RATING_HAS_R_COLUMN_NAME).charAt(0);
            array[1] = result.getString(PreferenceConfigurationTable.RATING_HAS_PG13_COLUMN_NAME).charAt(0);
            array[2] = result.getString(PreferenceConfigurationTable.RATING_HAS_PG_COLUMN_NAME).charAt(0);
            array[3] = result.getString(PreferenceConfigurationTable.RATING_HAS_G_COLUMN_NAME).charAt(0);
        } catch ( SQLException e ) {
            return null;
        }
        return array;
    }

    // Return a genres array based on a result set
    public char[] setGenresArray( ResultSet result ) { //{ ActionAdventure, Horror, KidsFamily, Drama, Comedy, SciFiFantasy}
        char[] array = new char[6];

        try {
            array[0] = result.getString(PreferenceConfigurationTable.GENRE_HAS_ACTION_ADVENTURE_COLUMN_NAME).charAt(0);
            array[1] = result.getString(PreferenceConfigurationTable.GENRE_HAS_HORROR_COLUMN_NAME).charAt(0);
            array[2] = result.getString(PreferenceConfigurationTable.GENRE_HAS_KIDS_FAMILY_COLUMN_NAME).charAt(0);
            array[3] = result.getString(PreferenceConfigurationTable.GENRE_HAS_DRAMA_COLUMN_NAME).charAt(0);
            array[4] = result.getString(PreferenceConfigurationTable.GENRE_HAS_COMEDY_COLUMN_NAME).charAt(0);
            array[5] = result.getString(PreferenceConfigurationTable.GENRE_HAS_SCIFI_FANTASY_COLUMN_NAME).charAt(0);
        } catch ( SQLException e ) {
            return null;
        }

        return array;
    }

    // return true if no genre preferences are set
    public boolean hasNoGenres(){
        for(int i = 0; i < genres.length; i++) {
            if (genres[i] != 'N') {
                return false;
            }
        }
        return true;
    }

    // return true if no rating preferences are set
    public boolean hasNoRatings(){
        for(int i = 0; i < ratings.length; i++) {
            if (ratings[i] != 'N') {
                return false;
            }
        }
        return true;
    }

    /*
     * GETTERS
     */
    public int getConfigurationId() { return configurationId; }

    public String getConfigurationName() { return configurationName; }

    public char getAnyYearFlag() { return anyYearFlag; }

    public int getReleaseYearFrom() { return releaseYearFrom; }

    public int getReleaseYearTo() { return releaseYearTo; }

    public String getDirector() { return director; }

    public char[] getRating() { return ratings; }

    public char[] getGenres() { return genres; }

    @Override
    public String toString() {
        return this.configurationName;
    }
}
