package com.example.fmoyader.popularmovies.network.offline;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.fmoyader.popularmovies.network.offline.contract.MovieContract;
import com.example.fmoyader.popularmovies.network.offline.contract.ReviewContract;
import com.example.fmoyader.popularmovies.network.offline.contract.TrailerContract;

/**
 * Created by fmoyader on 12/4/17.
 */

public class MovieDBProvider extends ContentProvider {

    private MovieSQLiteHelper movieSQLiteHelper;
    private static UriMatcher uriMatcher = buildUriMatcher();

    private static final int MOVIES = 100;
    private static final int MOVIES_WITH_ID = 101;
    private static final int TRAILERS = 200;
    private static final int TRAILERS_WITH_ID = 201;
    private static final int REVIEWS = 300;
    private static final int REVIEWS_WITH_ID = 301;

    @Override
    public boolean onCreate() {
        movieSQLiteHelper = new MovieSQLiteHelper(getContext());
        return true;
    }

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.MOVIES_PATH, MOVIES);
        matcher.addURI(MovieContract.AUTHORITY,
                MovieContract.MOVIES_PATH + "/" + TrailerContract.TRAILER_PATH, TRAILERS);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.MOVIES_PATH + "/#", MOVIES_WITH_ID);
        matcher.addURI(MovieContract.AUTHORITY,
                MovieContract.MOVIES_PATH + "/" + TrailerContract.TRAILER_PATH + "/#", TRAILERS_WITH_ID);
        matcher.addURI(MovieContract.AUTHORITY,
                MovieContract.MOVIES_PATH + "/" + ReviewContract.REVIEW_PATH, REVIEWS);
        matcher.addURI(MovieContract.AUTHORITY,
                MovieContract.MOVIES_PATH + "/" + ReviewContract.REVIEW_PATH + "/#", REVIEWS_WITH_ID);
        return matcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        SQLiteDatabase sqLiteDatabase = movieSQLiteHelper.getReadableDatabase();
        Cursor cursor = null;

        int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                try {
                    cursor = sqLiteDatabase.query(
                            MovieContract.MovieEntry.TABLE_NAME,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder
                    );
                } catch (SQLiteException e) {
                    Log.d("ERROR", e.getMessage());
                }
                break;
            case TRAILERS:
                cursor = sqLiteDatabase.query(
                        TrailerContract.TrailerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case REVIEWS:
                cursor = sqLiteDatabase.query(
                        ReviewContract.ReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
          throw new UnsupportedOperationException("Unsuported operation");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = movieSQLiteHelper.getWritableDatabase();
        int numberOfRowsDeleted;

        int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                numberOfRowsDeleted = sqLiteDatabase.delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        null,
                        null
                );
                break;
            case TRAILERS:
                numberOfRowsDeleted = sqLiteDatabase.delete(
                        TrailerContract.TrailerEntry.TABLE_NAME,
                        null,
                        null
                );
                break;
            case REVIEWS:
                numberOfRowsDeleted = sqLiteDatabase.delete(
                        ReviewContract.ReviewEntry.TABLE_NAME,
                        null,
                        null
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numberOfRowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = movieSQLiteHelper.getWritableDatabase();
        int numberOfRowsUpdated;

        int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                numberOfRowsUpdated = sqLiteDatabase.update(
                        MovieContract.MovieEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numberOfRowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        SQLiteDatabase sqLiteDatabase = movieSQLiteHelper.getWritableDatabase();
        int numberOfRowsInserted = 0;

        int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                sqLiteDatabase.beginTransaction();

                try {
                    for (ContentValues value : values) {
                        long id = sqLiteDatabase.insert(
                                MovieContract.MovieEntry.TABLE_NAME,
                                null,
                                value
                        );

                        if (id > 0) {
                            numberOfRowsInserted++;
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } finally {
                    sqLiteDatabase.endTransaction();
                }

                break;
            case TRAILERS:
                sqLiteDatabase.beginTransaction();

                try {
                    for (ContentValues value : values) {
                        long id = sqLiteDatabase.insert(
                                TrailerContract.TrailerEntry.TABLE_NAME,
                                null,
                                value
                        );

                        if (id > 0) {
                            numberOfRowsInserted++;
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } finally {
                    sqLiteDatabase.endTransaction();
                }

                break;
            case REVIEWS:
                sqLiteDatabase.beginTransaction();

                try {
                    for (ContentValues value : values) {
                        long id = sqLiteDatabase.insert(
                                ReviewContract.ReviewEntry.TABLE_NAME,
                                null,
                                value
                        );

                        if (id > 0) {
                            numberOfRowsInserted++;
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } finally {
                    sqLiteDatabase.endTransaction();
                }

                break;
            default:
                return super.bulkInsert(uri, values);
        }

        if (numberOfRowsInserted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsInserted;
    }
}
