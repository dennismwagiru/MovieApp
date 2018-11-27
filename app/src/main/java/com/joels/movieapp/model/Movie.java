package com.joels.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.objectbox.annotation.Id;

public class Movie implements Parcelable {

    private final String basePath = "http://image.tmdb.org/t/p/w185";

    @Id
    private float id;
    private String posterPath;
    private String titles;
    private String originalTitle;
    private String overview;
    private String releaseDate;

    public Movie() {

    }

    public Movie(String posterPath, String titles, String originalTitle, String overview, String releaseDate) {
        this.posterPath = posterPath;
        this.titles = titles;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    protected Movie(Parcel in) {
        id = in.readFloat();
        posterPath = in.readString();
        titles = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPosterPath() {
        return basePath + posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(id);
        dest.writeString(posterPath);
        dest.writeString(titles);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(releaseDate);
    }
}
