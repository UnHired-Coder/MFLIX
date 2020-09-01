package com.telitel.tiwari.mflix.Database;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telitel.tiwari.mflix.Models.SongModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class StorageUtil {


    public static StorageUtil mInstance;
    public static final String STORAGE = " com.telitel.tiwari.mflix.STORAGE";
    private static SharedPreferences preferences;
    private ArrayList<SongModel> cachedSongs;
    private int cachedPosition;
    private Context mContext;

    public static synchronized StorageUtil getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new StorageUtil(context);
        }
        return mInstance;
    }

    private StorageUtil(Context context) {
        cachedSongs = null;
        cachedPosition = -1;
        mContext = context;
    }

    public void storeAudio(ArrayList<SongModel> arrayArrayList) {
        preferences = mContext.getSharedPreferences(STORAGE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayArrayList);
        editor.putString("audioArrayArrayList", json);
        editor.apply();
        cachedSongs = arrayArrayList;
    }

    public ArrayList<SongModel> loadAudio() {

        if (cachedSongs != null)
            return cachedSongs;

        preferences = mContext.getSharedPreferences(STORAGE, android.content.Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("audioArrayArrayList", null);
        Type type = new TypeToken<ArrayList<SongModel>>() {
        }.getType();
        cachedSongs = gson.fromJson(json, type);
        return cachedSongs;
    }

    public void storeAudioIndex(int index) {
        preferences = mContext.getSharedPreferences(STORAGE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("audioIndex", index);
        editor.apply();
        cachedPosition = index;
    }

    public int loadAudioIndex() {
        if (cachedPosition != -1)
            return cachedPosition;
        preferences = mContext.getSharedPreferences(STORAGE, android.content.Context.MODE_PRIVATE);
        return preferences.getInt("audioIndex", -1);//return -1 if no data found
    }

    public void storeAudioPosition(int index) {
        preferences = mContext.getSharedPreferences(STORAGE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("audioPosition", index);
        editor.apply();
    }

    public void clearCachedAudioPlaylist() {
        preferences = mContext.getSharedPreferences(STORAGE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        editor.commit();
    }

    public static  void registerPres(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences preferences = context.getSharedPreferences(STORAGE, android.content.Context.MODE_PRIVATE);
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }
    public static  void unRegisterPres(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences preferences = context.getSharedPreferences(STORAGE, android.content.Context.MODE_PRIVATE);
        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
