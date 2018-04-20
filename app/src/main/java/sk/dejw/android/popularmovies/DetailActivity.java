package sk.dejw.android.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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
import sk.dejw.android.popularmovies.adapters.ReviewAdapter;
import sk.dejw.android.popularmovies.adapters.TrailerAdapter;
import sk.dejw.android.popularmovies.asyncTasks.AsyncTaskCompleteListener;
import sk.dejw.android.popularmovies.asyncTasks.FetchReviewsTask;
import sk.dejw.android.popularmovies.asyncTasks.FetchTrailersTask;
import sk.dejw.android.popularmovies.data.FavoriteMoviesContract;
import sk.dejw.android.popularmovies.models.Movie;
import sk.dejw.android.popularmovies.models.Review;
import sk.dejw.android.popularmovies.models.Trailer;
import sk.dejw.android.popularmovies.utils.GlobalNetworkUtils;
import sk.dejw.android.popularmovies.utils.ReviewJsonUtils;
import sk.dejw.android.popularmovies.utils.ReviewNetworkUtils;
import sk.dejw.android.popularmovies.utils.TrailerJsonUtils;
import sk.dejw.android.popularmovies.utils.TrailerNetworkUtils;

public class DetailActivity extends AppCompatActivity
        implements TrailerAdapter.TrailerAdapterOnClickHandler, ReviewAdapter.ReviewAdapterOnClickHandler {

    private static final String TAG = DetailActivity.class.getSimpleName();

    public static final String EXTRA_TEXT = "movie";

    Movie mMovie = null;

    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.tv_rating)
    TextView mRating;
    @BindView(R.id.tv_release_date)
    TextView mReleaseDate;
    @BindView(R.id.iv_favorite_icon)
    ImageView mFavoriteIcon;
    @BindView(R.id.tv_plot)
    TextView mPlot;
    @BindView(R.id.movie_image)
    ImageView mMovieImage;

    private TrailerAdapter mTrailerAdapter;
    @BindView(R.id.rv_trailers)
    RecyclerView mTrailerView;
    @BindView(R.id.pb_trailer_loading_indicator)
    ProgressBar mTrailerLoadingIndicator;
    @BindView(R.id.tv_trailer_error_message)
    TextView mTrailerErrorMessage;

    private ArrayList<Trailer> mListOfTrailers;

    Trailer[] mTrailers = {};

    public static final String BUNDLE_TRAILERS = "mTrailers";

    private ReviewAdapter mReviewAdapter;
    @BindView(R.id.rv_reviews)
    RecyclerView mReviewView;
    @BindView(R.id.pb_review_loading_indicator)
    ProgressBar mReviewLoadingIndicator;
    @BindView(R.id.tv_review_error_message)
    TextView mReviewErrorMessage;

    private ArrayList<Review> mListOfReviews;

    Review[] mReviews = {};

    public static final String BUNDLE_REVIEWS = "mReviews";

    boolean isFavorite = false;

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

        prepareTrailers(savedInstanceState);
        prepareReviews(savedInstanceState);
    }

    private void prepareTrailers(Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(BUNDLE_TRAILERS)) {
            mListOfTrailers = new ArrayList<Trailer>(Arrays.asList(mTrailers));
        } else {
            mListOfTrailers = savedInstanceState.getParcelableArrayList(BUNDLE_TRAILERS);
        }

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        mTrailerView.setLayoutManager(layoutManager);

        mTrailerView.setHasFixedSize(false);

        mTrailerAdapter = new TrailerAdapter(this, mListOfTrailers, this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mTrailerView.setAdapter(mTrailerAdapter);

        loadTrailerData();
    }

    private void prepareReviews(Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(BUNDLE_REVIEWS)) {
            mListOfReviews = new ArrayList<Review>(Arrays.asList(mReviews));
        } else {
            mListOfReviews = savedInstanceState.getParcelableArrayList(BUNDLE_REVIEWS);
        }

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        mReviewView.setLayoutManager(layoutManager);

        mReviewView.setHasFixedSize(false);

        mReviewAdapter = new ReviewAdapter(this, mListOfReviews, this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mReviewView.setAdapter(mReviewAdapter);

        loadReviewData();
    }

    private void setLayout() {
        if (mMovie != null) {
            mTitle.setText(mMovie.getTitle());
            mRating.setText(new DecimalFormat("##.##").format(mMovie.getRating()));
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
            mReleaseDate.setText(dateFormat.format(mMovie.getReleaseDate()));
            mPlot.setText(mMovie.getPlot());
            mFavoriteIcon.setImageResource(R.drawable.ic_star_border_black_24dp);

            //TODO check if movie is in DB, if so, set favorite to true

            if(isFavorite = checkIfFavorite()) {
                mFavoriteIcon.setImageResource(R.drawable.ic_star_black_24dp);
            }
            mFavoriteIcon.setClickable(true);
            mFavoriteIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isFavorite) {
                        //Remove from favorite
                        removeFromFavorites();
                        mFavoriteIcon.setImageResource(R.drawable.ic_star_border_black_24dp);
                        isFavorite = false;
                    } else {
                        //Add to favorite
                        addToFavorites();
                        mFavoriteIcon.setImageResource(R.drawable.ic_star_black_24dp);
                        isFavorite = true;
                    }
                }
            });

            Picasso.with(this)
                    .load(mMovie.getFullPosterPath(Movie.SIZE_W342))
