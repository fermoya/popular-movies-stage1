package com.example.fmoyader.popularmovies.network.offline;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.fmoyader.popularmovies.network.offline.contract.MovieContract;
import com.example.fmoyader.popularmovies.network.offline.contract.ReviewContract;
import com.example.fmoyader.popularmovies.network.offline.contract.TrailerContract;

/**
 * Created by fmoyader on 7/4/17.
 */

public class MovieSQLiteHelper extends SQLiteOpenHelper {

    public static final String MOVIE_SQLITE_DB_NAME = "movies.db";
    public static final int MOVIE_SQLITE_DB_INITIAL_VERSION = 0;
    public static final int MOVIE_SQLITE_DB_VERSION = 2;

    public MovieSQLiteHelper(Context context) {
        super(context, MOVIE_SQLITE_DB_NAME, null, MOVIE_SQLITE_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        executeQueriesForOldDatebaseVersion(db, MOVIE_SQLITE_DB_INITIAL_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        executeQueriesForOldDatebaseVersion(db, oldVersion);
    }

    private void executeQueriesForOldDatebaseVersion(SQLiteDatabase db, int oldVersion) {
        if (oldVersion < 1) {
            final String createTableQuery = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + "("
                    + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + MovieContract.MovieEntry.COLUMN_ID + " INTEGER UNIQUE NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_RATING + " REAL NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_POPULARITY + " INTEGER NOT NULL" + ");";
            db.execSQL(createTableQuery);
        }

        if (oldVersion <= 2) {
            final String dropMovieTable = "DELETE FROM " + MovieContract.MovieEntry.TABLE_NAME + " WHERE 1 = 1;";
            final String addColumntToMoviesTableQuery = "ALTER TABLE " +
                    MovieContract.MovieEntry.TABLE_NAME + " ADD COLUMN "
                    + MovieContract.MovieEntry.COLUMN_FAVOURITE + " INTEGER" + ";";

            final String createTrailerTableQuery = "CREATE TABLE " + TrailerContract.TrailerEntry.TABLE_NAME + "("
                    + TrailerContract.TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TrailerContract.TrailerEntry.COLUMN_TRAILER_ID + " TEXT UNIQUE NOT NULL, "
                    + TrailerContract.TrailerEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                    + TrailerContract.TrailerEntry.COLUMN_NAME + " TEXT NOT NULL, "
                    + TrailerContract.TrailerEntry.COLUMN_TRAILER_KEY + " TEXT NOT NULL" + ");";

            final String createReviewTableQuery = "CREATE TABLE " + ReviewContract.ReviewEntry.TABLE_NAME + "("
                    + ReviewContract.ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ReviewContract.ReviewEntry.COLUMN_REVIEW_ID + " TEXT UNIQUE NOT NULL, "
                    + ReviewContract.ReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                    + ReviewContract.ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL" + ");";

            db.execSQL(dropMovieTable);
            db.execSQL(addColumntToMoviesTableQuery);
            db.execSQL(createTrailerTableQuery);
            db.execSQL(createReviewTableQuery);
        }
    }
}
