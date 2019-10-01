package com.telitel.tiwari.mflix;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class homePage extends Fragment {


    private RecyclerView mySongsRecyclerView;

    public homePage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home_page, container, false);
        mySongsRecyclerView = (RecyclerView) v.findViewById(R.id.songs_recyclerView);


        return v;

    }

}
