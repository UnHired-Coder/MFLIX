package com.telitel.tiwari.mflix;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

class PagerViewAdapterFavourites extends FragmentStatePagerAdapter {
    public PagerViewAdapterFavourites(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        Fragment fragment = null;
        switch (i){
            case 0:
                fragment = new fragment_favourite();
                break;
            case 1:
                fragment = new fragment_playlists();
                break;

        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
