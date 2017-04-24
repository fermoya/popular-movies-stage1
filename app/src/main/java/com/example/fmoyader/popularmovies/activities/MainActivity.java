package com.example.fmoyader.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fmoyader.popularmovies.R;
import com.example.fmoyader.popularmovies.adapters.MovieSpinnerAdapter;
import com.example.fmoyader.popularmovies.adapters.MoviesAdapter;
import com.example.fmoyader.popularmovies.dto.Movie;
import com.example.fmoyader.popularmovies.dto.MovieReview;
import com.example.fmoyader.popularmovies.dto.MovieTrailer;
import com.example.fmoyader.popularmovies.enums.MovieSortingMode;
import com.example.fmoyader.popularmovies.network.MovieDispatcher;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements MovieDispatcher.MovieDispatcherListener,
        MoviesAdapter.RowListener,
        AdapterView.OnItemSelectedListener {

    @BindView(R.id.rv_popular_movies_list)
    RecyclerView popularMoviesRecyclerView;
    @BindView(R.id.pb_loader)
    ProgressBar progressBar;
    @BindView(R.id.sp_sort_mode)
    Spinner sortModeSpinner;

    private MoviesAdapter popularMoviesAdapter;
    private MovieSortingMode sortingMode;
    private MovieDispatcher movieDispatcher;
    private long moviePage;

    public static final int ANIMATION_MILLIS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns());
        popularMoviesRecyclerView.setLayoutManager(layoutManager);
        popularMoviesAdapter = new MoviesAdapter(this, this);
        popularMoviesRecyclerView.setAdapter(popularMoviesAdapter);

        popularMoviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    fetchMovies();
                }
            }
        });

        movieDispatcher = MovieDispatcher.getInstance();
        movieDispatcher.initialize(this, this);
        progressBar.setVisibility(View.VISIBLE);

        moviePage = 1;
        findMovieSortingMode();

        setTitle(getString(R.string.main_activity_title));

        if (isNetworkAvailable()) {
            fetchMovies();
        } else {
            Toast.makeText(this, getString(R.string.no_internet_error_message), Toast.LENGTH_SHORT).show();
            hideLoader();
        }


        ArrayAdapter<String> spinnerAdapter = new MovieSpinnerAdapter(
                this,
                R.layout.sorting_mode_spinner,
                Arrays.asList(getResources().getStringArray(R.array.sp_entries))
        );
        spinnerAdapter.setDropDownViewResource(R.layout.sorting_mode_dropdown_spinner);
        sortModeSpinner.setAdapter(spinnerAdapter);

        sortModeSpinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (popularMoviesAdapter.getItemCount() > 0) {
            hideLoader();
        }
    }

    private void findMovieSortingMode() {
        sortingMode = MovieSortingMode.POPULARITY;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortByOption = sharedPreferences.getString(
                getString(R.string.pref_sort_by_list_key),
                getString(R.string.action_sort_by_popularity)
        );

        updateMovieSortingMode(sortByOption);
    }

    private void updateMovieSortingMode(String sortByOption) {
        if (sortByOption.equalsIgnoreCase(MovieSortingMode.POPULARITY.name())) {
            sortingMode = MovieSortingMode.POPULARITY;
            sortModeSpinner.setSelection(0);
        } else {
            sortingMode = MovieSortingMode.RATING;
            sortModeSpinner.setSelection(1);
        }
    }

    private void startLoader() {
        popularMoviesRecyclerView.setVisibility(View.INVISIBLE);
        popularMoviesRecyclerView.setVisibility(View.INVISIBLE);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 300;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    private void hideLoader() {
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(ANIMATION_MILLIS);
        fadeOut.setRepeatCount(0);
        fadeOut.setRepeatMode(Animation.INFINITE);
        fadeOut.setFillAfter(true);
        fadeOut.setFillEnabled(true);
        progressBar.startAnimation(fadeOut);

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(ANIMATION_MILLIS);
        fadeIn.setRepeatCount(0);
        fadeIn.setFillAfter(true);
        fadeIn.setFillEnabled(true);
        fadeIn.setRepeatMode(Animation.INFINITE);
        popularMoviesRecyclerView.startAnimation(fadeIn);
        sortModeSpinner.startAnimation(fadeIn);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void fetchMovies() {
        if (moviePage == 1) {
            startLoader();
        }

        if (sortingMode == MovieSortingMode.POPULARITY) {
            movieDispatcher.requestPopularMovies(moviePage);
        } else {
            movieDispatcher.requestTopRatedMovies(moviePage);
        }
    }

    @Override
    public void onMoviesResponse(Movie[] movies, long nextPage) {
        if (moviePage == nextPage) {
            return;
        }

        popularMoviesAdapter.addMovies(movies);
        if (moviePage == 1) {
            hideLoader();
        }

        moviePage = nextPage;
    }

    @Override
    public void onMovieTrailersResponse(MovieTrailer[] movieTrailers) {

    }

    @Override
    public void onMovieReviewsResponse(MovieReview[] movieReviews, long nextPage) {

    }

    @Override
    public void onFailure() {  }

    @Override
    public void onClick(Movie movie) {
        Intent intentToDetailActivity = new Intent(this, DetailActivity.class);
        intentToDetailActivity.putExtra(DetailActivity.MOVIE_EXTRA, movie);
        startActivity(intentToDetailActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_settings) {
            Intent intentToSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(intentToSettingsActivity);
            return true;
        } else if (itemId == R.id.action_refresh) {
            if (isNetworkAvailable()) {
                startLoader();
                fetchMovies();
            } else {
                Toast.makeText(this, getString(R.string.no_internet_error_message), Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetMoviesList() {
        moviePage = 1;
        popularMoviesAdapter = new MoviesAdapter(this, this);
        popularMoviesRecyclerView.setAdapter(popularMoviesAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                sortingMode = MovieSortingMode.POPULARITY;
                break;
            case 1:
                sortingMode = MovieSortingMode.RATING;
                break;
            case 2:
            default:
                sortingMode = MovieSortingMode.FAVOURITE;
                break;
        }

        resetMoviesList();
        fetchMovies();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
}
