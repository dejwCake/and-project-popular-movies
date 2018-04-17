package sk.dejw.android.popularmovies.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import sk.dejw.android.popularmovies.BuildConfig;

public abstract class TheMovieDbNetworkUtils {

    protected static final String API_KEY = BuildConfig.MOVIE_DB_API_KEY;

    final static String API_KEY_PARAM = "api_key";

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