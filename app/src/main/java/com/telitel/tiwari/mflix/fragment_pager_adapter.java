package com.telitel.tiwari.mflix;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class fragment_pager_adapter extends FragmentPagerAdapter {


    private List<Fragment> Fragment = new ArrayList<>();
    private List<String> NameFraagment = new ArrayList<>();

    public fragment_pager_adapter(FragmentManager fm) {
        super(fm);

    }


    public void add(Fragment fragment,String fragmentName){
        Fragment.add(fragment);
        NameFraagment.add(fragmentName);

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
