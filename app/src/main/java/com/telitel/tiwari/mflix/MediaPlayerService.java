package com.telitel.tiwari.mflix;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.MediaSessionManager;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;

import static com.telitel.tiwari.mflix.App_start.CHANNEL_1_ID;
import static com.telitel.tiwari.mflix.MainActivity.Broadcast_PLAY_NEW_AUDIO;
import static com.telitel.tiwari.mflix.MainActivity.currentPos;

public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,
        AudioManager.OnAudioFocusChangeListener {




    private boolean playing=false;

    public MediaPlayer mediaPlayer;


    //Used to pause/resume MediaPlayer
    private int resumePosition;


    // Binder given to clients
    private final IBinder iBinder = new LocalBinder();


    private AudioManager audioManager;


    //Handle incoming phone calls
    private boolean ongoingCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;



    //List of available Audio files
    private ArrayList<song_template> audioList;
    private int audioIndex = -1;
    private song_template activeAudio; //an object of the currently playing audio
    private application_running_helper Helper;


    public class LocalBinder extends Binder {
        public MediaPlayerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MediaPlayerService.this;
        }
    }

  private NotificationManagerCompat notificationManager;




    public static final String ACTION_PLAY = "com.telitel.tiwari.mflix.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.telitel.tiwari.mflix.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.telitel.tiwari.mflix.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.telitel.tiwari.mflix.ACTION_NEXT";
    public static final String ACTION_STOP = "com.telitel.tiwari.mflix.ACTION_STOP";

    //MediaSession
    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;


    //AudioPlayer notification ID
    private static final int NOTIFICATION_ID = 101;





    @Override
    public void onCreate() {
        super.onCreate();
        // Perform one-time setup procedures

        // Manage incoming phone calls during playback.
        // Pause MediaPlayer on incoming call,
        // Resume on hangup.
        callStateListener();
        //ACTION_AUDIO_BECOMING_NOISY -- change in audio outputs -- BroadcastReceiver
        registerBecomingNoisyReceiver();
        //Listen for new Audio to play -- BroadcastReceiver
        register_playNewAudio();
        register_pauseAudio();
        notificationManager = NotificationManagerCompat.from(this);

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            stopMedia();
            mediaPlayer.release();
        }
        removeAudioFocus();
        //Disable the PhoneStateListener
        if (phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }

       // removeNotification();

        //unregister BroadcastReceivers
        unregisterReceiver(becomingNoisyReceiver);
        unregisterReceiver(playNewAudio);
        unregisterReceiver(pauseAudio);

        //clear cached playlist
       // new StorageUtil(getApplicationContext()).clearCachedAudioPlaylist();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            //Load data from SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            audioList = storage.loadAudio();
            audioIndex = storage.loadAudioIndex();


            if (audioIndex != -1 && audioIndex < audioList.size()) {
                //index is in a valid range
                activeAudio = audioList.get(audioIndex);
            } else {
                stopSelf();
            }
        } catch (NullPointerException e) {
            stopSelf();
        }

        //Request audio focus
        if (requestAudioFocus() == false) {
            //Could not gain focus
            stopSelf();
        }

        if (mediaSessionManager == null) {
            try {
                initMediaSession();
               // initMediaPlayer();
            } catch (RemoteException e) {
                e.printStackTrace();
                stopSelf();
            }
           // buildNotification(PlaybackStatus.PAUSED);
        }

        //Handle Intent action from MediaSession.TransportControls
        handleIncomingActions(intent);
        return START_NOT_STICKY;
    }



    private void buildNotification(PlaybackStatus playbackStatus) {




        int notificationAction = android.R.drawable.ic_media_pause;//needs to be initialized
        PendingIntent play_pauseAction = null;


        //Build a new notification according to the current state of the MediaPlayer
        if (playbackStatus == PlaybackStatus.PLAYING) {
            notificationAction = android.R.drawable.ic_media_pause;
            //create the pause action
            play_pauseAction = playbackAction(1);
        } else if (playbackStatus == PlaybackStatus.PAUSED) {
            notificationAction = android.R.drawable.ic_media_play;
            //create the play action
            play_pauseAction = playbackAction(0);
        }



        Intent startActivityOnClick = new Intent(this,MainActivity.class);
        PendingIntent pendingIntentActivity = PendingIntent.getActivity(this,0,startActivityOnClick,0);


        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap largeIcon = BitmapFactory.decodeFile(activeAudio.getSongArtPath(),bmOptions);


        Log.i("Notification","here------------------");
        Notification notificationBuilder =  new NotificationCompat.Builder(this,"channel1")
                .setShowWhen(false)
                // Set the Notification style
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        // Attach our MediaSession token
                        .setMediaSession(mediaSession.getSessionToken())
                        // Show our playback controls in the compact notification view.
                        .setShowActionsInCompactView(0, 1, 2,3))
           //     .setStyle(new NotificationCompat.BigPictureStyle()
           //     .bigPicture(largeIcon)
           //     .bigLargeIcon(null))
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.notification_icon)
                // Set Notification content information
                .setContentText(activeAudio.getSongArtist())
                .setContentTitle(activeAudio.getSongTitle())
                .setContentInfo(activeAudio.getSongAlbum())
                .setContentIntent(pendingIntentActivity)
                // Add playback actions
                .addAction(android.R.drawable.ic_media_previous, "previous", playbackAction(3))
                .addAction(notificationAction, "pause", play_pauseAction)
                .addAction(android.R.drawable.ic_media_next, "next", playbackAction(2))
                .addAction(android.R.drawable.ic_menu_close_clear_cancel,"close",playbackAction(4)).build();



