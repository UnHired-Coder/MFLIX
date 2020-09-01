package com.telitel.tiwari.mflix.Screens.Fragments;

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

import com.telitel.tiwari.mflix.Database.DatabaseHelper;
import com.telitel.tiwari.mflix.R;
import com.telitel.tiwari.mflix.Models.SongModel;
import com.telitel.tiwari.mflix.RecyclerViewAdapters.SongsListRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GenreFragment extends Fragment {


    private List<SongModel> genreList;


    static DatabaseHelper _songs_databaseHelper;
    static SQLiteDatabase songs_database;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_genre, null);


        RecyclerView myGenreRecyclerView = v.findViewById(R.id.genre_recyclerView);
        SongsListRecyclerViewAdapter albumAdapter = new SongsListRecyclerViewAdapter(getContext(), genreList, 2);
        myGenreRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        myGenreRecyclerView.setAdapter(albumAdapter);

        albumAdapter.setmOnClickListener(new SongsListRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.i("clicked", Integer.toString(position));
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SongsListFragment listSongsFragment = new SongsListFragment();
                Bundle args = new Bundle();
                args.putString("list_filter", genreList.get(position).getSongGener());
                args.putString("type", "genre");
                listSongsFragment.setArguments(args);
                fragmentTransaction.add(R.id.playlists_container, listSongsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        genreList = new ArrayList<>();


        _songs_databaseHelper = DatabaseHelper.getInstance(getContext());
        songs_database = _songs_databaseHelper.getWritableDatabase();


        Cursor songs_cursor = songs_database.rawQuery("SELECT _song_genre,count(_song_genre) FROM _songs_tb GROUP BY _song_genre ", new String[]{});
        if (songs_cursor != null) {

            if(songs_cursor.moveToFirst())
            do {
                SongModel song = new SongModel(0L, "", "", songs_cursor.getString(songs_cursor.getColumnIndex("_song_genre")), " ", 0L, " ", " ", " ", "", songs_cursor.getString(1));

                genreList.add(song);

            } while (songs_cursor.moveToNext());

            songs_cursor.close();
        }
    }
}
