package android.example.popularmovies.utilities;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class TMDBJsonUtils {
    private static final String TAG = TMDBJsonUtils.class.getSimpleName();
    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the list of movies
     *
     * @param movieJsonStr JSON response from server
     *
     * @return Array of Strings describing movie data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static Movie[] getMoviesFromJson(Context context, String movieJsonStr)
            throws JSONException {

        final String ID = "id";
        final String RESULT_LIST = "results";
        final String TITLE = "title";
        final String RELEASE_DATE = "release_date";
        final String POSTER_PATH = "poster_path";
        final String VOTE_AVERAGE = "vote_count";
        final String SYNOPSIS = "overview";

        // Keys for error message
        final String STATUS_CODE = "status_code";
        final String STATUS_MESSAGE = "status_message";

        Movie[] parsedMovieData = null;

        JSONObject movieJson = new JSONObject(movieJsonStr);

        if (movieJson.has(STATUS_CODE)) {
            int errorCode = movieJson.getInt(STATUS_CODE);

            return null;
        }

        JSONArray movieArray = movieJson.getJSONArray(RESULT_LIST);

        parsedMovieData = new Movie[movieArray.length()];


        for (int i = 0; i < movieArray.length(); i++) {
            int id;
            String title;
            String posterPath;
            String releaseDate;
            int voteCount;
            String synopsis;


            JSONObject movie = movieArray.getJSONObject(i);
            id = movie.getInt(ID);
            title = movie.getString(TITLE);
            posterPath = movie.getString(POSTER_PATH);
            releaseDate = movie.getString(RELEASE_DATE);
            voteCount = movie.getInt(VOTE_AVERAGE);
            synopsis = movie.getString(SYNOPSIS);


            parsedMovieData[i] = new Movie(id, title, posterPath, releaseDate, voteCount, synopsis);
        }

        return parsedMovieData;
    }

    public static ArrayList<Trailer> getTrailersFromJson(Context context, String trailerJsonStr)
            throws JSONException {

        final String RESULT_LIST = "results";
        final String NAME = "name";
        final String SITE = "site";
        final String TYPE = "type";
        final String KEY = "key";

        // Keys for error message
        final String STATUS_CODE = "status_code";
        final String STATUS_MESSAGE = "status_message";

        ArrayList<Trailer> parsedTrailerData = null;

        JSONObject trailerJson = new JSONObject(trailerJsonStr);

        if (trailerJson.has(STATUS_CODE)) {
            int errorCode = trailerJson.getInt(STATUS_CODE);

            return null;
        }

        JSONArray trailerArray = trailerJson.getJSONArray(RESULT_LIST);

        parsedTrailerData = new ArrayList<>();
        Log.v(TAG, trailerArray.toString());

        for (int i = 0; i < trailerArray.length(); i++) {
            String name;
            String key;
            String site;



            JSONObject trailer = trailerArray.getJSONObject(i);
            if (trailer.getString(TYPE).trim().equals("Trailer")) {
                name = trailer.getString(NAME);
                key = trailer.getString(KEY);
                site = trailer.getString(SITE);


                parsedTrailerData.add(new Trailer(name, site, key));
            }

        }

        return parsedTrailerData;
    }

    public static Review[] getReviewsFromJson(Context context, String reviewJsonStr)
            throws JSONException {
        final String RESULT_LIST = "results";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String URL = "url";

        // Keys for error message
        final String STATUS_CODE = "status_code";
        final String STATUS_MESSAGE = "status_message";

        Review[] parsedReviewData = null;

        JSONObject reviewJson = new JSONObject(reviewJsonStr);

        if (reviewJson.has(STATUS_CODE)) {
            int errorCode = reviewJson.getInt(STATUS_CODE);

            return null;
        }

        JSONArray reviewArray = reviewJson.getJSONArray(RESULT_LIST);

        parsedReviewData = new Review[reviewArray.length()];


        for (int i = 0; i < reviewArray.length(); i++) {
            String author;
            String content;
            String link;


            JSONObject review = reviewArray.getJSONObject(i);
            author = review.getString(AUTHOR);
            content = review.getString(CONTENT);
            link = review.getString(URL);


            parsedReviewData[i] = new Review(author, content, link);
        }

        return parsedReviewData;
    }
}
