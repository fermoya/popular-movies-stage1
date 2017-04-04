package com.example.fmoyader.popularmovies.network;

import com.example.fmoyader.popularmovies.dto.MoviePage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by fmoyader on 2/4/17.
 */

public interface MovieDBService {
    @GET("movie/popular")
    Call<MoviePage> popularMovies(@Query("api_key") String appKey, @Query("page") long page);

    @GET("movie/top_rated")
    Call<MoviePage> topRatedMovies(@Query("api_key") String appKey, @Query("page") long page);
}
