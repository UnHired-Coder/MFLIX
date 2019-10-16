package com.telitel.tiwari.mflix;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */




public class searchPage extends Fragment {


    public class DownloadTask extends AsyncTask<String , Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            String result= "";
            URL url;
            HttpURLConnection urlConnection=null;
            try {


                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in =urlConnection.getInputStream();

                InputStreamReader reader= new InputStreamReader(in);

                int data = reader.read();

                while (data!= -1){

                    char current=(char) data;
                    result += current;
                    data= reader.read();
                }

                return result;

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }


    public searchPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_search_page, container, false);


        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        DownloadTask task =new DownloadTask();
        String result= null;
        try {
            result=task.execute("https://www.googleapis.com/youtube/v3/search?q=abhay%20maura&key=AIzaSyCKAP7Y-RMjj3C4quc1pknoMjmYaw8lR5k&part=snippet&maxResults=5").get();


        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        catch (ExecutionException e){
            e.printStackTrace();
        }


        try {
            JSONObject jsonObject= new JSONObject(result);
            String weatherInfo =jsonObject.getString("items");
            Log.i("DATA",weatherInfo);

//            JSONArray arr = new JSONArray(weatherInfo);
//            for(int i=0;i<arr.length();i++){
//
//                JSONObject jsonPart =arr.getJSONObject(i);
//                Log.i("----------",jsonPart.getString("main"));
//                Log.i("==========",jsonPart.getString("description"));
//            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}