package com.example.fmoyader.popularmovies.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fmoyader on 24/4/17.
 */

public class MovieTrailer implements Parcelable {
    @SerializedName("id")
    private String trailerId;
    private String movieId;
    private String name;
    private String key;

    public MovieTrailer() {
    }

    protected MovieTrailer(Parcel in) {
        trailerId = in.readString();
        movieId = in.readString();
        name = in.readString();
        key = in.readString();
    }

    public static final Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trailerId);
        dest.writeString(movieId);
        dest.writeString(name);
        dest.writeString(key);
    }
}
