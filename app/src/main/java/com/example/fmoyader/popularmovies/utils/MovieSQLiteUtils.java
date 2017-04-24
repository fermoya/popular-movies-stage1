package com.example.fmoyader.popularmovies.utils;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.fmoyader.popularmovies.dto.Movie;
import com.example.fmoyader.popularmovies.network.offline.contract.MovieContract;

/**
 * Created by fmoyader on 22/4/17.
 */

public class MovieSQLiteUtils {

    public static ContentValues[] movieListToContentValuesList(Movie[] movies) {
        ContentValues[] values = new ContentValues[movies.length];
        for (int i = 0; i < movies.length; i++) {
            ContentValues value = mapMovieToContentValues(movies[i]);
            values[i] = value;
        }

        return values;
    }

    private static ContentValues mapMovieToContentValues(Movie movie) {
        //TODO add reviews and movies column
        ContentValues values = new ContentValues();

        values.put(MovieContract.MovieEntry.COLUMN_ID, movie.getId());
        values.put(MovieContract.MovieEntry.COLUMN_ORIGIAL_LANGUAGE, movie.getOriginalLanguage());
        values.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(MovieContract.MovieEntry.COLUMN_RATING, movie.getRating());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, movie.getSynopsis());
        values.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());

        return values;
    }

    public static Movie[] moviesCursorToMovieList(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }

        int i = 0;
        Movie[] movies = new Movie[cursor.getCount()];
        while (cursor.moveToNext()) {
            Movie movie = mapCursorToMovie(cursor);
            movies[i++] = movie;
        }

        return movies;
    }

    private static Movie mapCursorToMovie(Cursor cursor) {
        Movie movie = new Movie();

        movie.setId(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID)));
        movie.setOriginalLanguage(cursor.getString(
                cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGIAL_LANGUAGE)));
        movie.setPopularity(cursor.getString(
                cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULARITY)));
        movie.setOriginalTitle(cursor.getString(
                cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE)));
        movie.setPosterPath(cursor.getString(
                cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)));
        movie.setRating(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING)));
        movie.setReleaseDate(cursor.getString(
                cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
        movie.setSynopsis(cursor.getString(
                cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_SYNOPSIS)));

        return movie;
    }
}
