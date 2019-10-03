package com.telitel.tiwari.mflix;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class player_view extends AppCompatActivity {

    private RecyclerView mySongsRecyclerView;


    private List<song_template> songsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_view);

        mySongsRecyclerView = (RecyclerView) findViewById(R.id.songs_recyclerView);
        songs_recyclerView_adapter songAdapter = new songs_recyclerView_adapter(this, songsList, 2);
        mySongsRecyclerView.setAdapter(songAdapter);

        songsList = new ArrayList<>();


        song_template song = new song_template(0L,"",""," "," ",0L," "," ","No","","");






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
