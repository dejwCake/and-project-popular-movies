package sk.dejw.android.popularmovies.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sk.dejw.android.popularmovies.models.Movie;

public final class MovieJsonUtils {

    public static Movie[] getMoviesFromJson(Context context, String moviesJsonStr)
            throws JSONException {

        final String MOVIE_RESULTS = "results";

        final String MOVIE_TITLE = "title";
        final String MOVIE_POSTER_PATH = "poster_path";

        /* String array to hold each day's weather String */
        Movie[] movies = null;

        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        //TODO handle error

        JSONArray moviesArray = moviesJson.getJSONArray(MOVIE_RESULTS);

        movies = new Movie[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {
            /* Get the JSON object representing the day */
            JSONObject movieJson = moviesArray.getJSONObject(i);

            movies[i] = new Movie(movieJson.getString(MOVIE_TITLE),
                    movieJson.getString(MOVIE_POSTER_PATH));
        }

        return movies;
    }
}