package net.coronite.movies;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * A Movie object with data from themoviedb.org API.
 */
public class MovieItem implements Parcelable {
    public static final String IMAGE_BASE_PATH = "http://image.tmdb.org/t/p/w185";
    int id;
    String poster_path;
    String title;
    String overview;
    String releaseDate;
    Double voteAverage;

    /**
     * Constructs a MovieItem object.
     * @param movieId the identifier for the movie.
     * @param posterPath the path to the poster jpg.
     * @param title the title of the movie.
     * @param overview the overview of the movie.
     * @param releaseDate the release date of the movie as a string with format "yyyy-MM-dd".
     * @param voteAverage the average rating of the movie.
    */
    public MovieItem(int movieId, String posterPath, String title, String overview, String releaseDate, Double voteAverage )
    {
        this.id = movieId;
        this.poster_path = posterPath;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    private MovieItem(Parcel in){
        id = in.readInt();
        poster_path = in.readString();
        title = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's marshalled representation. (Not used here).
     * @return a bitmask indicating the set of special object types marshalled by the Parcelable. (Not used here, so return 0);
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @return poster_path from themoviedb.org API.
     */
    public String getPoster_path(){
        return poster_path;
    }

    /**
     * @return id the movie identifier from themoviedb.org API.
     */
    public int getId() {
        return id;
    }

    /**
     * Flatten this object into a Parcel.
     * @param parcel the parcel in which the object should be written
     * @param i Additional flags about how the object should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(poster_path);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeDouble(voteAverage);
    }

    /**
     * Interface that must be implemented and provided as a public CREATOR field that generates instances of your Parcelable class from a Parcel.
     */
    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {

        /**
         * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by Parcelable.writeToParcel().
         * @param parcel The Parcel containing the MovieItem data.
         * @return the Parcel as a MovieItem.
         */
        @Override
        public MovieItem createFromParcel(Parcel parcel) {
            return new MovieItem(parcel);
        }

        /**
         * Create a new array of the Parcelable class.
         * @param i the size of the array (the number of movies).
         * @return an array of the Parcelable class, with every entry initialized to null.
         */
        @Override
        public MovieItem[] newArray(int i) {
            return new MovieItem[i];
        }

    };

}
