package com.example.fmoyader.popularmovies.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.fmoyader.popularmovies.dto.Movie;
import com.example.fmoyader.popularmovies.network.MovieDispatcher;
import com.example.fmoyader.popularmovies.network.offline.contract.MovieContract;
import com.example.fmoyader.popularmovies.utils.MovieSQLiteUtils;


public class PersistMovieDataIntentService extends IntentService {

    public final static String MOVIES_LIST_EXTRA = "movies_list";

    public PersistMovieDataIntentService() {
        super("PersistMovieDataIntentService");
    }

    private MovieDispatcher movieDispatcher;
    protected void onHandleIntent(Intent intent) {
        if (intent.hasExtra(MOVIES_LIST_EXTRA)) {
            Movie[] movies = findMovies(intent);
            persistOnDatabase(movies);
        }
    }

    private void persistOnDatabase(Movie[] movies) {
        ContentValues[] values = MovieSQLiteUtils.movieListToContentValuesList(movies);
        getContentResolver().bulkInsert(
                MovieContract.MovieEntry.CONTENT_URI,
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
}
