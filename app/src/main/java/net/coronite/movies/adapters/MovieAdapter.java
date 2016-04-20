package net.coronite.movies.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.coronite.movies.R;
import net.coronite.movies.model.MovieItem;

/**
 * The {@code MovieAdapter} is a subclass of {@code ArrayAdapter} for displaying movie posters in a
 * gridView.
 */
public class MovieAdapter extends ArrayAdapter<MovieItem> {

    public static class ViewHolder {
        public final ImageView posterView;

        public ViewHolder(View view) {
            posterView = (ImageView) view.findViewById(R.id.movie_list_item_image_poster);
        }
    }

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

        ViewHolder viewHolder;

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Gets the Movie object from the ArrayAdapter at the appropriate position
        MovieItem movie = getItem(position);
        String imageUrl = MovieItem.IMAGE_BASE_PATH + movie.getPosterPath();
        Picasso.with(getContext()).load(imageUrl).error(R.drawable.no_image).into(viewHolder.posterView);

        return convertView;
    }
}
