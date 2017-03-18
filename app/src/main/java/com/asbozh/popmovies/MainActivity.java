package com.asbozh.popmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asbozh.popmovies.data.Movie;
import com.asbozh.popmovies.utilities.MoviesJsonUtils;
import com.asbozh.popmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String SORT_BY_POPULAR = "popular";
    private static final String SORT_BY_TOP_RATED = "top_rated";

    private static final int MOVIE_LOADER_ID = 111;
    private static final String MOVIE_SORT_TAG = "sort_by";

    private RecyclerView mRecyclerViewMovieList;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageTextView;
    private ProgressBar mProgressBar;
    private ImageButton mRetryImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find views
        mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mRetryImageButton = (ImageButton) findViewById(R.id.ib_retry);
        mRetryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMovieData(SORT_BY_POPULAR, true);
            }
        });

        // set recycler view
        mRecyclerViewMovieList = (RecyclerView) findViewById(R.id.rv_movie_list);
        GridLayoutManager gridLayoutManager;
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            gridLayoutManager = new GridLayoutManager(MainActivity.this, 3);
        } else {
            gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        }
        mRecyclerViewMovieList.setLayoutManager(gridLayoutManager);
        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerViewMovieList.setAdapter(mMovieAdapter);

        loadMovieData(SORT_BY_POPULAR, false);
    }

    private void loadMovieData(String sortBy, boolean restartLoader) {
        showMovieDataView();
        if (isOnline()) {
            Bundle loaderBundle = new Bundle();
            loaderBundle.putString(MOVIE_SORT_TAG, sortBy);
            if (restartLoader) {
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, loaderBundle, this);
            } else {
                getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, loaderBundle, this);
            }
        } else {
            showErrorMessage();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void showMovieDataView() {
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mRetryImageButton.setVisibility(View.INVISIBLE);
        mRecyclerViewMovieList.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerViewMovieList.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mRetryImageButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie selectedMovie) {
        Intent intentToStartDetailActivity = new Intent(this, DetailActivity.class);
        intentToStartDetailActivity.putExtra("movie", selectedMovie);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this) {

            List<Movie> mMovieData = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else if (args != null){
                    mProgressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public List<Movie> loadInBackground() {
                String sortBy = args.getString(MOVIE_SORT_TAG);

                if (sortBy == null || TextUtils.isEmpty(sortBy)) {
                    return null;
                }
                URL moviesRequestUrl = NetworkUtils.buildUrl(sortBy);
                try {
                    String jsonMoviesResponse = NetworkUtils
                            .getResponseFromHttpUrl(moviesRequestUrl);

                    return MoviesJsonUtils.getMoviesListStringsFromJson(jsonMoviesResponse);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(List<Movie> data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (movies != null) {
            showMovieDataView();
            mMovieAdapter.setMovieData(movies);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        loader = null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_sort_popular) {
            mMovieAdapter.setMovieData(null);
            loadMovieData(SORT_BY_POPULAR, true);
            return true;
        }
        if (id == R.id.menu_sort_top_rated) {
            mMovieAdapter.setMovieData(null);
            loadMovieData(SORT_BY_TOP_RATED, true);
            return true;
        }
        if (id == R.id.menu_favourites) {
            //TODO favourite movies
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
