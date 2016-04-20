package net.coronite.movies;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Utility {
    /**
     * Takes the date string, and returns the year as a string.
     * @param date the date string in the format "yyyy-MM-dd".
     * @return the year as a string in the format "yyyy".
     */

    public static String getYear(String date){
        String[] dateArray;
        dateArray = date.split("-", 3);
        return dateArray[0];
    }

    public static String getSortByPreference(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_by_key),
                context.getString(R.string.pref_sort_by_value_popular));
    }
}
