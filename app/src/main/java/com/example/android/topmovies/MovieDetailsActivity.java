package com.example.android.topmovies;


import android.content.ContentValues;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.topmovies.ReviewsUtils.ReviewAdabter;
import com.example.android.topmovies.ReviewsUtils.Reviews;
import com.example.android.topmovies.TrailerUtils.Trailer;
import com.example.android.topmovies.TrailerUtils.TrailerAdabter;
import com.example.android.topmovies.TrailerUtils.Trailers;
import com.example.android.topmovies.data.MovieContract;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> , TrailerAdabter.TrailerClickListneer {

    private static final String TRAILERLIST ="trailers",REVIEWSLIST ="Reviews" ;
    TextView Movie_Title,Movie_Date,Movie_Vote,Movie_OverView;
    ImageView Movie_Poster;
    RatingBar Vote;
    Long ID ;
    final int  TrailerLoaderID = 42;
    RecyclerView Trailer_RV , Review_RV;
    TrailerAdabter mTrailerAdabter;
    ReviewAdabter mReviewAdabter;
    Trailers trailers;
    Reviews reviews;
    Switch FavSwitch;
    ScrollView mScrollView;

     private String title,poster_path,release_date,overview;
     private double vote_average;

    //Movie Current_Movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_movie_details );
        //find all Views
        Movie_Title = (TextView) findViewById( R.id.tv_MovieName );
        Movie_Date = findViewById( R.id.tv_Date );
        Movie_Poster = findViewById( R.id.iv_poster );
        Movie_Vote = findViewById( R.id.tv_vote );
        Vote = findViewById( R.id.rb_Rating );
        Movie_OverView = findViewById( R.id.tv_OverView );
        Trailer_RV = findViewById( R.id.RV_Trailers );
        Review_RV=findViewById( R.id.RV_Reviews );
        FavSwitch=findViewById( R.id.favSwitch );
        mScrollView=findViewById( R.id.sv_all_info );
        Intent StartedIntent = getIntent();
        Bundle IdBundel = new Bundle(  );

        //


        //set all data
        if (StartedIntent.hasExtra( Utils.MOVIE_TITLE ))
        {



            Movie_Title.setText(title= StartedIntent.getStringExtra( Utils.MOVIE_TITLE ) );
           this.setTitle( title );

            Movie_Date.setText (release_date= StartedIntent.getStringExtra( Utils.RELEASE_DATE ) );
            Picasso.with( this ).load( getString( R.string.Image_Base_Bath ) +(poster_path= StartedIntent.getStringExtra( Utils.POSTER_BATH ) )).into( Movie_Poster );

            Vote.setNumStars( 5 );
            Vote.setEnabled( false );
            Vote.setRating((float)( vote_average=  (StartedIntent.getDoubleExtra( Utils.VOTE, 1 ) ) )/2);
            ID=StartedIntent.getLongExtra( Utils.ID ,-3);
            Movie_Vote.setText( String.valueOf( vote_average ) + "/" + String.valueOf( 10 ) );

            if (StartedIntent.getBooleanExtra( "IsFavorite",false ))
            {   FavSwitch.setChecked( true );
                FavSwitch.setText( "Remove From Favorites" );
            }

            Movie_OverView.setText(overview= StartedIntent.getStringExtra( Utils.OVERVIEW ) );

        }

        IdBundel.putLong( Utils.ID, this.ID );
        getSupportLoaderManager().initLoader(TrailerLoaderID, IdBundel,  this );

    }

        void LoadTrailers(InputStream stream)
    {
        InputStream source = stream;

        Gson gson = new Gson();

        Reader reader = new InputStreamReader(source);

         trailers = gson.fromJson(reader, Trailers.class);




    }
    void LoadReviews(InputStream stream)
    {
        InputStream source = stream;

        Gson gson = new Gson();

        Reader reader = new InputStreamReader(source);

        reviews = gson.fromJson(reader, Reviews.class);




    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );
        outState.putIntArray("SCROLL_POS",
             new int[]{mScrollView.getScrollX(),mScrollView.getScrollY()});


        outState.putSerializable( TRAILERLIST ,trailers);
        outState.putSerializable( REVIEWSLIST ,reviews);
    }
    @Override

    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null)
        {
            trailers=(Trailers)savedInstanceState.getSerializable( TRAILERLIST );
            reviews=(Reviews)savedInstanceState.getSerializable( REVIEWSLIST );

            final int[] position = savedInstanceState.getIntArray("SCROLL_POS");
            if(position != null)
                mScrollView.post(new Runnable() {
                    public void run() {
                        mScrollView.scrollTo(position[0], position[1]);
                    }
                });
        SetAdapters();
        }

    }
    void SetAdapters() {
        Trailer_RV.setLayoutManager( new LinearLayoutManager( this ) );
        mTrailerAdabter= new TrailerAdabter( this, trailers);
        Trailer_RV.setAdapter( mTrailerAdabter );

        Review_RV.setLayoutManager(  new LinearLayoutManager( this ));
        mReviewAdabter=new ReviewAdabter( reviews );
        Review_RV.setAdapter( mReviewAdabter );
    }


    public void OnFavoriteClick(View view) {
        if (FavSwitch.isChecked()) {
            FavSwitch.setText( "Remove From Favorites" );
            ContentValues contentValues = new ContentValues();
            // Put the task description and selected mPriority into the ContentValues
            contentValues.put( MovieContract.FavoritMovieEntery.Movie_id_column, ID );
            contentValues.put( MovieContract.FavoritMovieEntery.overview_column, overview );
            contentValues.put( MovieContract.FavoritMovieEntery.poster_path_column, poster_path );
            contentValues.put( MovieContract.FavoritMovieEntery.release_date_column, release_date );
            contentValues.put( MovieContract.FavoritMovieEntery.title_column, title );
            contentValues.put( MovieContract.FavoritMovieEntery.vote_average_column, vote_average );

            Uri uri = getContentResolver().insert( MovieContract.FavoritMovieEntery.CONTENT_URI, contentValues );


            if (uri != null) {
                Toast.makeText( getBaseContext(), "added to Favorites Successfully", Toast.LENGTH_LONG ).show();
            }
        }
        else {
            FavSwitch.setText( "Add to Favorites" );
            Uri uri = MovieContract.FavoritMovieEntery.CONTENT_URI;
            uri = uri.buildUpon().appendPath(String.valueOf(ID)).build();

            getContentResolver().delete(uri, null, null);
            Toast.makeText( getBaseContext(), "Removed From Favorites", Toast.LENGTH_LONG ).show();

        }


    }

    @Override
    public android.support.v4.content.Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this ) {
            @Override
            protected void onStartLoading() {
                if (args==null)
                    return;
                forceLoad();
            }

            @Override
            public String loadInBackground() {

                String Url="http://api.themoviedb.org/3/movie/"+ID+"/videos?api_key=b076ba9043bfdf02acc617c4059c7e7b";
                try {
                    LoadTrailers(Utils.getResponseStream( Url));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Url="http://api.themoviedb.org/3/movie/"+ID+"/reviews?api_key=b076ba9043bfdf02acc617c4059c7e7b";

                try {
                    LoadReviews(Utils.getResponseStream( Url));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;


            }
        }    ;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<String> loader, String data) {
        SetAdapters();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<String> loader) {

    }





    @Override
    public void OnTrailerClick(int Pos) {
        Toast t = Toast.makeText(this, trailers.getResults().get( Pos ).getName(),Toast.LENGTH_LONG );
        t.show();

        Uri Trailerwebpage = Uri.parse("https://www.youtube.com/watch?v="+trailers.getResults().get( Pos ).getKey());
        Intent intent = new Intent(Intent.ACTION_VIEW, Trailerwebpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
