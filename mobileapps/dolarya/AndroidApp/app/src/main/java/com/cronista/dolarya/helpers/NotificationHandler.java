package com.cronista.dolarya.helpers;
/**
 * Created by Ramiro E. Rinaldi on 19-Aug-15.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.cronista.dolarya.models.Configuration;
import com.microsoft.windowsazure.notifications.NotificationsHandler;
import com.cronista.dolarya.R;
import com.cronista.dolarya.activities.SplashActivity;

public class NotificationHandler extends NotificationsHandler {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager _notificationManager;
    Context _context;

    @Override
    public void onReceive(Context context, Bundle bundle) {
        _context = context;
        String nhMessage = bundle.getString("message");
        String nhCategory = bundle.getString("category");
        Configuration configuration = Configuration.Get(context);
        boolean showNotification = false;
        if (configuration.getReceiveHourly() && nhCategory.equalsIgnoreCase("Hourly"))
            showNotification = true;
        if (configuration.getReceiveOpenClose() && nhCategory.equalsIgnoreCase("OpenClose"))
            showNotification = true;
        if (showNotification)
            sendNotification(nhMessage);
    }

    private void sendNotification(String msg) {
        _notificationManager = (NotificationManager)
                _context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(_context, 0,
                new Intent(_context, SplashActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(_context)
                        .setSmallIcon(R.mipmap.launcher)
                        .setContentTitle("El Cronista - D\u00F3larYa")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setLights(Color.RED, 3000, 3000)
                        .setGroup("Cronista")
                        .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);
        _notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
