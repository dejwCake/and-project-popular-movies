package sk.dejw.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import sk.dejw.android.popularmovies.data.MoviePreferences;
import sk.dejw.android.popularmovies.models.Movie;
import sk.dejw.android.popularmovies.utils.MovieJsonUtils;
import sk.dejw.android.popularmovies.utils.MovieNetworkUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private GridView mGridView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;

    private MovieAdapter mMovieAdapter;

    private ArrayList<Movie> mListOfMovies;

    Movie[] movies = {};

    public static final String BUNDLE_MOVIES = "movies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey(BUNDLE_MOVIES)) {
            mListOfMovies = new ArrayList<Movie>(Arrays.asList(movies));
        }
        else {
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
    public void onResume()
    {
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

        if (hasConnection()) {
            String sortBy = MoviePreferences.sortBy(this);
            new FetchMoviesTask(this).execute(sortBy);
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

    public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {

        private Context mContext;

        public FetchMoviesTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
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
                        .getMoviesFromJson(MainActivity.this, jsonResponse);

                return movies;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            Log.e(TAG, mMovieAdapter.toString());
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

    public boolean hasConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        } catch (NullPointerException $npe) {
            return false;
        }
    }
}
