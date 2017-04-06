package com.example.fmoyader.popularmovies.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by fmoyader on 30/3/17.
 */

public class Movie implements Parcelable {

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

    protected Movie(Parcel in) {
        // Only these ones are the properties we need
        rating = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        releaseDate = in.readString();
        synopsis = in.readString();
        posterPath = in.readString();

        backdropPath = in.readString();
        adultsMovie = in.readByte() != 0;
        id = in.readString();
        genreIds = in.createStringArray();
        voteCount = in.readString();
        video = in.readByte() != 0;
        popularity = in.readString();
        title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rating);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeString(releaseDate);
        dest.writeString(synopsis);
        dest.writeString(posterPath);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }
}
