package com.joels.movieapp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.joels.movieapp.ui.Fragment.ScreenSlideFragment;

public class ScreenSlideAdapter extends FragmentPagerAdapter {
    private static final int NUM_SLIDES = 5;

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
