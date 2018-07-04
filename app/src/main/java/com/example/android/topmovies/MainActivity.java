package com.example.android.topmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListner {
    ArrayList<Movie> MoviesList = new ArrayList<>(  );
    RecyclerView MoviesRecyclerView;
    RecyclerView.LayoutManager mLayoutManger;
    MovieAdapter movieAdapter;
    ProgressBar progressBar;




    MovieAdapter.MovieItemClickListner Mov;
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

        //get data and fill Recycler View
        new GetMovieData().execute( Utils.SortMovies.POPULAR  );

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //toggle Sort Mode
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId ==R.id.mi_popular_Toprated) {

           if (item.getTitle()== getString(  R.string.most_popular))
           {

               MoviesRecyclerView.setAdapter( null );
               new GetMovieData().execute( Utils.SortMovies.POPULAR  );
               item.setTitle( getString( R.string.top_rated ) );

           }

           else
           {
               MoviesRecyclerView.setAdapter( null );
               new GetMovieData().execute( Utils.SortMovies.TOP_RATED  );
               item.setTitle( getString( R.string.most_popular ) );


           }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //open Another activity and pass all the Data
    @Override
    public void OnMovieClick(int pos) {
        Movie TempMovie =MoviesList.get( pos );
        Intent intent =new Intent( this ,MovieDetailsActivity.class )
                .putExtra( Utils.MOVIE_TITLE ,TempMovie.getTitle()  )
                .putExtra( Utils.OVERVIEW ,TempMovie.getOverview()  )
                .putExtra( Utils.POSTER_BATH ,TempMovie.getPoster_path()  )
                .putExtra( Utils.RELEASE_DATE ,TempMovie.getRelease_date()  )
                .putExtra( Utils.VOTE ,TempMovie.getVote_average() );
        startActivity( intent );


    }


    public class  GetMovieData extends AsyncTask<Utils.SortMovies , Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //show progress bar
            progressBar.setVisibility( View.VISIBLE );
            progressBar.bringToFront();

            //hide untill load
            MoviesRecyclerView.setVisibility( View.INVISIBLE );


        }

        @Override
        protected String doInBackground(Utils.SortMovies... params) {


            Utils.SortMovies SortBy=params[0];
            String MoviesResponse = null;
            try {
                MoviesResponse = Utils.getMoviesResponse(SortBy);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return MoviesResponse;
        }

        @Override
        protected void onPostExecute(String Results) {
            if (Results != null && !Results.equals("")) {

                MoviesList= Utils.GetFirstMovie(   Results );

                movieAdapter=new MovieAdapter( MoviesList, Mov);
                MoviesRecyclerView.setAdapter( movieAdapter );

                Utils.GetAllMovies( Results,movieAdapter );

                progressBar.setVisibility( View.INVISIBLE );
            }
        }
    }

}
