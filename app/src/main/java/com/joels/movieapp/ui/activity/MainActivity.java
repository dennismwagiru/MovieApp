package com.joels.movieapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.joels.movieapp.R;
import com.joels.movieapp.model.Movie;
import com.joels.movieapp.model.MovieResponse;
import com.joels.movieapp.rest.ApiClient;
import com.joels.movieapp.rest.ApiInterface;
import com.joels.movieapp.ui.adapter.LandMovieAdapter;
import com.joels.movieapp.ui.adapter.MovieAdapter;
import com.joels.movieapp.ui.adapter.ScreenSlideAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnCLickHandler, LandMovieAdapter.MovieAdapterOnCLickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String apiKey = ""; //Place your api key here

    RecyclerView mRecyclerView;
    RecyclerView mLandRecyclerView;
    RecyclerView latestRecyclerView;
    MovieAdapter topRatedAdapter;
    MovieAdapter mostPopularAdapter;
    MovieAdapter upcomingAdapter;

    List<Movie> topRated = new ArrayList<>();
    List<Movie> mostPopular = new ArrayList<>();
    List<Movie> upcoming = new ArrayList<>();

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPager = findViewById(R.id.view_pager);
        mPagerAdapter = new ScreenSlideAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

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
        
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<MovieResponse> call = apiInterface.getTopRatedMovies(apiKey);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.body() != null) {
                    topRated = response.body().getResults();
                    topRatedAdapter.setMovieList(topRated);
                    topRatedAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Number of top rated movies received: " + topRated.size());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

        Call<MovieResponse> mostPopularCall = apiInterface.getPopular(apiKey);
        mostPopularCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.body() != null) {
                    mostPopular = response.body().getResults();
                    mostPopularAdapter.setMovieList(mostPopular);
                    mostPopularAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Number of most popular movies received: " + mostPopular.size());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });

//        Call<MovieResponse> latestCall = apiInterface.getLatest(apiKey);
//        latestCall.enqueue(new Callback<MovieResponse>() {
//            @Override
//            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
//                if (response.body() != null) {
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieResponse> call, Throwable t) {
//
//            }
//        });

        Call<MovieResponse> upcomingCall = apiInterface.getUpcoming(apiKey);
        upcomingCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.body() != null) {
                    upcoming = response.body().getResults();
                    upcomingAdapter.setMovieList(upcoming);
                    upcomingAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
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
        startActivity(new Intent(this, MovieActivity.class));
    }

}
