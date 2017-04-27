package com.example.fmoyader.popularmovies.utils;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.fmoyader.popularmovies.dto.Movie;
import com.example.fmoyader.popularmovies.dto.MovieReview;
import com.example.fmoyader.popularmovies.dto.MovieTrailer;
import com.example.fmoyader.popularmovies.network.offline.contract.MovieContract;
import com.example.fmoyader.popularmovies.network.offline.contract.ReviewContract;
import com.example.fmoyader.popularmovies.network.offline.contract.TrailerContract;

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

    public static ContentValues mapMovieToContentValues(Movie movie) {
        ContentValues values = new ContentValues();

        values.put(MovieContract.MovieEntry.COLUMN_ID, movie.getId());
        values.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        values.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(MovieContract.MovieEntry.COLUMN_RATING, movie.getRating());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, movie.getSynopsis());
        values.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
        values.put(MovieContract.MovieEntry.COLUMN_FAVOURITE, movie.isFavourite() ? 1 : 0);

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
                cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE)));
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
        movie.setFavourite(cursor.getInt(
                cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_FAVOURITE)) == 1);

        return movie;
    }

    public static MovieTrailer[] moviesCursorToMovieTrailerList(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }

        int i = 0;
        MovieTrailer[] trailers = new MovieTrailer[cursor.getCount()];
        while (cursor.moveToNext()) {
            MovieTrailer trailer = mapCursorToMovieTrailer(cursor);
            trailers[i++] = trailer;
        }

        return trailers;
    }

    private static MovieTrailer mapCursorToMovieTrailer(Cursor cursor) {
        MovieTrailer trailer =  new MovieTrailer();
        trailer.setKey(cursor.getString(
                cursor.getColumnIndex(TrailerContract.TrailerEntry.COLUMN_TRAILER_KEY)));
        trailer.setName(cursor.getString(
                cursor.getColumnIndex(TrailerContract.TrailerEntry.COLUMN_NAME)));
        trailer.setTrailerId(cursor.getString(
                cursor.getColumnIndex(TrailerContract.TrailerEntry.COLUMN_TRAILER_ID)));
        trailer.setMovieId(cursor.getString(
                cursor.getColumnIndex(TrailerContract.TrailerEntry.COLUMN_MOVIE_ID)));

        return trailer;
    }

    public static MovieReview[] moviesCursorToMovieReviewList(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }

        int i = 0;
        MovieReview[] reviews = new MovieReview[cursor.getCount()];
        while (cursor.moveToNext()) {
            MovieReview review = mapCursorToMovieReview(cursor);
            reviews[i++] = review;
        }

        return reviews;
    }

    private static MovieReview mapCursorToMovieReview(Cursor cursor) {
        MovieReview review = new MovieReview();
        review.setMovieId(cursor.getString(
                cursor.getColumnIndex(ReviewContract.ReviewEntry.COLUMN_MOVIE_ID)));
        review.setContent(cursor.getString(cursor.getColumnIndex(ReviewContract.ReviewEntry.COLUMN_CONTENT)));
        review.setReviewId(cursor.getString(cursor.getColumnIndex(ReviewContract.ReviewEntry.COLUMN_REVIEW_ID)));

        return review;
    }

    public static ContentValues[] trailerListToContentValuesList(MovieTrailer[] trailers) {
        int i = 0;
        ContentValues[] values = new ContentValues[trailers.length];
        for (MovieTrailer trailer : trailers) {
            ContentValues value = new ContentValues();
            value.put(TrailerContract.TrailerEntry.COLUMN_TRAILER_KEY, trailer.getKey());
            value.put(TrailerContract.TrailerEntry.COLUMN_MOVIE_ID, trailer.getMovieId());
            value.put(TrailerContract.TrailerEntry.COLUMN_NAME, trailer.getName());
            value.put(TrailerContract.TrailerEntry.COLUMN_TRAILER_ID, trailer.getTrailerId());

            values[i++] = value;
        }

        return values;
    }

    public static ContentValues[] reviewListToContentValuesList(MovieReview[] reviews) {
        int i = 0;
        ContentValues[] values = new ContentValues[reviews.length];
        for (MovieReview review : reviews) {
            ContentValues value = new ContentValues();
            value.put(ReviewContract.ReviewEntry.COLUMN_CONTENT, review.getContent());
            value.put(ReviewContract.ReviewEntry.COLUMN_MOVIE_ID, review.getMovieId());
            value.put(ReviewContract.ReviewEntry.COLUMN_REVIEW_ID, review.getReviewId());

            values[i++] = value;
        }

        return values;
    }
}
