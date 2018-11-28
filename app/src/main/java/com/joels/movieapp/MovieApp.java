package com.joels.movieapp;

import androidx.multidex.MultiDexApplication;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RetryPolicy;
import com.joels.movieapp.model.MyObjectBox;

import io.objectbox.BoxStore;

public class MovieApp extends MultiDexApplication {

    private static BoxStore boxStore;
    private static RetryPolicy policy;
    private static final int socketTimeout = 120000;

    @Override
    public void onCreate() {
        super.onCreate();

        boxStore = MyObjectBox.builder().androidContext(this).build();

        policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }

    public RetryPolicy getPolicy() {
        return policy;
    }

    public static String getImdbImagePath() {
        return "http://image.tmdb.org/t/p/w185";
    }
}