//       ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notificationBuilder.build());
//        Log.i("Notification","here------------------");

        startForeground(1,notificationBuilder);
        //        sendOnChannel();
    }







   private void removeNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
        notificationManager.cancelAll();
        Log.i("cancel","notification");

   }

































































    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        //Invoked indicating buffering status of
        //a media resource being streamed over the network.
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //Invoked when playback of a media source has completed. //Invoked when playback of a media source has completed.
           // stopMedia();
            //stop the service
          // stopSelf();
        mp.reset();
        playing=false;
        currentPos=0;
        mp.seekTo(0);
        StorageUtil storage = new StorageUtil(getApplicationContext());

        if(storage.loadAudioIndex()+1>=storage.loadAudio().size()){
            storage.storeAudioIndex(0);
        }
        else
        {
            storage.storeAudioIndex(storage.loadAudioIndex()+1);
        }
        Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
        getApplicationContext().sendBroadcast(broadcastIntent);
        currentPos=0;
        if (Helper.isAppRunning(MediaPlayerService.this, "com.telitel.tiwari.mflix")) {
            // App is running
            MainActivity.songChanged(storage.loadAudioIndex());
            MainActivity.mySongsRecyclerView.smoothScrollToPosition(storage.loadAudioIndex());
            updateMetaData();
        } else {
            // App is not running
            updateMetaData();

        }

    }

    //Handle errors
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //Invoked when there has been an error during an asynchronous operation
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + extra);
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        //Invoked to communicate some info.
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
         //Invoked when the media source is ready for playback.
        mediaPlayer.seekTo((int) currentPos);
        playMedia();
        playing=true;

    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        //Invoked indicating the completion of a seek operation.
    }

    @Override
    public void onAudioFocusChange(int focusState) {
        //Invoked when the audio focus of the system is updated.
        switch (focusState) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mediaPlayer == null) initMediaPlayer();
                else if (!playing)
                    if(!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                        playing=true;
                        mediaPlayer.setVolume(1.0f, 1.0f);
                    }
                     break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if(mediaPlayer!=null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        playing=false;
                    }
                }
                mediaPlayer = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if(!playing)
                if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }


    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //Focus gained
            return true;
        }
        //Could not gain focus
        return false;
    }



    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager.abandonAudioFocus(this);
    }

    private void playMedia() {
        if(!playing)
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            playing=true;
            Log.i("From Here 1","playing");
        }
    }

    private void stopMedia() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            playing=false;
        }
    }

    private void pauseMedia() {
        if(playing)
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playing=false;
            resumePosition = mediaPlayer.getCurrentPosition();
        }
    }

    private void resumeMedia() {
        if(!playing)
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(2000);
            mediaPlayer.start();
            playing=true;
        }
    }






    private void initMediaSession() throws RemoteException {
        if (mediaSessionManager != null) return; //mediaSessionManager exists


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaSessionManager = (MediaSessionManager) getSystemService(MediaSessionManager.class);
        }


        // Create a new MediaSession
        mediaSession = new MediaSessionCompat(getApplicationContext(), "AudioPlayer");
        //Get MediaSessions transport controls
        transportControls = mediaSession.getController().getTransportControls();
        //set MediaSession -> ready to receive media commands`
        mediaSession.setActive(true);
        //indicate that the MediaSession handles transport control commands
        // through its MediaSessionCompat.Callback.
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        //Set mediaSession's MetaData
        updateMetaData();

        // Attach Callback to receive MediaSession updates
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            // Implement callbacks
            @Override
            public void onPlay() {
                super.onPlay();
                resumeMedia();
                buildNotification(PlaybackStatus.PLAYING);

            }

            @Override
            public void onPause() {
                super.onPause();
                pauseMedia();
                buildNotification(PlaybackStatus.PAUSED);
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                skipToNext();
                updateMetaData();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                skipToPrevious();
                updateMetaData();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onStop() {
                super.onStop();
                removeNotification();
                //Stop the service
                stopSelf();
            }


            @Override
            public void onSeekTo(long position) {
                super.onSeekTo(position);
            }
        });
    }



    private void skipToNext() {
        playing=false;
        if (audioIndex == audioList.size() - 1) {
            //if last in playlist
            audioIndex = 0;
            activeAudio = audioList.get(audioIndex);
        } else {
            //get next in playlist
            activeAudio = audioList.get(++audioIndex);
        }

        //Update stored index
        new StorageUtil(getApplicationContext()).storeAudioIndex(audioIndex);

        stopMedia();
        //reset mediaPlayer
        mediaPlayer.reset();
        if (Helper.isAppRunning(MediaPlayerService.this, "com.telitel.tiwari.mflix"))
            {
                MainActivity.songChanged(audioIndex);
            }
            initMediaPlayer();
    }

    private void skipToPrevious() {

        playing=false;
        if (audioIndex == 0) {
            //if first in playlist
            //set index to the last of audioList
            audioIndex = audioList.size() - 1;
            activeAudio = audioList.get(audioIndex);
        } else {
            //get previous in playlist
            activeAudio = audioList.get(--audioIndex);
        }

        //Update stored index
        new StorageUtil(getApplicationContext()).storeAudioIndex(audioIndex);

        stopMedia();
        //reset mediaPlayer
        mediaPlayer.reset();
        if (Helper.isAppRunning(MediaPlayerService.this, "com.telitel.tiwari.mflix")) {

            MainActivity.songChanged(audioIndex);
        }
        initMediaPlayer();
    }








