package com.asbozh.popmovies.utilities;

import com.asbozh.popmovies.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class MoviesJsonUtils {

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";


    public static List<Movie> getMoviesListStringsFromJson(String jsonMoviesResponse) throws JSONException {
        final String MOVIE_LIST = "results";

        final String MOVIE_TITLE = "title";
        final String MOVIE_POSTER = "poster_path";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_VOTE_AVERAGE = "vote_average";
        final String MOVIE_RELEASE_DATE = "release_date";

        List<Movie> parsedMovieData = new ArrayList<>();

        JSONObject movieJson = new JSONObject(jsonMoviesResponse);

        JSONArray movieListArray = movieJson.getJSONArray(MOVIE_LIST);

        for (int i = 0; i < movieListArray.length(); i++) {
            String title;
            String poster_path;
            String overview;
            double vote_average;
            String release_date;

            JSONObject movie = movieListArray.getJSONObject(i);

            title = movie.getString(MOVIE_TITLE);
            poster_path = POSTER_BASE_URL + movie.getString(MOVIE_POSTER);
            overview = movie.getString(MOVIE_OVERVIEW);
            vote_average = movie.getDouble(MOVIE_VOTE_AVERAGE);
            release_date = movie.getString(MOVIE_RELEASE_DATE);

            Movie movieToAdd = new Movie(title, poster_path, overview, vote_average, release_date);
            parsedMovieData.add(movieToAdd);
        }
        return parsedMovieData;
    }
}
