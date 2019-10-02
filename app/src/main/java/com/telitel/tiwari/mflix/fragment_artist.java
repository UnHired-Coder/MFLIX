package com.telitel.tiwari.mflix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class fragment_artist extends Fragment {


    private RecyclerView myArtistRecyclerView;
    private List<song_template> songsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


   View v= inflater.inflate(R.layout.fragment_artist,null);



        myArtistRecyclerView = (RecyclerView) v.findViewById(R.id.artist_recyclerView);
        songsList_recyclerView_adapter albumAdapter = new songsList_recyclerView_adapter(getContext(), songsList,1);
        myArtistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        myArtistRecyclerView.setAdapter(albumAdapter);




        return  v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        songsList = new ArrayList<>();

        song_template song = new song_template(0L,"",""," "," ",0L," "," "," ","");

        Log.i("this---","--------");
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
