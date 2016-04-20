# Movies
An Android App for finding popular and highest-rated movies.

This app requires the use of an API Key from themoviedb.org

In the app/build.gradle file the following code can be added to the buildTypes block:

buildTypes.each {
            it.buildConfigField 'String', 'THE_MOVIE_DB_API_KEY', ' "[moviedb.org API Key Here]" '
        }
        
  Replace [moviedb.org API Key Here] with your API Key.      