//    public void  sendOnChannel(){
//
//        Notification notification = new NotificationCompat.Builder(this,CHANNEL_1_ID)
//                .setSmallIcon(R.drawable.sample_avatar)
//                .setContentTitle("Hi")
//                .setContentText("This is the message")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .build();
//
//        notificationManager.notify(1,notification);
//
//     }



    private PendingIntent playbackAction(int actionNumber) {
        Intent playbackAction = new Intent(this, MediaPlayerService.class);
        switch (actionNumber) {
            case 0:
                // Play
                playbackAction.setAction(ACTION_PLAY);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 1:
                // Pause
                playbackAction.setAction(ACTION_PAUSE);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 2:
                // Next track
                playbackAction.setAction(ACTION_NEXT);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 3:
                // Previous track
                playbackAction.setAction(ACTION_PREVIOUS);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 4:
                playbackAction.setAction(ACTION_STOP);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            default:
                break;
        }
        return null;
    }


    private void handleIncomingActions(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
            {
                MainActivity.player_View_layout.findViewById(R.id.play_pause_button_expanded).performClick();
            }
            transportControls.play();
        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
            if (Helper.isAppRunning(MediaPlayerService.this, "com.telitel.tiwari.mflix")) {
                MainActivity.player_View_layout.findViewById(R.id.play_pause_button_expanded).performClick();
            }
            transportControls.pause();
        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
            currentPos=0;
            transportControls.skipToNext();
        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
            currentPos=0;
            transportControls.skipToPrevious();
        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
            transportControls.stop();
        }
    }












































    //Becoming noisy
    private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //pause audio on ACTION_AUDIO_BECOMING_NOISY
            //   Log.i("hi","noisy");
            pauseMedia();
            buildNotification(PlaybackStatus.PAUSED);
        }
    };

    private void registerBecomingNoisyReceiver() {
        //register after getting audio focus
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(becomingNoisyReceiver, intentFilter);
    }




    //Handle incoming phone calls
    private void callStateListener() {
        // Get the telephony manager
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //Starting listening for PhoneState changes
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    //if at least one call exists or the phone is ringing
                    //pause the MediaPlayer
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (mediaPlayer != null) {
                            pauseMedia();
                            ongoingCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        // Phone idle. Start playing.
                        if (mediaPlayer != null) {
                            if (ongoingCall) {
                                ongoingCall = false;
                                resumeMedia();
                            }
                        }
                        break;
                }
            }
        };
        // Register the listener with the telephony manager
        // Listen for changes to the device call state.
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);
    }


    private void updateMetaData() {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap albumArt = BitmapFactory.decodeFile(activeAudio.getSongArtPath(),bmOptions);

        //replace with medias albumArt
        // Update the current metadata
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, activeAudio.getSongArtist())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, activeAudio.getSongAlbum())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, activeAudio.getSongTitle())
                .build());

    }













    private void register_playNewAudio() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(Broadcast_PLAY_NEW_AUDIO);
        registerReceiver(playNewAudio, filter);
    }


    private BroadcastReceiver playNewAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //Get the new media index form SharedPreferences
            audioIndex = new StorageUtil(getApplicationContext()).loadAudioIndex();
            audioList = new StorageUtil(getApplicationContext()).loadAudio();
            if (audioIndex != -1 && audioIndex < audioList.size()) {
                //index is in a valid range
                activeAudio = audioList.get(audioIndex);
            } else {
                stopSelf();
            }

            //A PLAY_NEW_AUDIO action received
            //reset mediaPlayer to play the new Audio


            stopMedia();
            //mediaPlayer.reset();
            initMediaPlayer();
            updateMetaData();
            buildNotification(PlaybackStatus.PLAYING);

        }
    };


    private void register_pauseAudio() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(MainActivity.Broadcast_PAUSE_AUDIO);
        registerReceiver(pauseAudio, filter);
    }


    private BroadcastReceiver pauseAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //Get the new media index form SharedPreferences
            audioIndex = new StorageUtil(getApplicationContext()).loadAudioIndex();
            if (audioIndex != -1 && audioIndex < audioList.size()) {
                //index is in a valid range
                activeAudio = audioList.get(audioIndex);
            } else {
                stopSelf();
            }

            //A PLAY_NEW_AUDIO action received
            //reset mediaPlayer to play the new Audio
            pauseMedia();
            //stopMedia();
            buildNotification(PlaybackStatus.PAUSED);


        }
    };



    private void initMediaPlayer() {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();//new MediaPlayer instance

        //Set up MediaPlayer event listeners
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnInfoListener(this);
        //Reset so that the MediaPlayer is not pointing to another data source

        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            // Set the data source to the mediaFile location
            mediaPlayer.setDataSource(activeAudio.getSongPath());
        } catch (IOException e) {
            e.printStackTrace();
            stopSelf();
        }
        mediaPlayer.prepareAsync();
    }

}