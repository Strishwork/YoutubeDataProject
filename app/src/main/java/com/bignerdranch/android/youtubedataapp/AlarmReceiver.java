package com.bignerdranch.android.youtubedataapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String CUSTOM_INTENT = "com.test.intent.action.ALARM";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            AlarmReceiver.setAlarm(context, true);
        } else{
            PollService.enqueueWork(context, intent);
        }
    }
    public static void cancelAlarm(Context context) {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarm.cancel(getPendingIntent(context));
    }
    public static void setAlarm(Context context, boolean force) {
        cancelAlarm(context);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // EVERY X MINUTES
        long delay = (1000 * 60 * 15);
        long when = System.currentTimeMillis();
        if (!force) {
            when += delay;
        }

        /* fire the broadcast */
        alarm.set(AlarmManager.RTC_WAKEUP, when, getPendingIntent(context));
    }
    private static PendingIntent getPendingIntent(Context context) {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.setAction(CUSTOM_INTENT);

        return PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

}