//                .placeholder(R.drawable.ic_image_black_24px)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(mMovieImage);
        }
    }

    private boolean checkIfFavorite() {
        boolean isFavorite = false;
        ContentResolver popularMoviesContentResolver = this.getContentResolver();
        String[] projectionColumns = {FavoriteMoviesContract.FavoriteMovieEntry._ID};

        Cursor cursor = popularMoviesContentResolver.query(
                FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(mMovie.getId())).build(),
                projectionColumns,
                null,
                null,
                null);
        if (null == cursor || cursor.getCount() == 0) {
            isFavorite = false;
        } else {
            isFavorite = true;
        }
        cursor.close();
        return isFavorite;
    }

    private void addToFavorites() {
        ContentValues contentValue = new ContentValues();
        contentValue.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_ID, mMovie.getId());
        contentValue.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_TITLE, mMovie.getTitle());
        contentValue.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_PLOT, mMovie.getPlot());
        contentValue.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_POSTER_PATH, mMovie.getPosterPath());
        contentValue.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_RATING, mMovie.getRating());
        contentValue.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate().toString());

        ContentResolver popularMoviesContentResolver = this.getContentResolver();

        popularMoviesContentResolver.insert(
                FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI,
                contentValue);
    }

    private void removeFromFavorites() {
            ContentResolver popularMoviesContentResolver = this.getContentResolver();

            popularMoviesContentResolver.delete(
                    FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(mMovie.getId())).build(),
                    null,
                    null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(BUNDLE_TRAILERS, mListOfTrailers);
        outState.putParcelableArrayList(BUNDLE_REVIEWS, mListOfReviews);
        super.onSaveInstanceState(outState);
    }

    private void loadTrailerData() {
        showTrailerDataView();

        if (GlobalNetworkUtils.hasConnection(this)) {
            mTrailerLoadingIndicator.setVisibility(View.VISIBLE);
            new FetchTrailersTask(this, new FetchTrailersTaskCompleteListener()).execute(mMovie.getId());
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

    public class FetchTrailersTaskCompleteListener implements AsyncTaskCompleteListener<Trailer[]>
    {
        @Override
        public void onTaskComplete(Trailer[] trailers)
        {
            mTrailerLoadingIndicator.setVisibility(View.INVISIBLE);
            Log.i(TAG, String.valueOf(trailers.length));
            if (trailers != null) {
                showTrailerDataView();
                mListOfTrailers = new ArrayList<Trailer>(Arrays.asList(trailers));
                mTrailerAdapter.swapTrailers(mListOfTrailers);
            } else {
                showTrailerErrorMessage();
            }
        }
    }

    private void loadReviewData() {
        showReviewDataView();

        if (GlobalNetworkUtils.hasConnection(this)) {
            mReviewLoadingIndicator.setVisibility(View.VISIBLE);
            new FetchReviewsTask(this, new FetchReviewsTaskCompleteListener()).execute(mMovie.getId());
        } else {
            showReviewErrorMessage();
        }
    }

    private void showReviewDataView() {
        mReviewErrorMessage.setVisibility(View.INVISIBLE);
        mReviewView.setVisibility(View.VISIBLE);
    }

    private void showReviewErrorMessage() {
        mReviewView.setVisibility(View.INVISIBLE);
        mReviewErrorMessage.setVisibility(View.VISIBLE);
    }

    public class FetchReviewsTaskCompleteListener implements AsyncTaskCompleteListener<Review[]>
    {
        @Override
        public void onTaskComplete(Review[] reviews)
        {
            mReviewLoadingIndicator.setVisibility(View.INVISIBLE);
            Log.i(TAG, String.valueOf(reviews.length));
            if (reviews != null) {
                showReviewDataView();
                mListOfReviews = new ArrayList<Review>(Arrays.asList(reviews));
                mReviewAdapter.swapReviews(mListOfReviews);
            } else {
                showReviewErrorMessage();
            }
        }
    }

    @Override
    public void onTrailerClick(Trailer trailer) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getYouTubeApp()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(trailer.getYouTubePath()));
        try {
            this.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            this.startActivity(webIntent);
        }
    }

    @Override
    public void onReviewClick(Review review) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl()));
        this.startActivity(webIntent);
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
