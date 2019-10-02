package com.telitel.tiwari.mflix;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class homePage extends Fragment {




    private RecyclerView myTopRecyclerView;
    private RecyclerView myRecentRecyclerView;
    private RecyclerView mySongsRecyclerView;


    private List<song_template> songsList;

    public homePage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home_page, container, false);


        myTopRecyclerView = (RecyclerView) v.findViewById(R.id.top_recyclerView);
        songs_recyclerView_adapter topSongAdapter = new songs_recyclerView_adapter(getContext(), songsList,0);
        myTopRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        myTopRecyclerView.setAdapter(topSongAdapter);

        myRecentRecyclerView = (RecyclerView) v.findViewById(R.id.recent_recyclerView);
        songs_recyclerView_adapter recentSongAdapter = new songs_recyclerView_adapter(getContext(), songsList,0);
        myRecentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        myRecentRecyclerView.setAdapter(recentSongAdapter);

        mySongsRecyclerView = (RecyclerView) v.findViewById(R.id.songs_recyclerView);
        songs_recyclerView_adapter songAdapter = new songs_recyclerView_adapter(getContext(), songsList,1);
        mySongsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mySongsRecyclerView.setAdapter(songAdapter);







        return v;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        songsList = new ArrayList<>();

        song_template song = new song_template(0L,"",""," "," ",0L," "," "," ","");


        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);
        songsList.add(song);



    }
}
