package com.example.fmoyader.popularmovies.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by fmoyader on 2/4/17.
 */

public class MoviePage {
    @SerializedName("results")
    private Movie[] movies;

    private long page;

    @SerializedName("total_results")
    private long numberOfResults;

    @SerializedName("total_pages")
    private long numberOfPages;

    public Movie[] getMovies() {
        return movies;
    }

    public long getPage() {
        return page;
    }

    public long getNumberOfResults() {
        return numberOfResults;
    }

    public long getNumberOfPages() {
        return numberOfPages;
    }

    @Override
    public String toString() {
        return "MoviePage{" +
                "movies=" + Arrays.toString(movies) +
                ", page=" + page +
                ", numberOfResults=" + numberOfResults +
                ", numberOfPages=" + numberOfPages +
                '}';
    }
}

