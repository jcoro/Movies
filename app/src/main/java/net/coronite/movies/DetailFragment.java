package net.coronite.movies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.coronite.movies.adapters.ReviewAdapter;
import net.coronite.movies.adapters.TrailerAdapter;
import net.coronite.movies.callbacks.AsyncTaskCompleteListener;
import net.coronite.movies.data.MovieContract;
import net.coronite.movies.model.MovieItem;
import net.coronite.movies.model.Review;
import net.coronite.movies.model.Trailer;
import net.coronite.movies.tasks.AddFavoriteTask;
import net.coronite.movies.tasks.DeleteFavoriteTask;
import net.coronite.movies.tasks.FetchReviewsTask;
import net.coronite.movies.tasks.FetchTrailersTask;

import java.util.ArrayList;

/**
 * Encapsulates showing the:
 * title
 * poster image
 * rating
 * release year
 * vote average
 * overview
 * reviews
 * trailers
 * for the selected movie.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

    private MovieItem mMovie;
    private TrailerAdapter mTrailerAdapter;
    private ArrayList<Trailer> trailerList;
    private ReviewAdapter mReviewAdapter;
    private ArrayList<Review> reviewList;
    private Button mButton;
    private Toast mToast;
    private Boolean mIsAFavorite;
    private ListView mTrailersListView;
    private ListView mReviewsListView;
    private ShareActionProvider mShareActionProvider;
    static final String DETAIL_MOVIE = "movie";
    private static final String TRAILER_LINK = "http://www.youtube.com/watch?v=";

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

    public static final int COL_MOVIE_ID = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_POSTER = 3;
    public static final int COL_OVERVIEW = 4;
    public static final int COL_RELEASE_DATE = 5;
    public static final int COL_VOTE_AVERAGE = 6;

    private TextView mTrailerHeaderTextView;
    private TextView mReviewHeaderTextView;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View detailView =  inflater.inflate(R.layout.fragment_detail, container, false);
        TextView mTitleView = (TextView) detailView.findViewById(R.id.detail_movie_title);
        ImageView mPosterView = (ImageView) detailView.findViewById(R.id.detail_movie_image);
        TextView mReleaseDate = (TextView) detailView.findViewById(R.id.detail_movie_release_year);
        TextView mVoteAverage = (TextView) detailView.findViewById(R.id.detail_movie_rating);
        TextView mOverview = (TextView) detailView.findViewById(R.id.detail_movie_overview);
        mTrailerHeaderTextView = (TextView) detailView.findViewById(R.id.detail_movie_trailers_header);
        mReviewHeaderTextView = (TextView) detailView.findViewById(R.id.detail_movie_reviews_header);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovie = arguments.getParcelable(DETAIL_MOVIE);
            if (mMovie != null){
                String voteAverage = mMovie.getVoteAverage().toString() + " / 10";
                mVoteAverage.setText(voteAverage);
                mOverview.setText(mMovie.getOverview());
                mTitleView.setText(mMovie.getTitle());
                mReleaseDate.setText(Utility.getYear(mMovie.getReleaseDate()));
                Picasso.with(getActivity()).load(MovieItem.IMAGE_BASE_PATH + mMovie.getPosterPath()).error(R.drawable.no_image).into(mPosterView);
                mTrailerAdapter = new TrailerAdapter(getActivity());
                mReviewAdapter = new ReviewAdapter(getActivity());

                mTrailersListView = (ListView) detailView.findViewById(R.id.listview_trailers);
                mTrailersListView.setAdapter(mTrailerAdapter);

                mReviewsListView = (ListView) detailView.findViewById(R.id.listview_reviews);
                mReviewsListView.setAdapter(mReviewAdapter);

                FetchReviewsTaskCompleteListener reviewsListener = new FetchReviewsTaskCompleteListener();
                FetchTrailersTaskCompleteListener mTrailersListener = new FetchTrailersTaskCompleteListener();

                // If the review/trailer data HASN'T already been fetched...
                if(savedInstanceState == null) {
                    new FetchTrailersTask(mTrailersListener).execute(Integer.toString(mMovie.getId()));
                    new FetchReviewsTask(reviewsListener).execute(Integer.toString(mMovie.getId()));
                }
                else {
                    trailerList = savedInstanceState.getParcelableArrayList("trailers");
                    reviewList = savedInstanceState.getParcelableArrayList("reviews");
                    if (trailerList != null) {
                        mTrailerAdapter.addAll(trailerList);
                    } else {
                        mTrailerHeaderTextView.setText(R.string.no_trailers);
                        mTrailersListView.setVisibility(View.GONE);
                    }

                    if (reviewList != null) {
                        mReviewAdapter.addAll(reviewList);
                    } else {
                        mReviewHeaderTextView.setText(R.string.no_reviews);
                        mReviewsListView.setVisibility(View.GONE);
                    }

                }

                mTrailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> linearListView, View view,
                                            int position, long id) {
                        Trailer trailer = mTrailerAdapter.getItem(position);

                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getKey()));
                            // Verify that the intent will resolve to an activity
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            }
                        } catch (ActivityNotFoundException ex) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(TRAILER_LINK + trailer.getKey()));
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            }
                        }
                    }
                });

                mButton = (Button) detailView.findViewById(R.id.detail_movie_favorite_button);
                mButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //either add or delete in AsyncTasks
                        if (mIsAFavorite) {
                            // delete from favorites
                            DeleteFavoriteTaskCompleteListener deleteFavoriteListener = new DeleteFavoriteTaskCompleteListener();
                            new DeleteFavoriteTask(deleteFavoriteListener, getActivity()).execute(Integer.toString(mMovie.getId()));

                        } else {
                            // Add the movie
                            AddFavoriteTaskCompleteListener addFavoriteListener = new AddFavoriteTaskCompleteListener();
                            new AddFavoriteTask(addFavoriteListener, getActivity()).execute(mMovie);
                        }
                    }
                });

            }
        }
        return detailView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (trailerList != null && trailerList.size() > 0){
            outState.putParcelableArrayList("trailers", trailerList);
        }
        if (reviewList != null && reviewList.size() > 0) {
            outState.putParcelableArrayList("reviews", reviewList);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (mMovie != null) {
            getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (trailerList != null && trailerList.size() > 0) {
            mShareActionProvider.setShareIntent(createShareLinkIntent());
        }
    }

    @SuppressWarnings("deprecation")
    private Intent createShareLinkIntent() {
        String firstKey = trailerList.get(0).getKey();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        } else {
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, TRAILER_LINK + firstKey );
        return shareIntent;
    }

    public class FetchReviewsTaskCompleteListener implements AsyncTaskCompleteListener<ArrayList<Review>>
    {

        @Override
        public void onTaskComplete(ArrayList<Review> result)
        {
            if (result != null && mReviewAdapter != null) {
                if(result.size() == 0){
                    mReviewsListView.setVisibility(View.GONE);
                    mReviewHeaderTextView.setText(R.string.no_reviews);
                }
                mReviewAdapter.clear();
                mReviewAdapter.addAll(result);
                reviewList = result;
            }
        }
    }

    public class FetchTrailersTaskCompleteListener implements AsyncTaskCompleteListener<ArrayList<Trailer>>
    {

        @Override
        public void onTaskComplete(ArrayList<Trailer> result)
        {
            if (result != null && mTrailerAdapter != null) {
                if(result.size() == 0){
                    mTrailersListView.setVisibility(View.GONE);
                    mTrailerHeaderTextView.setText(R.string.no_trailers);
                }
                mTrailerAdapter.clear();
                mTrailerAdapter.addAll(result);
                trailerList = result;
                if (mShareActionProvider != null && trailerList.size() > 0) {
                    mShareActionProvider.setShareIntent(createShareLinkIntent());
                }
            }
        }
    }

    public class DeleteFavoriteTaskCompleteListener implements AsyncTaskCompleteListener<Integer>
    {

        @Override
        public void onTaskComplete(Integer rowsDeleted) {
            if (rowsDeleted > 0) {
                mButton.setText(R.string.add_to_favorites);
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(getActivity(), getString(R.string.movie_removed), Toast.LENGTH_SHORT);
                mToast.show();
            }
        }
    }

    public class AddFavoriteTaskCompleteListener implements AsyncTaskCompleteListener<Uri>
    {
        @Override
        public void onTaskComplete(Uri addedUri) {
            mButton.setText(R.string.remove_from_favorites);
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(getActivity(), getString(R.string.movie_added), Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        int id = mMovie.getId();

        return new CursorLoader(
                getActivity(),                                      // context
                MovieContract.MovieEntry.CONTENT_URI,               // uri
                MOVIE_COLUMNS,                                      // projection
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",  // selection
                new String[] { Integer.toString(id) },              // seletionArgs
                null                                                // sort order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        /**
         * If the cursor comes back with an entry, the movie is a favorite, so the button should say "Remove From Favorites"
         * else the button should say "Add To Favorites"
         */
        if (data != null && data.moveToFirst()) {
            mButton.setText(R.string.remove_from_favorites);
            mIsAFavorite = true;

        } else {
            mButton.setText(R.string.add_to_favorites);
            mIsAFavorite = false;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
