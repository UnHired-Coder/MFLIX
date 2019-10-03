package com.telitel.tiwari.mflix;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private List<song_template> albumList;



    static database_helper _songs_database_helper;
    static SQLiteDatabase songs_database;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_album,null);


        myAlbumRecyclerView = (RecyclerView) v.findViewById(R.id.album_recyclerView);
        songsList_recyclerView_adapter albumAdapter = new songsList_recyclerView_adapter(getContext(), albumList,0);
        myAlbumRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        myAlbumRecyclerView.setAdapter(albumAdapter);



       return v;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        albumList = new ArrayList<>();







        _songs_database_helper= new database_helper(getContext());
        songs_database = _songs_database_helper.getWritableDatabase();


        Cursor songs_cursor =songs_database.rawQuery("SELECT _song_album,count(_song_album),_song_album_art_path FROM _songs_tb GROUP BY _song_album ",new String[]{}) ;
        if(songs_cursor != null){
            songs_cursor.moveToFirst();

            do {

                // Log.i("song Name",cursor.getString(0)+"--- "+cursor.getString(1)+"---- "+cursor.getString(2)+" ---"+cursor.getString(3)+" ---"+cursor.getString(4)+"--- "+cursor.getString(5)+"--- "+cursor.getString(6)+" ---"+cursor.getString(7)+" ---"+cursor.getString(8));

                //  Log.i("song Name*******",songs_cursor.getString(0)+"----"+songs_cursor.getString(1));

                String albumArtPath=songs_cursor.getString(songs_cursor.getColumnIndex("_song_album_art_path"));

                song_template song = new song_template(0L,"",""," "," ",0L,songs_cursor.getString(songs_cursor.getColumnIndex("_song_album")),songs_cursor.getString(songs_cursor.getColumnIndex("_song_album_art_path"))," ","",songs_cursor.getString(1));

                albumList.add(song);

            }while (songs_cursor.moveToNext());


        }

//        song_template song = new song_template(0L,"",""," "," ",0L," "," "," ","");
//
//        Log.i("this---","--------");
//        albumList.add(song);
//        albumList.add(song);
//        albumList.add(song);
//        albumList.add(song);
//        albumList.add(song);
//        albumList.add(song);
//        albumList.add(song);
//        albumList.add(song);
//        albumList.add(song);
//        albumList.add(song);
//        albumList.add(song);
//        albumList.add(song);
//        albumList.add(song);
//        albumList.add(song);
//        albumList.add(song);




    }
}
