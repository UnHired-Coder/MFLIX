package com.telitel.tiwari.mflix;

import android.app.PictureInPictureParams;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.audiofx.DynamicsProcessing;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Config;
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class video_player_intent extends YouTubeBaseActivity {

  private   VideoView videoView;
  private Button pipmode;
    private YouTubePlayerView youTubeView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private static final String key = "AIzaSyCKAP7Y-RMjj3C4quc1pknoMjmYaw8lR5k";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player_intent);

        Intent myIntent = getIntent();
        final String videoId = myIntent.getStringExtra("videoId");
        pipmode = findViewById(R.id.pipmode_button);

        youTubeView = (YouTubePlayerView) findViewById(R.id.player_video_view);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
             //   youTubePlayer.loadVideo("https://www.youtube.com/watch?v=oCBDl58lBoU");
                if (!b) {
                    youTubePlayer.cueVideo(videoId);

                    youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                        @Override
                        public void onLoading() {

                        }

                        @Override
                        public void onLoaded(String s) {
                            Log.i("Entered","LOADED  ");
                            youTubePlayer.play();
                        }

                        @Override
                        public void onAdStarted() {

                        }

                        @Override
                        public void onVideoStarted() {

                        }

                        @Override
                        public void onVideoEnded() {

                        }

                        @Override
                        public void onError(YouTubePlayer.ErrorReason errorReason) {

                        }
                    });

                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                        @Override
                        public void onPlaying() {
                            Log.i("Entered","PIP MODE PLAY");
                        }

                        @Override
                        public void onPaused() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                if(isInPictureInPictureMode()) {
                                    youTubePlayer.play();
                                    Log.i("Entered","PIP MODE PAUSE");
                                }
                            }
                        }

                        @Override
                        public void onStopped() {
                            youTubePlayer.play();
                            Log.i("Entered","PIP MODE STOP");
                        }

                        @Override
                        public void onBuffering(boolean b) {

                        }

                        @Override
                        public void onSeekTo(int i) {

                        }
                    });

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };


        youTubeView.initialize(key,onInitializedListener);

        //        videoView= findViewById(R.id.player_video_view);
//        videoView.setVideoURI(Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"));
//        videoView.start();

      pipmode.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (android.os.Build.VERSION.SDK_INT >= 26) {
                  //Trigger PiP mode
                  try {
                      Rational rational = new Rational(youTubeView.getWidth(), youTubeView.getHeight());

                      PictureInPictureParams mParams =
                              new PictureInPictureParams.Builder()
                                      .setAspectRatio(rational)
                                      .build();

                      enterPictureInPictureMode(mParams);
                      youTubeView.initialize(key,onInitializedListener);
                  } catch (IllegalStateException e) {
                      e.printStackTrace();
                  }
              } else {
                  Toast.makeText(getApplicationContext(), "API 26 needed to perform PiP", Toast.LENGTH_SHORT).show();
              }
          }
      });

    }

    @Override
    public void onUserLeaveHint () {

        if (android.os.Build.VERSION.SDK_INT >= 26) {
            //Trigger PiP mode
            try {
                Rational rational = new Rational(youTubeView.getWidth()/10, youTubeView.getHeight()/10);

                PictureInPictureParams mParams =
                        new PictureInPictureParams.Builder()
                                .setAspectRatio(rational)
                                .build();

                enterPictureInPictureMode(mParams);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "API 26 needed to perform PiP", Toast.LENGTH_SHORT).show();
        }

    }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//
//            Rational aspectRatio = new Rational(
//                    youTubeView.getWidth(), youTubeView.getHeight());
//            PictureInPictureParams params = null;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                params = new PictureInPictureParams.Builder()
//                        .setAspectRatio(aspectRatio)
//                        .build();
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                enterPictureInPictureMode(params);
//            }
//        }


    @Override
    public void onPictureInPictureModeChanged (boolean isInPictureInPictureMode, Configuration newConfig) {
        if (isInPictureInPictureMode) {
            // Hide the full-screen UI (controls, etc.) while in picture-in-picture mode.
        } else {
            // Restore the full-screen UI.
        }
    }
}