package com.telitel.tiwari.mflix.RecyclerViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.telitel.tiwari.mflix.R;
import com.telitel.tiwari.mflix.Models.SongModel;

import java.util.List;

public class SongsListRecyclerViewAdapter extends RecyclerView.Adapter<SongsListRecyclerViewAdapter.mySongsViewHolder> {

    Context mContext;
    List<SongModel> mData;
    public int type;

    private OnItemClickListener mOnClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public void setmOnClickListener(OnItemClickListener ClickListener) {
        mOnClickListener = ClickListener;
    }

    public SongsListRecyclerViewAdapter(Context mContext, List<SongModel> mData, int type) {

        this.mContext = mContext;
        this.mData = mData;
        this.type = type;

    }


    @NonNull
    @Override
    public mySongsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;

        if (type == 0)
            v = LayoutInflater.from(mContext).inflate(R.layout.album_item, viewGroup, false);
        else if (type == 1)
            v = LayoutInflater.from(mContext).inflate(R.layout.artist_item, viewGroup, false);
        else
            v = LayoutInflater.from(mContext).inflate(R.layout.artist_item, viewGroup, false);

        return new mySongsViewHolder(v, mOnClickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull mySongsViewHolder mySongsViewHolder, int i) {
        if (type == 0) {
            if (mData.get(i).getSongAlbum().equals("Un Known"))
                mySongsViewHolder.tv_Name.setText("No Album");
            else
                mySongsViewHolder.tv_Name.setText(mData.get(i).getSongAlbum());

            mySongsViewHolder.tv_Count.setText(mData.get(i).getSongCount());

            if (mData.get(i).getSongAlbumArtPath().equals("Un Known"))
                mySongsViewHolder.iv_AlbumArt.setImageResource(R.drawable.sample_avatar);
            else {
//                mySongsViewHolder.iv_AlbumArt.setImageURI(Uri.parse(mData.get(i).getSongAlbumArtPath()));
            }
        } else if (type == 1) {
            if (mData.get(i).getSongArtist().equals("Un Known"))
                mySongsViewHolder.tv_Name.setText("No Artist");
            else
                mySongsViewHolder.tv_Name.setText(mData.get(i).getSongArtist());

            mySongsViewHolder.tv_Count.setText(mData.get(i).getSongCount());
            mySongsViewHolder.iv_AlbumArt.setImageResource(R.drawable.sample_artist);


        } else if (type == 2) {
            if (mData.get(i).getSongArtist().equals("Un Known"))
                mySongsViewHolder.tv_Name.setText("No Artist");
            else
                mySongsViewHolder.tv_Name.setText(mData.get(i).getSongGener());

            mySongsViewHolder.tv_Count.setText(mData.get(i).getSongCount());
            mySongsViewHolder.iv_AlbumArt.setImageResource(R.drawable.sample_genre);

        } else if (type == 3) {
            if (mData.get(i).getSongArtist().equals("Un Known"))
                mySongsViewHolder.tv_Name.setText("No Title");
            else
                mySongsViewHolder.tv_Name.setText(mData.get(i).getSongTitle());

            mySongsViewHolder.tv_Count.setText(mData.get(i).getSongCount());
            mySongsViewHolder.iv_AlbumArt.setImageResource(R.drawable.play_list_logo);

        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class mySongsViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_Name;
        private TextView tv_Count;
        private ImageView iv_AlbumArt;


        public mySongsViewHolder(View itemView, final OnItemClickListener Clicklistener) {
            super(itemView);
            tv_Name = itemView.findViewById(R.id.album_name);
            tv_Count = itemView.findViewById(R.id.song_count);
            iv_AlbumArt = itemView.findViewById(R.id.song_artView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Clicklistener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Clicklistener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
