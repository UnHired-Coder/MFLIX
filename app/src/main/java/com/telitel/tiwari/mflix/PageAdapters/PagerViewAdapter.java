package com.telitel.tiwari.mflix.PageAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.telitel.tiwari.mflix.Screens.Fragments.AlbumFragment;
import com.telitel.tiwari.mflix.Screens.Fragments.ArtistFragment;
import com.telitel.tiwari.mflix.Screens.Fragments.GenreFragment;

public class PagerViewAdapter extends FragmentStatePagerAdapter {
    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        Fragment fragment = null;
        switch (i) {
            case 0:

                fragment = new AlbumFragment();
                break;
            case 1:
                fragment = new ArtistFragment();
                break;
            case 2:
                fragment = new GenreFragment();
                break;

        }


        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
