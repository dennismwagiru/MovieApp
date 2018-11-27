package com.joels.movieapp.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.joels.movieapp.model.Movie;
import com.joels.movieapp.ui.Fragment.ScreenSlideFragment;

import java.util.ArrayList;

public class ScreenSlideAdapter extends FragmentPagerAdapter {
    private static final int NUM_SLIDES = 5;
    private ArrayList<Movie> movies;

    public ScreenSlideAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return new ScreenSlideFragment();
    }

    @Override
    public int getCount() {
        return NUM_SLIDES;
    }
}
