package com.telitel.tiwari.mflix.Screens.MainScreens;

import android.content.Intent;
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

import com.google.android.youtube.player.internal.v;
import com.google.gson.Gson;
import com.telitel.tiwari.mflix.Database.DatabaseHelper;
import com.telitel.tiwari.mflix.Database.StorageUtil;
import com.telitel.tiwari.mflix.MainActivity;
import com.telitel.tiwari.mflix.R;
import com.telitel.tiwari.mflix.Models.SongModel;
import com.telitel.tiwari.mflix.RecyclerViewAdapters.SongsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePage extends Fragment {


    static DatabaseHelper _songs_databaseHelper;
    static SQLiteDatabase songs_database;

    String TAG = "HOME";
    private ArrayList<SongModel> songsList;
    private ArrayList<SongModel> topPicsList;
    private ArrayList<SongModel> recentList;
    boolean mediaAvailable;

    public HomePage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (!mediaAvailable) {
            return inflater.inflate(R.layout.no_media_found, container, false);
        }

        View v = inflater.inflate(R.layout.fragment_home_page, container, false);
        RecyclerView myTopRecyclerView = v.findViewById(R.id.top_recyclerView);
        SongsRecyclerViewAdapter topSongAdapter = new SongsRecyclerViewAdapter(getContext(), topPicsList, 0);
        myTopRecyclerView.setVisibility(View.GONE);
        v.findViewById(R.id.tv_top_pics).setVisibility(View.GONE);
//        if (topPicsList.size() > 6) {
//            myTopRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//            myTopRecyclerView.setAdapter(topSongAdapter);
//        } else {
//            myTopRecyclerView.setVisibility(View.GONE);
//            v.findViewById(R.id.tv_top_pics).setVisibility(View.GONE);
//        }

        RecyclerView myRecentRecyclerView = v.findViewById(R.id.recent_recyclerView);
        SongsRecyclerViewAdapter recentSongAdapter = new SongsRecyclerViewAdapter(getContext(), recentList, 0);
        myRecentRecyclerView.setVisibility(View.GONE);
        v.findViewById(R.id.tv_recents).setVisibility(View.GONE);
//        if (recentList.size() > 6) {
//            myRecentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//            myRecentRecyclerView.setAdapter(recentSongAdapter);
//        } else {
//            myRecentRecyclerView.setVisibility(View.GONE);
//            v.findViewById(R.id.tv_recents).setVisibility(View.GONE);
//        }

        RecyclerView mySongsRecyclerView = v.findViewById(R.id.songs_recyclerView);
        SongsRecyclerViewAdapter songAdapter = new SongsRecyclerViewAdapter(getContext(), songsList, 1);
        mySongsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mySongsRecyclerView.setAdapter(songAdapter);

        topSongAdapter.setOnItemClickListener(new SongsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.i("clicked", Integer.toString(position));
                StorageUtil storage = StorageUtil.getInstance(getContext());
                storage.clearCachedAudioPlaylist();
                storage.storeAudio(topPicsList);
                storage.storeAudioIndex(position);
                Intent broadcastIntent = new Intent(MainActivity.Broadcast_MEDIA_CHANGED);
                Gson gson = new Gson();
                String json = gson.toJson(topPicsList);
                broadcastIntent.putExtra("data", json);
                broadcastIntent.putExtra("position", position);
                if (getContext() != null)
                    getContext().sendBroadcast(broadcastIntent);

            }
        });
//        recentSongAdapter.setOnItemClickListener(new SongsRecyclerViewAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                Log.i("clicked", Integer.toString(position));
//                StorageUtil storage = StorageUtil.getInstance(getContext());
//                storage.clearCachedAudioPlaylist();
//                storage.storeAudio(recentList);
//                storage.storeAudioIndex(position);
//                Intent broadcastIntent = new Intent(MainActivity.Broadcast_MEDIA_CHANGED);
//                Gson gson = new Gson();
//                String json = gson.toJson(recentList);
//                broadcastIntent.putExtra("data", json);
//                broadcastIntent.putExtra("position", position);
//                if (getContext() != null)
//                    getContext().sendBroadcast(broadcastIntent);
//            }
//        });


        songAdapter.setOnItemClickListener(new SongsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.i("clicked", Integer.toString(position));
                StorageUtil storage = StorageUtil.getInstance(getContext());
                storage.clearCachedAudioPlaylist();
                storage.storeAudio(songsList);
                storage.storeAudioIndex(position);
                Intent broadcastIntent = new Intent(MainActivity.Broadcast_MEDIA_CHANGED);
                Gson gson = new Gson();
                String json = gson.toJson(songsList);
                broadcastIntent.putExtra("data", json);
                broadcastIntent.putExtra("position", position);
                if (getContext() != null)
                    getContext().sendBroadcast(broadcastIntent);
            }
        });


        return v;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mediaAvailable = true;
        _songs_databaseHelper = DatabaseHelper.getInstance(getContext());
        songs_database = _songs_databaseHelper.getWritableDatabase();


        topPicsList = new ArrayList<>();
        recentList = new ArrayList<>();
        songsList = new ArrayList<>();

        Cursor songs_cursor = songs_database.rawQuery("SELECT * FROM _songs_tb ", new String[]{});
        if (songs_cursor != null) {
            if (songs_cursor.moveToFirst())
                do {
                    SongModel song = new SongModel(songs_cursor.getLong(songs_cursor.getColumnIndex("_song_id")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_title")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_artist")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_genre")), songs_cursor.getString(songs_cursor.getColumnIndex("_is_favourite")), songs_cursor.getLong(songs_cursor.getColumnIndex("_song_album_id")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_album")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_album_art_path")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_art_path")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_path")), "0");
                    songsList.add(song);
                } while (songs_cursor.moveToNext());


            int SONGS_COUNT = songsList.size();
            if (SONGS_COUNT == 0)
                mediaAvailable = false;


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
        } else
            mediaAvailable = false;
    }
}
