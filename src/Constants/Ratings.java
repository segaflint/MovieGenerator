package Constants;

public enum Ratings {
    ANY_RATING,
    R,
    PG13,
    PG,
    G;

    @Override
    public String toString() {
        if(this == R) return "R";
        else if(this == PG13) return "PG-13";
        else if(this == PG) return "PG";
        else if(this == G) return "G";
        else return "Any Rating"; // NO_RATING or error
    }
}
