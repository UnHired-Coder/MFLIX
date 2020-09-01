package com.telitel.tiwari.mflix.YouTubeVideo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.telitel.tiwari.mflix.R;
import com.telitel.tiwari.mflix.YouTubeVideo.Models.searchVideos.SearchVideosList;

public class VideoRecyclerAdapter extends RecyclerView.Adapter<VideoRecyclerAdapter.mySongsViewHolder> {

    Context mContext;
    SearchVideosList mData;
    int typeSong;
    private OnItemClickListener mClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener clickListener) {

        mClickListener = clickListener;

    }

    public VideoRecyclerAdapter(Context mContext, SearchVideosList mData, int type) {

        this.mContext = mContext;
        this.mData = mData;
        this.typeSong = type;

    }


    @NonNull
    @Override
    public mySongsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;


        v = LayoutInflater.from(mContext).inflate(R.layout.video_item, viewGroup, false);

        return new mySongsViewHolder(v, mClickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull mySongsViewHolder mySongsViewHolder, int i) {

        if (mData.getItems().get(i).getSnippet().getTitle() != null)
            mySongsViewHolder.tv_Name.setText(mData.getItems().get(i).getSnippet().getTitle());
        else mySongsViewHolder.tv_Name.setText("Not Found");

        if (mData.getItems().get(i).getSnippet().getChannelTitle() != null)
            mySongsViewHolder.tv_Artist.setText(mData.getItems().get(i).getSnippet().getChannelTitle());
        else mySongsViewHolder.tv_Artist.setText("Not Found");


        String ImageUrl = mData.getItems().get(i).getSnippet().getThumbnails().getMedium().getUrl();
        Picasso.with(this.mContext).load(ImageUrl).into(mySongsViewHolder.iv_AlbumArt);

    }


    @Override
    public void onViewRecycled(@NonNull mySongsViewHolder holder) {

        holder.itemView.setAlpha(1f);

    }

    @Override
    public int getItemCount() {
        if(mData==null)
            return  0;
        return mData.getItems().size();
    }


    public static class mySongsViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_Name;
        private TextView tv_Artist;
        private ImageView iv_AlbumArt;



        public mySongsViewHolder(View itemView, final OnItemClickListener clickListener) {

            super(itemView);

            tv_Name =  itemView.findViewById(R.id.video_title_list);
            tv_Artist =  itemView.findViewById(R.id.channel_title_list);
            iv_AlbumArt = itemView.findViewById(R.id.video_thumbnail);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
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
