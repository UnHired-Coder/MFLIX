package com.telitel.tiwari.mflix;

import com.telitel.tiwari.mflix.searchVideos.SearchVideosList;
import com.telitel.tiwari.mflix.videoDetails.VideosList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class youtubeDataApi {

    private static final String key = "AIzaSyCKAP7Y-RMjj3C4quc1pknoMjmYaw8lR5k";
    private static final String url = "https://www.googleapis.com/youtube/v3/";


    public static VideoService videoService = null;

    public static VideoService getVideoService() {
        if (videoService == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            videoService = retrofit.create(VideoService.class);
        }
        return videoService;

    }

    public static VideoSearchService videoSearchService = null;

    public static VideoSearchService getVideoSearchService() {
        if (videoSearchService == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            videoSearchService = retrofit.create(VideoSearchService.class);
        }
        return videoSearchService;

    }




    public interface VideoService {

        @GET("videos?id=7lCDEYXw3mM&fields=items(id,snippet(channelId,title,categoryId,channelTitle,thumbnails(medium(url))),statistics(viewCount))&part=snippet,statistics&key=" + key)
        Call<VideosList> getVideos();

    }

    public interface VideoSearchService {

        @GET("search?q=car&type=video&fields=items(id(videoId),snippet(channelId,title,channelTitle,thumbnails(medium(url))))&maxResults=20&part=snippet&key=" + key)
        Call<SearchVideosList> getSearchVideos();

    }


}
