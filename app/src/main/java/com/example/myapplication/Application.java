package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


import com.example.myapplication.data.repository.NewsRepository;
import com.example.myapplication.data.repository.NewsRepositoryImpl;
import com.google.firebase.messaging.FirebaseMessaging;

public class Application extends android.app.Application {

    private static NewsRepository newsRepository;


    public static NewsRepository getRepository(){
        return newsRepository;
    }

    private static String location;

    public static void setLocation(String country){
        location = country;
    }

    public static String getLocation(){
        return location;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseMessaging.getInstance().subscribeToTopic("test");
        createNotificationChannel();
        newsRepository = new NewsRepositoryImpl();
    }
}
