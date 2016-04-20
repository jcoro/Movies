package net.coronite.movies.tasks;

import android.content.Context;
import android.os.AsyncTask;

import net.coronite.movies.callbacks.AsyncTaskCompleteListener;
import net.coronite.movies.data.MovieContract;

/**
 * Deletes a movie from the list of favorites stored in the database.
 */
public class DeleteFavoriteTask extends AsyncTask<String, Void, Integer> {
    private AsyncTaskCompleteListener<Integer> mListener;
    private Context mContext;

    public DeleteFavoriteTask(AsyncTaskCompleteListener<Integer> listener, Context context){
        mListener = listener;
        mContext = context;
    }
    @Override
    protected Integer doInBackground(String... params) {
        String movieId = params[0];

        return mContext.getContentResolver().delete(
                MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{movieId}
        );
    }

    @Override
    protected void onPostExecute(Integer result) {

        mListener.onTaskComplete(result);
    }
}
