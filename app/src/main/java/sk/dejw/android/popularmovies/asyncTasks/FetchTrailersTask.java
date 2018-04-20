package sk.dejw.android.popularmovies.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import java.net.URL;

import sk.dejw.android.popularmovies.models.Trailer;
import sk.dejw.android.popularmovies.utils.TrailerJsonUtils;
import sk.dejw.android.popularmovies.utils.TrailerNetworkUtils;

public class FetchTrailersTask extends AsyncTask<Integer, Void, Trailer[]> {
    private static final String TAG = "FetchTrailersTask";
    private Context mContext;
    private AsyncTaskCompleteListener<Trailer[]> listener;

    public FetchTrailersTask(Context context, AsyncTaskCompleteListener<Trailer[]> listener) {
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Trailer[] doInBackground(Integer... params) {
        if (params.length == 0) {
            return null;
        }

        Integer id = params[0];
        URL requestUrl = TrailerNetworkUtils.buildUrl(id, mContext);

        try {
            String jsonResponse = TrailerNetworkUtils
                    .getResponseFromHttpUrl(requestUrl);

            Trailer[] trailers = TrailerJsonUtils
                    .getTrailersFromJson(mContext, jsonResponse);

            return trailers;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Trailer[] trailers) {
        super.onPostExecute(trailers);
        listener.onTaskComplete(trailers);
    }
}
