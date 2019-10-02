package com.telitel.tiwari.mflix;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public  class songs_recyclerView_adapter extends RecyclerView.Adapter<songs_recyclerView_adapter.mySongsViewHolder> {

    Context mContext;
    List<song_template> mData;
    int typeSong;

    public songs_recyclerView_adapter(Context mContext,List<song_template> mData,int type){

        this.mContext=mContext;
        this.mData=mData;
        this.typeSong=type;

    }




    @NonNull
    @Override
    public mySongsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;

        if(typeSong==0)
        v= LayoutInflater.from(mContext).inflate(R.layout.song_item,viewGroup,false);
        else if(typeSong==1)
        v= LayoutInflater.from(mContext).inflate(R.layout.song_item_large,viewGroup,false);
        else if(typeSong==2)
        v= LayoutInflater.from(mContext).inflate(R.layout.favourite_song_item,viewGroup,false);
        else
        v= LayoutInflater.from(mContext).inflate(R.layout.song_item,viewGroup,false);


        mySongsViewHolder vHolder = new mySongsViewHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull mySongsViewHolder mySongsViewHolder, int i) {


        if(this.typeSong==0){
            mySongsViewHolder.tv_Name.setText("Dont It to ME");
            mySongsViewHolder.tv_Artist.setText("Dj Nick");
            mySongsViewHolder.iv_AlbumArt.setImageResource(R.drawable.sample_avatar);

        }else if(this.typeSong==1) {

            mySongsViewHolder.tv_Name.setText("Give It to ME");
            mySongsViewHolder.tv_Artist.setText("Dj Nick");
            mySongsViewHolder.iv_AlbumArt.setImageResource(R.drawable.sample_avatar);
        }
        else {
            mySongsViewHolder.tv_Name.setText("Give It to ME");
            mySongsViewHolder.tv_Artist.setText("Dj Nick");
            mySongsViewHolder.iv_AlbumArt.setImageResource(R.drawable.sample_avatar);
        }

    }

    @Override
    public int getItemCount() {
       return mData.size();
    }



    public static class  mySongsViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_Name;
        private TextView tv_Artist;
        private ImageView iv_AlbumArt;




        public mySongsViewHolder(View itemView){

            super(itemView);

            tv_Name=(TextView) itemView.findViewById(R.id.song_name);
            tv_Artist=(TextView)itemView.findViewById(R.id.song_artist);
            iv_AlbumArt=(ImageView)itemView.findViewById(R.id.playerSongArtView);



//
//            if(typeSong==2)
//                itemView.findViewById(R.id.favourite_View).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.i("thgis",Integer.toString(getPosition()));
//
//
//                    }
//                });



        }

    }



}
