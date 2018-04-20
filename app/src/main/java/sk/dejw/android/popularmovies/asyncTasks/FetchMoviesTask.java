package sk.dejw.android.popularmovies.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import java.net.URL;

import sk.dejw.android.popularmovies.models.Movie;
import sk.dejw.android.popularmovies.utils.MovieJsonUtils;
import sk.dejw.android.popularmovies.utils.MovieNetworkUtils;

public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {
    private static final String TAG = "FetchMoviesTask";
    private Context mContext;
    private AsyncTaskCompleteListener<Movie[]> listener;

    public FetchMoviesTask(Context context, AsyncTaskCompleteListener<Movie[]> listener) {
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Movie[] doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        String sortBy = params[0];
        URL requestUrl = MovieNetworkUtils.buildUrl(sortBy, mContext);

        try {
            String jsonResponse = MovieNetworkUtils
                    .getResponseFromHttpUrl(requestUrl);

            Movie[] movies = MovieJsonUtils
                    .getMoviesFromJson(mContext, jsonResponse);

            return movies;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);
        listener.onTaskComplete(movies);
    }
}
