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

import com.squareup.picasso.Picasso;
import com.telitel.tiwari.mflix.searchVideos.SearchVideosList;

import java.util.List;

public class videos_recyclerView_adapter extends RecyclerView.Adapter<videos_recyclerView_adapter.mySongsViewHolder> {

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

    public videos_recyclerView_adapter(Context mContext, SearchVideosList mData, int type) {

        this.mContext = mContext;
        this.mData = mData;
        this.typeSong = type;

    }


    @NonNull
    @Override
    public mySongsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;


        v = LayoutInflater.from(mContext).inflate(R.layout.video_item, viewGroup, false);
        mySongsViewHolder vHolder = new mySongsViewHolder(v, mClickListener);

        return vHolder;
    }

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


    public SearchVideosList getmData() {
        return mData;
    }

    @Override
    public int getItemCount() {
        return mData.getItems().size();
    }


    public static class mySongsViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_Name;
        private TextView tv_Artist;
        private ImageView iv_AlbumArt;

        private TextView cp_tv_Name;
        private ImageView cp_iv_AlbumArt;


        public mySongsViewHolder(View itemView, final OnItemClickListener clickListener) {

            super(itemView);

            tv_Name = (TextView) itemView.findViewById(R.id.video_title_list);
            tv_Artist = (TextView) itemView.findViewById(R.id.channel_title_list);
            iv_AlbumArt = (ImageView) itemView.findViewById(R.id.video_thumbnail);


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
