package Constants;

public enum Ratings {
    ANY_RATING,
    R,
    PG13,
    PG,
    G;

    // get an enum based on the string passed in.
    public Ratings getEnum(String rating) {
        if(rating.compareTo("R") == 0) return R;
        else if(rating.compareTo("PG-13") == 0) return PG13;
        else if(rating.compareTo("PG") == 0) return PG;
        else if(rating.compareTo("G") == 0) return G;
        else return ANY_RATING; // ANY_RATING or error
    }

    @Override
    public String toString() {
        if(this == R) return "R";
        else if(this == PG13) return "PG-13";
        else if(this == PG) return "PG";
        else if(this == G) return "G";
        else return "Any Rating"; // NO_RATING or error
    }

    public static final int R_INDEX = 0;
    public static final int PG13_INDEX = 1;
    public static final int PG_INDEX = 2;
    public static final int G_INDEX = 3;

}
