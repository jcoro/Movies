package net.coronite.movies.callbacks;

/**
 * A Callback to listen for the completion of AsyncTasks.  This allows AsyncTasks to be placed in their own classes.
 *
 * @param <T>
 */
public interface AsyncTaskCompleteListener<T>
{
    /**
     * Invoked when the AsyncTask has completed its execution.
     * @param result The resulting object from the AsyncTask.
     */
     void onTaskComplete(T result);
}