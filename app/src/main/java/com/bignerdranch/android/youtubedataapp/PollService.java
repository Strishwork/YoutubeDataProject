package com.bignerdranch.android.youtubedataapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bignerdranch.android.youtubedataapp.model.DataLab;
import com.bignerdranch.android.youtubedataapp.video.VideoItem;

import java.util.List;

public class PollService extends JobIntentService {

    /* Give the Job a Unique Id */
    private static final int JOB_ID = 1000;

    public static void enqueueWork(Context ctx, Intent intent) {
        enqueueWork(ctx, PollService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        /* your code here */
        /* reset the alarm */
        Log.i("DEBUG", "OnHandleIntent NetworkAvailableAndConnected " + isNetworkAvailableAndConnected());
        updateVideos();
        AlarmReceiver.setAlarm(getApplicationContext(),false);
        stopSelf();
    }

    private void updateVideos(){
        if (isNetworkAvailableAndConnected()) {
            new YouTubeAsync().updateVideos(getApplicationContext());
            checkVideos(getApplicationContext());
        }
    }

    private void checkVideos(Context context){
        DataLab dataLab = DataLab.get(context);
        List<VideoItem> items = dataLab.getVideos();
        for (VideoItem item : items){
            if(item.getGoal() <= 0) {
                continue;
            }
            if(item.getGoal() <= item.getLikesCount()){
                showNotification("One of your videos has reached its goal!", item.getTitle(), (int)item.getId());
            }
        }
    }

    private void showNotification(String title, String message, int id){
        NotificationHelper mNotificationHelper = new NotificationHelper(getApplicationContext());
        NotificationCompat.Builder nb = mNotificationHelper.getChannelNotification(title, message);
        nb.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        mNotificationHelper.getManager().notify(id, nb.build());
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

}