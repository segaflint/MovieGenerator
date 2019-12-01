package Constants.DataTables;

public enum PreferenceConfigurationTable {
    ;

    public static final String TABLE_NAME = "PreferenceConfiguration";
    public static final String CONFIGURATION_ID_COLUMN_NAME = "ConfigurationId";
    public static final String ANY_YEAR_FLAG_COLUMN_NAME = "AnyYearFlag";
    public static final String RELEASE_YEAR_FROM_COLUMN_NAME = "ReleaseYearFrom";
    public static final String RELEASE_YEAR_TO_COLUMN_NAME = "ReleaseYearTo";
    public static final String DIRECTOR_COLUMN_NAME = "Director";
//    public static final String RATING_COLUMN_NAME = "Rating";
//    public static final String GENRE_COLUMN_NAME = "Genre";

    //RATINGS COLUMNS
    public static final String RATING_HAS_R_COLUMN_NAME = "HasR";
    public static final String RATING_HAS_PG13_COLUMN_NAME = "HasPG13";
    public static final String RATING_HAS_PG_COLUMN_NAME = "HasPG";
    public static final String RATING_HAS_G_COLUMN_NAME = "HasG";

    //GENRES COLUMNS
    public static final String GENRE_HAS_ACTION_ADVENTURE_COLUMN_NAME = "HasActionAdventure";
    public static final String GENRE_HAS_HORROR_COLUMN_NAME = "HasHorror";
    public static final String GENRE_HAS_KIDS_FAMILY_COLUMN_NAME = "HasKidsFamily";
    public static final String GENRE_HAS_DRAMA_COLUMN_NAME = "HasDrama";
    public static final String GENRE_HAS_COMEDY_COLUMN_NAME = "HasComedy";
    public static final String GENRE_HAS_SCIFI_FANTASY_COLUMN_NAME = "HasSciFiFantasy";
}
