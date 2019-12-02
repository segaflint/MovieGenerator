import Constants.DataTables.MovieTable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Movie {

    private int movieId;
    private String title;
    private String genre;
    private int releaseYear;
    private String rating;
    private String director;

    public static final String MOVIE_ERROR_STRING = "Something went wrong getting a movie.";

    public Movie( int id, String title, String genre, int releaseYear, String rating, String director) {
        this.movieId = id;
        this.title = title;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.director = director;
    }

    public Movie( ResultSet result ) {
        try {
            movieId = result.getInt(MovieTable.MOVIE_ID_COLUMN_NAME);
            title = result.getString(MovieTable.TITLE_COLUMN_NAME);
            genre = result.getString(MovieTable.GENRE_COLUMN_NAME);
            releaseYear = result.getInt(MovieTable.RELEASE_YEAR_COLUMN_NAME);
            rating = result.getString(MovieTable.RATING_COLUMN_NAME);
            director = result.getString(MovieTable.DIRECTOR_COLUMN_NAME);
        } catch (SQLException e) {
            title = "SQLerror";
            genre = "error";
            rating = "error";
            director = "error";
            releaseYear = 0;
            movieId = 0;
        }
    }

    @Override
    public String toString() {
        return title + "(" + releaseYear + ") by " + director + ", rated " + rating + ", " + genre;
    }

    public int getMovieId() {
        return movieId;
    }
}
