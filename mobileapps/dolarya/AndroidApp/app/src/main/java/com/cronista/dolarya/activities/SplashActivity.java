package com.cronista.dolarya.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.cronista.dolarya.helpers.APIHandler;
import com.cronista.dolarya.R;
import com.cronista.dolarya.models.Configuration;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Ramiro E. Rinaldi on 02-Oct-15.
 */
public class SplashActivity extends AppCompatActivity {
    private static final int STOPSPLASH = 0;
    private static final long SPLASHTIME = 3000;
    public static APIHandler _apiHandler;
    private ImageView _splash;
    public static Notifications _notifications;
    private Handler splashHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOPSPLASH:
                    RedirectToMain();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    public static final String SENDER_ID = "515278941085";
    private void RedirectToMain()
    {
        splashHandler.removeMessages(0);
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _apiHandler = new APIHandler(this.getApplicationContext());
        _apiHandler.GetLastWeekCurrenciesAsync();
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        _splash = (ImageView) findViewById(R.id.splashimage);
        _splash.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RedirectToMain();
            }
        });
        Message msg = new Message();
        msg.what = STOPSPLASH;
        _notifications = new Notifications(this, SENDER_ID);
        subscribe(getApplicationContext());
        splashHandler.sendMessageDelayed(msg, SPLASHTIME);
    }

    public static void subscribe(Context context) {
        Log.i("SplashActivity", "subscribe");
        final Set<String> categories = new HashSet<String>();
        Configuration config = Configuration.Get(context);
        if(config.getDeviceId() == null || config.getDeviceId().isEmpty())
        {
            config.setDeviceId(UUID.randomUUID().toString());
            Configuration.Save(config, context);
        }
        categories.add("deviceId:" + config.getDeviceId());
        if (config.getReceiveHourly())
            categories.add("Hourly");
        if (config.getReceiveOpenClose())
            categories.add("OpenClose");
        Log.i("SplashActivity", "Categories: " + categories.toString());
        _notifications.subscribeToCategories(categories);
    }
}
