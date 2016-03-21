package net.coronite.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Encapsulates showing the:
 * title
 * poster image
 * rating
 * release year
 * vote average
 * overview
 * for the selected movie.
 */
public class DetailFragment extends Fragment {


    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MovieItem movie;
        ImageView view;
        String voteAverage;

        View detailView =  inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            movie = intent.getParcelableExtra("movie");
            ((TextView) detailView.findViewById(R.id.detail_movie_title)).setText(movie.title);
            view = (ImageView) detailView.findViewById(R.id.detail_movie_image);
            Picasso.with(getActivity()).load(MovieItem.IMAGE_BASE_PATH + movie.poster_path).into(view);
            ((TextView) detailView.findViewById(R.id.detail_movie_release_year)).setText( getYear(movie.releaseDate) );
            voteAverage = movie.voteAverage.toString() + " / 10";
            ((TextView) detailView.findViewById(R.id.detail_movie_rating)).setText(voteAverage);
            ((TextView) detailView.findViewById(R.id.detail_movie_overview)).setText(movie.overview);
        }

        return detailView;
    }

    /**
     * Takes the date string, and returns the year as a string.
     * @param date the date string in the format "yyyy-MM-dd".
     * @return the year as a string in the format "yyyy".
     */

    private String getYear(String date){
        String[] dateArray;
        dateArray = date.split("-", 3);
        return dateArray[0];
    }
}
