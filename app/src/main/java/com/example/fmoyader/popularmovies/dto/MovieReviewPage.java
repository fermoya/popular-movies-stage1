package com.example.fmoyader.popularmovies.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fmoyader on 25/4/17.
 */

public class MovieReviewPage {
    private long page;
    @SerializedName("id")
    private String movieId;
    @SerializedName("results")
    private MovieReview[] movieReviews;
    @SerializedName("total_results")
    private long numberOfResults;
    @SerializedName("total_pages")
    private long numberOfPages;

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public MovieReview[] getMovieReviews() {
        return movieReviews;
    }

    public void setMovieReviews(MovieReview[] movieReviews) {
        this.movieReviews = movieReviews;
    }

    public long getNumberOfResults() {
        return numberOfResults;
    }

    public void setNumberOfResults(long numberOfResults) {
        this.numberOfResults = numberOfResults;
    }

    public long getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(long numberOfPages) {
        this.numberOfPages = numberOfPages;
    }
}
