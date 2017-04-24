package com.example.fmoyader.popularmovies.network.offline;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.fmoyader.popularmovies.network.offline.contract.MovieContract;

/**
 * Created by fmoyader on 7/4/17.
 */

public class MovieSQLiteHelper extends SQLiteOpenHelper {

    public static final String MOVIE_SQLITE_DB_NAME = "movies.db";
    public static final int MOVIE_SQLITE_DB_INITIAL_VERSION = 0;
    public static final int MOVIE_SQLITE_DB_VERSION = 1;

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
        if (oldVersion <= 0) {
            final String createTableQuery = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + "("
                    + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + MovieContract.MovieEntry.COLUMN_ID + " INTEGER UNIQUE NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_ORIGIAL_LANGUAGE + " TEXT NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_RATING + " REAL NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, "
                    + MovieContract.MovieEntry.COLUMN_POPULARITY + " TEXT NOT NULL" + ");";
            db.execSQL(createTableQuery);
        }
    }
}
