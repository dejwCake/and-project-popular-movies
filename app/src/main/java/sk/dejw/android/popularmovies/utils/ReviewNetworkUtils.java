package sk.dejw.android.popularmovies.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import sk.dejw.android.popularmovies.BuildConfig;

public final class ReviewNetworkUtils extends TheMovieDbNetworkUtils {

    private static final String TAG = ReviewNetworkUtils.class.getSimpleName();

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie";

    private static final String RESOURCE = "reviews";

    public static URL buildUrl(Integer movieId, Context context) {
        Uri builtUri = Uri.parse(BASE_URL);
        builtUri = builtUri.buildUpon()
                .appendPath(movieId.toString())
                .appendPath(RESOURCE)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
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
}