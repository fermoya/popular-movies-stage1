package com.example.fmoyader.popularmovies.network.online;

import com.example.fmoyader.popularmovies.dto.MoviePage;
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

    //TODO: check if these two calls down here are paginated
    //TODO: map the response into an object
    //TODO: analyse use of POST requests to hide api_key query
    @GET("movie/{id}/videos")
    Call<MovieTrailerPage> getMovieTrailers(
            @Path("id") String movieId,
            @Query("api_key") String appKey
    );

    @GET("movie/{id}/reviews")
    Call<MoviePage> getMovieReviews(
            @Path("id") String movieId,
            @Query("api_key") String appKey,
            @Query("page") long page
    );

}
