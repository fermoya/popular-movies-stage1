package com.example.fmoyader.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.fmoyader.popularmovies.R;
import com.example.fmoyader.popularmovies.dto.MovieTrailer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fmoyader on 24/4/17.
 */

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.MovieTrailerViewHolder> {


    private static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";
    private final Context context;
    private final MovieTrailer[] movieTrailers;

    public MovieTrailersAdapter(Context context, MovieTrailer[] movieTrailers) {
        this.context = context;
        this.movieTrailers = movieTrailers;
    }

    @Override
    public MovieTrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_trailer, parent, false);
        return new MovieTrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailerViewHolder holder, int position) {
        if (movieTrailers != null && movieTrailers.length > 0) {
            holder.bind(movieTrailers[position]);
        }
    }

    @Override
    public int getItemCount() {
        return movieTrailers == null ? 0 : movieTrailers.length;
    }

    class MovieTrailerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ib_movie_trailer)
        ImageButton movieTrailerImageButton;
        @BindView(R.id.tv_trailer_title)
        TextView movieTrailerTitleTextView;

        public MovieTrailerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            movieTrailerImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int trailerIndex = MovieTrailerViewHolder.this.getAdapterPosition();
                    MovieTrailer movieTrailer = movieTrailers[trailerIndex];

                    Uri trailerUri = Uri.parse(YOUTUBE_URL + movieTrailer.getKey());
                    Intent intentToYoutube = new Intent(Intent.ACTION_VIEW, trailerUri);
                    context.startActivity(intentToYoutube);
                }
            });
        }

        public void bind(MovieTrailer movieTrailer) {
            movieTrailerTitleTextView.setText(movieTrailer.getName());
        }
    }

    public MovieTrailer[] getItems() {
        return movieTrailers;
    }
}
