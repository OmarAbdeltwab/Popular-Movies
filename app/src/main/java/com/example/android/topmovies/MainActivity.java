package com.example.android.topmovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.topmovies.data.MovieContract;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener {
    ArrayList<Movie> MoviesList = new ArrayList<>(  );
    RecyclerView MoviesRecyclerView;
    RecyclerView.LayoutManager mLayoutManger;
    MovieAdapter movieAdapter;
    ProgressBar progressBar;
    Gson a = new Gson();
    Cursor Favorites ;
    ArrayList<Movie> FavoriteMoviesList =new ArrayList<>(  );
    final String LAYOUT_STATE = "layoutstate";
    Parcelable Movies_Rv_Layout_state=null;
    private static final String SELECTED_PREFRENCE_KEY ="Selectedpref",TITLE="title" ;

    MovieAdapter.MovieItemClickListener Mov;
    @Override

    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        MoviesRecyclerView = (RecyclerView) findViewById( R.id.rv_MoviesRV );
        mLayoutManger= new GridLayoutManager(this,2);

        MoviesRecyclerView.setLayoutManager( mLayoutManger );

        Mov=this;
        MoviesList.clear();
        progressBar=findViewById( R.id.pb_Progress );
        if (savedInstanceState!=null)
        Selected_Prefrence= Utils.SortMovies.valueOf(  savedInstanceState.getString( SELECTED_PREFRENCE_KEY ));
        //get data and fill Recycler View
        new GetMovieData().execute( );

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    Utils.SortMovies Selected_Prefrence=Utils.SortMovies.TOP_RATED;
    //toggle Sort Mode
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId ==R.id.mi_popular_Toprated) {

           if (item.getTitle()== getString(  R.string.most_popular))
           {

               MoviesRecyclerView.setAdapter( null );
               Selected_Prefrence=Utils.SortMovies.POPULAR;
               new GetMovieData().execute(  );
               item.setTitle( getString( R.string.top_rated ) );
               this.setTitle( getString( R.string.most_pop_title) );

           }

           else
           {
               MoviesRecyclerView.setAdapter( null );
               Selected_Prefrence=Utils.SortMovies.TOP_RATED;
               new GetMovieData().execute( );
               item.setTitle( getString( R.string.most_popular ) );
               this.setTitle( getString( R.string.top_rated_title) );



           }
            return true;
        }
         else if (itemThatWasClickedId==R.id.my_favorite)
        {Selected_Prefrence=Utils.SortMovies.FAV;
            new GetMovieData().execute(  );
            this.setTitle( "Favorites" );



        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );
        outState.putParcelable(LAYOUT_STATE, MoviesRecyclerView .getLayoutManager().onSaveInstanceState());
        outState.putString( TITLE, (String) this.getTitle() );
        outState.putString( SELECTED_PREFRENCE_KEY, Selected_Prefrence.toString() );

    }
    @Override

    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null)
        {String tempTitle="";
            this.setTitle( savedInstanceState.getString(TITLE) );
Selected_Prefrence=(Utils.SortMovies.valueOf(  savedInstanceState.getString( SELECTED_PREFRENCE_KEY )));



    Movies_Rv_Layout_state = savedInstanceState.getParcelable(LAYOUT_STATE);
            MoviesRecyclerView.getLayoutManager().onRestoreInstanceState(Movies_Rv_Layout_state);
        }
    }
    //open Another activity and pass all the Data
    @Override
    public void OnMovieClick(int pos) {
        Movie TempMovie =((MovieAdapter)MoviesRecyclerView.getAdapter()).MoviesArrayList.get( pos );
        Intent intent =new Intent( this ,MovieDetailsActivity.class )
                .putExtra( Utils.MOVIE_TITLE ,TempMovie.getTitle()  )
                .putExtra( Utils.OVERVIEW ,TempMovie.getOverview()  )
                .putExtra( Utils.POSTER_BATH ,TempMovie.getPoster_path()  )
                .putExtra( Utils.RELEASE_DATE ,TempMovie.getRelease_date()  )
                .putExtra( Utils.VOTE ,TempMovie.getVote_average() )
                .putExtra( Utils.ID,TempMovie.getId() )
                ;
        Boolean IS_Favorite= false ;

        if (Favorites!=null && Favorites.moveToFirst() ) {


            for(Favorites.moveToFirst(); !Favorites.isAfterLast(); Favorites.moveToNext()) {
                if (TempMovie.getId()==Favorites.getLong( Favorites.getColumnIndex( MovieContract.FavoritMovieEntery.Movie_id_column )))
                    IS_Favorite=true;

            }}

                if(IS_Favorite)
        {
            intent. putExtra( "IsFavorite",true );

        }
        startActivity( intent );


    }


    public class  GetMovieData extends AsyncTask<Utils.SortMovies , Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //show progress bar
            progressBar.setVisibility( View.VISIBLE );
            progressBar.bringToFront();

            //hide until load
            MoviesRecyclerView.setVisibility( View.INVISIBLE );


        }

        @Override
        protected String doInBackground(Utils.SortMovies... params) {






            if (Selected_Prefrence== Utils.SortMovies.FAV)
            {

                Favorites= getContentResolver().query( MovieContract.FavoritMovieEntery.CONTENT_URI,
                        null,
                        null,
                        null,
                        MovieContract.FavoritMovieEntery.title_column );
                Utils.Load_Favorites( Favorites, FavoriteMoviesList );
                return "FAV";
            }
            String MoviesResponse = null;
            try {
                MoviesResponse = Utils.getMoviesResponse(Selected_Prefrence);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Favorites= getContentResolver().query( MovieContract.FavoritMovieEntery.CONTENT_URI,
                        null,
                        null,
                        null,
                        MovieContract.FavoritMovieEntery.title_column );

            } catch (Exception e) {
                Log.e("LOADER", "Failed to asynchronously load data.");
                e.printStackTrace();
                Favorites= null;
            }
            return MoviesResponse;
        }


        @Override
        protected void onPostExecute(String Results) {
            if (Results != null && !Results.equals("")) {
                if (Results!="FAV") {
                    MoviesList = Utils.GetFirstMovie( Results );

                    movieAdapter = new MovieAdapter( MoviesList, Mov );
                    MoviesRecyclerView.setAdapter( movieAdapter );

                    Utils.GetAllMovies( Results, movieAdapter );



                    // Load Favorites in case of normal Loading top monies or most popular
                    Utils.Load_Favorites( Favorites, FavoriteMoviesList );
                }
               else //in case of Favotites option selected
                {
                    movieAdapter = new MovieAdapter( FavoriteMoviesList, Mov );
                    MoviesRecyclerView.setAdapter( movieAdapter );

                }

                progressBar.setVisibility( View.INVISIBLE );
               }


                if (Movies_Rv_Layout_state!=null)
                    MoviesRecyclerView.getLayoutManager().onRestoreInstanceState(Movies_Rv_Layout_state);

                MoviesRecyclerView.setVisibility(View.VISIBLE );
            }
        }

}


