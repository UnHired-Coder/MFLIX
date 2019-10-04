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

public class fragment_artist extends Fragment {


    private RecyclerView myArtistRecyclerView;
    private List<song_template> artistList;




    static database_helper _songs_database_helper;
    static SQLiteDatabase songs_database;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


   View v= inflater.inflate(R.layout.fragment_artist,null);



        myArtistRecyclerView = (RecyclerView) v.findViewById(R.id.artist_recyclerView);
        songsList_recyclerView_adapter albumAdapter = new songsList_recyclerView_adapter(getContext(), artistList,1);
        myArtistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        myArtistRecyclerView.setAdapter(albumAdapter);

        albumAdapter.setmOnClickListener(new songsList_recyclerView_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.i("clicked",Integer.toString(position));
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragment_songs_list listSongsFragment=new fragment_songs_list();
                Bundle args = new Bundle();
                args.putString("list_filter",artistList.get(position).getSongArtist());
                args.putString("type","artist");
                listSongsFragment.setArguments(args);
                fragmentTransaction.add(R.id.playlists_container,listSongsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



        return  v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        artistList = new ArrayList<>();

        _songs_database_helper= new database_helper(getContext());
        songs_database = _songs_database_helper.getWritableDatabase();



        Cursor songs_cursor =songs_database.rawQuery("SELECT _song_artist,count(_song_artist) FROM _songs_tb GROUP BY _song_artist ",new String[]{}) ;
        if(songs_cursor != null){
            songs_cursor.moveToFirst();

            do {

                // Log.i("song Name",cursor.getString(0)+"--- "+cursor.getString(1)+"---- "+cursor.getString(2)+" ---"+cursor.getString(3)+" ---"+cursor.getString(4)+"--- "+cursor.getString(5)+"--- "+cursor.getString(6)+" ---"+cursor.getString(7)+" ---"+cursor.getString(8));

                // Log.i("song Name",songs_cursor.getString(0)+"----"+songs_cursor.getString(1));

                song_template song = new song_template(0L,"",songs_cursor.getString(songs_cursor.getColumnIndex("_song_artist"))," "," ",0L," "," "," ","",songs_cursor.getString(1));

                artistList.add(song);

            }while (songs_cursor.moveToNext());


        }



//        song_template song = new song_template(0L,"",""," "," ",0L," "," "," ","","");
//
//        Log.i("this---","--------");
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
