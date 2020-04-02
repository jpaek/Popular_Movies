package android.example.popularmovies.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class TMDBJsonUtils {
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

        final String RESULT_LIST = "results";
        final String TITLE = "title";
        final String RELEASE_DATE = "release_date";
        final String POSTER_PATH = "poster_path";
        final String VOTE_AVERAGE = "vote_count";
        final String SYNOPSIS = "overview";

        // Keys for error message
        final String STATUS_CODE = "status_code";
        final String STATUS_MESSAGE = "status_message";

        /* String array to hold each day's weather String */
        Movie[] parsedMovieData = null;

        JSONObject movieJson = new JSONObject(movieJsonStr);

        /* Is there an error? */
        if (movieJson.has(STATUS_CODE)) {
            int errorCode = movieJson.getInt(STATUS_CODE);

            return null;
        }

        JSONArray movieArray = movieJson.getJSONArray(RESULT_LIST);

        parsedMovieData = new Movie[movieArray.length()];


        for (int i = 0; i < movieArray.length(); i++) {
            String title;
            String posterPath;
            String releaseDate;
            int voteCount;
            String synopsis;


            /* Get the JSON object representing the day */
            JSONObject movie = movieArray.getJSONObject(i);
            title = movie.getString(TITLE);
            posterPath = movie.getString(POSTER_PATH);
            releaseDate = movie.getString(RELEASE_DATE);
            voteCount = movie.getInt(VOTE_AVERAGE);
            synopsis = movie.getString(SYNOPSIS);


            parsedMovieData[i] = new Movie(title, posterPath, releaseDate, voteCount, synopsis);
        }

        return parsedMovieData;
    }
}
