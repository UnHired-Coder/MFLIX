package com.telitel.tiwari.mflix;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;
import java.util.List;

public class player_view extends BottomSheetDialogFragment{


    private DiscreteScrollView mySongsRecyclerView;


    private List<song_template> songsList;
   private   songs_recyclerView_adapter songAdapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.activity_player_view,container,false);


//        mySongsRecyclerView = v.findViewById(R.id.songs_recyclerView_2);
//        songAdapter = new songs_recyclerView_adapter(getContext(), songsList, 3);
//        mySongsRecyclerView.setAdapter(songAdapter);
//
//        songsList = new ArrayList<>();
//
//
//        song_template song = new song_template(0L,"",""," "," ",0L," "," ","No","","");
//

       Log.i("Player view ","Created");




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






        return v;
    }



    //    private DiscreteScrollView mySongsRecyclerView;
//
//
//    private List<song_template> songsList;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_player_view);
//
//
//
//    }
//
//    @Override
//    public View onCreateView(String name, Context context, AttributeSet attrs) {
//       View v= super.onCreateView(name, context, attrs);
//
//
//        mySongsRecyclerView = v.findViewById(R.id.songs_recyclerView_2);
//        songs_recyclerView_adapter songAdapter = new songs_recyclerView_adapter(this, songsList, 3);
//        mySongsRecyclerView.setAdapter(songAdapter);
//
//        songsList = new ArrayList<>();
//
//
//        song_template song = new song_template(0L,"",""," "," ",0L," "," ","No","","");
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
//
//
//
//
//        return v;
//}
}
