package com.example.android.topmovies;

import android.database.Cursor;

import com.example.android.topmovies.data.MovieContract;

import java.net.URL;

/**
 * Created by maom3 on 21-Mar-18.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 * and Get All Movies Data and set the Movies Array List
 */

public  class Utils {

    public static  final String MOVIES_JSON_ARRAY_NAME ="results"
                         ,  MOVIE_TITLE="title",
                            VOTE ="vote_average",
                            POSTER_BATH="poster_path"   ,
                            RELEASE_DATE ="release_date",
                            OVERVIEW="overview",ID="id";
    public enum SortMovies{POPULAR,TOP_RATED,FAV};


    private final static String  MOVIES_DB_BASE="http://api.themoviedb.org/3/movie/",
                         POPULAR_BASE= "popular",
                         API_KEY="?api_key=",/*API Key Removed*/
                         TOPRATED_BASE = "top_rated" ;//, API_QUERY="";


    /**
     * This method sets  only the first Movie on the list
     *
     *
     * @param  JsonData all data returned from server
     *
     */

    public static ArrayList<Movie> GetFirstMovie(String JsonData)
    {


        ArrayList<Movie> AllMovies = new ArrayList<>(  );
        try {
            JSONObject AllData = new JSONObject(JsonData);
            JSONArray MoviesJsonArray=AllData.getJSONArray( MOVIES_JSON_ARRAY_NAME );


                JSONObject TempMovie= MoviesJsonArray.getJSONObject( 0 );
               //use Constructor To set all data at once
                AllMovies.add(  new Movie(
                        TempMovie.getString( MOVIE_TITLE ),
                        TempMovie.getString( POSTER_BATH ),
                        TempMovie.getString( RELEASE_DATE ),
                        TempMovie.getString( OVERVIEW ),
                        TempMovie.getDouble( VOTE ),
                        TempMovie.getLong(  ID)

                ));






        } catch (JSONException e) {
            e.printStackTrace();
        }


        return AllMovies;
    }

public  static void Load_Favorites (Cursor Favorites,ArrayList<Movie> FavoritMoviess){
        if (Favorites!=null && Favorites.moveToFirst() ) {
        FavoritMoviess.clear();

        for(Favorites.moveToFirst(); !Favorites.isAfterLast(); Favorites.moveToNext()) {

            FavoritMoviess.add( new Movie   (
                    Favorites.getString( Favorites.getColumnIndex( MovieContract.FavoritMovieEntery.title_column ) )
                    , Favorites.getString( Favorites.getColumnIndex( MovieContract.FavoritMovieEntery.poster_path_column ) )
                    , Favorites.getString( Favorites.getColumnIndex( MovieContract.FavoritMovieEntery.release_date_column ))
                    , Favorites.getString( Favorites.getColumnIndex( MovieContract.FavoritMovieEntery.overview_column ))
                    , Favorites.getDouble( Favorites.getColumnIndex( MovieContract.FavoritMovieEntery.vote_average_column ))
                    , Favorites.getLong  ( Favorites.getColumnIndex( MovieContract.FavoritMovieEntery.Movie_id_column ) )
            ));

        }
    }
}
    /**
     * This take jason data and set the rest of the Movies
     *
     *
     * @param  JsonData all data from response
     * @param  AllMoviesRVA    RecyclerView Adapter
     *
     *
     */
    public static void GetAllMovies(String JsonData , MovieAdapter AllMoviesRVA)
    {


       ArrayList<Movie> AllMovies = AllMoviesRVA.MoviesArrayList;

        try {
            JSONObject AllData = new JSONObject(JsonData);
            JSONArray MoviesJsonArray=AllData.getJSONArray( MOVIES_JSON_ARRAY_NAME );

           for(int i =1 ;i<MoviesJsonArray.length();i++)
           {
               JSONObject TempMovie= MoviesJsonArray.getJSONObject( i );
               AllMovies.add(  new Movie(
                                            TempMovie.getString( MOVIE_TITLE ),
                                            TempMovie.getString( POSTER_BATH ),
                                            TempMovie.getString( RELEASE_DATE ),
                                            TempMovie.getString( OVERVIEW ),
                                            TempMovie.getInt( VOTE ),TempMovie.getLong( ID )));
              // AllMoviesRVA.notifyDataSetChanged();



           }



        } catch (JSONException e) {
            e.printStackTrace();
        }


      //  return AllMovies;
    }
    public static InputStream getResponseStream (String Url) throws IOException

    { InputStream ret=null;
        try {
            ret = new URL(Url).openConnection().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
return ret;
    }

    /**
             * This method returns the entire result from the HTTP response as String.
             *
             *
             * @param  sortby Set Sort method TopRated/Popular
             * @return The contents of the HTTP response as asingle String .
             * @throws IOException Related to network and stream reading
             */
    public static String getMoviesResponse ( SortMovies sortby ) throws IOException {
        StringBuilder builder = new StringBuilder(  );
        builder.append(MOVIES_DB_BASE);
        switch (sortby)
        {
            case POPULAR:

                builder.append(POPULAR_BASE);
                builder.append( API_KEY );
                break;
            case TOP_RATED:
                builder.append(TOPRATED_BASE);
                builder.append( API_KEY );
                break;
            case FAV:
              return null;

                default:break;
        }




        String CompleteUrl = builder.toString();
        String AllData="";
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(CompleteUrl ).openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            AllData =scanner.useDelimiter("\\A").next();

            boolean hasInput = scanner.hasNext();


                return AllData;
            }
            catch (Exception a)
            {}
        finally {
            urlConnection.disconnect();
        }

        return AllData;
    }
}
