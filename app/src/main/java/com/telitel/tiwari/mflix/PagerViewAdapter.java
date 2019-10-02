package com.telitel.tiwari.mflix;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class PagerViewAdapter extends FragmentPagerAdapter {
    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        Fragment fragment = null;
        switch (i){
            case 0:
                fragment = new fragment_album();
                break;
            case 1:
                fragment = new fragment_artist();
                break;
            case 2:
                fragment = new fragment_genre();
                break;

        }



        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
