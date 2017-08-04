package com.cronista.dolarya.activities;

/**
 * Created by Ramiro E. Rinaldi on 02-Oct-15.
 */
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.cronista.dolarya.AnalyticsApplication;
import com.cronista.dolarya.adapters.CustomAdapter;
import com.cronista.dolarya.R;
import com.cronista.dolarya.helpers.CustomTextView;
import com.cronista.dolarya.models.CurrencyRate;


public class MainActivity extends AppCompatActivity {
    private String SCREEN_NAME = "Cotizaciones";
    private String TAG = "MainActivity";
    private Tracker _tracker;
    private CustomAdapter _customAdapter;
    public static CustomTextView _lastUpdateValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _lastUpdateValue = (CustomTextView) findViewById(R.id.last_update_value);
        ConfigurationFragment.ShowHideFragment(getFragmentManager(), false);
        _customAdapter = new CustomAdapter(this.getApplicationContext());
        ListView currencies = (ListView) findViewById(R.id.listView1);
        currencies.setAdapter(_customAdapter);
        _lastUpdateValue.setText(CurrencyRate.GetLatestUpdateTime(getApplicationContext()));
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _tracker = application.getDefaultTracker();
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch (keycode) {
            case KeyEvent.KEYCODE_MENU:
                if (e.getAction() == KeyEvent.ACTION_DOWN && e.getRepeatCount() == 1) {
                    ConfigurationFragment.ShowHideFragment(getFragmentManager());
                    return true;
                }
        }

        return super.onKeyDown(keycode, e);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            ConfigurationFragment.ShowHideFragment(getFragmentManager());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            ConfigurationFragment.ShowHideFragment(getFragmentManager());
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        Log.i(TAG, "Setting screen name: " + SCREEN_NAME);
        _tracker.setScreenName(SCREEN_NAME);
        _tracker.send(new HitBuilders.ScreenViewBuilder().build());
        SplashActivity._apiHandler.UpdateCurrenciesAsync(_customAdapter);
        super.onResume();
    }
}
