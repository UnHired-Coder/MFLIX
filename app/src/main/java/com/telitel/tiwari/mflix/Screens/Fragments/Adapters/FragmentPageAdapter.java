package com.telitel.tiwari.mflix.Screens.Fragments.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FragmentPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> Fragment = new ArrayList<>();

    public FragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void add(Fragment fragment, String fragmentName) {
        Fragment.add(fragment);
        Log.i("Added Fragment", ": " + fragmentName);
    }

    @Override
    public Fragment getItem(int i) {
        return Fragment.get(i);
    }

    @Override
    public int getCount() {
        return 5;
    }
}
