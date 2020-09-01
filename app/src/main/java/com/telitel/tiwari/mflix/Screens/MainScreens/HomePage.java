package com.telitel.tiwari.mflix.Screens.MainScreens;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.telitel.tiwari.mflix.Database.DatabaseHelper;
import com.telitel.tiwari.mflix.Database.StorageUtil;
import com.telitel.tiwari.mflix.R;
import com.telitel.tiwari.mflix.Models.SongModel;
import com.telitel.tiwari.mflix.RecyclerViewAdapters.SongsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;


//import static com.telitel.tiwari.mflix.MainActivity.Broadcast_PLAY_NEW_AUDIO;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomePage extends Fragment {


    static DatabaseHelper _songs_databaseHelper;
    static SQLiteDatabase songs_database;


    private ArrayList<SongModel> songsList;
    private ArrayList<SongModel> topPicsList;
    private ArrayList<SongModel> recentList;

    public HomePage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_page, container, false);

        RecyclerView myTopRecyclerView = v.findViewById(R.id.top_recyclerView);
        SongsRecyclerViewAdapter topSongAdapter = new SongsRecyclerViewAdapter(getContext(), topPicsList, 0);
        myTopRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        myTopRecyclerView.setAdapter(topSongAdapter);

        RecyclerView myRecentRecyclerView = v.findViewById(R.id.recent_recyclerView);
        SongsRecyclerViewAdapter recentSongAdapter = new SongsRecyclerViewAdapter(getContext(), recentList, 0);
        myRecentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        myRecentRecyclerView.setAdapter(recentSongAdapter);

        RecyclerView mySongsRecyclerView = v.findViewById(R.id.songs_recyclerView);
        SongsRecyclerViewAdapter songAdapter = new SongsRecyclerViewAdapter(getContext(), songsList, 1);
        mySongsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mySongsRecyclerView.setAdapter(songAdapter);

        topSongAdapter.setOnItemClickListener(new SongsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.i("clicked", Integer.toString(position));

                StorageUtil storage = StorageUtil.getInstance(getContext());
                storage.clearCachedAudioPlaylist();
                storage.storeAudio(topPicsList);
                storage.storeAudioIndex(position);
                storage.storeAudioPosition(0);
                // MainActivity.playAudio(0);
                //Service is active
                //Send a broadcast to the service -> PLAY_NEW_AUDIO
//                Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
//                getActivity().sendBroadcast(broadcastIntent);
                Log.i("But", "here");
//                MainActivity.setPlayerSongsRecyclerView(topPicsList, position);


            }
        });
        recentSongAdapter.setOnItemClickListener(new SongsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.i("clicked", Integer.toString(position));
                StorageUtil storage = StorageUtil.getInstance(getContext());
                storage.clearCachedAudioPlaylist();
                storage.storeAudio(recentList);
                storage.storeAudioIndex(position);
                storage.storeAudioPosition(0);

                Log.i("But", "here");
//                MainActivity.setPlayerSongsRecyclerView(recentList, position);
            }
        });


        songAdapter.setOnItemClickListener(new SongsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.i("clicked", Integer.toString(position));

//
                StorageUtil storage = StorageUtil.getInstance(getContext());
                storage.clearCachedAudioPlaylist();
                storage.storeAudio(songsList);
                storage.storeAudioIndex(position);
                storage.storeAudioPosition(0);
                //  storage.storeAudioIndex(songsList.indexOf(songsList.get(position)));
                //  MainActivity.playAudio(0);

//                //Service is active
//                //Send a broadcast to the service -> PLAY_NEW_AUDIO
//                Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
//                getActivity().sendBroadcast(broadcastIntent);
                Log.i("But", "here");
//                MainActivity.setPlayerSongsRecyclerView(songsList, position);

            }
        });


        return v;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _songs_databaseHelper = DatabaseHelper.getInstance(getContext());
        songs_database = _songs_databaseHelper.getWritableDatabase();


        topPicsList = new ArrayList<>();
        recentList = new ArrayList<>();
        songsList = new ArrayList<>();

        Cursor songs_cursor = songs_database.rawQuery("SELECT * FROM _songs_tb ", new String[]{});
        if (songs_cursor != null) {
            songs_cursor.moveToFirst();

            do {
                SongModel song = new SongModel(songs_cursor.getLong(songs_cursor.getColumnIndex("_song_id")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_title")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_artist")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_genre")), songs_cursor.getString(songs_cursor.getColumnIndex("_is_favourite")), songs_cursor.getLong(songs_cursor.getColumnIndex("_song_album_id")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_album")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_album_art_path")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_art_path")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_path")), "0");
                songsList.add(song);

            } while (songs_cursor.moveToNext());


            int SONGS_COUNT = songsList.size();

            if (SONGS_COUNT > 10) {
                for (int i = 1; i <= 10; i++) {
                    recentList.add(songsList.get(SONGS_COUNT - i));
                    topPicsList.add(songsList.get(i));

                }
            } else {
                for (int i = 1; i <= SONGS_COUNT; i++) {
                    recentList.add(songsList.get(SONGS_COUNT - i));

                }

            }

            Collections.shuffle(topPicsList);
            songs_cursor.close();
        }
    }
}
