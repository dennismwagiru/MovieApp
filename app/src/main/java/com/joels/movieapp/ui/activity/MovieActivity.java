package com.joels.movieapp.ui.activity;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.objectbox.Box;
import io.objectbox.BoxStore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.joels.movieapp.MovieApp;
import com.joels.movieapp.R;
import com.joels.movieapp.model.Movie;
import com.joels.movieapp.model.Movie_;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static com.joels.movieapp.ui.activity.MainActivity.BASE_URL;

public class MovieActivity extends AppCompatActivity {

    final String TAG = MovieActivity.class.getSimpleName();
    BoxStore boxStore;
    Box<Movie> movieBox;
    Movie movie;

    TextView tagLine, releaseDate, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);

        YouTubePlayerView youTubePlayerView = findViewById(R.id.videoView);
        TextView overview = findViewById(R.id.overview);
        ProgressBar progressBar = findViewById(R.id.progress);
        tagLine = findViewById(R.id.tag_line);
        releaseDate = findViewById(R.id.release_date);
        title = findViewById(R.id.title);

        boxStore = ((MovieApp)getApplicationContext()).getBoxStore();
        movieBox = boxStore.boxFor(Movie.class);

        Intent intent = getIntent();
        movie = intent.getParcelableExtra("movie");

        if (movie != null) {
            title.setText(movie.title);
            releaseDate.setText("Released on: " + movie.releaseDate);
            overview.setText(movie.overview);
            getLifecycle().addObserver(youTubePlayerView);
            if (movie.youtubeId == null || movie.tagLine == null) {
                RequestQueue requestQueue = Volley.newRequestQueue(MovieActivity.this, new HurlStack());
                final String url = BASE_URL + "movie/" + movie.imdbId + "?api_key=" + MovieApp.getApiKey() + "&append_to_response=videos";
                StringRequest request = new StringRequest(Request.Method.GET, url,
                        response -> {
                            try {
                                JSONObject object = new JSONObject(response);
                                JSONObject videos = object.getJSONObject("videos");
                                JSONArray results = videos.getJSONArray("results");
                                for (int i = 0; i < results.length(); i++) {
                                    JSONObject video = results.getJSONObject(i);
                                    if (video.getString("site").equalsIgnoreCase("youtube")) {
                                        String key = video.getString("key");
                                        Log.e(TAG + " key", key);
                                        for (Movie m : movieBox.query().equal(Movie_.imdbId, movie.imdbId).build().find()) {
                                            m.youtubeId = video.getString("key");
                                            m.tagLine = object.getString("tagline");
                                            m.runtime = object.getString("runtime");
                                            m.budget = object.getString("budget");
                                            m.revenue = object.getString("revenue");
                                            movieBox.put(m);

                                            tagLine.setText(object.getString("tagline"));
                                            tagLine.setVisibility(View.VISIBLE);
                                        }
                                        progressBar.setVisibility(View.GONE);
                                        youTubePlayerView.setVisibility(View.VISIBLE);
                                        youTubePlayerView.initialize(initializedYouTubePlayer -> initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                                            @Override
                                            public void onReady() {
                                                initializedYouTubePlayer.loadVideo(key, 0);
                                            }
                                        }), true);
                                        break;
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

                request.setRetryPolicy(((MovieApp) getApplicationContext()).getPolicy());
                requestQueue.add(request);
            } else {
                tagLine.setText(movie.tagLine);
                tagLine.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                youTubePlayerView.setVisibility(View.VISIBLE);
                youTubePlayerView.initialize(initializedYouTubePlayer -> initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        initializedYouTubePlayer.loadVideo(movie.youtubeId, 0);
                    }
                }), true);
            }
        }


    }

}
