package net.coronite.movies.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import net.coronite.movies.callbacks.AsyncTaskCompleteListener;
import net.coronite.movies.data.MovieContract;
import net.coronite.movies.model.MovieItem;

/**
 * Adds a movie to the list of favorites stored in the database.
 */
public class AddFavoriteTask extends AsyncTask<MovieItem, Void, Uri> {
    private AsyncTaskCompleteListener<Uri> mListener;
    private Context mContext;

    public AddFavoriteTask(AsyncTaskCompleteListener<Uri> listener, Context context){
        mListener = listener;
        mContext = context;
    }
    @Override
    protected Uri doInBackground(MovieItem... params) {
        MovieItem movie = params[0];

        ContentValues values = new ContentValues();

        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());

        return mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,
                values);

    }

    @Override
    protected void onPostExecute(Uri result) {

        mListener.onTaskComplete(result);
    }
}