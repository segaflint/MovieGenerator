import Constants.DataTables.PreferenceConfigurationTable;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PreferenceConfiguration extends Component {

    private int configurationId;
    private String configurationName;
    private char anyYearFlag;
    private int releaseYearFrom;
    private int releaseYearTo;
    private String director;
    private String rating;
    private String genre;

    /*
     * CONSTRUCTORS
     */

    // Constructor based on each property
    public PreferenceConfiguration(int id, String name, char anyYear, int yearFrom, int yearTo, String director, String rating, String genre) {
        configurationId = id;
        configurationName = name;
        anyYearFlag = anyYear;
        releaseYearFrom = yearFrom;
        releaseYearTo = yearTo;
        this.director = director;
        this.rating = rating;
        this.genre = genre;
    }

    // Constructor based on a result set
    public PreferenceConfiguration(ResultSet result) {
        try {
            if(result.next()) {
                configurationId = result.getInt(PreferenceConfigurationTable.CONFIGURATION_ID_COLUMN_NAME);
                configurationName = result.getString(PreferenceConfigurationTable.CONFIGURATION_NAME_COLUMN_NAME);
                anyYearFlag = result.getString(PreferenceConfigurationTable.ANY_YEAR_FLAG_COLUMN_NAME).charAt(0);
                releaseYearFrom = result.getInt(PreferenceConfigurationTable.RELEASE_YEAR_FROM_COLUMN_NAME);
                releaseYearTo = result.getInt(PreferenceConfigurationTable.RELEASE_YEAR_TO_COLUMN_NAME);
                director = result.getString(PreferenceConfigurationTable.DIRECTOR_COLUMN_NAME);
                rating = result.getString(PreferenceConfigurationTable.RATING_COLUMN_NAME);
                genre = result.getString(PreferenceConfigurationTable.GENRE_COLUMN_NAME);
            }
        } catch (SQLException e) {
            configurationName = "SQLerror";
        }
    }

    public PreferenceConfiguration(String configurationName) {
        this.configurationName = configurationName;
    }


    /*
     * GETTERS
     */
    public int getConfigurationId() {
        return configurationId;
    }

    public String getConfigurationName() {
        return configurationName;
    }

    public char getAnyYearFlag() { return anyYearFlag; }

    public int getReleaseYearFrom() {
        return releaseYearFrom;
    }

    public int getReleaseYearTo() {
        return releaseYearTo;
    }

    public String getDirector() {
        return director;
    }

    public String getRating() {
        return rating;
    }

    public String getGenre() {
        return genre;
    }

    @Override
    public String toString() {
        return this.configurationName;
    }
}
