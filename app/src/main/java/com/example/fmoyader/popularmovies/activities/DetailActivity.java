package com.example.fmoyader.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fmoyader.popularmovies.R;
import com.example.fmoyader.popularmovies.dto.Movie;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    public static final String MOVIE_EXTRA = "Movie Selected";

    private TextView movieOriginalTitleTextView;
    private TextView movieReleaseDateTextView;
    private TextView movieRatingTextView;
    private TextView movieSynopsisTextView;
    private TextView movieOriginalLanguage;
    private ImageView movieThumbnailImageView;

    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String SIZE = "w500";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movieOriginalTitleTextView = (TextView) findViewById(R.id.tv_movie_original_title);
        movieReleaseDateTextView = (TextView) findViewById(R.id.tv_movie_release_date);
        movieRatingTextView = (TextView) findViewById(R.id.tv_movie_rating);
        movieSynopsisTextView = (TextView) findViewById(R.id.tv_movie_synopsis);
        movieThumbnailImageView = (ImageView) findViewById(R.id.iv_movie_thumbnail);
        movieOriginalLanguage = (TextView) findViewById(R.id.tv_movie_original_language);

        Intent intentFromMainActivity = getIntent();
        if (intentFromMainActivity.hasExtra(MOVIE_EXTRA)) {
            Movie movie = (Movie) intentFromMainActivity.getSerializableExtra(MOVIE_EXTRA);
            movieOriginalTitleTextView.setText(movie.getOriginalTitle());
            movieReleaseDateTextView.setText(findLongFormattedReleaseDate(movie.getReleaseDate()));
            movieRatingTextView.setText(movie.getRating() + "/10");
            movieSynopsisTextView.setText(movie.getSynopsis());
            movieOriginalLanguage.setText(findLanguageLongName(movie.getOriginalLanguage()));
            Picasso.with(this).load(BASE_URL + SIZE + movie.getPosterPath()).into(movieThumbnailImageView);
        }

        setTitle(getString(R.string.detail_activity_title));
    }

    private String findLanguageLongName(String originalLanguage) {
        Locale locale = new Locale(originalLanguage);
        return locale.getDisplayName();
    }

    private String findLongFormattedReleaseDate(String shortFormatReleaseDate) {
        DateFormat jsonDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date releaseDate = null;

        String longFormattedReleaseDate = "";
        try {
            releaseDate = jsonDateFormat.parse(shortFormatReleaseDate);
        } catch (ParseException e) {
            movieReleaseDateTextView.setVisibility(View.INVISIBLE);
            e.printStackTrace();
        }

        if (releaseDate != null) {
            DateFormat longDateFormat = new SimpleDateFormat("dd MMMM yyyy");
            longFormattedReleaseDate = longDateFormat.format(releaseDate);
        }

        return longFormattedReleaseDate;
    }
}
