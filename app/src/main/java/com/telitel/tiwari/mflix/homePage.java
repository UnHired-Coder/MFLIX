package com.telitel.tiwari.mflix;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.telitel.tiwari.mflix.MainActivity.Broadcast_PLAY_NEW_AUDIO;


/**
 * A simple {@link Fragment} subclass.
 */
public class homePage extends Fragment {


    private RecyclerView myTopRecyclerView;
    private RecyclerView myRecentRecyclerView;
    private RecyclerView mySongsRecyclerView;


    static database_helper _songs_database_helper;
    static SQLiteDatabase songs_database;


    private List<song_template> songsList;
    private List<song_template> topPicsList;
    private List<song_template> recentList;

    private int SONGS_COUNT;

    public homePage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_page, container, false);


        myTopRecyclerView = (RecyclerView) v.findViewById(R.id.top_recyclerView);
        songs_recyclerView_adapter topSongAdapter = new songs_recyclerView_adapter(getContext(), topPicsList, 0);
        myTopRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        myTopRecyclerView.setAdapter(topSongAdapter);

        myRecentRecyclerView = (RecyclerView) v.findViewById(R.id.recent_recyclerView);
        songs_recyclerView_adapter recentSongAdapter = new songs_recyclerView_adapter(getContext(), recentList, 0);
        myRecentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        myRecentRecyclerView.setAdapter(recentSongAdapter);

        mySongsRecyclerView = (RecyclerView) v.findViewById(R.id.songs_recyclerView);
        songs_recyclerView_adapter songAdapter = new songs_recyclerView_adapter(getContext(), songsList, 1);
        mySongsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mySongsRecyclerView.setAdapter(songAdapter);

       topSongAdapter.setOnItemClickListener(new songs_recyclerView_adapter.OnItemClickListener() {
           @Override
           public void onItemClick(int position) {
               Log.i("clicked",Integer.toString(position));

               StorageUtil storage = new StorageUtil(getActivity().getApplicationContext());
               storage.clearCachedAudioPlaylist();
               storage.storeAudio(topPicsList);
               storage.storeAudioIndex(position);
              // MainActivity.playAudio(0);
               //Service is active
               //Send a broadcast to the service -> PLAY_NEW_AUDIO
               Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
               getActivity().sendBroadcast(broadcastIntent);
               Log.i("But","here");
               MainActivity.setPlayerSongsRecyclerView(topPicsList,position);


           }
       });
        recentSongAdapter.setOnItemClickListener(new songs_recyclerView_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.i("clicked",Integer.toString(position));
                StorageUtil storage = new StorageUtil(getActivity().getApplicationContext());
                storage.clearCachedAudioPlaylist();
                storage.storeAudio(recentList);
                storage.storeAudioIndex(position);
              //  storage.storeAudioIndex(songsList.indexOf(recentList.get(position)));
               // MainActivity.playAudio(0);
//
//                //Service is active
//                //Send a broadcast to the service -> PLAY_NEW_AUDIO
//                Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
//                getActivity().sendBroadcast(broadcastIntent);
               Log.i("But","here");
                MainActivity.setPlayerSongsRecyclerView(recentList,position);


            }
        });



        songAdapter.setOnItemClickListener(new songs_recyclerView_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.i("clicked",Integer.toString(position));

//
                StorageUtil storage = new StorageUtil(getActivity().getApplicationContext());
                storage.clearCachedAudioPlaylist();
                storage.storeAudio(songsList);
                storage.storeAudioIndex(position);
              //  storage.storeAudioIndex(songsList.indexOf(songsList.get(position)));
              //  MainActivity.playAudio(0);

//                //Service is active
//                //Send a broadcast to the service -> PLAY_NEW_AUDIO
//                Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
//                getActivity().sendBroadcast(broadcastIntent);
                Log.i("But","here");
                MainActivity.setPlayerSongsRecyclerView(songsList,position);

            }
        });



        return v;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _songs_database_helper = MainActivity._songs_database_helper;
        songs_database = _songs_database_helper.getWritableDatabase();


        topPicsList = new ArrayList<>();
        recentList = new ArrayList<>();
        songsList = new ArrayList<>();

        Cursor songs_cursor = songs_database.rawQuery("SELECT * FROM _songs_tb ", new String[]{});
        if (songs_cursor != null) {
            songs_cursor.moveToFirst();

            do {

                // Log.i("song Name",cursor.getString(0)+"--- "+cursor.getString(1)+"---- "+cursor.getString(2)+" ---"+cursor.getString(3)+" ---"+cursor.getString(4)+"--- "+cursor.getString(5)+"--- "+cursor.getString(6)+" ---"+cursor.getString(7)+" ---"+cursor.getString(8));

                //  Log.i("song Name", songs_cursor.getString(0) + "----" + songs_cursor.getString(1));

                song_template song = new song_template(songs_cursor.getLong(songs_cursor.getColumnIndex("_song_id")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_title")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_artist")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_genre")), songs_cursor.getString(songs_cursor.getColumnIndex("_is_favourite")), songs_cursor.getLong(songs_cursor.getColumnIndex("_song_album_id")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_album")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_album_art_path")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_art_path")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_path")),"0");

                songsList.add(song);

            } while (songs_cursor.moveToNext());


         SONGS_COUNT=songsList.size();

         if(SONGS_COUNT>10){
             for(int i=1;i<=10;i++)
             {
                 recentList.add(songsList.get(SONGS_COUNT-i));
                 topPicsList.add(songsList.get(i));

             }
         }else{
             for(int i=1;i<=SONGS_COUNT;i++)
             {
                 recentList.add(songsList.get(SONGS_COUNT-i));

             }

         }

            Collections.shuffle(topPicsList);





       song_template song = new song_template(0L,"",""," "," ",0L," "," "," ","","");
//
//
//
//
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

    public void findSongId(){

    }


}
