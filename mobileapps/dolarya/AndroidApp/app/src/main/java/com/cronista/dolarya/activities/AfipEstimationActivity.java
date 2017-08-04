package com.cronista.dolarya.activities;

/**
 * Created by Ramiro E. Rinaldi on 02-Oct-15.
 */
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.cronista.dolarya.AnalyticsApplication;
import com.cronista.dolarya.R;
import com.cronista.dolarya.helpers.CustomTextView;
import com.cronista.dolarya.models.CurrencyRate;

import java.util.List;

public class AfipEstimationActivity extends AppCompatActivity {
    private String SCREEN_NAME = "EstimacionAFIP";
    private String TAG = "AfipEstimationActivity";
    private Tracker _tracker;
    private List<CurrencyRate> _currencyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afip_estimation);
        ConfigurationFragment.ShowHideFragment(getFragmentManager(), false);
        _currencyList = CurrencyRate.GetLatestUpdate(getApplicationContext());

        final EditText amountToChangeEditText = (EditText) findViewById(R.id.converter_amount_to_change);
        amountToChangeEditText.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed()) {
                                String amount = amountToChangeEditText.getText().toString();
                                UpdateEstimation((amount == null || amount.isEmpty()) ? 0f : Float.parseFloat(amount));
                            }
                        }
                        return false;
                    }
                });
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _tracker = application.getDefaultTracker();
    }

    private void UpdateEstimation(Float amountToChange) {
        if (amountToChange == null) {
            amountToChange = 0f;
        }
        CurrencyRate dolarRate = null;
        for (CurrencyRate rate : _currencyList) {
            if(rate.getCurrencyId() == 1)
            {
                dolarRate = rate;
                break;
            }
        }
        if(dolarRate != null) {
            Float pesos = 0f;
            Float dolares = 0f;
            Float percepcion = 0f;
            CustomTextView ctvPesos = (CustomTextView) findViewById(R.id.estimacion_pesos);
            CustomTextView ctvDolares = (CustomTextView) findViewById(R.id.estimacion_dolares);
            CustomTextView ctvPercepcion = (CustomTextView) findViewById(R.id.estimacion_percepcion);
            if(amountToChange >= 11176)
            {
                pesos = amountToChange * 0.2f;
                percepcion = pesos * 0.2f;
                dolares = pesos / dolarRate.getVenta();
                if(dolares > 2000)
                {
                    dolares = 2000f;
                    pesos = dolares * dolarRate.getVenta();
                    percepcion = pesos * 0.2f;
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Su nivel de ingresos no alcanza al mínimo exigido", Toast.LENGTH_LONG).show();
            }
            ctvPesos.setText(String.format("%.0f", pesos));
            ctvDolares.setText(String.format("%.0f", dolares));
            ctvPercepcion.setText(String.format("%.0f", percepcion));
        }
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch(keycode) {
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
        if (fm.getBackStackEntryCount() > 0 ){
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
        super.onResume();
    }
}
