package com.telitel.tiwari.mflix;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class fragment_favourite extends Fragment {


    private RecyclerView myFavouriteRecyclerView;
    private List<song_template> songsList;


    static database_helper _songs_database_helper;
    static SQLiteDatabase songs_database;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


  View v =inflater.inflate(R.layout.fragment_favourite,null);

        myFavouriteRecyclerView = (RecyclerView) v.findViewById(R.id.favourite_Songs_recyclerView);
        songs_recyclerView_adapter songAdapter = new songs_recyclerView_adapter(getContext(), songsList,2);
        myFavouriteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        myFavouriteRecyclerView.setAdapter(songAdapter);

        songAdapter.setOnItemClickListener(new songs_recyclerView_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.i("clicked",Integer.toString(position));
                MainActivity.setPlayerSongsRecyclerView(songsList,position,0);

            }
        });

        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        songsList = new ArrayList<>();



        _songs_database_helper = MainActivity._songs_database_helper;
        songs_database = _songs_database_helper.getWritableDatabase();


        songsList = new ArrayList<>();


        Cursor songs_cursor = songs_database.rawQuery("SELECT * FROM _songs_tb ", new String[]{});
        if (songs_cursor != null) {
            songs_cursor.moveToFirst();

            do {

                // Log.i("song Name",cursor.getString(0)+"--- "+cursor.getString(1)+"---- "+cursor.getString(2)+" ---"+cursor.getString(3)+" ---"+cursor.getString(4)+"--- "+cursor.getString(5)+"--- "+cursor.getString(6)+" ---"+cursor.getString(7)+" ---"+cursor.getString(8));

                //  Log.i("song Name", songs_cursor.getString(0) + "----" + songs_cursor.getString(1));

                song_template song = new song_template(songs_cursor.getLong(songs_cursor.getColumnIndex("_song_id")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_title")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_artist")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_genre")), songs_cursor.getString(songs_cursor.getColumnIndex("_is_favourite")), songs_cursor.getLong(songs_cursor.getColumnIndex("_song_album_id")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_album")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_album_art_path")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_art_path")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_path")),"");

                songsList.add(song);

            } while (songs_cursor.moveToNext());

        }

//        song_template song = new song_template(0L,"",""," "," ",0L," "," "," ","");
//
//
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);
//        songsList.add(song);

    }
}
