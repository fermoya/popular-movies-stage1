package com.example.fmoyader.popularmovies.network.offline.contract;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by fmoyader on 12/4/17.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.example.fmoyader.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String MOVIES_PATH = MovieEntry.TABLE_NAME;

    public static class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(MOVIES_PATH).build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_ORIGINAL_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_ORIGIAL_LANGUAGE = "original_language";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_ID = "movie_id";
        public static final String COLUMN_POPULARITY = "popularity";

    }
}
