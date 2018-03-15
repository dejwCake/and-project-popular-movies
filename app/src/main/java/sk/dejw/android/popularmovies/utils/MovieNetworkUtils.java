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

public final class MovieNetworkUtils {

    private static final String TAG = MovieNetworkUtils.class.getSimpleName();

    private static final String POPULAR_URL =
            "http://api.themoviedb.org/3/movie/popular";

    private static final String TOP_RATED_URL =
            "http://api.themoviedb.org/3/movie/top_rated";

    private static final String API_KEY = BuildConfig.MOVIE_DB_API_KEY;

    final static String API_KEY_PARAM = "api_key";

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
}