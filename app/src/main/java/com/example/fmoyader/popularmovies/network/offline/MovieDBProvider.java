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

import com.example.fmoyader.popularmovies.network.offline.contract.MovieContract;

/**
 * Created by fmoyader on 12/4/17.
 */

public class MovieDBProvider extends ContentProvider {

    private MovieSQLiteHelper movieSQLiteHelper;
    private static UriMatcher uriMatcher = buildUriMatcher();

    private static final int MOVIES = 100;
    private static final int MOVIES_WITH_ID = 101;

    @Override
    public boolean onCreate() {
        movieSQLiteHelper = new MovieSQLiteHelper(getContext());
        return true;
    }

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.MOVIES_PATH, MOVIES);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.MOVIES_PATH + "/#", MOVIES_WITH_ID);
        return matcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        SQLiteDatabase sqLiteDatabase = movieSQLiteHelper.getReadableDatabase();
        Cursor cursor;

        int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                cursor = sqLiteDatabase.query(
                        MovieContract.MovieEntry.TABLE_NAME,
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

    //TODO review if needed
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase sqLiteDatabase = movieSQLiteHelper.getWritableDatabase();
        Uri returnUri;

        int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                long id = sqLiteDatabase.insert(
                        MovieContract.MovieEntry.TABLE_NAME,
                        null,
                        values
                );

                if (id < 0) {
                    throw new SQLiteException("Failed inserting uri " + uri);
                }

                returnUri = ContentUris.withAppendedId(MovieContract.BASE_CONTENT_URI, id);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = movieSQLiteHelper.getWritableDatabase();
        int numberOfRowsDeleted;

        int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIES_WITH_ID:
                String id = uri.getLastPathSegment();
                String whereClause = "_id = ?";
                String[] whereArgs = new String[]{id};

                numberOfRowsDeleted = sqLiteDatabase.delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        whereClause,
                        whereArgs
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
            case MOVIES_WITH_ID:
                String id = uri.getLastPathSegment();
                String whereClause = "_id = ?";
                String[] whereArgs = new String[]{id};

                numberOfRowsUpdated = sqLiteDatabase.update(
                        MovieContract.MovieEntry.TABLE_NAME,
                        values,
                        whereClause,
                        whereArgs
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
            default:
                return super.bulkInsert(uri, values);
        }

        if (numberOfRowsInserted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsInserted;
    }
}
