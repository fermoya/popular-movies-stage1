package com.example.fmoyader.popularmovies.network.online;

import com.example.fmoyader.popularmovies.BuildConfig;
import com.example.fmoyader.popularmovies.dto.Movie;
import com.example.fmoyader.popularmovies.dto.MoviePage;
import com.example.fmoyader.popularmovies.dto.MovieReview;
import com.example.fmoyader.popularmovies.dto.MovieReviewPage;
import com.example.fmoyader.popularmovies.dto.MovieTrailer;
import com.example.fmoyader.popularmovies.dto.MovieTrailerPage;

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
        void onMoviesResponse(Movie[] movies, long nextPage);
        void onMovieReviewsResponse(MovieReview[] movieReviews, long nextPage);
        void onMovieTrailersResponse(MovieTrailer[] movieTrailers);
        void onFailure();
    }

    public static final String BASE_URL = "https://api.themoviedb.org/3/";

    public String API_KEY;

    private MovieDBRestInterface movieDBService;

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
        movieDBService = movieDB.create(MovieDBRestInterface.class);
    }

    public synchronized static MovieDBNetworkHelper getInstance() {
        if (movieDBNetworkHelper == null) {
            movieDBNetworkHelper = new MovieDBNetworkHelper();
        }

        return movieDBNetworkHelper;
    }

    public void requestPopularMovies(long page) {
        Call<MoviePage> response = movieDBService.getPopularMovies(API_KEY, page);

        response.enqueue(new Callback<MoviePage>() {
            @Override
            public void onResponse(Call<MoviePage> call, Response<MoviePage> response) {
                if (movieDBNetworkListener != null) {
                    MoviePage moviePage = response.body();
                    if (moviePage == null) return;
                    Movie[] movies = moviePage.getMovies();
                    long nextPage = findNextPage(moviePage);
                    movieDBNetworkListener.onMoviesResponse(movies, nextPage);
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

    public void requestTopRatedMovies(long page) {
        Call<MoviePage> response = movieDBService.getTopRatedMovies(API_KEY, page);

        response.enqueue(new Callback<MoviePage>() {
            @Override
            public void onResponse(Call<MoviePage> call, Response<MoviePage> response) {
                if (movieDBNetworkListener != null) {
                    MoviePage moviePage = response.body();
                    Movie[] movies = moviePage.getMovies();
                    long nextPage = findNextPage(moviePage);
                    movieDBNetworkListener.onMoviesResponse(movies, nextPage);
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

    private long findNextPage(MoviePage page) {
        long currentPage = page.getPage();
        long totalPages = page.getNumberOfPages();

        return currentPage + 1 > totalPages ? 0 : currentPage + 1;
    }
    private long findNextPage(MovieReviewPage page) {
        long currentPage = page.getPage();
        long totalPages = page.getNumberOfPages();

        return currentPage + 1 > totalPages ? 0 : currentPage + 1;
    }

    public void requestMovieReviews(String movieId, long page) {
        Call<MovieReviewPage> movieReviewsResponse = movieDBService.getMovieReviews(movieId, API_KEY, page);

        movieReviewsResponse.enqueue(new Callback<MovieReviewPage>() {
            @Override
            public void onResponse(Call<MovieReviewPage> call, Response<MovieReviewPage> response) {
                MovieReviewPage movieReviewPage = response.body();
                MovieReview[] movieReviews = movieReviewPage.getMovieReviews();

                for (MovieReview review : movieReviews) {
                    review.setMovieId(movieReviewPage.getMovieId());
                }

                long nextPage = findNextPage(movieReviewPage);
                movieDBNetworkListener.onMovieReviewsResponse(movieReviews, nextPage);
            }

            @Override
            public void onFailure(Call<MovieReviewPage> call, Throwable t) {

            }
        });

    }

    public void requestMovieTrailers(String movieId) {
        final Call<MovieTrailerPage> movieTrailersResponse =
                movieDBService.getMovieTrailers(movieId, API_KEY);
        movieTrailersResponse.enqueue(new Callback<MovieTrailerPage>() {
            @Override
            public void onResponse(Call<MovieTrailerPage> call, Response<MovieTrailerPage> response) {
                MovieTrailerPage page = response.body();
                MovieTrailer[] trailers = page.getTrailers();
                for (MovieTrailer trailer : trailers) {
                    trailer.setMovieId(page.getMovieId());
                }
                movieDBNetworkListener.onMovieTrailersResponse(trailers);
            }

            @Override
            public void onFailure(Call<MovieTrailerPage> call, Throwable t) {
                movieDBNetworkListener.onFailure();
            }
        });
    }
}
