package com.example.userevents;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BroadcastManager extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,intent.getStringExtra("eventname"))
                    .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
                    .setContentTitle(intent.getStringExtra("eventname"))
                    .setContentText("Tommorrow is "+ intent.getStringExtra("eventname")+" event")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(100,builder.build());

    }



    }

