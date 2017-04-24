package com.example.fmoyader.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fmoyader.popularmovies.R;
import com.example.fmoyader.popularmovies.adapters.MovieReviewsAdapter;
import com.example.fmoyader.popularmovies.adapters.MovieTrailersAdapter;
import com.example.fmoyader.popularmovies.dto.Movie;
import com.example.fmoyader.popularmovies.dto.MovieReview;
import com.example.fmoyader.popularmovies.dto.MovieTrailer;
import com.example.fmoyader.popularmovies.network.MovieDispatcher;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements MovieDispatcher.MovieDispatcherListener{

    @BindView(R.id.tv_movie_original_title)
    TextView movieOriginalTitleTextView;
    @BindView(R.id.tv_movie_release_date)
    TextView movieReleaseDateTextView;
    @BindView(R.id.tv_movie_rating)
    TextView movieRatingTextView;
    @BindView(R.id.tv_movie_synopsis)
    TextView movieSynopsisTextView;
    @BindView(R.id.tv_movie_original_language)
    TextView movieOriginalLanguage;
    @BindView(R.id.iv_movie_thumbnail)
    ImageView movieThumbnailImageView;
    @BindView(R.id.rv_movie_trailers)
    RecyclerView movieTrailersRecyclerView;
    @BindView(R.id.rv_movie_reviews)
    RecyclerView movieReviewsRecyclerView;
    @BindView(R.id.ib_favourite)
    ImageButton movieFavouriteImageButton;

    // Paths to compose a URL and request movie poster
    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String SIZE = "w500";

    // Extras for retrieving data from intents
    public static final String MOVIE_EXTRA = "Movie Selected";

    // Extras for save data when facing Activity recreation
    private static final String MOVIE_ORIGINAL_TITLE_EXTRA = "movie title";
    private static final String MOVIE_RELEASE_DATE_EXTRA = "movie release date";
    private static final String MOVIE_RATING_EXTRA = "movie rating";
    private static final String MOVIE_SYNOPSIS_EXTRA = "movie synopsis";
    private static final String MOVIE_ORIGIAL_LANGUAGE_EXTRA = "movie original language";
    private static final String MOVIE_THUMBNAIL_URL_EXTRA = "movie thumnnail";

    private String posterUrlString;
    private MovieTrailersAdapter movieTrailersAdapter;
    private MovieReviewsAdapter movieReviewsAdapter;
    private MovieDispatcher movieDispatcher;

    private Movie movie;
    private long reviewPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        movieOriginalTitleTextView = (TextView) findViewById(R.id.tv_movie_original_title);
        movieReleaseDateTextView = (TextView) findViewById(R.id.tv_movie_release_date);
        movieRatingTextView = (TextView) findViewById(R.id.tv_movie_rating);
        movieSynopsisTextView = (TextView) findViewById(R.id.tv_movie_synopsis);
        movieThumbnailImageView = (ImageView) findViewById(R.id.iv_movie_thumbnail);
        movieOriginalLanguage = (TextView) findViewById(R.id.tv_movie_original_language);

        movieDispatcher = MovieDispatcher.getInstance();
        movieDispatcher.initialize(this, this);
        if (savedInstanceState != null) {
            restoreViewValues(savedInstanceState);
        } else {
            Intent intentFromMainActivity = getIntent();
            if (intentFromMainActivity.hasExtra(MOVIE_EXTRA)) {
                movie = intentFromMainActivity.getParcelableExtra(MOVIE_EXTRA);
                fillViewValues(movie);
                fetchTrailers();

                reviewPage = 1;
                fetchReviews();
            }
        }

        movieTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final LinearLayoutManager movieReviewsLayoutManager = new LinearLayoutManager(this);
        movieReviewsRecyclerView.setLayoutManager(movieReviewsLayoutManager);
        movieReviewsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visibleItemCount = movieReviewsLayoutManager.getChildCount();
                int totalItemCount = movieReviewsLayoutManager.getItemCount();
                int pastVisibleItems = movieReviewsLayoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    if (reviewPage > 0) {
                        fetchReviews();
                    }
                }
            }
        });
        movieReviewsAdapter = new MovieReviewsAdapter(null);
        movieReviewsRecyclerView.setAdapter(movieReviewsAdapter);

        setTitle(getString(R.string.detail_activity_title));
    }

    private void fetchReviews() {
        movieDispatcher.requestMovieReviews(movie.getId(), reviewPage);
    }

    private void fetchTrailers() {
        movieDispatcher.requestMovieTrailers(movie.getId());
    }

    private void restoreViewValues(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(MOVIE_RELEASE_DATE_EXTRA)) {
            movieReleaseDateTextView.setText(savedInstanceState.getString(MOVIE_RELEASE_DATE_EXTRA));
        }

        if (savedInstanceState.containsKey(MOVIE_SYNOPSIS_EXTRA)) {
            movieSynopsisTextView.setText(savedInstanceState.getString(MOVIE_SYNOPSIS_EXTRA));
        }

        if (savedInstanceState.containsKey(MOVIE_RATING_EXTRA)) {
            movieRatingTextView.setText(savedInstanceState.getString(MOVIE_RATING_EXTRA));
        }

        if (savedInstanceState.containsKey(MOVIE_ORIGIAL_LANGUAGE_EXTRA)) {
            movieOriginalLanguage.setText(savedInstanceState.getString(MOVIE_ORIGIAL_LANGUAGE_EXTRA));
        }

        if (savedInstanceState.containsKey(MOVIE_ORIGINAL_TITLE_EXTRA)) {
            movieOriginalTitleTextView.setText(savedInstanceState.getString(MOVIE_ORIGINAL_TITLE_EXTRA));
        }

        if (savedInstanceState.containsKey(MOVIE_THUMBNAIL_URL_EXTRA)) {
            posterUrlString = savedInstanceState.getString(MOVIE_THUMBNAIL_URL_EXTRA);
            loadImageFromUrl();
        }
    }

    private void loadImageFromUrl() {
        // Picasso caches its responses
        Picasso.with(this)
                .load(posterUrlString)
                //Use of new images to control errors
//                    .placeholder(R.drawable.user_placeholder)
                .placeholder(R.drawable.ic_timer_white_48dp)
                .error(R.drawable.ic_error_white_48dp)
                .into(movieThumbnailImageView);
    }

    private void fillViewValues(Movie movie) {
        movieOriginalTitleTextView.setText(movie.getOriginalTitle());
        movieReleaseDateTextView.setText(findLongFormattedReleaseDate(movie.getReleaseDate()));
        movieRatingTextView.setText(movie.getRating() + "/10");
        movieSynopsisTextView.setText(movie.getSynopsis());
        movieOriginalLanguage.setText(findLanguageLongName(movie.getOriginalLanguage()));

        posterUrlString = BASE_URL + SIZE + movie.getPosterPath();
        loadImageFromUrl();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(MOVIE_ORIGINAL_TITLE_EXTRA, movieOriginalTitleTextView.getText().toString());
        outState.putString(MOVIE_ORIGIAL_LANGUAGE_EXTRA, movieOriginalLanguage.getText().toString());
        outState.putString(MOVIE_RATING_EXTRA, movieRatingTextView.getText().toString());
        outState.putString(MOVIE_RELEASE_DATE_EXTRA, movieRatingTextView.getText().toString());
        outState.putString(MOVIE_SYNOPSIS_EXTRA, movieSynopsisTextView.getText().toString());
        outState.putString(MOVIE_THUMBNAIL_URL_EXTRA, posterUrlString);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMoviesResponse(Movie[] movies, long nextPage) {

    }

    @Override
    public void onMovieTrailersResponse(MovieTrailer[] movieTrailers) {
        movieTrailersAdapter = new MovieTrailersAdapter(this, movieTrailers);
        movieTrailersRecyclerView.setAdapter(movieTrailersAdapter);
    }

    @Override
    public void onMovieReviewsResponse(MovieReview[] movieReviews, long nextPage) {
        reviewPage = nextPage;
        movieReviewsAdapter.addReviews(movieReviews);
    }

    @Override
    public void onFailure() {

    }

    public void movieMarkedAsFavorite(View view) {
        if (movie.isFavourite()) {
            movieFavouriteImageButton.setImageResource(R.drawable.ic_stars_black_24dp);
            movieFavouriteImageButton.setColorFilter(R.color.colorAccent);
        } else {
            movieFavouriteImageButton.setImageResource(R.drawable.ic_stars_white_24dp);
            movieFavouriteImageButton.setColorFilter(R.color.colorPrimaryDark);
        }
        movie.setFavourite(!movie.isFavourite());

    }
}
