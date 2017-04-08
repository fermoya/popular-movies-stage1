package com.example.fmoyader.popularmovies.network.offline.schema;

/**
 * Created by fmoyader on 7/4/17.
 */

public class MovieDatabaseSchema {
    private static final String TABLE_NAME = "Movies";
    private static final String SPACE = " ";
    private static final String COMMA = ",";
    private static final String LEFT_BRACKET = "(";
    private static final String RIGHT_BRACKET = ")";

    public static String createTableQuery() {
        return Keyword.CREATE + SPACE + Keyword.TABLE + SPACE
                + TABLE_NAME + SPACE + Keyword.COLUMNS + LEFT_BRACKET
                + Column.MOVIE_ID.getName() + SPACE + Column.MOVIE_ID.getType() + COMMA
                + Column.TITLE.getName() + SPACE + Column.TITLE.getType() + COMMA
                + Column.ORIGINAL_LANGUAGE.getName() + SPACE + Column.ORIGINAL_LANGUAGE.getType() + COMMA
                + Column.RATING.getName() + SPACE + Column.RATING.getType() + COMMA
                + Column.SYNOPSIS.getName() + SPACE + Column.SYNOPSIS.getType() + COMMA
                + Column.POSTER_URL.getName() + SPACE + Column.POSTER_URL.getType() + COMMA
                + Column.RELEASE_DATE.getName() + SPACE + Column.RELEASE_DATE.getType() + COMMA
                + Column.FAVORITE.getName() + SPACE + Column.FAVORITE.getType() + RIGHT_BRACKET;
    }

    public enum Column {
        MOVIE_ID("movie_id", "INTEGER"),
        TITLE("title", "TEXT"),
        ORIGINAL_LANGUAGE("original_language", "TEXT"),
        RATING("rating", "TEXT"),
        SYNOPSIS("synopsis", "TEXT"),
        POSTER_URL("poster_url", "TEXT"),
        RELEASE_DATE("relase_date", "TEXT"),
        FAVORITE("favorite", "INTEGER");

        private String name;
        private String type;

        Column(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }

    public enum Keyword {
        CREATE, TABLE, COLUMNS
    }
}
