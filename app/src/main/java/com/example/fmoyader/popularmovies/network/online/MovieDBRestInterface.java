package com.example.fmoyader.popularmovies.network.online;

import com.example.fmoyader.popularmovies.dto.MoviePage;
import com.example.fmoyader.popularmovies.dto.MovieReviewPage;
import com.example.fmoyader.popularmovies.dto.MovieTrailer;
import com.example.fmoyader.popularmovies.dto.MovieTrailerPage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by fmoyader on 2/4/17.
 */

public interface MovieDBRestInterface {
    @GET("movie/popular")
    Call<MoviePage> getPopularMovies(
            @Query("api_key") String appKey,
            @Query("page") long page
    );

    @GET("movie/top_rated")
    Call<MoviePage> getTopRatedMovies(@Query("api_key") String appKey, @Query("page") long page);

    @GET("movie/{id}/videos")
    Call<MovieTrailerPage> getMovieTrailers(
            @Path("id") String movieId,
            @Query("api_key") String appKey
    );

    @GET("movie/{id}/reviews")
    Call<MovieReviewPage> getMovieReviews(
            @Path("id") String movieId,
            @Query("api_key") String appKey,
            @Query("page") long page
    );

}
