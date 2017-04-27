package com.example.fmoyader.popularmovies.network.offline.contract;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by fmoyader on 25/4/17.
 */

public class TrailerContract {
    public static final String TRAILER_PATH = TrailerContract.TrailerEntry.TABLE_NAME;

    public static class TrailerEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(TRAILER_PATH).build();

        public static final String TABLE_NAME = "trailers";

        public static final String COLUMN_TRAILER_ID = "trailer_id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TRAILER_KEY = "key";
        public static final String COLUMN_NAME = "trailer_name";
    }
}
