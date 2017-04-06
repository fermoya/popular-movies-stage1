package com.example.fmoyader.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fmoyader.popularmovies.R;
import com.example.fmoyader.popularmovies.dto.Movie;
import com.example.fmoyader.popularmovies.network.MovieDBNetworkHelper;
import com.example.fmoyader.popularmovies.utils.PopularMoviesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements MovieDBNetworkHelper.MovieDBNetworkListener,
        PopularMoviesAdapter.EndOfListListener, PopularMoviesAdapter.RowListener {

    @BindView(R.id.rv_popular_movies_list)
    RecyclerView popularMoviesRecyclerView;
    @BindView(R.id.pb_loader)
    ProgressBar progressBar;

    private PopularMoviesAdapter popularMoviesAdapter;
    MovieSortOption sortOptionSelected;
    private MovieDBNetworkHelper movieDBNetworkHelper;
    private Menu mainMenu;
    private long moviesPage;

    public static final int ANIMATION_MILLIS = 2000;

    public enum MovieSortOption {
        BY_POPULARITY,
        BY_RATING;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //TODO: see the results of this suggested method
        popularMoviesRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns()));
        popularMoviesAdapter = new PopularMoviesAdapter(this, this, this);
        popularMoviesRecyclerView.setAdapter(popularMoviesAdapter);

        movieDBNetworkHelper = MovieDBNetworkHelper.getInstance();
        movieDBNetworkHelper.setMovieDBNetworkListener(this);
        progressBar.setVisibility(View.VISIBLE);

        moviesPage = 1;
        sortOptionSelected = MovieSortOption.BY_POPULARITY;

        setTitle(getString(R.string.main_activity_title));

        if (isNetworkAvailable()) {
            fetchMovies();
        } else {
            Toast.makeText(this, getString(R.string.no_internet_error_message), Toast.LENGTH_SHORT).show();
            hideLoader();
        }
    }

    private void startLoader() {
        popularMoviesRecyclerView.setVisibility(View.INVISIBLE);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
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
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void fetchMovies() {
        if (moviesPage == 1) {
            startLoader();
        }

        if (sortOptionSelected == MovieSortOption.BY_POPULARITY) {
            movieDBNetworkHelper.getPopularMovies(moviesPage);
        } else {
            movieDBNetworkHelper.getTopRatedMovies(moviesPage);
        }
    }

    @Override
    public void onResponse(Movie[] movies, long nextPage) {
        popularMoviesAdapter.addMovies(movies);
        if (moviesPage == 1) {
            hideLoader();
        }

        enableMenuItems();
        moviesPage = nextPage;
    }

    private void enableMenuItems() {
        MenuItem sortByPopularityMenuItem = mainMenu.findItem(R.id.action_sort_by_popularity);
        sortByPopularityMenuItem.setEnabled(true);
        MenuItem sortByRatingMenuItem = mainMenu.findItem(R.id.action_sort_by_rating);
        sortByRatingMenuItem.setEnabled(true);
    }

    @Override
    public void onFailure() {  }

    @Override
    public void onDispayLastElement() {
        fetchMovies();
    }

    @Override
    public void onClick(Movie movie) {
        Intent intentToDetailActivity = new Intent(this, DetailActivity.class);
        intentToDetailActivity.putExtra(DetailActivity.MOVIE_EXTRA, movie);
        startActivity(intentToDetailActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mainMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_sort_by_popularity) {
            if (sortOptionSelected != MovieSortOption.BY_POPULARITY) {
                sortOptionSelected = MovieSortOption.BY_POPULARITY;
                resetMoviesList();
                fetchMovies();
            }
            return true;
        } else if (itemId == R.id.action_sort_by_rating) {
            if (sortOptionSelected != MovieSortOption.BY_RATING) {
                sortOptionSelected = MovieSortOption.BY_RATING;
                resetMoviesList();
                fetchMovies();
            }
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
        moviesPage = 1;
        popularMoviesAdapter = new PopularMoviesAdapter(this, this, this);
        popularMoviesRecyclerView.setAdapter(popularMoviesAdapter);
    }
}
