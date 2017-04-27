package com.example.fmoyader.popularmovies.network;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.example.fmoyader.popularmovies.R;
import com.example.fmoyader.popularmovies.dto.Movie;
import com.example.fmoyader.popularmovies.dto.MovieReview;
import com.example.fmoyader.popularmovies.dto.MovieTrailer;
import com.example.fmoyader.popularmovies.enums.MovieSortingMode;
import com.example.fmoyader.popularmovies.network.offline.contract.MovieContract;
import com.example.fmoyader.popularmovies.network.offline.contract.ReviewContract;
import com.example.fmoyader.popularmovies.network.offline.contract.TrailerContract;
import com.example.fmoyader.popularmovies.network.online.MovieDBNetworkHelper;
import com.example.fmoyader.popularmovies.services.PersistMovieDataIntentService;
import com.example.fmoyader.popularmovies.utils.MovieSQLiteUtils;
import com.example.fmoyader.popularmovies.utils.NetworkStatusUtils;

import static android.R.attr.offset;

/**
 * Created by fmoyader on 21/4/17.
 */

public class MovieDispatcher implements MovieDBNetworkHelper.MovieDBNetworkListener {

    private static final long UNITS_PER_PAGE = 20;

    public interface MovieDispatcherListener {
        void onMoviesResponse(Movie[] movies, long nextPage);
        void onMovieTrailersResponse(MovieTrailer[] movieTrailers);
        void onMovieReviewsResponse(MovieReview[] movieReviews, long nextPage);
        void onFailure(String message);
    }

    private Context context;
    private MovieDispatcherListener movieDispatcherListener;
    private static MovieDispatcher movieDispatcher;

    private MovieDBNetworkHelper movieDBNetworkHelper;

    private MovieDispatcher() {}

    public synchronized static MovieDispatcher getInstance() {
        if (movieDispatcher == null) {
            movieDispatcher = new MovieDispatcher();
        }

        return movieDispatcher;
    }

    public void initialize(@NonNull Context context, @NonNull MovieDispatcherListener movieDispatcherListener) {
        this.context = context;
        this.movieDispatcherListener = movieDispatcherListener;
        movieDBNetworkHelper = MovieDBNetworkHelper.getInstance();
        movieDBNetworkHelper.setMovieDBNetworkListener(this);
    }

    public void requestPopularMovies(long page) {
        requestMovies(page, MovieSortingMode.POPULARITY);
    }

    public void requestTopRatedMovies(long page) {
        requestMovies(page, MovieSortingMode.RATING);
    }

    public void requestMovieTrailers(String movieId) {
        if (NetworkStatusUtils.isNetworkAvailable(context)) {
            movieDBNetworkHelper.requestMovieTrailers(movieId);
        } else {
            Cursor cursor = context.getContentResolver().query(
                    TrailerContract.TrailerEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

            MovieTrailer[] trailers = MovieSQLiteUtils.moviesCursorToMovieTrailerList(cursor);
            cursor.close();
            if (trailers != null && trailers.length > 0) {
                movieDispatcherListener.onMovieTrailersResponse(trailers);
            }
        }
    }
    public void requestMovieReviews(String movieId, long page) {
        if (NetworkStatusUtils.isNetworkAvailable(context)) {
            movieDBNetworkHelper.requestMovieReviews(movieId, page);
        } else {
            Cursor cursor = context.getContentResolver().query(
                    ReviewContract.ReviewEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

            MovieReview[] reviews = MovieSQLiteUtils.moviesCursorToMovieReviewList(cursor);
            cursor.close();
            if (reviews != null && reviews.length > 0) {
                movieDispatcherListener.onMovieReviewsResponse(reviews, 0);
            }
        }
    }

    private void requestMovies(long page, MovieSortingMode mode) {
        if (NetworkStatusUtils.isNetworkAvailable(context)) {
            switch (mode) {
                case POPULARITY:
                    movieDBNetworkHelper.requestPopularMovies(page);
                    break;
                case RATING:
                    movieDBNetworkHelper.requestTopRatedMovies(page);
                    break;
                case FAVOURITE:
                    requestFavouriteMovies();
                    break;
            }
        } else {
            queryDatabase(page, mode);
        }
    }

    public void requestFavouriteMovies() {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry.COLUMN_FAVOURITE + " = ?",
                new String[]{"1"},
                null
        );

        Movie[] movies = MovieSQLiteUtils.moviesCursorToMovieList(cursor);
        if (cursor != null) cursor.close();
        if (movies != null && movies.length > 0) {
            movieDispatcherListener.onMoviesResponse(movies, 0);
        } else {
            movieDispatcherListener.onFailure(context.getResources().getString(R.string.no_results));
        }
    }

    private void queryDatabase(long page, MovieSortingMode mode) {
        String sortOrder = null;
        switch (mode) {
            case POPULARITY:
                sortOrder = MovieContract.MovieEntry.COLUMN_POPULARITY;
                break;
            case FAVOURITE:
                requestFavouriteMovies();
                return;
            case RATING:
                sortOrder = MovieContract.MovieEntry.COLUMN_RATING;
                break;
        }

        Cursor cursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                sortOrder + " DESC"
        );
        Movie[] movies = MovieSQLiteUtils.moviesCursorToMovieList(cursor);
        cursor.close();

        if (movies != null && movies.length > 0) {
            movieDispatcherListener.onMoviesResponse(movies, 0);
        }
    }

    @Override
    public void onMoviesResponse(Movie[] movies, long nextPage) {
        movieDispatcherListener.onMoviesResponse(movies, nextPage);

        Intent intentToPersistDataMovieIntentService =
                new Intent(context, PersistMovieDataIntentService.class);
        intentToPersistDataMovieIntentService
                .putExtra(PersistMovieDataIntentService.MOVIES_LIST_EXTRA, movies);
        context.startService(intentToPersistDataMovieIntentService);
    }

    @Override
    public void onMovieReviewsResponse(MovieReview[] movieReviews, long nextPage) {
        movieDispatcherListener.onMovieReviewsResponse(movieReviews, nextPage);

        Intent intentToPersistDataMovieIntentService =
                new Intent(context, PersistMovieDataIntentService.class);
        intentToPersistDataMovieIntentService
                .putExtra(PersistMovieDataIntentService.MOVIES_REVIEWS_LIST_EXTRA, movieReviews);
        context.startService(intentToPersistDataMovieIntentService);
    }

    @Override
    public void onMovieTrailersResponse(MovieTrailer[] movieTrailers) {
        movieDispatcherListener.onMovieTrailersResponse(movieTrailers);

        Intent intentToPersistDataMovieIntentService =
                new Intent(context, PersistMovieDataIntentService.class);
        intentToPersistDataMovieIntentService
                .putExtra(PersistMovieDataIntentService.MOVIES_TRAILERS_LIST_EXTRA, movieTrailers);
        context.startService(intentToPersistDataMovieIntentService);
    }

    @Override
    public void onFailure() { }

}
