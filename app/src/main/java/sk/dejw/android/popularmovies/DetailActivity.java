package sk.dejw.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.dejw.android.popularmovies.models.Movie;
import sk.dejw.android.popularmovies.models.Trailer;
import sk.dejw.android.popularmovies.utils.GlobalNetworkUtils;
import sk.dejw.android.popularmovies.utils.MovieJsonUtils;
import sk.dejw.android.popularmovies.utils.MovieNetworkUtils;
import sk.dejw.android.popularmovies.utils.TrailerJsonUtils;
import sk.dejw.android.popularmovies.utils.TrailerNetworkUtils;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    public static final String EXTRA_TEXT = "movie";

    Movie mMovie = null;

    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.tv_rating)
    TextView mRating;
    @BindView(R.id.tv_release_date)
    TextView mReleaseDate;
    @BindView(R.id.tv_plot)
    TextView mPlot;
    @BindView(R.id.movie_image)
    ImageView mMovieImage;

    @BindView(R.id.rv_trailers)
    RecyclerView mTrailerView;
    @BindView(R.id.pb_review_loading_indicator)
    ProgressBar mTrailerLoadingIndicator;
    @BindView(R.id.tv_trailer_error_message)
    TextView mTrailerErrorMessage;

//    private TrailerAdapter mTrailerAdapter;

    private ArrayList<Trailer> mListOfTrailers;

    Trailer[] trailers = {};

    public static final String BUNDLE_TRAILERS = "trailers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent startingIntent = getIntent();

        if (startingIntent != null) {
            if (startingIntent.hasExtra(EXTRA_TEXT)) {
                mMovie = getIntent().getExtras().getParcelable(EXTRA_TEXT);
                setLayout();
            }
        }

        if (savedInstanceState == null || !savedInstanceState.containsKey(BUNDLE_TRAILERS)) {
            mListOfTrailers = new ArrayList<Trailer>(Arrays.asList(trailers));
        } else {
            mListOfTrailers = savedInstanceState.getParcelableArrayList(BUNDLE_TRAILERS);
        }

//        //set adapter for gridview
//        mMovieAdapter = new MovieAdapter(this, mListOfMovies, this);
//
//        // Get a reference to the ListView, and attach this adapter to it.
//        mGridView.setAdapter(mMovieAdapter);
//
//        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//                Toast.makeText(MainActivity.this, "" + position,
//                        Toast.LENGTH_SHORT).show();
//            }
//        });

        loadTrailerData();
    }

    private void setLayout() {
        if (mMovie != null) {
            mTitle.setText(mMovie.getTitle());
            mRating.setText(new DecimalFormat("##.##").format(mMovie.getRating()));
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
            mReleaseDate.setText(dateFormat.format(mMovie.getReleaseDate()));
            mPlot.setText(mMovie.getPlot());

            Picasso.with(this)
                    .load(mMovie.getFullPosterPath(Movie.SIZE_W342))
//                .placeholder(R.drawable.ic_image_black_24px)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(mMovieImage);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(BUNDLE_TRAILERS, mListOfTrailers);
        super.onSaveInstanceState(outState);
    }

    private void loadTrailerData() {
        showTrailerDataView();

        if (GlobalNetworkUtils.hasConnection(this)) {
            new DetailActivity.FetchTrailersTask(this).execute(mMovie.getId());
        } else {
            showTrailerErrorMessage();
        }
    }

    private void showTrailerDataView() {
        mTrailerErrorMessage.setVisibility(View.INVISIBLE);
        mTrailerView.setVisibility(View.VISIBLE);
    }

    private void showTrailerErrorMessage() {
        mTrailerView.setVisibility(View.INVISIBLE);
        mTrailerErrorMessage.setVisibility(View.VISIBLE);
    }

    //TODO start youtube intent
//    @Override
//    public void onTrailerClick(Trailer trailer) {
//        Intent movieDetailIntent = new Intent(MainActivity.this, DetailActivity.class);
//        movieDetailIntent.putExtra(DetailActivity.EXTRA_TEXT, movie);
//        startActivity(movieDetailIntent);
//    }

    public class FetchTrailersTask extends AsyncTask<Integer, Void, Trailer[]> {

        private Context mContext;

        public FetchTrailersTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mTrailerLoadingIndicator.setVisibility(View.VISIBLE);
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
                        .getTrailersFromJson(DetailActivity.this, jsonResponse);

                return trailers;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Trailer[] trailers) {
            mTrailerLoadingIndicator.setVisibility(View.INVISIBLE);
//            Log.i(TAG, mMovieAdapter.toString());
            Log.i(TAG, String.valueOf(trailers.length));
            if (trailers != null) {
                showTrailerDataView();
                ArrayList<Trailer> listOfTrailers = new ArrayList<Trailer>(Arrays.asList(trailers));
//                mMovieAdapter.clear();
//                mMovieAdapter.addAll(listOfTrailers);
//                mMovieAdapter.notifyDataSetChanged();
            } else {
                showTrailerErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
