package net.coronite.movies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * The {@code MovieAdapter} is a subclass of {@code ArrayAdapter} for displaying movie posters in a
 * grid layout.
 */
public class MovieAdapter extends ArrayAdapter<MovieItem> {

    /**
     * This is a custom constructor.
     * The context is used to inflate the layout file, and movies is the list of MovieItems we want
     * to populate into the GridView.
     *
     * @param context The current context. Used to inflate the layout file.
     */
    public MovieAdapter(Activity context) {
        super(context, 0);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the Movie object from the ArrayAdapter at the appropriate position
        MovieItem movie = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_list_item, parent, false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.movie_list_item_image_poster);
        imageView.setTag(movie);
        String imageUrl = MovieItem.IMAGE_BASE_PATH + movie.getPoster_path();
        Picasso.with(getContext()).load(imageUrl).error(R.drawable.no_image).into(imageView);

        return convertView;
    }
}
