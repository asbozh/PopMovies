package com.asbozh.popmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.asbozh.popmovies.data.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_poster_details) ImageView mDetailPosterImageView;
    @BindView(R.id.tv_movie_title) TextView mDetailTitleTextView;
    @BindView(R.id.tv_movie_release_date) TextView mDetailReleaseDateTextView;
    @BindView(R.id.tv_user_rating) TextView mDetailRatingTextView;
    @BindView(R.id.tv_movie_overview) TextView mDetailOverview;
    @BindView(R.id.cb_favourite_star) CheckBox mCheckBoxFavourite;
    @BindView(R.id.rv_trailers_list) RecyclerView mTrailersList;
    @BindView(R.id.rv_reviews_list) RecyclerView mReviewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle(getString(R.string.movie_details));
        ButterKnife.bind(this);
        Intent receivedIntent = getIntent();
        if (receivedIntent != null && receivedIntent.hasExtra("movie")) {
            Movie movie = receivedIntent.getParcelableExtra("movie");
            mDetailTitleTextView.setText(movie.getMovieTitle());
            mDetailReleaseDateTextView.setText(movie.getMovieReleaseDate());
            mDetailRatingTextView.setText(String.valueOf(movie.getMovieRating()));
            mDetailOverview.setText(movie.getMovieOverview());
            Picasso.with(this).load(movie.getMoviePoster()).into(mDetailPosterImageView);
        }
    }
}
