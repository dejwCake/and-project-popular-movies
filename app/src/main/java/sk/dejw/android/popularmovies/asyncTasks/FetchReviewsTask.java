package sk.dejw.android.popularmovies.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import java.net.URL;

import sk.dejw.android.popularmovies.DetailActivity;
import sk.dejw.android.popularmovies.models.Review;
import sk.dejw.android.popularmovies.models.Trailer;
import sk.dejw.android.popularmovies.utils.ReviewJsonUtils;
import sk.dejw.android.popularmovies.utils.ReviewNetworkUtils;
import sk.dejw.android.popularmovies.utils.TrailerJsonUtils;
import sk.dejw.android.popularmovies.utils.TrailerNetworkUtils;

public class FetchReviewsTask extends AsyncTask<Integer, Void, Review[]> {
    private static final String TAG = "FetchReviewsTask";
    private Context mContext;
    private AsyncTaskCompleteListener<Review[]> listener;

    public FetchReviewsTask(Context context, AsyncTaskCompleteListener<Review[]> listener) {
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Review[] doInBackground(Integer... params) {
        if (params.length == 0) {
            return null;
        }

        Integer id = params[0];
        URL requestUrl = ReviewNetworkUtils.buildUrl(id, mContext);

        try {
            String jsonResponse = ReviewNetworkUtils
                    .getResponseFromHttpUrl(requestUrl);

            Review[] reviews = ReviewJsonUtils
                    .getReviewsFromJson(mContext, jsonResponse);

            return reviews;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Review[] reviews) {
        super.onPostExecute(reviews);
        listener.onTaskComplete(reviews);
    }
}
