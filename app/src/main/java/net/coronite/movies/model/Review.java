package net.coronite.movies.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;


public class Review implements Parcelable {

    private String id;
    private String author;
    private String content;

    public Review() {
    }

    /**
     * Constructs a Review Object from a JSONObject
     * @param review the JSONObject for the review.
     */

    public Review(JSONObject review) throws JSONException {
        this.id = review.getString("id");
        this.author = review.getString("author");
        this.content = review.getString("content");
    }

    private Review(Parcel in){
        id = in.readString();
        author = in.readString();
        content = in.readString();
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
     * @return id the movie identifier from themoviedb.org API.
     */
    public String getId() { return id; }

    /**
     * @return author of the movie review.
     */
    public String getAuthor() { return author; }

    /**
     * @return content the content of the review.
     */
    public String getContent() { return content; }

    /**
     * Flatten this object into a Parcel.
     * @param parcel the parcel in which the object should be written
     * @param i Additional flags about how the object should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(author);
        parcel.writeString(content);
    }

    /**
     * Interface that must be implemented and provided as a public CREATOR field that generates instances of your Parcelable class from a Parcel.
     */
    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {

        /**
         * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by Parcelable.writeToParcel().
         * @param parcel The Parcel containing the Review data.
         * @return the Parcel as a Review.
         */
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        /**
         * Create a new array of the Parcelable class.
         * @param i the size of the array (the number of reviews).
         * @return an array of the Parcelable class, with every entry initialized to null.
         */
        @Override
        public Review[] newArray(int i) {
            return new Review[i];
        }

    };
}

