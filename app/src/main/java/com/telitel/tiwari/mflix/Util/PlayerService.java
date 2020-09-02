package com.telitel.tiwari.mflix.Util;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.MediaController;

import com.telitel.tiwari.mflix.Database.StorageUtil;
import com.telitel.tiwari.mflix.Models.SongModel;


import java.io.IOException;
import java.util.ArrayList;


public class PlayerService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,
        AudioManager.OnAudioFocusChangeListener {

    private final IBinder iBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    String TAG = "Player Service";

    public static final String ACTION_PLAY = "com.telitel.tiwari.mflix.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.telitel.tiwari.mflix.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.telitel.tiwari.mflix.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.telitel.tiwari.mflix.ACTION_NEXT";
    public static final String ACTION_STOP = "com.telitel.tiwari.mflix.ACTION_STOP";

    public final String Broadcast_PLAY_NEW_AUDIO = "com.telitel.tiwari.mflix.PlayNewAudio";
    public final String Broadcast_PAUSE_AUDIO = "com.telitel.tiwari.mflix.PauseAudio";
    public final String Broadcast_RESUME_AUDIO = "com.telitel.tiwari.mflix.ResumeAudio";
    public final String Broadcast_STOP_AUDIO = "com.telitel.tiwari.mflix.StopAudio";


    private MediaPlayer mediaPlayer;
    private SongModel currentSong;

    @Override
    public void onCreate() {

        IntentFilter filter;
        filter = new IntentFilter(Broadcast_PLAY_NEW_AUDIO);
        registerReceiver(playAudioReceiver, filter);
        filter = new IntentFilter(Broadcast_PAUSE_AUDIO);
        registerReceiver(pauseAudioReceiver, filter);
        filter = new IntentFilter(Broadcast_RESUME_AUDIO);
        registerReceiver(resumeAudioReceiver, filter);
        filter = new IntentFilter(Broadcast_STOP_AUDIO);
        registerReceiver(stopAudioReceiver, filter);

        super.onCreate();
    }

    ////////////////////////
    //  BROADCAST RECEIVERS
    ////////////////////////
    private BroadcastReceiver playAudioReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: " + "Play Audio");
            playNewMedia();
        }
    };


    private BroadcastReceiver pauseAudioReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: " + "Pause Audio");
            pauseMedia();
        }
    };

    private BroadcastReceiver resumeAudioReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: " + "Resume Audio");
            resumeMedia();
        }
    };

    private BroadcastReceiver stopAudioReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: " + "Stop Audio");Log.i("TAG", "onStart: "+"-------------------------------");
            stopMedia();
        }
    };
    ////////////////////////
    //  CONTROL RECEIVERS
    ////////////////////////

    public void stopMedia() {
        Log.i(TAG, "stopMedia: ");
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
        }
    }

    public void pauseMedia() {
        Log.i(TAG, "pauseMedia: ");
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void resumeMedia() {
        Log.i(TAG, "resumeMedia: ");
        if (mediaPlayer != null && (!mediaPlayer.isPlaying())) {
            mediaPlayer.start();
        }
    }

    public void playNewMedia() {
        Log.i(TAG, "playNewMedia: ");
        final StorageUtil storageUtil = StorageUtil.getInstance(this);
        int index = storageUtil.loadAudioIndex();
        ArrayList<SongModel> songsList = storageUtil.loadAudio();
        if (index != -1 && (index < songsList.size())) {
            currentSong = songsList.get(index);
        } else {
            Log.i(TAG, "Error " + "Stop Audio Reason Invalid Index " + index);
            stopSelf();
            return;
        }
        pauseMedia();

        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (currentSong == null)
            return;
        try {
            mediaPlayer.setDataSource(currentSong.getSongPath());
        } catch (IOException e) {
            e.printStackTrace();
            stopMedia();
        }
        try {
            mediaPlayer.prepareAsync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void seekTo(int sec){
        mediaPlayer.seekTo(sec);
    }

    public MediaPlayer getPlayerInstance() {
        Log.i(TAG, "getPlayerInstance: " + mediaPlayer);
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        return mediaPlayer;
    }


    public void registerReceiver() {
        IntentFilter filter;
        filter = new IntentFilter(Broadcast_PLAY_NEW_AUDIO);
        registerReceiver(playAudioReceiver, filter);
        filter = new IntentFilter(Broadcast_PAUSE_AUDIO);
        registerReceiver(pauseAudioReceiver, filter);
        filter = new IntentFilter(Broadcast_RESUME_AUDIO);
        registerReceiver(resumeAudioReceiver, filter);
        filter = new IntentFilter(Broadcast_STOP_AUDIO);
        registerReceiver(stopAudioReceiver, filter);
    }

    public void unRegisterReceiver() {
        unregisterReceiver(playAudioReceiver);
        unregisterReceiver(pauseAudioReceiver);
        unregisterReceiver(resumeAudioReceiver);
        unregisterReceiver(stopAudioReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onAudioFocusChange(int i) {
        Log.i(TAG, "onAudioFocusChange: " + i);

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        Log.i(TAG, "onBufferingUpdate: " + i);

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.i(TAG, "onCompletion: " + mediaPlayer);
        Intent broadcastIntent = new Intent(ACTION_NEXT);
        if(getApplicationContext()!=null)
            getApplicationContext().sendBroadcast(broadcastIntent);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Log.i(TAG, "onError: " + i);

        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        Log.i(TAG, "onInfo: " + i);

        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.i(TAG, "onPrepared: " + mediaPlayer);
        mediaPlayer.start();
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        Log.i(TAG, "onSeekComplete: " + mediaPlayer);

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "onRebind: ");
        super.onRebind(intent);
        playNewMedia();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        stopSelf();
        super.onDestroy();
    }


}
