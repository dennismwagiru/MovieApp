package com.joels.movieapp;

import android.support.multidex.MultiDexApplication;

import com.joels.movieapp.model.MyObjectBox;

import io.objectbox.BoxStore;

public class MovieApp extends MultiDexApplication {

    private static BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();

        boxStore = MyObjectBox.builder().androidContext(this).build();
    }

    public static BoxStore getBoxStore() {
        return boxStore;
    }
}
