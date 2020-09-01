package com.telitel.tiwari.mflix.Screens.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.telitel.tiwari.mflix.Database.DatabaseHelper;
import com.telitel.tiwari.mflix.Database.StorageUtil;
import com.telitel.tiwari.mflix.MainActivity;
import com.telitel.tiwari.mflix.R;
import com.telitel.tiwari.mflix.Models.SongModel;
import com.telitel.tiwari.mflix.RecyclerViewAdapters.SongsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//import static com.telitel.tiwari.mflix.MainActivity.Broadcast_PLAY_NEW_AUDIO;

public class FavouriteFragment extends Fragment {


    public static ArrayList<SongModel> songsList;
    public static SongsRecyclerViewAdapter songAdapter;

    static DatabaseHelper _songs_databaseHelper;
    static SQLiteDatabase songs_database;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("But", "here");

        View v = inflater.inflate(R.layout.fragment_favourite, null);

        RecyclerView myFavouriteRecyclerView = v.findViewById(R.id.favourite_Songs_recyclerView);
        songAdapter = new SongsRecyclerViewAdapter(getContext(), songsList, 2);
        myFavouriteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        myFavouriteRecyclerView.setAdapter(songAdapter);

        songAdapter.setOnItemClickListener(new SongsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                StorageUtil storage = StorageUtil.getInstance(getContext());
                storage.clearCachedAudioPlaylist();
                storage.storeAudio(songsList);
                storage.storeAudioIndex(position);
                storage.storeAudioPosition(0);
                // MainActivity.playAudio(0);
                //Service is active
                //Send a broadcast to the service -> PLAY_NEW_AUDIO
//                if (MainActivity.isPlaying) {
//                    Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
//                    getActivity().sendBroadcast(broadcastIntent);
//                }
//                Log.i("But", "here");
//                MainActivity.setPlayerSongsRecyclerView(songsList, position);

            }
        });

        songAdapter.notifyDataSetChanged();
        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        songsList = new ArrayList<>();

        Log.i("But", "here");

        _songs_databaseHelper = DatabaseHelper.getInstance(getContext());
        songs_database = _songs_databaseHelper.getWritableDatabase();

        songsList = new ArrayList<>();

        Cursor songs_cursor = songs_database.rawQuery("SELECT * FROM _favourite_list_00100_ ", new String[]{});
        if (songs_cursor != null) {
            if (songs_cursor.moveToFirst()) {
                songs_cursor.moveToFirst();
                do {
                    SongModel song = new SongModel(songs_cursor.getLong(songs_cursor.getColumnIndex("_song_id")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_title")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_artist")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_genre")), songs_cursor.getString(songs_cursor.getColumnIndex("_is_favourite")), songs_cursor.getLong(songs_cursor.getColumnIndex("_song_album_id")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_album")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_album_art_path")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_art_path")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_path")), "");
                    songsList.add(song);

                } while (songs_cursor.moveToNext());
            }
            songs_cursor.close();
        }
    }
}
