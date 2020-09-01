package com.telitel.tiwari.mflix.Application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class ApplicationInit extends Application {

    public static final String CHANNEL_1_ID="channel1";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }
    private void createNotificationChannel(){
        NotificationChannel channel1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel1 = new NotificationChannel(CHANNEL_1_ID,"Media playing",
                    NotificationManager.IMPORTANCE_LOW);
            channel1.setDescription("Playing media notification");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}
