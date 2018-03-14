package sk.dejw.android.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import sk.dejw.android.popularmovies.R;
import sk.dejw.android.popularmovies.models.Movie;

public class MovieAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private final MovieAdapterOnClickHandler mClickHandler;

    public MovieAdapter(@NonNull Context context, @NonNull List<Movie> movies, MovieAdapterOnClickHandler clickHandler) {
        super(context, 0, movies);
        mClickHandler = clickHandler;
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movies_list_item, parent, false);
        }

        ImageView movieImage = (ImageView) convertView.findViewById(R.id.movie_image);
        if (movie != null) {
            Picasso.with(getContext())
                    .load(movie.getFullPosterPath(Movie.SIZE_W185))
//                    .placeholder(R.drawable.ic_image_black_24px)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(movieImage);
        }

        movieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickHandler.onClick(movie);
            }
        });

        return convertView;
    }
}
