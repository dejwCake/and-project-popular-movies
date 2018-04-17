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
import sk.dejw.android.popularmovies.R;

public final class MovieNetworkUtils extends TheMovieDbNetworkUtils {

    private static final String TAG = MovieNetworkUtils.class.getSimpleName();

    private static final String POPULAR_URL =
            "http://api.themoviedb.org/3/movie/popular";

    private static final String TOP_RATED_URL =
            "http://api.themoviedb.org/3/movie/top_rated";

    public static URL buildUrl(String sortBy, Context context) {
        Uri builtUri = Uri.parse(POPULAR_URL);
        if(sortBy.equals(context.getString(R.string.pref_sort_top_rated))) {
            builtUri = Uri.parse(TOP_RATED_URL);
        }
        builtUri = builtUri.buildUpon()
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