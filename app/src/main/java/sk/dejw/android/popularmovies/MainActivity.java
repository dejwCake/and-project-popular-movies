package sk.dejw.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import sk.dejw.android.popularmovies.adapters.MovieAdapter;
import sk.dejw.android.popularmovies.asyncTasks.AsyncTaskCompleteListener;
import sk.dejw.android.popularmovies.asyncTasks.FetchMoviesTask;
import sk.dejw.android.popularmovies.data.FavoriteMoviesContract;
import sk.dejw.android.popularmovies.data.MoviePreferences;
import sk.dejw.android.popularmovies.models.Movie;
import sk.dejw.android.popularmovies.utils.GlobalNetworkUtils;
import sk.dejw.android.popularmovies.utils.MovieCursorUtils;
import sk.dejw.android.popularmovies.utils.MovieJsonUtils;
import sk.dejw.android.popularmovies.utils.MovieNetworkUtils;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private GridView mGridView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;

    private MovieAdapter mMovieAdapter;

    private ArrayList<Movie> mListOfMovies;

    Movie[] movies = {};

    public static final String BUNDLE_MOVIES = "movies";

    private static final int ID_FAVORITE_MOVIES_LOADER = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey(BUNDLE_MOVIES)) {
            mListOfMovies = new ArrayList<Movie>(Arrays.asList(movies));
        } else {
            mListOfMovies = savedInstanceState.getParcelableArrayList(BUNDLE_MOVIES);
        }

        setContentView(R.layout.activity_main);

        mGridView = (GridView) findViewById(R.id.gw_posters);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);


        //set adapter for gridview
        mMovieAdapter = new MovieAdapter(this, mListOfMovies, this);

        // Get a reference to the ListView, and attach this adapter to it.
        mGridView.setAdapter(mMovieAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        //Get data from remote with async task
        loadMovieData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(BUNDLE_MOVIES, mListOfMovies);
        super.onSaveInstanceState(outState);
    }

    private void loadMovieData() {
        showMovieDataView();

        String sortBy = MoviePreferences.sortBy(this);
        if (sortBy.equals(getString(R.string.pref_sort_favorites))) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            getSupportLoaderManager().restartLoader(ID_FAVORITE_MOVIES_LOADER, null, this);
        } else {
            if (GlobalNetworkUtils.hasConnection(this)) {
                mLoadingIndicator.setVisibility(View.VISIBLE);
                new FetchMoviesTask(this, new FetchMoviesTaskCompleteListener()).execute(sortBy);
            } else {
                showErrorMessage();
            }
        }
    }

    private void showMovieDataView() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mGridView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mGridView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movie) {
        Intent movieDetailIntent = new Intent(MainActivity.this, DetailActivity.class);
        movieDetailIntent.putExtra(DetailActivity.EXTRA_TEXT, movie);
        startActivity(movieDetailIntent);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case ID_FAVORITE_MOVIES_LOADER:
                Log.d(TAG, "Loading favorite movies");
                String[] projection = {
                        FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_ID,
                        FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_PLOT,
                        FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_POSTER_PATH,
                        FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_RATING,
                        FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE,
                        FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_TITLE,
                };
                return new CursorLoader(this,
                        FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "Favorite movies loaded: ".concat(String.valueOf(data.getCount())));
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data.getCount() != 0) {
            showMovieDataView();
            ArrayList<Movie> listOfMovies = MovieCursorUtils.getMoviesFromCursor(data);
            mMovieAdapter.clear();
            mMovieAdapter.addAll(listOfMovies);
            mMovieAdapter.notifyDataSetChanged();
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mMovieAdapter.clear();
        mMovieAdapter.notifyDataSetChanged();
    }

    public class FetchMoviesTaskCompleteListener implements AsyncTaskCompleteListener<Movie[]>
    {
        @Override
        public void onTaskComplete(Movie[] movies)
        {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            Log.i(TAG, mMovieAdapter.toString());
            if (movies != null) {
                showMovieDataView();
                ArrayList<Movie> listOfMovies = new ArrayList<Movie>(Arrays.asList(movies));
                mMovieAdapter.clear();
                mMovieAdapter.addAll(listOfMovies);
                mMovieAdapter.notifyDataSetChanged();
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        if (id == R.id.action_refresh) {
            loadMovieData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
