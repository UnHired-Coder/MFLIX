package com.telitel.tiwari.mflix;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.telitel.tiwari.mflix.searchVideos.SearchVideosList;
import com.telitel.tiwari.mflix.videoDetails.VideosList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */




public class searchPage extends Fragment {


    private TextView textView;
    private RecyclerView myVideosRecyclerView;
    SearchVideosList sv;
    public searchPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_search_page, container, false);
        myVideosRecyclerView = (RecyclerView) v.findViewById(R.id.recomended_videos_recyclerView);

        getData();
        getSearchData();



        return v;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void getData()
    {
        retrofit2.Call<VideosList> videosListCall = youtubeDataApi.getVideoService().getVideos();
        videosListCall.enqueue(new Callback<VideosList>() {
            @Override
            public void onResponse(Call<VideosList> call, Response<VideosList> response) {
                VideosList v = response.body();
                Toast.makeText(getContext(),"success",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<VideosList> call, Throwable t) {

                Toast.makeText(getContext(),"failed",Toast.LENGTH_LONG).show();

            }
        });

    }

    private void getSearchData()
    {
        retrofit2.Call<SearchVideosList> searchVideosListCall = youtubeDataApi.getVideoSearchService().getSearchVideos();
        searchVideosListCall.enqueue(new Callback<SearchVideosList>() {
            @Override
            public void onResponse(Call<SearchVideosList> call, Response<SearchVideosList> response) {
                 sv = response.body();
                Toast.makeText(getContext(),"success",Toast.LENGTH_LONG).show();

                videos_recyclerView_adapter videosAdapter = new videos_recyclerView_adapter(getContext(), sv,1);
                myVideosRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                myVideosRecyclerView.setAdapter(videosAdapter);

            }

            @Override
            public void onFailure(Call<SearchVideosList> call, Throwable t) {
                Toast.makeText(getContext(),"failed",Toast.LENGTH_LONG).show();
                Log.i("error",t.getMessage());

            }
        });

    }
}