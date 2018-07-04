package com.example.android.topmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by maom3 on 21-Mar-18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    ArrayList<Movie> MoviesArrayList;
    final private MovieItemClickListner movieItemClickListner;
     public MovieAdapter(ArrayList<Movie> moviesArrayLis, MovieItemClickListner movieItemClickListner)
     {               MoviesArrayList=moviesArrayLis;
                     this.movieItemClickListner = movieItemClickListner;

     }
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the itemList View

       return new MovieViewHolder(
                    LayoutInflater.from(parent.getContext())
                    .inflate( R.layout.movie_list_item,parent,false ) );
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieViewHolder holder, int position) {
holder.bind( position );
    }

    @Override
    public long getItemId(int position) {
        return MoviesArrayList.get( position ).hashCode();
    }

    @Override
    public int getItemCount() {
        return MoviesArrayList.size();
    }

    public interface MovieItemClickListner{
         void OnMovieClick(int Position);

    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView  Tiltle;
        ImageView IV_Poster;
        public MovieViewHolder(View itemView) {
            super( itemView );
            itemView.setOnClickListener( this );

            IV_Poster=itemView.findViewById( R.id.iv_Posetr );
            Tiltle=itemView.findViewById( R.id.tv_title);

        }


        public void  bind (final int Position)
        {


           //load Image
            Picasso.with(IV_Poster.getContext())
                    .load( IV_Poster.getContext().getString( R.string.Image_Base_Bath)+MoviesArrayList.get(Position).getPoster_path())
                    .into(IV_Poster, new Callback() {
                        //Show RecyclerView after Loading first Picture Successfully
                        @Override
                        public void onSuccess() {

                            if(Position==1)
                            {
                            RecyclerView TempRecycleView =((RecyclerView)IV_Poster.getParent().getParent());
                            if(TempRecycleView!=null)
                            if( TempRecycleView.getVisibility()==View.INVISIBLE )
                                TempRecycleView.setVisibility(View.VISIBLE );
                            }
                        }

                        @Override
                        public void onError() {

                        }


                    });

            //Set Title Below the Poster

            Tiltle.setText(MoviesArrayList.get( Position ).getTitle());

        }

        @Override
        public void onClick(View v) {
            movieItemClickListner.OnMovieClick(getAdapterPosition());

        }
    }

}




