package com.asbozh.popmovies.utilities;


import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String TRAILER_URL = "videos";
    private static final String REVIEW_URL = "reviews";
    private static final String API_QUERY = "api_key";
    
    private static final String API_KEY = "77f04ead68e7a0cfa2104d1d196a5010";

    public static URL buildUrl(String sortByQuery) {
        String startURL = MOVIES_BASE_URL + "/" + sortByQuery;
        Uri builtUri = Uri.parse(startURL).buildUpon()
                .appendQueryParameter(API_QUERY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static URL buildTrailersUrl(int movieId) {
        String startURL = MOVIES_BASE_URL + "/" + movieId + "/" + TRAILER_URL;
        Uri builtUri = Uri.parse(startURL).buildUpon()
                .appendQueryParameter(API_QUERY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI for trailers " + url);

        return url;
    }

    public static URL buildReviewsUrl(int movieId) {
        String startURL = MOVIES_BASE_URL + "/" + movieId + "/" + REVIEW_URL;
        Uri builtUri = Uri.parse(startURL).buildUpon()
                .appendQueryParameter(API_QUERY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI for reviews " + url);

        return url;
    }
}
