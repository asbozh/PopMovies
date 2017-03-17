package com.asbozh.popmovies.utilities;

import com.asbozh.popmovies.data.Movie;
import com.asbozh.popmovies.data.Review;
import com.asbozh.popmovies.data.Trailer;

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
        final String MOVIE_ID = "id";

        List<Movie> parsedMovieData = new ArrayList<>();

        JSONObject movieJson = new JSONObject(jsonMoviesResponse);

        JSONArray movieListArray = movieJson.getJSONArray(MOVIE_LIST);

        for (int i = 0; i < movieListArray.length(); i++) {
            String title;
            String poster_path;
            String overview;
            double vote_average;
            String release_date;
            int id;

            JSONObject movie = movieListArray.getJSONObject(i);

            title = movie.getString(MOVIE_TITLE);
            poster_path = POSTER_BASE_URL + movie.getString(MOVIE_POSTER);
            overview = movie.getString(MOVIE_OVERVIEW);
            vote_average = movie.getDouble(MOVIE_VOTE_AVERAGE);
            release_date = movie.getString(MOVIE_RELEASE_DATE);
            id = movie.getInt(MOVIE_ID);

            Movie movieToAdd = new Movie(title, poster_path, overview, vote_average, release_date, id);
            parsedMovieData.add(movieToAdd);
        }
        return parsedMovieData;
    }

    public static List<Trailer> getTrailersListStringsFromJson(String jsonTrailersResponse) throws JSONException {
        final String TRAILER_LIST = "results";
        final String TRAILER_NAME = "name";
        final String TRAILER_KEY = "key";

        List<Trailer> parsedTrailerData = new ArrayList<>();

        JSONObject trailerJson = new JSONObject(jsonTrailersResponse);
        JSONArray trailerListArray = trailerJson.getJSONArray(TRAILER_LIST);

        for (int i = trailerListArray.length() - 1; i >= 0; i--) {
            String name;
            String key;

            JSONObject trailer = trailerListArray.getJSONObject(i);

            name = trailer.getString(TRAILER_NAME);
            key = trailer.getString(TRAILER_KEY);

            Trailer trailerToAdd = new Trailer(name, key);
            parsedTrailerData.add(trailerToAdd);
        }
        return parsedTrailerData;
    }

    public static List<Review> getReviewsListStringsFromJson(String jsonReviewsResponse) throws JSONException {
        final String REVIEW_LIST = "results";
        final String REVIEW_AUTHOR = "author";
        final String REVIEW_CONTENT = "content";

        List<Review> parsedReviewData = new ArrayList<>();

        JSONObject reviewJson = new JSONObject(jsonReviewsResponse);
        JSONArray reviewListArray = reviewJson.getJSONArray(REVIEW_LIST);

        for (int i = reviewListArray.length() - 1; i >= 0; i--) {
            String author;
            String content;

            JSONObject review = reviewListArray.getJSONObject(i);

            author = review.getString(REVIEW_AUTHOR);
            content = review.getString(REVIEW_CONTENT);

            Review reviewToAdd = new Review(author, content);
            parsedReviewData.add(reviewToAdd);
        }
        return parsedReviewData;
    }
}
