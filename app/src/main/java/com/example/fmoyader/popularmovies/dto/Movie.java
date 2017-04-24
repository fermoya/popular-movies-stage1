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

    private String id;

    @SerializedName("overview")
    private String synopsis;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("poster_path")
    private String posterPath;

    private String popularity;

    public Movie () {}

    protected Movie(Parcel in) {
        // Only these ones are the properties we need
        rating = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        releaseDate = in.readString();
        synopsis = in.readString();
        posterPath = in.readString();
        id = in.readString();
        popularity = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rating);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeString(releaseDate);
        dest.writeString(synopsis);
        dest.writeString(posterPath);
        dest.writeString(id);
        dest.writeString(popularity);
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

    public String getPosterPath() {
        return posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getId() {
        return id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
