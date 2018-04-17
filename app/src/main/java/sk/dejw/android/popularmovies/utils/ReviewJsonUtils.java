package sk.dejw.android.popularmovies.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sk.dejw.android.popularmovies.models.Review;
import sk.dejw.android.popularmovies.models.Trailer;

public final class ReviewJsonUtils {

    public static Review[] getReviewsFromJson(Context context, String reviewsJsonStr)
            throws JSONException {

        final String REVIEW_RESULTS = "results";

        final String REVIEW_ID = "id";
        final String REVIEW_AUTHOR = "author";
        final String REVIEW_CONTENT = "content";
        final String REVIEW_URL = "url";

        /* String array to hold each day's weather String */
        Review[] reviews;

        JSONObject reviewsJson = new JSONObject(reviewsJsonStr);

        //TODO handle error

        JSONArray reviewsArray = reviewsJson.getJSONArray(REVIEW_RESULTS);

        reviews = new Review[reviewsArray.length()];

        for (int i = 0; i < reviewsArray.length(); i++) {
            /* Get the JSON object representing the day */
            JSONObject reviewJson = reviewsArray.getJSONObject(i);

            reviews[i] = new Review(
                reviewJson.getString(REVIEW_ID),
                reviewJson.getString(REVIEW_AUTHOR),
                reviewJson.getString(REVIEW_CONTENT),
                reviewJson.getString(REVIEW_URL)
            );
        }

        return reviews;
    }
}