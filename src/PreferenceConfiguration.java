import javax.xml.transform.Result;
import java.sql.ResultSet;

public class PreferenceConfiguration {

    private int configurationId;
    private String configurationName;
    private int releaseYearFrom;
    private int releaseYearTo;
    private String director;
    private String rating;
    private String genre;

    /*
     * CONSTRUCTORS
     */

    // Constructor based on each property
    public PreferenceConfiguration(int id, String name, int yearFrom, int yearTo, String director, String rating, String genre) {
        configurationId = id;
        configurationName = name;
        releaseYearFrom = yearFrom;
        releaseYearTo = yearTo;
        this.director = director;
        this.rating = rating;
        this.genre = genre;
    }

    // Constructor based on a result set
    public PreferenceConfiguration(ResultSet result) {

    }


    /*
     * SETTERS
     */
    public int getConfigurationId() {
        return configurationId;
    }

    public String getConfigurationName() {
        return configurationName;
    }

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

}
