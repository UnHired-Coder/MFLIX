package com.telitel.tiwari.mflix;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import static android.support.v4.view.ViewCompat.animate;

public class splash_screen extends AppCompatActivity {


    AnimatedVectorDrawable avd1;
    AnimatedVectorDrawableCompat avd2;
    public ImageView splash;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //Screen Orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        splash= findViewById(R.id.splash_logo);
        animate(splash);


    }



    public void animate(ImageView view) {


        ImageView v= (ImageView) view;
        Drawable d = v.getDrawable();
        if(d instanceof AnimatedVectorDrawableCompat){

            avd2= (AnimatedVectorDrawableCompat) d;
            avd2.start();


        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if ( d instanceof AnimatedVectorDrawable){
                avd1= (AnimatedVectorDrawable) d;
                avd1.start();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    avd1.registerAnimationCallback(new Animatable2.AnimationCallback() {
                        @Override
                        public void onAnimationEnd(Drawable drawable) {
                            avd1.stop();
                            Intent intent = new Intent(splash_screen.this,MainActivity.class);
                            startActivity(intent);
                            splash_screen.this.finish();

                        }
                    });
                }
            }
        }
    }




}
