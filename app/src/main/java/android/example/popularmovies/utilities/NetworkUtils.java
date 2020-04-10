package android.example.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;
import android.example.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the TMDB servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String API_URL =
            "http://api.themoviedb.org/3/movie/";



    /* The language we want our API to return */
    private static final String language = "en-US";



    final static String API_PARAM = "api_key";
    final static String LANGUAGE_PARAM = "language";

    /**
     * Builds the URL used to retrieve popular movies from TMDB
     *
     * @return The URL to use to query the TMDB server.
     */
    public static URL buildUrl(String type) {
        String popularUrl = API_URL + type + "/";
        Uri builtUri = Uri.parse(popularUrl).buildUpon()
                .appendQueryParameter(API_PARAM, BuildConfig.TMDB_API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, language)
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

    public static URL buildTrailerUtrl(int id) {
        String trailerUrl = API_URL + id + "/videos";
        Uri builtUri = Uri.parse(trailerUrl).buildUpon()
                .appendQueryParameter(API_PARAM, BuildConfig.TMDB_API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildReviewUrl(int id) {
        String trailerUrl = API_URL + id + "/reviews";
        Uri builtUri = Uri.parse(trailerUrl).buildUpon()
                .appendQueryParameter(API_PARAM, BuildConfig.TMDB_API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        Log.v(TAG, "current url: "+ url);
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
}
