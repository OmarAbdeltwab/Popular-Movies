package com.example.android.topmovies;

/**
 * Created by maom3 on 21-Mar-18.
 */

public class Movie {

final private String title,poster_path,release_date,overview;
final private double vote_average;
   public Movie(String title)
   {
       this(title,"NotAvaialble","NotAvaialble","NotAvaialble",-1);

   }
    public Movie(String title , String poster_path, String release_date, String overview, double vote_average) {
        this.title = title;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.overview = overview;
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getOverview() {
        return overview;
    }

    public double getVote_average() {
        return vote_average;
    }
}
