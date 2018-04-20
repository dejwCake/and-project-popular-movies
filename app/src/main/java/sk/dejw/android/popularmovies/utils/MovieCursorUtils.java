package sk.dejw.android.popularmovies.utils;

import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sk.dejw.android.popularmovies.data.FavoriteMoviesContract;
import sk.dejw.android.popularmovies.models.Movie;

public final class MovieCursorUtils {

    public static ArrayList<Movie> getMoviesFromCursor(Cursor data) {
        final Integer MOVIE_ID = data.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_ID);
        final Integer MOVIE_TITLE = data.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_TITLE);
        final Integer MOVIE_POSTER_PATH = data.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_POSTER_PATH);
        final Integer MOVIE_PLOT = data.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_PLOT);
        final Integer MOVIE_RATING = data.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_RATING);
        final Integer MOVIE_RELEASE_DATE = data.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE);

        ArrayList<Movie> listOfMovies = new ArrayList<Movie>();
        try {
            while (data.moveToNext()) {
                Date releaseDate;
                try {
                    releaseDate = (new SimpleDateFormat()).parse(data.getString(MOVIE_RELEASE_DATE));
                } catch (ParseException e) {
                    releaseDate = Calendar.getInstance().getTime();
                }
                Movie movie = new Movie(
                        data.getInt(MOVIE_ID),
                        data.getString(MOVIE_TITLE),
                        data.getString(MOVIE_POSTER_PATH),
                        data.getString(MOVIE_PLOT),
                        data.getDouble(MOVIE_RATING),
                        releaseDate
                );
                listOfMovies.add(movie);
            }
        } finally {
            data.close();
        }
        return listOfMovies;
    }
}