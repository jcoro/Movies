package net.coronite.movies;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import net.coronite.movies.adapters.MovieAdapter;
import net.coronite.movies.data.MovieContract;
import net.coronite.movies.callbacks.AsyncTaskCompleteListener;
import net.coronite.movies.callbacks.DetailFragmentCallback;
import net.coronite.movies.model.MovieItem;
import net.coronite.movies.tasks.FetchMoviesTask;

import java.util.ArrayList;

/**
 * Encapsulates fetching the movie data and displaying its poster image in a {@link GridView} layout.
 */
public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private MovieAdapter mMovieAdapter;
    private ArrayList<MovieItem> movieList;
    private static final int MOVIE_LOADER = 0;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
    };

    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        mMovieAdapter = new MovieAdapter(getActivity());

        // Get a reference to the GridView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        gridView.setAdapter(mMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MovieItem clickedMovie = mMovieAdapter.getItem(i);

                // Send the MovieItem to the .onItemSelected() method of the parent activity (i.e., MainActivity).
                ((DetailFragmentCallback) getActivity()).onItemSelected(clickedMovie);

            }
        });

        // If the movie data HASN'T already been fetched...
        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            updateMovies();
        }
        // If the movie data HAS already been fetched...
        else {

            movieList = savedInstanceState.getParcelableArrayList("movies");
            if (movieList != null) {

                for (MovieItem movie : movieList) {
                    mMovieAdapter.add(movie);
                }
            }
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", movieList);
        super.onSaveInstanceState(outState);
    }

    private void updateMovies() {

        String sortBy = Utility.getSortByPreference(getActivity());

        FetchMoviesTaskCompleteListener mMoviesListener = new FetchMoviesTaskCompleteListener();

        if (sortBy.equals(getString(R.string.pref_sort_by_value_favorite))) {
            getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        }
        else {
            new FetchMoviesTask(mMoviesListener).execute(sortBy);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    void onSortByChanged( ) {
        updateMovies();
    }

    public class FetchMoviesTaskCompleteListener implements AsyncTaskCompleteListener<ArrayList<MovieItem>>
    {

        @Override
        public void onTaskComplete(ArrayList<MovieItem> result)
        {
            if (result != null && mMovieAdapter != null) {
                mMovieAdapter.clear();
                mMovieAdapter.addAll(result);
                movieList = result;
            } else {
                updateMovies();
            }
        }
    }

    public void setMovieData(ArrayList<MovieItem> result){
        if (mMovieAdapter != null) {
            mMovieAdapter.clear();
            for (MovieItem movie : result) {
                mMovieAdapter.add(movie);
            }
            movieList = result;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),                                      // context
                MovieContract.MovieEntry.CONTENT_URI,               // uri
                MOVIE_COLUMNS,                                      // projection
                null,                                               // selection
                null,                                               // seletionArgs
                null                                                // sort order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ArrayList<MovieItem> results = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                MovieItem movie = new MovieItem(cursor);
                results.add(movie);
            } while (cursor.moveToNext());
        }
        setMovieData(results);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

