package com.joels.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Movie implements Parcelable {

//    private final String basePath = "http://image.tmdb.org/t/p/w185";

    @Id
    Long id;
    public String imdbId;
    public String voteCount;
    public String voteAverage;
    public String popularity;
    public String posterPath;
    public String backdropPath;
    public String title;
    public String originalTitle;
    public String overview;
    public String releaseDate;
    public String youtubeId;
    public String tagLine;
    public String runtime;
    public String budget;
    public String revenue;
    public String category;

    public Movie() {

    }

    public Movie(String imdbId, String voteCount, String voteAverage, String popularity,
                 String posterPath, String backdropPath, String title, String originalTitle, String overview,
                 String releaseDate, String category) {
        this.imdbId = imdbId;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.category = category;
    }


    protected Movie(Parcel in) {
        id = in.readLong();
        imdbId = in.readString();
        voteCount = in.readString();
        voteAverage = in.readString();
        popularity = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        title = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        youtubeId = in.readString();
        tagLine = in.readString();
        runtime = in.readString();
        budget = in.readString();
        revenue = in.readString();
        category = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(imdbId);
        dest.writeString(voteCount);
        dest.writeString(voteAverage);
        dest.writeString(popularity);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(youtubeId);
        dest.writeString(tagLine);
        dest.writeString(runtime);
        dest.writeString(budget);
        dest.writeString(revenue);
        dest.writeString(category);
    }
}
