package com.joels.movieapp.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.joels.movieapp.R;
import com.joels.movieapp.model.Movie;
import com.joels.movieapp.ui.adapter.LandMovieAdapter;
import com.joels.movieapp.ui.adapter.MovieAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnCLickHandler, LandMovieAdapter.MovieAdapterOnCLickHandler {

    RecyclerView mRecyclerView;
    RecyclerView mLandRecyclerView;
    MovieAdapter movieAdapter;
    LandMovieAdapter mLandMovieAdapter;

    private List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = findViewById(R.id.recyclerView);
        mLandRecyclerView = findViewById(R.id.landRecyclerView);
        Movie m = new Movie();
        movies = new ArrayList<>();
        movies.add(m);
        movies.add(m);
        movies.add(m);
        movies.add(m);
        movies.add(m);
        movies.add(m);
        movies.add(m);
        movies.add(m);
        movieAdapter = new MovieAdapter(this, this, movies);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(movieAdapter);

        mLandMovieAdapter = new LandMovieAdapter(this, this, movies);
        RecyclerView.LayoutManager mLandLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mLandRecyclerView.setLayoutManager(mLandLayoutManager);
        mLandRecyclerView.setAdapter(mLandMovieAdapter);
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
        Toast.makeText(this, "Movie Clicked", Toast.LENGTH_SHORT).show();
    }
}
