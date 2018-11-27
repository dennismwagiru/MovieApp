package com.joels.movieapp.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.joels.movieapp.MovieApp;
import com.joels.movieapp.R;
import com.joels.movieapp.model.Movie;
import com.joels.movieapp.ui.adapter.LandMovieAdapter;
import com.joels.movieapp.ui.adapter.MovieAdapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnCLickHandler, LandMovieAdapter.MovieAdapterOnCLickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static final String apiKey = "7e8f60e325cd06e164799af1e317d7a7"; //Place your api key here

    RecyclerView mRecyclerView;
    RecyclerView mLandRecyclerView;
    RecyclerView latestRecyclerView;
    MovieAdapter topRatedAdapter;
    MovieAdapter mostPopularAdapter;
    MovieAdapter upcomingAdapter;

    List<Movie> topRated = new ArrayList<>();
    List<Movie> mostPopular = new ArrayList<>();
    List<Movie> upcoming = new ArrayList<>();

    ImageView imageView;
    Movie latest = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageView = findViewById(R.id.image_view);
        imageView.setOnClickListener(v -> MainActivity.this.onClick(latest));

        mRecyclerView = findViewById(R.id.recyclerView);
        mLandRecyclerView = findViewById(R.id.landRecyclerView);
        latestRecyclerView = findViewById(R.id.upcomingRecyclerView);

        topRatedAdapter = new MovieAdapter(MainActivity.this, MainActivity.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(topRatedAdapter);


        mostPopularAdapter = new MovieAdapter(MainActivity.this, MainActivity.this);
        mLandRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mLandRecyclerView.setAdapter(mostPopularAdapter);

        upcomingAdapter = new MovieAdapter(MainActivity.this, MainActivity.this);
        latestRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        latestRecyclerView.setAdapter(upcomingAdapter);

        if (apiKey.isEmpty()) {
            Toast.makeText(this, "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_SHORT).show();
        }
        fetchMovies("latest");
        fetchMovies("upcoming");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Movie movie) {
        final AlertDialog myAlert = new AlertDialog.Builder(MainActivity.this)
                .setTitle(movie.getOriginalTitle())
                .setPositiveButton("Okay", null)
                .setMessage(movie.getOverview()).create();

        Glide.with(this)
                .load(movie.getPosterPath())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        myAlert.setIcon(resource);
                    }
                });

        myAlert.show();

    }

    private void fetchMovies(String resource) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this, new HurlStack());
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL + "movie/"+ resource +"?apiKey=" + apiKey,
                response -> {
                    Log.e(TAG + ' ' + resource, response);
                },
                error -> {
                    NetworkResponse networkResponse = error.networkResponse;
                    try {
                        String response = new String(networkResponse.data, "UTF-8");
                        Log.e(TAG + " error", response);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                });

        request.setRetryPolicy(((MovieApp)getApplicationContext()).getPolicy());
        requestQueue.add(request);
    }

}
