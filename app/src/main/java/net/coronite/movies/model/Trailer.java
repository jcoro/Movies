package net.coronite.movies.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Trailer implements Parcelable {

    private String id;
    private String key;
    private String name;
    private String site;
    private String type;

    public Trailer() {

    }

    /**
     * Constructs a Trailer Object from a JSONObject
     * @param trailer the JSONObject for the review.
     */

    public Trailer(JSONObject trailer) throws JSONException {
        this.id = trailer.getString("id");
        this.key = trailer.getString("key");
        this.name = trailer.getString("name");
        this.site = trailer.getString("site");
        this.type = trailer.getString("type");
    }

    private Trailer(Parcel in){
        id = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        type = in.readString();
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
    public String getId() {
        return id;
    }

    /**
     * @return key the trailer key that needs to be added to the base Youtube url.
     */
    public String getKey() { return key; }

    /**
     * @return name the name of the trailer from themoviedb.org API.
     */
    public String getName() { return name; }

    /**
     * @return site the website where the trailer is located.
     */
    public String getSite() { return site; }

    /**
     * @return type the type of video e.g., "Trailer".
     */
    public String getType() { return type; }

    /**
     * Flatten this object into a Parcel.
     * @param parcel the parcel in which the object should be written
     * @param i Additional flags about how the object should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(key);
        parcel.writeString(name);
        parcel.writeString(site);
        parcel.writeString(type);
    }

    /**
     * Interface that must be implemented and provided as a public CREATOR field that generates instances of your Parcelable class from a Parcel.
     */
    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {

        /**
         * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by Parcelable.writeToParcel().
         * @param parcel The Parcel containing the Review data.
         * @return the Parcel as a Review.
         */
        @Override
        public Trailer createFromParcel(Parcel parcel) {
            return new Trailer(parcel);
        }

        /**
         * Create a new array of the Parcelable class.
         * @param i the size of the array (the number of trailers).
         * @return an array of the Parcelable class, with every entry initialized to null.
         */
        @Override
        public Trailer[] newArray(int i) {
            return new Trailer[i];
        }

    };
}
