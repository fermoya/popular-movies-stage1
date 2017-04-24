package com.example.fmoyader.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fmoyader.popularmovies.R;
import com.example.fmoyader.popularmovies.dto.MovieReview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fmoyader on 24/4/17.
 */

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewViewHolder> {

    private final List<MovieReview> movieReviews;

    public MovieReviewsAdapter(MovieReview[] movieReviews) {
        this.movieReviews = new ArrayList<>();
    }

    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            default:
            case 0:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.movie_review_left, parent, false);
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.movie_review_right, parent, false);
                break;
        }

        return new MovieReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewViewHolder holder, int position) {
        holder.bind(movieReviews.get(position));
    }

    public void addReviews(MovieReview[] newReviews) {
        if (newReviews != null) {
            this.movieReviews.addAll(Arrays.asList(newReviews));
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    @Override
    public int getItemCount() {
        return movieReviews == null ? 0 : movieReviews.size();
    }

    public class MovieReviewViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_movie_review)
        TextView movieReviewTextView;

        public MovieReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(MovieReview movieReview) {
            movieReviewTextView.setText(movieReview.getContent());
        }
    }
}
