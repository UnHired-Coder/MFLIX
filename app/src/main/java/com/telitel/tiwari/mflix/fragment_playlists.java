package com.telitel.tiwari.mflix;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class fragment_playlists extends Fragment {

    private RecyclerView myPlaylistsRecyclerView;
    public static List<song_template> playListsList;
    public static songsList_recyclerView_adapter songAdapter;

    static SQLiteDatabase songs_database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_playlists,null);


        myPlaylistsRecyclerView = (RecyclerView) v.findViewById(R.id.playlists_Songs_recyclerView);
        songAdapter = new songsList_recyclerView_adapter(getContext(), playListsList,3);
        myPlaylistsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        myPlaylistsRecyclerView.setAdapter(songAdapter);

        songAdapter.setmOnClickListener(new songsList_recyclerView_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragment_songs_list listSongsFragment=new fragment_songs_list();
                Bundle args = new Bundle();
                args.putString("list_filter",playListsList.get(position).getSongTitle());
                Log.i("playList is for ",playListsList.get(position).getSongTitle());
                args.putString("type","playlist");
                listSongsFragment.setArguments(args);
                fragmentTransaction.add(R.id.created_playlist_container,listSongsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        playListsList = new ArrayList<>();

        songs_database = MainActivity._songs_database_helper.getWritableDatabase();
        Cursor songs_cursor = songs_database.rawQuery("SELECT * FROM  _playlists_tb ", new String[]{});
        if (songs_cursor != null) {
            if (songs_cursor.moveToFirst()) {

                do {
                    song_template song = new song_template(0L,songs_cursor.getString(songs_cursor.getColumnIndex("_playlist_name_")),""," "," ",0L," "," "," ","","");
                    playListsList.add(song);

                } while (songs_cursor.moveToNext());

            }

        }



    }
}
