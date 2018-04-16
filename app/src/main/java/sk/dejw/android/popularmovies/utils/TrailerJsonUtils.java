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
import sk.dejw.android.popularmovies.models.Trailer;

public final class TrailerJsonUtils {

    public static Trailer[] getTrailersFromJson(Context context, String trailersJsonStr)
            throws JSONException {

        final String TRAILER_RESULTS = "results";

        final String TRAILER_ID = "id";
        final String TRAILER_NAME = "name";
        final String TRAILER_KEY = "key";

        /* String array to hold each day's weather String */
        Trailer[] trailers;

        JSONObject trailersJson = new JSONObject(trailersJsonStr);

        //TODO handle error

        JSONArray trailersArray = trailersJson.getJSONArray(TRAILER_RESULTS);

        trailers = new Trailer[trailersArray.length()];

        for (int i = 0; i < trailersArray.length(); i++) {
            /* Get the JSON object representing the day */
            JSONObject movieJson = trailersArray.getJSONObject(i);

            trailers[i] = new Trailer(
                movieJson.getString(TRAILER_ID),
                movieJson.getString(TRAILER_NAME),
                movieJson.getString(TRAILER_KEY)
            );
        }

        return trailers;
    }
}