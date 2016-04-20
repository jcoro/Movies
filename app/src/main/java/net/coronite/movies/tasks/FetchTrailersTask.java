package net.coronite.movies.tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import net.coronite.movies.BuildConfig;
import net.coronite.movies.callbacks.AsyncTaskCompleteListener;
import net.coronite.movies.model.Trailer;

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

/**
 * Fetches trailer/video data from themoviedb.org.
 */
public class FetchTrailersTask extends AsyncTask<String, Void, ArrayList<Trailer>> {

    private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();
    private AsyncTaskCompleteListener<ArrayList<Trailer>> mListener;

    public FetchTrailersTask(AsyncTaskCompleteListener<ArrayList<Trailer>> listener) {
        mListener = listener;
    }

    /**
     * Take the String representing the trailer data in JSON Format and
     * pull out the data we need.
     *
     */
    private ArrayList<Trailer> getTrailerDataFromJson(String trailerJsonStr)
            throws JSONException {

        // JSON objects that need to be extracted.
        final String RESULTS = "results";

        JSONObject trailerJson = new JSONObject(trailerJsonStr);
        JSONArray resultsArray = trailerJson.getJSONArray(RESULTS);

        ArrayList<Trailer> trailerResults = new ArrayList<>();

        for(int i = 0; i < resultsArray.length(); i++) {

            // Get the JSON object representing the movie
            JSONObject singleTrailerJson = resultsArray.getJSONObject(i);
            if (singleTrailerJson.getString("site").contentEquals("YouTube")) {
                Trailer singleTrailer = new Trailer(singleTrailerJson);
                trailerResults.add(singleTrailer);
            }
        }
        return trailerResults;
    }

    @Override
    protected ArrayList<Trailer> doInBackground(String... params) {

        String movieId = params[0];

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String theTrailerJsonStr = null;

        try {
            // Construct the URL for the moviedb.org query
            final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String VIDEOS = "videos";
            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                    .appendPath(movieId)
                    .appendPath(VIDEOS)
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
            theTrailerJsonStr = builder.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the trailer data, there's no point in attempting
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
            return getTrailerDataFromJson(theTrailerJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> result) {

        mListener.onTaskComplete(result);
    }

}
