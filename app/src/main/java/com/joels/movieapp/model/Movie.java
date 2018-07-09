package com.joels.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Movie implements Parcelable {

    @Id
    Long id;
    String name;
    String genre;
    String desc;
    String img_url;

    public Movie() {

    }

    public Movie(Parcel in) {
        id = in.readLong();
        name = in.readString();
        genre = in.readString();
        desc = in.readString();
        img_url = in.readString();
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(genre);
        parcel.writeString(desc);
        parcel.writeString(img_url);
    }
}
