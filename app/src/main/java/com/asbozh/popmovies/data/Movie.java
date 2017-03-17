package com.asbozh.popmovies.data;


import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String movieTitle;
    private String moviePoster;
    private String movieOverview;
    private double movieRating;
    private String movieReleaseDate;
    private int movieId;

    public Movie(String movieTitle, String moviePoster, String movieOverview, double movieRating, String movieReleaseDate, int movieId) {
        this.movieTitle = movieTitle;
        this.moviePoster = moviePoster;
        this.movieOverview = movieOverview;
        this.movieRating = movieRating;
        this.movieReleaseDate = movieReleaseDate;
        this.movieId = movieId;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public double getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(double movieRating) {
        this.movieRating = movieRating;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }
    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }


    protected Movie(Parcel in) {
        movieTitle = in.readString();
        moviePoster = in.readString();
        movieOverview = in.readString();
        movieRating = in.readDouble();
        movieReleaseDate = in.readString();
        movieId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieTitle);
        dest.writeString(moviePoster);
        dest.writeString(movieOverview);
        dest.writeDouble(movieRating);
        dest.writeString(movieReleaseDate);
        dest.writeInt(movieId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


}
