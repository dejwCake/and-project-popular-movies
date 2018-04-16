package sk.dejw.android.popularmovies.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sk.dejw.android.popularmovies.models.Movie;

public final class MovieJsonUtils {

    public static Movie[] getMoviesFromJson(Context context, String moviesJsonStr)
            throws JSONException {

        final String MOVIE_RESULTS = "results";

        final String MOVIE_ID = "id";
        final String MOVIE_TITLE = "title";
        final String MOVIE_POSTER_PATH = "poster_path";
        final String MOVIE_PLOT = "overview";
        final String MOVIE_RATING = "vote_average";
        final String MOVIE_RELEASE_DATE = "release_date";

        /* String array to hold each day's weather String */
        Movie[] movies;

        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        //TODO handle error

        JSONArray moviesArray = moviesJson.getJSONArray(MOVIE_RESULTS);

        movies = new Movie[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {
            /* Get the JSON object representing the day */
            JSONObject movieJson = moviesArray.getJSONObject(i);

            Date releaseDate;
            try {
                releaseDate = (new SimpleDateFormat()).parse(movieJson.getString(MOVIE_RELEASE_DATE));
            } catch (ParseException e) {
                releaseDate = Calendar.getInstance().getTime();
            }

            movies[i] = new Movie(
                movieJson.getInt(MOVIE_ID),
                movieJson.getString(MOVIE_TITLE),
                movieJson.getString(MOVIE_POSTER_PATH),
                movieJson.getString(MOVIE_PLOT),
                movieJson.getDouble(MOVIE_RATING),
                releaseDate
            );
        }

        return movies;
    }
}