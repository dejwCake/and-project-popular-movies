package sk.dejw.android.popularmovies.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import sk.dejw.android.popularmovies.R;

public final class MoviePreferences {

    public static String sortBy(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForSort = context.getString(R.string.pref_sort_key);
        String defaultSort = context.getString(R.string.pref_sort_popular);
        String preferredSort = sp.getString(keyForSort, defaultSort);

        return preferredSort;
    }
}