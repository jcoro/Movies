package net.coronite.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import net.coronite.movies.callbacks.DetailFragmentCallback;
import net.coronite.movies.model.MovieItem;

public class MainActivity extends AppCompatActivity implements DetailFragmentCallback {
    private boolean mTwoPane;
    private String mSortBy;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSortBy = Utility.getSortByPreference(this);

        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp-land). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;

        } else {
            mTwoPane = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String sortBy = Utility.getSortByPreference(this);
        // update the location in our second pane using the fragment manager
        if (sortBy != null && !sortBy.equals(mSortBy)) {
            MovieFragment mf = (MovieFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_main);
            if ( null != mf ) {
                mf.onSortByChanged();
            }
            mSortBy = sortBy;
        }
    }

    /*
    This method is defined in the callback, DetailFragmentCallback.  It allows the MovieItem to be sent from
    the MovieFragment to the MainActivity, so that the DetailFragment can be populated with the info if we are in one-pane mode
    (i.e., if the Detailfragment is included in the MainActivity), or the intent can be set and DetailActivity launched if we are in
     two-pane mode.
     */
    @Override
    public void onItemSelected(MovieItem movie) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_MOVIE, movie);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(DetailFragment.DETAIL_MOVIE, movie);
            startActivity(intent);
        }
    }
}
