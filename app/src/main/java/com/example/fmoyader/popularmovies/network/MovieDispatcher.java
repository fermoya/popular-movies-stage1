package com.example.fmoyader.popularmovies.network;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.example.fmoyader.popularmovies.dto.Movie;
import com.example.fmoyader.popularmovies.dto.MovieReview;
import com.example.fmoyader.popularmovies.dto.MovieTrailer;
import com.example.fmoyader.popularmovies.enums.MovieSortingMode;
import com.example.fmoyader.popularmovies.network.offline.contract.MovieContract;
import com.example.fmoyader.popularmovies.network.online.MovieDBNetworkHelper;
import com.example.fmoyader.popularmovies.services.PersistMovieDataIntentService;
import com.example.fmoyader.popularmovies.utils.MovieSQLiteUtils;
import com.example.fmoyader.popularmovies.utils.NetworkStatusUtils;

/**
 * Created by fmoyader on 21/4/17.
 */

public class MovieDispatcher implements MovieDBNetworkHelper.MovieDBNetworkListener {

    public interface MovieDispatcherListener {
        void onMoviesResponse(Movie[] movies, long nextPage);
        void onMovieTrailersResponse(MovieTrailer[] movieTrailers);
        void onMovieReviewsResponse(MovieReview[] movieReviews, long nextPage);
        void onFailure();
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
        movieDBNetworkHelper.requestMovieTrailers(movieId);
    }
    public void requestMovieReviews(String movieId, long page) {
        movieDBNetworkHelper.requestMovieReviews(movieId, page);
    }

    private void requestMovies(long page, MovieSortingMode mode) {
        if (NetworkStatusUtils.isNetworkAvailable(context)) {
            switch (mode) {
                case POPULARITY:
                    movieDBNetworkHelper.requestPopularMovies(page);
                    break;
                case RATING:
                //TODO: change and add favorite mode
                case FAVOURITE:
                    movieDBNetworkHelper.requestTopRatedMovies(page);
                    break;
            }
        } else {
            queryDatabase(page, mode);
        }
    }

    private void queryDatabase(long page, MovieSortingMode mode) {
        String sortOrder = null;
        switch (mode) {
            case POPULARITY:
                sortOrder = MovieContract.MovieEntry.COLUMN_POPULARITY;
                break;
            case RATING:
                //TODO: change and add favorite mode
            case FAVOURITE:
                sortOrder = MovieContract.MovieEntry.COLUMN_RATING;
                break;
        }

        //TODO: paginate search of results
        Cursor cursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                sortOrder + " DESC LIMIT 20 OFFSET 0"
        );

        Movie[] movies = MovieSQLiteUtils.moviesCursorToMovieList(cursor);

        if (movies != null && movies.length > 0) {
            movieDispatcherListener.onMoviesResponse(movies, page + 1);
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
    }

    @Override
    public void onMovieTrailersResponse(MovieTrailer[] movieTrailers) {
        movieDispatcherListener.onMovieTrailersResponse(movieTrailers);

//        Intent intentToPersistDataMovieIntentService =
//                new Intent(context, PersistMovieDataIntentService.class);
//        intentToPersistDataMovieIntentService
//                .putExtra(PersistMovieDataIntentService.MOVIES_LIST_EXTRA, movies);
//        context.startService(intentToPersistDataMovieIntentService);
    }

    @Override
    public void onFailure() { }

}
