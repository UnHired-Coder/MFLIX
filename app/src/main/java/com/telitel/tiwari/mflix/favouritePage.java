package com.telitel.tiwari.mflix;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class favouritePage extends Fragment {


    ViewPager viewPager;
    PagerViewAdapterFavourites pagerViewAdapterFavourite;

    TextView favouritesTab;
    TextView playlistsTab;



    public favouritePage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_favourite_page, container, false);


        favouritesTab=v.findViewById(R.id.favourites);
        playlistsTab=v.findViewById(R.id.playlists);





        viewPager = v.findViewById(R.id.container_tab_fragment_favourite);
        pagerViewAdapterFavourite = new PagerViewAdapterFavourites(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerViewAdapterFavourite);

        viewPager.setCurrentItem(0);
        favouritesTab.setTextColor(getResources().getColor(R.color.navItemChecked));

        favouritesTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        playlistsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i){
                    case 0:
                        favouritesTab.setTextColor(getResources().getColor(R.color.navItemChecked));
                        playlistsTab.setTextColor(getResources().getColor(R.color.navItemUnChecked));

                        break;
                    case 1:
                        playlistsTab.setTextColor(getResources().getColor(R.color.navItemChecked));
                        favouritesTab.setTextColor(getResources().getColor(R.color.navItemUnChecked));

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
