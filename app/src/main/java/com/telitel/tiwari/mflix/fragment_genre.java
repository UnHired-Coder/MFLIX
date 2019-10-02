package com.telitel.tiwari.mflix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class fragment_genre extends Fragment {


    private RecyclerView myGenreRecyclerView;
    private List<song_template> songsList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


  View v= inflater.inflate(R.layout.fragment_genre,null);


        myGenreRecyclerView = (RecyclerView) v.findViewById(R.id.genre_recyclerView);
        songsList_recyclerView_adapter albumAdapter = new songsList_recyclerView_adapter(getContext(), songsList,1);
        myGenreRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        myGenreRecyclerView.setAdapter(albumAdapter);

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
