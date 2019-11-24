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
}
