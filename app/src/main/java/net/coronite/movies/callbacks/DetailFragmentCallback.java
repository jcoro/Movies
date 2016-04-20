package net.coronite.movies.callbacks;

import net.coronite.movies.model.MovieItem;

/**
 * A callback interface implemented in the MainActivity, and called in the movie fragment when a movie is selected.
 * It allows the movie item to be sent to the MainActivity where either a dynamically-added DetailFragment can be
 * added and populated with the info (two-pane),
 * or the info can be packaged into an intent, and a new DetailActivity launched (one-pane).
 */
public interface DetailFragmentCallback {
    /**
     * DetailFragmentCallback for when an item has been selected.
     * @param movie the MovieItem selected.
     */
    void onItemSelected(MovieItem movie);
}
