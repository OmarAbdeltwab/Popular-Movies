package com.example.android.topmovies;

/**
 * Created by maom3 on 21-Mar-18.
 */

public class Movie {

final private String title,poster_path,release_date,overview;
final private double vote_average;
final private Long id;
   public Movie(Long id,String title,String path)
   {
       this(title,path,"NotAvaialble","NotAvaialble",-1,id);

   }
    public Movie(String title , String poster_path, String release_date, String overview, double vote_average,Long id) {
        this.title = title;
        this.id=id;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.overview = overview;
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
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
