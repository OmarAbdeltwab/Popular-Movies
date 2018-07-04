package com.example.android.topmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {
    TextView Movie_Title,Movie_Date,Movie_Vote,Movie_OverView;
    ImageView Movie_Poster;
    RatingBar Vote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_movie_details );
        //find all Views
        Movie_Title = (TextView) findViewById( R.id.tv_MovieName ) ;
        Movie_Date= findViewById( R.id.tv_Date );
        Movie_Poster=findViewById( R.id.iv_poster );
        Movie_Vote=findViewById( R.id.tv_vote );
        Vote=findViewById( R.id.rb_Rating );
        Movie_OverView=findViewById( R.id.tv_OverView );

        Intent StartedIntent= getIntent();

        //set all data
        if( StartedIntent.hasExtra( Utils.MOVIE_TITLE ))
        {
            Movie_Title.setText( StartedIntent.getStringExtra( Utils.MOVIE_TITLE ) );
            Movie_Date.setText( StartedIntent.getStringExtra( Utils.RELEASE_DATE ) );
            Picasso.with( this ).load( getString(R.string.Image_Base_Bath)+ StartedIntent.getStringExtra( Utils.POSTER_BATH ) ).into( Movie_Poster );

            Vote.setNumStars( 5 ); Vote.setEnabled( false );
            Vote.setRating((float) (StartedIntent.getDoubleExtra( Utils.VOTE ,1)/2 ));

            Movie_Vote.setText( String.valueOf(  StartedIntent.getDoubleExtra( Utils.VOTE ,1) ) + "/" +String.valueOf( 10 ));

            Movie_OverView.setText( StartedIntent.getStringExtra( Utils.OVERVIEW ) );

        }


    }
}
