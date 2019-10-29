package com.telitel.tiwari.mflix;


import android.content.Context;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.telitel.tiwari.mflix.MainActivity.Broadcast_PLAY_NEW_AUDIO;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_songs_list extends Fragment {



   public static TextView tv;



    private RecyclerView myFavouriteRecyclerView;
    private List<song_template> songsList;

    static database_helper _songs_database_helper;
    static SQLiteDatabase songs_database;


//    private albumFragmebtListener listener;
//
//    public interface  albumFragmebtListener{
//        void  onListSentTo(CharSequence input);
//    }
//
//


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v =inflater.inflate(R.layout.fragment_fragment_songs_list, container, false);


        myFavouriteRecyclerView = (RecyclerView) v.findViewById(R.id.songs_list_recyclerView);
        songs_recyclerView_adapter songAdapter = new songs_recyclerView_adapter(getContext(), songsList,0);
        myFavouriteRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        myFavouriteRecyclerView.setAdapter(songAdapter);

        songAdapter.setOnItemClickListener(new songs_recyclerView_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.i("clicked",Integer.toString(position));

                StorageUtil storage = new StorageUtil(getActivity().getApplicationContext());
                storage.clearCachedAudioPlaylist();
                storage.storeAudio(songsList);
                storage.storeAudioIndex(position);
                storage.storeAudioPosition(0);
                // MainActivity.playAudio(0);
                //Service is active
                //Send a broadcast to the service -> PLAY_NEW_AUDIO
                if(MainActivity.isPlaying) {
                    Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
                    getActivity().sendBroadcast(broadcastIntent);
                }
                Log.i("But","here");
                MainActivity.setPlayerSongsRecyclerView(songsList,position);

            }
        });



        tv=v.findViewById(R.id.textView7);

       if(getArguments()!=null) {
           tv.setText( "[ "+getArguments().getString("list_filter")+" ]");
       }


       return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        songsList = new ArrayList<>();

        _songs_database_helper = MainActivity._songs_database_helper;
        songs_database = _songs_database_helper.getWritableDatabase();


        songsList = new ArrayList<>();

        Cursor songs_cursor=null;


        if(getArguments().getString("type").equals("album")){

             songs_cursor = songs_database.rawQuery("SELECT * FROM _songs_tb WHERE _song_album=?", new String[]{getArguments().getString("list_filter")});

        }
        if(getArguments().getString("type").equals("artist")){

             songs_cursor = songs_database.rawQuery("SELECT * FROM _songs_tb WHERE _song_artist=?", new String[]{getArguments().getString("list_filter")});

        }
        if(getArguments().getString("type").equals("genre")){

             songs_cursor = songs_database.rawQuery("SELECT * FROM _songs_tb WHERE _song_genre=?", new String[]{getArguments().getString("list_filter")});

        }
        if(getArguments().getString("type").equals("playlist")){

            songs_cursor = songs_database.rawQuery("SELECT * FROM '"+getArguments().getString("list_filter")+"'", new String[]{});

        }







        if (songs_cursor != null) {
            if (songs_cursor.moveToFirst()) {
                songs_cursor.moveToFirst();
                do {

                    // Log.i("song Name",cursor.getString(0)+"--- "+cursor.getString(1)+"---- "+cursor.getString(2)+" ---"+cursor.getString(3)+" ---"+cursor.getString(4)+"--- "+cursor.getString(5)+"--- "+cursor.getString(6)+" ---"+cursor.getString(7)+" ---"+cursor.getString(8));

                     Log.i("song Name", songs_cursor.getString(0) + "----" +songs_cursor.getString(songs_cursor.getColumnIndex("_song_title")));

                   song_template song = new song_template(songs_cursor.getLong(songs_cursor.getColumnIndex("_song_id")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_title")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_artist")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_genre")), songs_cursor.getString(songs_cursor.getColumnIndex("_is_favourite")), songs_cursor.getLong(songs_cursor.getColumnIndex("_song_album_id")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_album")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_album_art_path")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_art_path")), songs_cursor.getString(songs_cursor.getColumnIndex("_song_path")),"0");
                   songsList.add(song);

                } while (songs_cursor.moveToNext());

            }
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
