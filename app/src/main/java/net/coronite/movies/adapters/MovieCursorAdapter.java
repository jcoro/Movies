package net.coronite.movies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.coronite.movies.R;
import net.coronite.movies.model.MovieItem;

/**
 * Manages the cursor that's needed when favorite movies are fetched from the database.
 */
public class MovieCursorAdapter extends CursorAdapter {

    public static class ViewHolder {
        public final ImageView posterView;

        public ViewHolder(View view) {
            posterView = (ImageView) view.findViewById(R.id.movie_list_item_image_poster);
        }
    }

    // Default constructor
    public MovieCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        MovieItem movie = new MovieItem(cursor);
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String imageUrl = MovieItem.IMAGE_BASE_PATH + movie.getPosterPath();
        Picasso.with(context).load(imageUrl).error(R.drawable.no_image).into(viewHolder.posterView);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }
}
