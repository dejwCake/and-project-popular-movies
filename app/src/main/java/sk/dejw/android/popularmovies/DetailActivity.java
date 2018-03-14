package sk.dejw.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.dejw.android.popularmovies.models.Movie;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_TEXT = "movie";

    Movie mMovie = null;

    @BindView(R.id.tv_title) TextView mTitle;
    @BindView(R.id.tv_rating) TextView mRating;
    @BindView(R.id.tv_release_date) TextView mReleaseDate;
    @BindView(R.id.tv_plot) TextView mPlot;
    @BindView(R.id.movie_image) ImageView mMovieImage;

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
    }

    private void setLayout() {
        if(mMovie != null) {
            mTitle.setText(mMovie.getTitle());
            mRating.setText(new DecimalFormat("##.##").format(mMovie.getRating()));
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
            mReleaseDate.setText(dateFormat.format(mMovie.getReleaseDate()));
            mPlot.setText(mMovie.getPlot());

            Picasso.with(this)
                .load(mMovie.getBigFullPosterPath())
                .placeholder(R.drawable.ic_image_black_24px)
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(mMovieImage);
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
