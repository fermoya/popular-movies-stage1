package com.example.fmoyader.popularmovies.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fmoyader on 25/4/17.
 */

public class MovieReview implements Parcelable{
    @SerializedName("id")
    private String reviewId;
    private String content;
    private String movieId;

    public MovieReview() {
    }

    protected MovieReview(Parcel in) {
        reviewId = in.readString();
        content = in.readString();
        movieId = in.readString();
    }

    public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reviewId);
        dest.writeString(content);
        dest.writeString(movieId);
    }
}
