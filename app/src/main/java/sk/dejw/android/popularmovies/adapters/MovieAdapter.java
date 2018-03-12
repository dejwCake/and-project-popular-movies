package sk.dejw.android.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import sk.dejw.android.popularmovies.R;
import sk.dejw.android.popularmovies.models.Movie;

public class MovieAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(@NonNull Context context, @NonNull List<Movie> movies) {
        super(context, 0, movies);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movies_list_item, parent, false);
        }

        ImageView movieImage = (ImageView) convertView.findViewById(R.id.movie_image);
        if (movie != null) {
            Picasso.with(getContext())
                    .load(movie.getFullPosterPath())
//                    .placeholder(R.drawable.user_placeholder)
//                    .error(R.drawable.user_placeholder_error)
                    .into(movieImage);
        }

        return convertView;
    }
}
