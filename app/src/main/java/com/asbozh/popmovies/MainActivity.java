package com.asbozh.popmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String SORT_BY_POPULAR = "popular";
    private static final String SORT_BY_TOP_RATED = "top_rated";

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
                loadMovieData(SORT_BY_POPULAR);
            }
        });

        // set recycler view
        mRecyclerViewMovieList = (RecyclerView) findViewById(R.id.rv_movie_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        mRecyclerViewMovieList.setLayoutManager(gridLayoutManager);
        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerViewMovieList.setAdapter(mMovieAdapter);

        loadMovieData(SORT_BY_POPULAR);
    }

    private void loadMovieData(String sortBy) {
        showMovieDataView();
        if (isOnline()) {
            new FetchMovieDataTask().execute(sortBy);
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

    public class FetchMovieDataTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }
            String sortBy = strings[0];
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
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            mProgressBar.setVisibility(View.INVISIBLE);
            if (movies != null) {
                showMovieDataView();
                mMovieAdapter.setMovieData(movies);
            }
        }
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
            loadMovieData(SORT_BY_POPULAR);
            return true;
        }
        if (id == R.id.menu_sort_top_rated) {
            mMovieAdapter.setMovieData(null);
            loadMovieData(SORT_BY_TOP_RATED);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
