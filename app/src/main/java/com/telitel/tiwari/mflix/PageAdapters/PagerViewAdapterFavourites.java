package com.telitel.tiwari.mflix.PageAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.telitel.tiwari.mflix.Screens.Fragments.FavouriteFragment;
import com.telitel.tiwari.mflix.Screens.Fragments.PlayListFragment;

public class PagerViewAdapterFavourites extends FragmentStatePagerAdapter {
    public PagerViewAdapterFavourites(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        Fragment fragment = null;
        switch (i) {
            case 0:
                fragment = new FavouriteFragment();
                break;
            case 1:
                fragment = new PlayListFragment();
                break;

        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
