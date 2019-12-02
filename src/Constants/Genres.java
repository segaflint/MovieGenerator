package Constants;

import javafx.scene.chart.ScatterChart;

public enum Genres {
    ANY_GENRE,
    ACTION_ADVENTURE,
    HORROR,
    KIDS_FAMILY,
    DRAMA,
    COMEDY,
    SCIFI_FANTASY;

    // get an enum based on the string passed in.
    public Genres getEnum(String genre) {
        if(genre.compareTo("Action & Adventure") == 0) return ACTION_ADVENTURE;
        else if(genre.compareTo("Horror") == 0) return HORROR;
        else if(genre.compareTo("Kids & Family") == 0) return KIDS_FAMILY;
        else if(genre.compareTo("Drama") == 0) return DRAMA;
        else if(genre.compareTo("Comedy") == 0) return COMEDY;
        else if(genre.compareTo("Science Fiction & Fantasy" ) == 0) return SCIFI_FANTASY;
        else return ANY_GENRE; // ANY_GENRE or error
    }

    @Override
    public String toString() {
        if(this == ACTION_ADVENTURE) return "Action & Adventure";
        else if(this == HORROR) return "Horror";
        else if(this == KIDS_FAMILY) return "Kids & Family";
        else if(this == DRAMA) return "Drama";
        else if(this == COMEDY) return "Comedy";
        else if(this == SCIFI_FANTASY) return "Science Fiction & Fantasy";
        else return "Any Genre"; // ANY_GENRE or error
    }

    public static final int ACTION_ADVENTURE_INDEX = 0;
    public static final int HORROR_INDEX = 1;
    public static final int KIDS_FAMILY_INDEX = 2;
    public static final int DRAMA_INDEX = 3;
    public static final int COMEDY_INDEX = 4;
    public static final int SCIFI_FANTASY_INDEX = 5;


}
