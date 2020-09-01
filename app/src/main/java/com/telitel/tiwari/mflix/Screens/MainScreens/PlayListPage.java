package com.telitel.tiwari.mflix.Screens.MainScreens;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.telitel.tiwari.mflix.PageAdapters.PagerViewAdapter;
import com.telitel.tiwari.mflix.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayListPage extends Fragment {


    ViewPager viewPager;
    PagerViewAdapter pagerViewAdapter;

    TextView albumsTab;
    TextView artistTab;
    TextView genreTab;


    public PlayListPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_playlist_page, container, false);

        albumsTab = v.findViewById(R.id.favourites);
        artistTab = v.findViewById(R.id.playlists);
        genreTab = v.findViewById(R.id.Genre);


        viewPager = v.findViewById(R.id.container_tab_fragment);
        pagerViewAdapter = new PagerViewAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerViewAdapter);


        albumsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        artistTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);

            }
        });
        genreTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        viewPager.setCurrentItem(0);
        albumsTab.setTextColor(getResources().getColor(R.color.navItemChecked));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        albumsTab.setTextColor(getResources().getColor(R.color.navItemChecked));
                        artistTab.setTextColor(getResources().getColor(R.color.navItemUnChecked));
                        genreTab.setTextColor(getResources().getColor(R.color.navItemUnChecked));
                        break;
                    case 1:
                        artistTab.setTextColor(getResources().getColor(R.color.navItemChecked));
                        albumsTab.setTextColor(getResources().getColor(R.color.navItemUnChecked));
                        genreTab.setTextColor(getResources().getColor(R.color.navItemUnChecked));
                        break;
                    case 2:
                        genreTab.setTextColor(getResources().getColor(R.color.navItemChecked));
                        albumsTab.setTextColor(getResources().getColor(R.color.navItemUnChecked));
                        artistTab.setTextColor(getResources().getColor(R.color.navItemUnChecked));

                        break;

                }
            }


            @Override
            public void onPageScrollStateChanged(int i) {


            }
        });


        return v;
    }


}
