package com.example.fmoyader.popularmovies.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by fmoyader on 30/3/17.
 */

public class Movie implements Serializable {

    @SerializedName("vote_average")
    private String rating;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("adult")
    private boolean adultsMovie;

    private String id;

    private String title;

    @SerializedName("overview")
    private String synopsis;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("genre_ids")
    private String[] genreIds;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("vote_count")
    private String voteCount;

    @SerializedName("poster_path")
    private String posterPath;

    private boolean video;

    private String popularity;

    public String getPopularity() {
        return popularity;
    }

    public boolean isVideo() {
        return video;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String[] getGenreIds() {
        return genreIds;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public boolean isAdultsMovie() {
        return adultsMovie;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getRating() {
        return rating;
    }

}
