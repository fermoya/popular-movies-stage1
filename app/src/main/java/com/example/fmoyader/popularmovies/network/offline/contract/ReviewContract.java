package com.example.fmoyader.popularmovies.network.offline.contract;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by fmoyader on 25/4/17.
 */

public class ReviewContract {
    public static final String REVIEW_PATH = ReviewEntry.TABLE_NAME;

    public static class ReviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(REVIEW_PATH).build();

        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_CONTENT = "content";
    }
}
