package net.coronite.movies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Encapsulates fetching the movie data and displaying its poster image in a {@link GridView} layout.
 */
public class MovieFragment extends Fragment {

    private MovieAdapter mMovieAdapter;
    private ArrayList<MovieItem> movieList;

    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        mMovieAdapter = new MovieAdapter(getActivity());

        // Get a reference to the GridView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        gridView.setAdapter(mMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MovieItem clickedMovie = mMovieAdapter.getItem(i);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("movie", clickedMovie);
                startActivity(intent);

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
        FetchMovieTask movieTask = new FetchMovieTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = prefs.getString(getString(R.string.pref_sort_by_key),
                getString(R.string.pref_sort_by_value_popular));
        movieTask.execute(sortBy);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, MovieItem[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        //Show a dialog to let the user know the movie data is loading.
        private ProgressDialog dialog = new ProgressDialog(getActivity());

        /**
         * Take the String representing the movie data in JSON Format and
         * pull out the data we need for the detail view.
         *
         */
        private MovieItem[] getMovieDataFromJson(String movieJsonStr)
                throws JSONException {

            // JSON objects that need to be extracted.
            final String RESULTS = "results";
            final String ID = "id";
            final String POSTER_PATH = "poster_path";
            final String TITLE = "title";
            final String OVERVIEW = "overview";
            final String RELEASE_DATE = "release_date";
            final String VOTE_AVERAGE = "vote_average";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray resultsArray = movieJson.getJSONArray(RESULTS);

            MovieItem[] movieResults = new MovieItem[resultsArray.length()];

            for(int i = 0; i < resultsArray.length(); i++) {

                // Get the JSON object representing the movie
                JSONObject singleMovie = resultsArray.getJSONObject(i);

                int id = singleMovie.getInt(ID);
                String posterPath = singleMovie.getString(POSTER_PATH);
                String title = singleMovie.getString(TITLE);
                String overview = singleMovie.getString(OVERVIEW);
                String releaseDate = singleMovie.getString(RELEASE_DATE);
                Double voteAverage = singleMovie.getDouble(VOTE_AVERAGE);

                movieResults[i] = new MovieItem(id, posterPath, title, overview, releaseDate, voteAverage );
            }
            return movieResults;
        }

        @Override
        protected void onPreExecute() {
            // Show the dialog while the movie data is loading.
            this.dialog.setMessage("Movie Data Is Downloading...");
            this.dialog.show();
        }

        @Override
        protected MovieItem[] doInBackground(String... params) {

            // doInBackground takes the sortBy string given to execute()

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String theMovieJsonStr = null;

            try {
                // Construct the URL for the moviedb.org query
                final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY = "sort_by";
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY, params[0])
                        .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to themoviedb, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // builder for debugging.
                    builder.append(line).append("\n");
                }

                if (builder.length() == 0) {
                    // Stream was empty.  No point in parsing.

                    return null;
                }
                theMovieJsonStr = builder.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(theMovieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(MovieItem[] result) {
            // dismiss the dialog when the data is done loading.
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (result != null) {
                if (mMovieAdapter != null) {
                    mMovieAdapter.clear();
                    for (MovieItem movie : result) {
                        mMovieAdapter.add(movie);
                    }
                    movieList = new ArrayList<>();
                    Collections.addAll(movieList, result);
                }
            }
        }

    }
}
