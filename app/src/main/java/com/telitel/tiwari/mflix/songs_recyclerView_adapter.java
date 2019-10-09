package com.telitel.tiwari.mflix;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
    private OnItemClickListener mClickListener;

    public interface OnItemClickListener{
        void  onItemClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener clickListener){

        mClickListener = clickListener;

    }
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
        else if (typeSong==4)
        v= LayoutInflater.from(mContext).inflate(R.layout.play_list_item,viewGroup,false);
        else
            v= LayoutInflater.from(mContext).inflate(R.layout.plyer_song_art_view,viewGroup,false);

        mySongsViewHolder vHolder = new mySongsViewHolder(v,mClickListener);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull mySongsViewHolder mySongsViewHolder, int i) {





        if(this.typeSong==0){
            if(mData.get(i).getSongTitle()!=null)
                mySongsViewHolder.tv_Name.setText(mData.get(i).getSongTitle());
            else mySongsViewHolder.tv_Name.setText("Not Found");

            if(mData.get(i).getSongArtist()!=null)
                mySongsViewHolder.tv_Artist.setText(mData.get(i).getSongArtist());
            else mySongsViewHolder.tv_Artist.setText("Not Found");

            if(mData.get(i).getSongArtPath().equals("No"))
                mySongsViewHolder.iv_AlbumArt.setImageResource(R.drawable.sample_avatar);
            else
                mySongsViewHolder.iv_AlbumArt.setImageURI(Uri.parse(mData.get(i).getSongArtPath()));


        }else if(this.typeSong==1) {

            if(mData.get(i).getSongTitle()!=null)
                mySongsViewHolder.tv_Name.setText(mData.get(i).getSongTitle());
            else mySongsViewHolder.tv_Name.setText("Not Found");

            if(mData.get(i).getSongArtist()!=null)
                mySongsViewHolder.tv_Artist.setText(mData.get(i).getSongArtist());
            else mySongsViewHolder.tv_Artist.setText("Not Found");

            if(mData.get(i).getSongArtPath().equals("No"))
                mySongsViewHolder.iv_AlbumArt.setImageResource(R.drawable.sample_avatar);
            else
                mySongsViewHolder.iv_AlbumArt.setImageURI(Uri.parse(mData.get(i).getSongArtPath()));

        }
        else if(this.typeSong==3) {


            mySongsViewHolder.itemView.setAlpha(1f);
            if(mData.get(i).getSongTitle()!=null)
                mySongsViewHolder.tv_Name.setText(mData.get(i).getSongTitle());
            else mySongsViewHolder.tv_Name.setText("Not Found");
            if(mData.get(i).getSongArtPath().equals("No"))
                mySongsViewHolder.iv_AlbumArt.setImageResource(R.drawable.sample_avatar);
            else
                mySongsViewHolder.iv_AlbumArt.setImageURI(Uri.parse(mData.get(i).getSongArtPath()));

        }
        else if(this.typeSong==4) {


            mySongsViewHolder.itemView.setAlpha(1f);
            if(mData.get(i).getSongTitle()!=null)
                mySongsViewHolder.tv_Name.setText(mData.get(i).getSongTitle());
            else mySongsViewHolder.tv_Name.setText("Not Found");
                mySongsViewHolder.iv_AlbumArt.setImageResource(R.drawable.splash_logo);


        }
        else {
            if(mData.get(i).getSongTitle()!=null)
                mySongsViewHolder.tv_Name.setText(mData.get(i).getSongTitle());
            else mySongsViewHolder.tv_Name.setText("Not Found");

            if(mData.get(i).getSongArtist()!=null)
                mySongsViewHolder.tv_Artist.setText(mData.get(i).getSongArtist());
            else mySongsViewHolder.tv_Artist.setText("Not Found");

            if(mData.get(i).getSongArtPath().equals("No"))
                mySongsViewHolder.iv_AlbumArt.setImageResource(R.drawable.sample_avatar);
            else
                mySongsViewHolder.iv_AlbumArt.setImageURI(Uri.parse(mData.get(i).getSongArtPath()));

        }






//        mySongsViewHolder.iv_AlbumArt.setAlpha(selectedItem == null || hero.equals(selectedItem) ? 1f : 0.5f);
//        mySongsViewHolder.iv_AlbumArt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectedItem = hero.equals(selectedItem) ? null : hero;
//            ...
//                notifyDataSetChanged();
//            }
//        });



    }




    @Override
    public void onViewRecycled(@NonNull mySongsViewHolder holder) {

        holder.itemView.setAlpha(1f);

    }



    public List<song_template> getmData() {
        return mData;
    }

    @Override
    public int getItemCount() {
       return mData.size();
    }



    public static class  mySongsViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_Name;
        private TextView tv_Artist;
        private ImageView iv_AlbumArt;

        private TextView cp_tv_Name;
        private ImageView cp_iv_AlbumArt;





        public mySongsViewHolder(View itemView, final OnItemClickListener clickListener){

            super(itemView);

            tv_Name=(TextView) itemView.findViewById(R.id.song_name);
            tv_Artist=(TextView)itemView.findViewById(R.id.song_artist);
            iv_AlbumArt=(ImageView)itemView.findViewById(R.id.song_artView);



           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(clickListener!=null){
                       int position = getAdapterPosition();
                       if(position!= RecyclerView.NO_POSITION){
                           clickListener.onItemClick(position);
                       }

                   }
               }
           });
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
