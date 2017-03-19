package com.asbozh.popmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asbozh.popmovies.data.Movie;
import com.asbozh.popmovies.data.Review;
import com.asbozh.popmovies.data.Trailer;
import com.asbozh.popmovies.database.FavouriteMoviesContract;
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

    private static final int TRAILER_LOADER_ID = 222;
    private static final int REVIEW_LOADER_ID = 333;

    private int movieId;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle(getString(R.string.movie_details));
        ButterKnife.bind(this);
        Intent receivedIntent = getIntent();
        if (receivedIntent != null && receivedIntent.hasExtra("movie")) {
            movie = receivedIntent.getParcelableExtra("movie");
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
            getSupportLoaderManager().initLoader(TRAILER_LOADER_ID, null, new TrailerCallback(this));
            getSupportLoaderManager().initLoader(REVIEW_LOADER_ID, null, new ReviewCallback(this));
        }
        // Set the Checkbox depending of the movie status (is it presented in the db)
        String stringId = Integer.toString(movieId);
        Uri uri = FavouriteMoviesContract.FavouriteMoviesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        Cursor checkCursor = getContentResolver().query(uri, null, null, null, null);
        if (checkCursor != null && checkCursor.getCount() > 0) {
            mCheckBoxFavourite.setChecked(true);
        } else {
            mCheckBoxFavourite.setChecked(false);
        }
        mCheckBoxFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckBoxFavourite.isChecked()) {

                    // Add to Favourite Movies
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(FavouriteMoviesContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID, movieId);
                    contentValues.put(FavouriteMoviesContract.FavouriteMoviesEntry.COLUMN_TITLE, movie.getMovieTitle());
                    contentValues.put(FavouriteMoviesContract.FavouriteMoviesEntry.COLUMN_POSTER, movie.getMoviePoster());
                    contentValues.put(FavouriteMoviesContract.FavouriteMoviesEntry.COLUMN_OVERVIEW, movie.getMovieOverview());
                    contentValues.put(FavouriteMoviesContract.FavouriteMoviesEntry.COLUMN_RATING, movie.getMovieRating());
                    contentValues.put(FavouriteMoviesContract.FavouriteMoviesEntry.COLUMN_RELEASE_DATE, movie.getMovieReleaseDate());

                    Uri uri = getContentResolver().insert(FavouriteMoviesContract.FavouriteMoviesEntry.CONTENT_URI, contentValues);

                    Toast.makeText(getBaseContext(), movie.getMovieTitle() + " has been added to Favourite Movies", Toast.LENGTH_LONG).show();

                } else {
                    // Remove from Favourite Movies

                    String stringId = Integer.toString(movieId);
                    Uri uri = FavouriteMoviesContract.FavouriteMoviesEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(stringId).build();

                    getContentResolver().delete(uri, null, null);
                    Toast.makeText(getBaseContext(), movie.getMovieTitle() + " has been removed from Favourite Movies", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onTrailerClick(Trailer selectedTrailer) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + selectedTrailer.getTrailerKey())));
    }

    private class TrailerCallback implements LoaderManager.LoaderCallbacks<List<Trailer>> {

        private Context context;

        private TrailerCallback(Context context) {
            this.context = context;
        }

        @Override
        public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<List<Trailer>>(context) {

                List<Trailer> mTrailerData = null;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    if (mTrailerData != null) {
                        deliverResult(mTrailerData);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public List<Trailer> loadInBackground() {
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
                public void deliverResult(List<Trailer> data) {
                    mTrailerData = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> trailers) {
            if (trailers != null) {
                mTrailerAdapter.setTrailerData(trailers);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {
            loader = null;
        }
    }

    private class ReviewCallback implements LoaderManager.LoaderCallbacks<List<Review>> {

        private Context context;

        private ReviewCallback(Context context) {
            this.context = context;
        }

        @Override
        public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<List<Review>>(context) {

                List<Review> mReviewData = null;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    if (mReviewData != null) {
                        deliverResult(mReviewData);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public List<Review> loadInBackground() {
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
                public void deliverResult(List<Review> data) {
                    mReviewData = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<Review>> loader, List<Review> reviews) {
            if (reviews != null) {
                mReviewAdapter.setReviewData(reviews);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Review>> loader) {
            loader = null;
        }
    }
}
