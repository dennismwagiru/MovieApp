package com.joels.movieapp.ui.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.joels.movieapp.R;

public class MovieActivity extends AppCompatActivity {

    VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        mVideoView = findViewById(R.id.videoView);
        String address = "https://www.youtube.com/watch?v=fU6mInJ20OE";

        Uri vidUri = Uri.parse(address);
        mVideoView.setVideoURI(vidUri);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mediaController);

    }

}
