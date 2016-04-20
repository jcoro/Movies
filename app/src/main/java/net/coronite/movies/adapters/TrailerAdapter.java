package net.coronite.movies.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.coronite.movies.R;
import net.coronite.movies.model.Trailer;

/**
 * The {@code TrailerAdapter} is a subclass of {@code ArrayAdapter} for displaying links to movie trailers in a
 * ListView.
 */
public class TrailerAdapter extends ArrayAdapter<Trailer> {

    public static class ViewHolder {
        public final TextView trailerNameView;

        public ViewHolder(View view) {
            trailerNameView = (TextView) view.findViewById(R.id.trailer_list_item_name);
        }
    }

    /**
     * This is a custom constructor.
     * The context is used to inflate the layout file, and trailers is the list of Trailers we want
     * to populate into the ListView.
     *
     * @param context The current context. Used to inflate the layout file.
     */
    public TrailerAdapter(Activity context) {
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

        // Gets the Trailer object from the ArrayAdapter at the appropriate position
        Trailer trailer = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        if (trailer.getName() != null) {
            viewHolder.trailerNameView.setText(trailer.getName());
        }

        return convertView;
    }
}
