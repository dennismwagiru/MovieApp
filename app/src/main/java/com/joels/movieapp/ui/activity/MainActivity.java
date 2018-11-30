package com.joels.movieapp.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import io.objectbox.Box;
import io.objectbox.BoxStore;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.joels.movieapp.MovieApp;
import com.joels.movieapp.R;
import com.joels.movieapp.model.Movie;
import com.joels.movieapp.model.Movie_;
import com.joels.movieapp.ui.adapter.LandMovieAdapter;
import com.joels.movieapp.ui.adapter.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnCLickHandler, LandMovieAdapter.MovieAdapterOnCLickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String BASE_URL = "https://api.themoviedb.org/3/";

    private final String[] resources = {"now_playing", "popular", "top_rated", "upcoming"};
    private static int resource_index = 0;

    ProgressBar progressBar;
    ScrollView scrollView;

    LinearLayout layout;

    ImageView imageView;
    Movie latest = null ;

    BoxStore boxStore;
    Box<Movie> movieBox;
    
    ArrayList<MovieAdapter> movieAdapters = new ArrayList<>();

    private SearchView searchView;
    Bitmap bitmap;
    Toolbar toolbar;
    AppBarLayout appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        appBar = (AppBarLayout) findViewById(R.id.appBar);

//        imageView = findViewById(R.id.image_view);
//        imageView.setOnClickListener(v -> MainActivity.this.onClick(latest));
        progressBar = findViewById(R.id.progress);
        scrollView = findViewById(R.id.scrollable);

        layout = findViewById(R.id.layout);

        boxStore = ((MovieApp)getApplicationContext()).getBoxStore();
        movieBox = boxStore.boxFor(Movie.class);

        movieBox.removeAll();

        fetchMovies(resources[resource_index]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // Listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                Log.e(TAG, String.valueOf(movieAdapters.size()));
                if (movieAdapters.size() > 0)
                    for (MovieAdapter adapter : movieAdapters) {
                        adapter.getFilter().filter(query);
                    }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // filter recycler view when text is changed
                Log.e(TAG, String.valueOf(movieAdapters.size()));
                if (movieAdapters.size() > 0)
                    for (MovieAdapter adapter : movieAdapters) {
                        adapter.getFilter().filter(newText);
                    }
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(MainActivity.this, MovieActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
//        YouTubePlayerView youTubePlayerView;
//        TextView overview;
//        ProgressBar progressBar;
//        if (movie != null) {
//
//            LayoutInflater li = LayoutInflater.from(MainActivity.this);
//            final View promptsView = li.inflate(R.layout.player, null);
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                    MainActivity.this);
//            alertDialogBuilder.setView(promptsView);
//
//            youTubePlayerView = promptsView.findViewById(R.id.videoView);
//            overview = promptsView.findViewById(R.id.overview);
//            progressBar = promptsView.findViewById(R.id.progress);
//
//            getLifecycle().addObserver(youTubePlayerView);
//            if (movie.youtubeId == null) {
//                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this, new HurlStack());
//                final String url = BASE_URL + "movie/" + movie.imdbId + "?api_key=" + MovieApp.getApiKey() + "&append_to_response=videos";
//                StringRequest request = new StringRequest(Request.Method.GET, url,
//                        response -> {
//                            try {
//                                JSONObject object = new JSONObject(response);
//                                Log.e(TAG, response);
//                                JSONObject videos = object.getJSONObject("videos");
//                                Log.e(TAG, videos.toString());
//                                JSONArray results = videos.getJSONArray("results");
//                                Log.e(TAG, results.toString());
//                                for (int i = 0; i < results.length(); i++) {
//                                    JSONObject video = results.getJSONObject(i);
//                                    if (video.getString("site").equalsIgnoreCase("youtube")) {
//                                        movie.youtubeId = video.getString("key");
//                                        movieBox.put(movie);
//                                        for (Movie m : movieBox.getAll()) {
//                                            if (m.imdbId.equals(movie.imdbId)) {
//                                                m.youtubeId = video.getString("key");
//                                                movieBox.put(m);
//                                            }
//                                        }
//                                        progressBar.setVisibility(View.GONE);
//                                        youTubePlayerView.setVisibility(View.VISIBLE);
//                                        youTubePlayerView.initialize(initializedYouTubePlayer -> initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
//                                            @Override
//                                            public void onReady() {
//                                                initializedYouTubePlayer.loadVideo(movie.youtubeId, 0);
//                                            }
//                                        }), true);
//                                        break;
//                                    }
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        },
//                        error -> {
//                            NetworkResponse networkResponse = error.networkResponse;
//                            try {
//                                String response = new String(networkResponse.data, "UTF-8");
//                                Log.e(TAG + " error", response);
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            }
//                        });
//
//                request.setRetryPolicy(((MovieApp) getApplicationContext()).getPolicy());
//                requestQueue.add(request);
//            } else  {
//                youTubePlayerView.initialize(initializedYouTubePlayer -> initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
//                    @Override
//                    public void onReady() {
//                        initializedYouTubePlayer.loadVideo(movie.youtubeId, 0);
//                    }
//                }), true);
//            }
//
//            overview.setText(movie.overview);
//            alertDialogBuilder
//                    .setCancelable(false)
//                    .setNegativeButton("Cancel",
//                            (dialog, id) -> dialog.cancel());
//
//            AlertDialog alertDialog = alertDialogBuilder.create();
//
//            alertDialog.setTitle(movie.originalTitle);
//
//            Glide.with(this)
//                    .load(MovieApp.getImdbImagePath() + movie.posterPath)
//                    .into(new SimpleTarget<Drawable>() {
//                        @Override
//                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                            alertDialog.setIcon(resource);
//                        }
//                    });
//            alertDialog.show();
//        }
    }

    private void fetchMovies(String resource) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this, new HurlStack());
        final String url = BASE_URL + "movie/"+ resource +"?api_key=" + MovieApp.getApiKey() + "&append_to_response=videos";
        Log.e(TAG + " url", url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray results = object.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject this_movie = results.getJSONObject(i);
                            Movie movie = new Movie(
                                    this_movie.getString("id"),
                                    this_movie.getString("vote_count"),
                                    this_movie.getString("vote_average"),
                                    this_movie.getString("popularity"),
                                    this_movie.getString("poster_path"),
                                    this_movie.getString("backdrop_path"),
                                    this_movie.getString("title"),
                                    this_movie.getString("original_title"),
                                    this_movie.getString("overview"),
                                    this_movie.getString("release_date"), resource);
                            movieBox.put(movie);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    resource_index += 1;
                    if (resources.length > resource_index) {
                        fetchMovies(resources[resource_index]);
                    } else
                        displayMovies();
                },
                error -> {
                    Log.e(TAG + " " + resource, "Error ");
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

    private void displayMovies() {
        for (String resource : resources) {
            TextView textView = new TextView(MainActivity.this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            String text = resource.replace("_", " ");
            textView.setText(Character.toUpperCase(text.charAt(0)) + text.substring(1));
            textView.setPadding(0, (int) getResources().getDimension(R.dimen.margin_extra_small), 0, 0);
            textView.setTextSize(getResources().getDimension(R.dimen.text_size_normal));
            layout.addView(textView);

            RecyclerView recyclerView = new RecyclerView(MainActivity.this);
            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            recyclerView.setClipToPadding(true);
            layout.addView(recyclerView);

            List<Movie> movies = movieBox.query().equal(Movie_.category, resource).build().find();
            MovieAdapter movieAdapter = new MovieAdapter(MainActivity.this, MainActivity.this, movies);

            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(movieAdapter);
            
            movieAdapters.add(movieAdapter);

            if (resource.equalsIgnoreCase("popular")) {
                latest = movies.get(movies.size() / 2);

//                Glide.with(MainActivity.this).load(MovieApp.getImdbImagePath() + latest.backdropPath).into(imageView);

                new BitmapOperations().execute();



            }
        }
//        appBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private class BitmapOperations extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Void ... params) {
            try {
                Log.e(TAG, MovieApp.getImdbImagePath() + latest.backdropPath);
                bitmap = (Bitmap) Glide.with(MainActivity.this)
                        .asBitmap()
                        .load(MovieApp.getImdbImagePath() + latest.backdropPath)
                        .submit(50, 50).get();
                createPaletteAsync(bitmap);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            createPaletteAsync(bitmap);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    // Generate palette synchronously and return it
    public Palette createPaletteSync(Bitmap bitmap) {
        return Palette.from(bitmap).generate();
    }

    // Generate palette asynchronously and use it on a different
// thread using onGenerated()
    public void createPaletteAsync(Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette p) {
                // Use generated instance
                int darkPrimaryColor = p.getDarkVibrantColor(getResources().getColor(R.color.colorPrimaryDark));
                int primaryColor = p.getVibrantColor(getResources().getColor(R.color.colorPrimary));
                toolbar.setBackgroundColor(primaryColor);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(darkPrimaryColor);
                }
            }
        });
    }


}
