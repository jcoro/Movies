# Movies App
This App is Projects 1 & 2 for the Udacity Android Developer Nanodegree. The Movies App showcases Gridviews, SQLite Databases, Content Providers, AsyncTasks, Parcelables, Intents, and Master-Detail Fragment Interfaces.

![Movies App](http://www.coronite.net/assets/img/github/project1-2.jpg)

## Android Features Implemented:

- [GridViews](https://developer.android.com/reference/android/widget/GridView.html)
- [SQLite Databases](https://developer.android.com/training/basics/data-storage/databases.html)
- [Content Providers](https://developer.android.com/reference/android/content/ContentProvider.html)
- [AsyncTasks](https://developer.android.com/reference/android/os/AsyncTask.html)
- [Parcelables](https://developer.android.com/reference/android/os/Parcelable.html)
- [Explicit and Implicit Intents](https://developer.android.com/reference/android/content/Intent.html)
- [Master-Detail Fragment Interfaces](https://developer.android.com/training/multiscreen/adaptui.html)

## Specifications
- `compileSdkVersion 23`
- `buildToolsVersion "23.0.2"`
- `minSdkVersion 15`
- `targetSdkVersion 23`

## Dependencies
- `compile 'com.android.support:appcompat-v7:23.2.0'`
- `testCompile 'junit:junit:4.12'`
- `compile 'com.android.support:design:23.2.0'`
- `compile 'com.squareup.picasso:picasso:2.5.2'`
- `compile 'com.android.support:support-v4:23.2.0'`


## Implementation
To obtain movie data, this App requires an API key from [themoviedatabase.org API](https://www.themoviedb.org/documentation/api). This key can be inserted in the `app`-level `build.gradle` file. In the following lines:
```
buildTypes.each {
  it.buildConfigField 'String', 'THE_MOVIE_DB_API_KEY', ' "" '
}
```
The API Key goes inside the double quotes. Note: double quotes inside single quotes are required to correctly access the API Key.

This sample uses the Gradle build system. To build this project, use the "gradlew build" command or use "Import Project" in Android Studio.

If you have any questions I'd be happy to try and help. Please contact me at: john@coronite.net.

# License
This is a public domain work under [CC0 1.0](https://creativecommons.org/publicdomain/zero/1.0/). Feel free to use it as you see fit.