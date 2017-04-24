package com.example.fmoyader.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fmoyader.popularmovies.R;
import com.example.fmoyader.popularmovies.dto.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fmoyader on 30/3/17.
 */

public class MoviesAdapter
        extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>{

    private final Context context;
    private final List<Movie> movies;
    private final MoviesAdapter.RowListener rowListener;

    public interface EndOfListListener {
        void onDispayLastElement();
    }

    public interface RowListener {
        void onClick(Movie movie);
    }

    public MoviesAdapter(
            Context context, RowListener rowListener) {
        this.movies = new ArrayList<>();
        this.context = context;
        this.rowListener = rowListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        if (movies != null && !movies.isEmpty()) {
            Movie movie = movies.get(position);
            holder.bind(movie);
        }
    }

    public void addMovies(Movie[] newMovies) {
        this.movies.addAll(Arrays.asList(newMovies));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {

        private static final String BASE_URL = "http://image.tmdb.org/t/p/";
        private static final String SIZE = "w342";

        @BindView(R.id.iv_movie_poster)
        ImageView moviePosterImageView;

        @BindView(R.id.tv_movie_title_connection_error)
        TextView movieTitleTextView;

        public MovieViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            ButterKnife.bind(this, view);
        }

        public void bind (final Movie movie) {
            Picasso.with(context)
                    .load(BASE_URL + SIZE + movie.getPosterPath())
                    .into(moviePosterImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            movieTitleTextView.setVisibility(View.GONE);
                            moviePosterImageView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {
                            moviePosterImageView.setVisibility(View.GONE);
                            movieTitleTextView.setVisibility(View.VISIBLE);
                            movieTitleTextView.setText(movie.getOriginalTitle());
                        }
                    });
        }

        @Override
        public void onClick(View v) {
            rowListener.onClick(movies.get(getAdapterPosition()));
        }
    }

}
