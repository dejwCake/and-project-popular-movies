package sk.dejw.android.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class FavoriteMoviesProvider extends ContentProvider {

    public static final int CODE_FAVORITE_MOVIES = 100;
    public static final int CODE_FAVORITE_MOVIES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavoriteMoviesDbHelper mFavoriteMoviesHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoriteMoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FavoriteMoviesContract.PATH_FAVORITE_MOVIE, CODE_FAVORITE_MOVIES);

        matcher.addURI(authority, FavoriteMoviesContract.PATH_FAVORITE_MOVIE + "/#", CODE_FAVORITE_MOVIES_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mFavoriteMoviesHelper = new FavoriteMoviesDbHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, @NonNull ContentValues value) {
        final SQLiteDatabase db = mFavoriteMoviesHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITE_MOVIES:
                long _id = db.insert(FavoriteMoviesContract.FavoriteMovieEntry.TABLE_NAME, null, value);
                if (_id > 0) {
                    returnUri = FavoriteMoviesContract.FavoriteMovieEntry.buildFavoriteMoviesUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mFavoriteMoviesHelper.getReadableDatabase();
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITE_MOVIES:
                cursor = db.query(
                        FavoriteMoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_FAVORITE_MOVIES_WITH_ID:
                String[] selectionArguments = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(
                        FavoriteMoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mFavoriteMoviesHelper.getWritableDatabase();
        int numRowsDeleted;

        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITE_MOVIES:
                numRowsDeleted = db.delete(
                        FavoriteMoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_FAVORITE_MOVIES_WITH_ID:
                String[] selectionArguments = new String[]{String.valueOf(ContentUris.parseId(uri))};
                numRowsDeleted = db.delete(FavoriteMoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_ID + " = ?",
                        selectionArguments);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mFavoriteMoviesHelper.getWritableDatabase();
        int numUpdated = 0;

        if (values == null) {
            throw new IllegalArgumentException("Cannot have null values");
        }

        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITE_MOVIES:
                numUpdated = db.update(FavoriteMoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CODE_FAVORITE_MOVIES_WITH_ID:
                String[] selectionArguments = new String[]{String.valueOf(ContentUris.parseId(uri))};
                numUpdated = db.update(FavoriteMoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        values,
                        FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_ID + " = ?",
                        selectionArguments);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing getType.");
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mFavoriteMoviesHelper.close();
        super.shutdown();
    }
}