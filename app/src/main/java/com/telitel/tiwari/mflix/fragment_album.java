package com.telitel.tiwari.mflix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class fragment_album extends Fragment {


    private RecyclerView myAlbumRecyclerView;
    private List<song_template> songsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_album,null);


        myAlbumRecyclerView = (RecyclerView) v.findViewById(R.id.album_recyclerView);
        songsList_recyclerView_adapter albumAdapter = new songsList_recyclerView_adapter(getContext(), songsList,0);
        myAlbumRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        myAlbumRecyclerView.setAdapter(albumAdapter);



       return v;

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
