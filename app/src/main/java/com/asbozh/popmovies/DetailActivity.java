package com.asbozh.popmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.asbozh.popmovies.data.Movie;
import com.asbozh.popmovies.data.Review;
import com.asbozh.popmovies.data.Trailer;
import com.asbozh.popmovies.utilities.MoviesJsonUtils;
import com.asbozh.popmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {

    @BindView(R.id.iv_poster_details) ImageView mDetailPosterImageView;
    @BindView(R.id.tv_movie_title) TextView mDetailTitleTextView;
    @BindView(R.id.tv_movie_release_date) TextView mDetailReleaseDateTextView;
    @BindView(R.id.tv_user_rating) TextView mDetailRatingTextView;
    @BindView(R.id.tv_movie_overview) TextView mDetailOverview;
    @BindView(R.id.cb_favourite_star) CheckBox mCheckBoxFavourite;
    @BindView(R.id.rv_trailers_list) RecyclerView mTrailersList;
    @BindView(R.id.rv_reviews_list) RecyclerView mReviewsList;

    private int movieId;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

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
            movieId = movie.getMovieId();
            mTrailerAdapter = new TrailerAdapter(this, this);
            RecyclerView.LayoutManager mTrailersLayoutManager = new LinearLayoutManager(getApplicationContext());
            mTrailersList.setLayoutManager(mTrailersLayoutManager);
            mTrailersList.setAdapter(mTrailerAdapter);
            mReviewAdapter = new ReviewAdapter(this);
            RecyclerView.LayoutManager mReviewsLayoutManager = new LinearLayoutManager(getApplicationContext());
            mReviewsList.setLayoutManager(mReviewsLayoutManager);
            mReviewsList.setAdapter(mReviewAdapter);
            new FetchTrailerDataTask().execute(movieId);
            new FetchReviewDataTask().execute(movieId);
        }
    }

    @Override
    public void onTrailerClick(Trailer selectedTrailer) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + selectedTrailer.getTrailerKey())));
    }

    public class FetchTrailerDataTask extends AsyncTask<Integer, Void, List<Trailer>> {

        @Override
        protected List<Trailer> doInBackground(Integer... params) {
            if (params.length == 0) {
                return null;
            }
            int movieId = params[0];

            URL trailersRequestUrl = NetworkUtils.buildTrailersUrl(movieId);

            try {
                String jsonTrailersResponse = NetworkUtils
                        .getResponseFromHttpUrl(trailersRequestUrl);

                return MoviesJsonUtils.getTrailersListStringsFromJson(jsonTrailersResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            super.onPostExecute(trailers);
            if (trailers != null) {
                mTrailerAdapter.setTrailerData(trailers);
            }
        }
    }

    public class  FetchReviewDataTask extends AsyncTask<Integer, Void, List<Review>> {

        @Override
        protected List<Review> doInBackground(Integer... params) {
            if (params.length == 0) {
                return null;
            }
            int movieId = params[0];

            URL reviewsRequestUrl = NetworkUtils.buildReviewsUrl(movieId);

            try {
                String jsonReviewsResponse = NetworkUtils
                        .getResponseFromHttpUrl(reviewsRequestUrl);

                return MoviesJsonUtils.getReviewsListStringsFromJson(jsonReviewsResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            super.onPostExecute(reviews);
            if (reviews != null) {
                mReviewAdapter.setReviewData(reviews);
            }
        }
    }
}
