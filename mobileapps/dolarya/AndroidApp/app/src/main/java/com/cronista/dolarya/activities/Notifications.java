package com.cronista.dolarya.activities;

/**
 * Created by Ramiro E. Rinaldi on 02-Oct-15.
 */
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.cronista.dolarya.helpers.NotificationHandler;
import com.cronista.dolarya.models.Configuration;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.microsoft.windowsazure.messaging.NotificationHub;
import com.microsoft.windowsazure.notifications.NotificationsManager;


public class Notifications {
    private GoogleCloudMessaging _gcm;
    private NotificationHub _hub;
    private Context _context;
    private String _senderId;

    public Notifications(Context context, String _senderId) {
        this._context = context;
        this._senderId = _senderId;
        NotificationsManager.handleNotifications(context, _senderId, NotificationHandler.class);

        _gcm = GoogleCloudMessaging.getInstance(context);
        _hub = new NotificationHub("cronista-dolaryahub", "Endpoint=sb://cronista-dolaryahub-ns.servicebus.windows.net/;SharedAccessKeyName=DefaultListenSharedAccessSignature;SharedAccessKey=53GF9Q2w8tjdrHr1gpih+LaLNqchcFCsFiLUldL2Dw0=", context);
    }

    public void subscribeToCategories(final Set<String> categories) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                int count = 0;
                int maxTries = 5;
                while(true) {
                    try {
                        Log.i("Notifications", count + ": subscribeToCategories - doInBackground...");
                        String regId = _gcm.register(_senderId);
                        categories.add("registrationId:" + regId.substring(0, 20));
                        _hub.register(regId, categories.toArray(new String[categories.size()]));
                        Configuration config = Configuration.Get(_context);
                        config.setRegistrationId(regId);
                        Configuration.Save(config, _context);
                        break;
                    } catch (Exception e) {
                        Log.e("Notifications", "Failed to register - " + e.getMessage());
                        if (++count == maxTries) return e;
                    }
                }
                Log.i("Notifications", count +  ": Success");
                return null;
            }

            protected void onPostExecute(Object result) {
                String message = "Ninguna alerta configurada";
                final Set<String> newCategories = new HashSet<String>();
                if(categories.contains("Hourly"))
                    newCategories.add("Hourly");
                if(categories.contains("OpenClose"))
                    newCategories.add("OpenClose");
                if(newCategories.size() > 0)
                {
                    message = "Alertas configuradas: " + newCategories.toString().replace("Hourly", "Cada hora").replace("OpenClose", "Apertura y cierre");
                }
                Toast.makeText(_context, message, Toast.LENGTH_LONG).show();
                if(result != null) {
                    Toast.makeText(_context, result.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }
}