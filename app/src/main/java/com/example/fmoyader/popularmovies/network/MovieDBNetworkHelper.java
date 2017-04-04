package com.example.fmoyader.popularmovies.network;

import com.example.fmoyader.popularmovies.BuildConfig;
import com.example.fmoyader.popularmovies.dto.Movie;
import com.example.fmoyader.popularmovies.dto.MoviePage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fmoyader on 2/4/17.
 */

public class MovieDBNetworkHelper {

    public interface MovieDBNetworkListener {
        void onResponse(Movie[] movies, long nextPage);
        void onFailure();
    }

    public static final String BASE_URL = "https://api.themoviedb.org/3/";

    //TODO delete the key
    public String API_KEY;

    private MovieDBService movieDBService;

    private static MovieDBNetworkHelper movieDBNetworkHelper;

    private MovieDBNetworkListener movieDBNetworkListener;

    public void setMovieDBNetworkListener(MovieDBNetworkListener movieDBNetworkListener) {
        this.movieDBNetworkListener = movieDBNetworkListener;
    }

    private MovieDBNetworkHelper() {
        this.API_KEY = BuildConfig.API_KEY;

        Retrofit movieDB = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        movieDBService = movieDB.create(MovieDBService.class);
    }

    public synchronized static MovieDBNetworkHelper getInstance() {
        if (movieDBNetworkHelper == null) {
            movieDBNetworkHelper = new MovieDBNetworkHelper();
        }

        return movieDBNetworkHelper;
    }

    public void getPopularMovies(long page) {
        Call<MoviePage> response = movieDBService.popularMovies(API_KEY, page);

        response.enqueue(new Callback<MoviePage>() {
            @Override
            public void onResponse(Call<MoviePage> call, Response<MoviePage> response) {
                if (movieDBNetworkListener != null) {
                    MoviePage moviePage = response.body();
                    Movie[] movies = moviePage.getMovies();
                    long nextPage = findNextPage(moviePage);
                    movieDBNetworkListener.onResponse(movies, nextPage);
                }
            }

            @Override
            public void onFailure(Call<MoviePage> call, Throwable t) {
                if (movieDBNetworkListener != null) {
                    movieDBNetworkListener.onFailure();
                }
            }

        });
    }

    public void getTopRatedMovies(long page) {
        Call<MoviePage> response = movieDBService.topRatedMovies(API_KEY, page);

        response.enqueue(new Callback<MoviePage>() {
            @Override
            public void onResponse(Call<MoviePage> call, Response<MoviePage> response) {
                if (movieDBNetworkListener != null) {
                    MoviePage moviePage = response.body();
                    Movie[] movies = moviePage.getMovies();
                    long nextPage = findNextPage(moviePage);
                    movieDBNetworkListener.onResponse(movies, nextPage);
                }
            }

            @Override
            public void onFailure(Call<MoviePage> call, Throwable t) {
                if (movieDBNetworkListener != null) {
                    movieDBNetworkListener.onFailure();
                }
            }

        });
    }

    private long findNextPage(MoviePage moviePage) {
        long currentPage = moviePage.getPage();
        long totalPages = moviePage.getNumberOfPages();

        return currentPage + 1 > totalPages ? 0 : currentPage + 1;
    }
}
