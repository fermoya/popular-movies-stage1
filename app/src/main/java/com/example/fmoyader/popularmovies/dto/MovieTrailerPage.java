package com.example.fmoyader.popularmovies.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fmoyader on 24/4/17.
 */

public class MovieTrailerPage {
    @SerializedName("id")
    private String movieId;
    @SerializedName("results")
    private MovieTrailer[] trailers;

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String id) {
        this.movieId = id;
    }

    public MovieTrailer[] getTrailers() {
        return trailers;
    }

    public void setTrailers(MovieTrailer[] trailers) {
        this.trailers = trailers;
    }
}
