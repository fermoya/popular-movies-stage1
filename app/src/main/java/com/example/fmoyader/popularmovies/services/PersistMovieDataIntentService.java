package com.example.fmoyader.popularmovies.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.fmoyader.popularmovies.dto.Movie;
import com.example.fmoyader.popularmovies.dto.MovieReview;
import com.example.fmoyader.popularmovies.dto.MovieTrailer;
import com.example.fmoyader.popularmovies.network.MovieDispatcher;
import com.example.fmoyader.popularmovies.network.offline.contract.MovieContract;
import com.example.fmoyader.popularmovies.network.offline.contract.ReviewContract;
import com.example.fmoyader.popularmovies.network.offline.contract.TrailerContract;
import com.example.fmoyader.popularmovies.utils.MovieSQLiteUtils;

import java.util.ArrayList;
import java.util.List;


public class PersistMovieDataIntentService extends IntentService {

    public final static String MOVIES_LIST_EXTRA = "movies_list";
    public static final String MOVIES_TRAILERS_LIST_EXTRA = "trailers_list";
    public static final String MOVIES_REVIEWS_LIST_EXTRA = "reviews_list";

    public PersistMovieDataIntentService() {
        super("PersistMovieDataIntentService");
    }

    protected void onHandleIntent(Intent intent) {
        if (intent.hasExtra(MOVIES_LIST_EXTRA)) {
            Movie[] movies = findMovies(intent);
            persistOnDatabase(movies);
        }

        if (intent.hasExtra(MOVIES_TRAILERS_LIST_EXTRA)) {
            MovieTrailer[] trailers = findTrailers(intent);
            persistOnDatabase(trailers);
        }

        if (intent.hasExtra(MOVIES_REVIEWS_LIST_EXTRA)) {
            MovieReview[] reviews = findReviews(intent);
            persistOnDatabase(reviews);
        }
    }

    private void persistOnDatabase(Movie[] movies) {
        /*for (Movie movie : movies) {
            try {
                ContentValues value = MovieSQLiteUtils.mapMovieToContentValues(movie);
                getContentResolver().update(
                        MovieContract.MovieEntry.CONTENT_URI,
                        value,
                        MovieContract.MovieEntry.COLUMN_ID + " = ? ",
                        new String[]{(String) value.get(MovieContract.MovieEntry.COLUMN_ID)}
                );
            } catch (Exception e) {
                moviesToUpdate.add(movie);
            }
        }*/
//        getContentResolver().delete(
//                MovieContract.MovieEntry.CONTENT_URI,
//                null,
//                null
//        );

        ContentValues[] values =
                MovieSQLiteUtils.movieListToContentValuesList(movies);
        getContentResolver().bulkInsert(
                MovieContract.MovieEntry.CONTENT_URI,
                values
        );
    }

    private void persistOnDatabase(MovieTrailer[] trailers) {
        ContentValues[] values = MovieSQLiteUtils.trailerListToContentValuesList(trailers);
        getContentResolver().bulkInsert(
                TrailerContract.TrailerEntry.CONTENT_URI,
                values
        );
    }

    private void persistOnDatabase(MovieReview[] reviews) {
        ContentValues[] values = MovieSQLiteUtils.reviewListToContentValuesList(reviews);
        getContentResolver().bulkInsert(
                ReviewContract.ReviewEntry.CONTENT_URI,
                values
        );
    }

    @NonNull
    private Movie[] findMovies(Intent intent) {
        Parcelable[] parcelableMovies = intent.getParcelableArrayExtra(MOVIES_LIST_EXTRA);
        Movie[] movies = new Movie[parcelableMovies.length];
        for (int i = 0; i < parcelableMovies.length; i++) {
            Movie movie = (Movie) parcelableMovies[i];
            movies[i] = movie;
        }
        return movies;
    }

    @NonNull
    private MovieTrailer[] findTrailers(Intent intent) {
        Parcelable[] parcelableTrailers = intent.getParcelableArrayExtra(MOVIES_TRAILERS_LIST_EXTRA);
        MovieTrailer[] trailers = new MovieTrailer[parcelableTrailers.length];
        for (int i = 0; i < parcelableTrailers.length; i++) {
            MovieTrailer trailer = (MovieTrailer) parcelableTrailers[i];
            trailers[i] = trailer;
        }
        return trailers;
    }

    @NonNull
    private MovieReview[] findReviews(Intent intent) {
        Parcelable[] parcelableReviews = intent.getParcelableArrayExtra(MOVIES_REVIEWS_LIST_EXTRA);
        MovieReview[] reviews = new MovieReview[parcelableReviews.length];
        for (int i = 0; i < parcelableReviews.length; i++) {
            MovieReview review = (MovieReview) parcelableReviews[i];
            reviews[i] = review;
        }
        return reviews;
    }
}
