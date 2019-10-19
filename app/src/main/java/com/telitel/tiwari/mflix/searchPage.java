package com.telitel.tiwari.mflix;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.telitel.tiwari.mflix.searchVideos.SearchVideosList;
import com.telitel.tiwari.mflix.videoDetails.VideosList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.telitel.tiwari.mflix.MainActivity.Broadcast_PAUSE_AUDIO;
import static com.telitel.tiwari.mflix.MainActivity.context;


/**
 * A simple {@link Fragment} subclass.
 */


public class searchPage extends Fragment {


    private EditText searchField;
    private ImageView searchButton;
    private RecyclerView myVideosRecyclerView;
    SearchVideosList sv;
    String query_str;




    public searchPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search_page, container, false);
        myVideosRecyclerView = (RecyclerView) v.findViewById(R.id.recomended_videos_recyclerView);
        searchButton = v.findViewById(R.id.search_button);
        searchField = v.findViewById(R.id.search_field);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query_str = searchField.getText().toString();
                if (!query_str.equals("")) {
                    getSearchData();
                }
            }
        });


        getData();
        getSearchData();


        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void getData() {
        retrofit2.Call<VideosList> videosListCall = youtubeDataApi.getVideoService().getVideos();
        videosListCall.enqueue(new Callback<VideosList>() {
            @Override
            public void onResponse(Call<VideosList> call, Response<VideosList> response) {
                VideosList v = response.body();
                Toast.makeText(getContext(), "success", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<VideosList> call, Throwable t) {

                Toast.makeText(getContext(), "failed", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void getSearchData() {
        retrofit2.Call<SearchVideosList> searchVideosListCall = youtubeDataApi.getVideoSearchService().getSearchVideos(query_str);
        searchVideosListCall.enqueue(new Callback<SearchVideosList>() {
            @Override
            public void onResponse(Call<SearchVideosList> call, Response<SearchVideosList> response) {
                sv = response.body();
                Toast.makeText(getContext(), "success", Toast.LENGTH_LONG).show();

                videos_recyclerView_adapter videosAdapter = new videos_recyclerView_adapter(getContext(), sv, 1);
                myVideosRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                myVideosRecyclerView.setAdapter(videosAdapter);
                videosAdapter.setOnItemClickListener(new videos_recyclerView_adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {


                        Intent playerIntent = new Intent(getContext(), video_player_intent.class);
                        playerIntent.putExtra("videoId", sv.getItems().get(position).getId().getVideoId());


                        Pair[] pairs = new Pair[1];
                        pairs[0] = new Pair<View, String>(myVideosRecyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.video_thumbnail), "thumbnail_to_video");

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
                            startActivity(playerIntent, options.toBundle());
                        } else
                            startActivity(playerIntent);
                    }
                });
            }

            @Override
            public void onFailure(Call<SearchVideosList> call, Throwable t) {
                Toast.makeText(getContext(), "failed", Toast.LENGTH_LONG).show();
                Log.i("error", t.getMessage());

            }
        });


    }
}