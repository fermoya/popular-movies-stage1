package com.example.fmoyader.popularmovies.network.offline;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.fmoyader.popularmovies.network.offline.schema.MovieDatabaseSchema;

/**
 * Created by fmoyader on 7/4/17.
 */

public class MovieSQLiteHelper extends SQLiteOpenHelper {

    public static final String MOVIE_SQLITE_DB_NAME = "movie offline db";
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
            db.beginTransaction();
            db.execSQL(MovieDatabaseSchema.createTableQuery());
            db.endTransaction();
        }
    }
}
