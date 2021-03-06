package com.telitel.tiwari.mflix.Screens.Fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class SongsListFragment extends Fragment {


    public TextView tv;
    private ArrayList<SongModel> songsList;

    static DatabaseHelper _songs_databaseHelper;
    static SQLiteDatabase songs_database;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragment_songs_list, container, false);

        RecyclerView myFavouriteRecyclerView = (RecyclerView) v.findViewById(R.id.songs_list_recyclerView);
        SongsRecyclerViewAdapter songAdapter = new SongsRecyclerViewAdapter(getContext(), songsList, 0);
        myFavouriteRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        myFavouriteRecyclerView.setAdapter(songAdapter);

        songAdapter.setOnItemClickListener(new SongsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.i("clicked", Integer.toString(position));

                StorageUtil storage = StorageUtil.getInstance(getContext());
                storage.clearCachedAudioPlaylist();
                storage.storeAudio(songsList);
                storage.storeAudioIndex(position);
                storage.storeAudioPosition(0);
                Intent broadcastIntent = new Intent(MainActivity.Broadcast_MEDIA_CHANGED);
                Gson gson = new Gson();
                String json = gson.toJson(songsList);
                broadcastIntent.putExtra("data", json);
                broadcastIntent.putExtra("position", position);
                if (getContext() != null)
                    getContext().sendBroadcast(broadcastIntent);
            }
        });


        tv = v.findViewById(R.id.textView7);

        if (getArguments() != null) {
            tv.setText("[ " + getArguments().getString("list_filter") + " ]");
        }

        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        songsList = new ArrayList<>();

        _songs_databaseHelper = DatabaseHelper.getInstance(getContext());
        songs_database = _songs_databaseHelper.getWritableDatabase();


        songsList = new ArrayList<>();

        Cursor songs_cursor = null;


        assert getArguments() != null;
        if (Objects.equals(getArguments().getString("type"), "album")) {

            songs_cursor = songs_database.rawQuery("SELECT * FROM _songs_tb WHERE _song_album=?", new String[]{getArguments().getString("list_filter")});

        }
        if (Objects.equals(getArguments().getString("type"), "artist")) {

            songs_cursor = songs_database.rawQuery("SELECT * FROM _songs_tb WHERE _song_artist=?", new String[]{getArguments().getString("list_filter")});

        }
        if (Objects.equals(getArguments().getString("type"), "genre")) {

            songs_cursor = songs_database.rawQuery("SELECT * FROM _songs_tb WHERE _song_genre=?", new String[]{getArguments().getString("list_filter")});

        }
        if (Objects.equals(getArguments().getString("type"), "playlist")) {

            songs_cursor = songs_database.rawQuery("SELECT * FROM '" + getArguments().getString("list_filter") + "'", new String[]{});

        }


        if (songs_cursor != null) {
            if (songs_cursor.moveToFirst()) {
                songs_cursor.moveToFirst();
                do {

                    // Log.i("song Name",cursor.getString(0)+"--- "+cursor.getString(1)+"---- "+cursor.getString(2)+" ---"+cursor.getString(3)+" ---"+cursor.getString(4)+"--- "+cursor.getString(5)+"--- "+cursor.getString(6)+" ---"+cursor.getString(7)+" ---"+cursor.getString(8));

                    Log.i("song Name", songs_cursor.getString(0) + "----" + songs_cursor.getString(songs_cursor.getColumnIndex("_song_title")));

                    SongModel song = new SongModel(songs_cursor.getLong(songs_cursor.getColumnIndex("_song_id")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_title")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_artist")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_genre")), songs_cursor.getString(songs_cursor.getColumnIndex("_is_favourite")), songs_cursor.getLong(songs_cursor.getColumnIndex("_song_album_id")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_album")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_album_art_path")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_art_path")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_path")), "0");
                    songsList.add(song);

                } while (songs_cursor.moveToNext());

            }
            songs_cursor.close();
        }

    }


//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        if(context instanceof albumFragmebtListener){
//            listener = (albumFragmebtListener) context;
//        }else {
//            throw  new RuntimeException(context.toString()+" must implement --------");
//        }
//
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        listener = null;
//    }
//
//


}
