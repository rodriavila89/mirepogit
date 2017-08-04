package com.cronista.dolarya.activities;

/**
 * Created by Ramiro E. Rinaldi on 02-Oct-15.
 */
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.cronista.dolarya.adapters.CurrencyConverterAdapter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.cronista.dolarya.AnalyticsApplication;
import com.cronista.dolarya.R;
import com.cronista.dolarya.helpers.CustomTextView;
import com.cronista.dolarya.models.CurrencyRate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CurrencyConverterActivity extends AppCompatActivity {
    private String SCREEN_NAME = "Conversor";
    private String TAG = "CurrencyConverterActivity";
    private Tracker _tracker;
    private List<CurrencyRate> _currencyList;
    private ListView _currencyConverterList;
    private CurrencyConverterAdapter _currencyConverterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_converter);
        ConfigurationFragment.ShowHideFragment(getFragmentManager(), false);

        _currencyList = CurrencyRate.GetLatestUpdate(getApplicationContext());
        CurrencyRate ars = new CurrencyRate();
        ars.setNombre("AR$");
        ars.setCurrencyId(0);
        ars.setCompra(1f);
        ars.setVenta(1f);
        _currencyList.add(ars);

        Collections.sort(_currencyList, new Comparator<CurrencyRate>() {
            @Override
            public int compare(CurrencyRate p1, CurrencyRate p2) {
                return p1.getCurrencyId() - p2.getCurrencyId(); // Ascending
            }

        });

        _currencyConverterList = (ListView) findViewById(R.id.currency_converter_list);
        _currencyConverterAdapter = new CurrencyConverterAdapter(getApplicationContext(), _currencyList);
        _currencyConverterList.setAdapter(_currencyConverterAdapter);
        final EditText amountToChangeEditText = (EditText) findViewById(R.id.converter_amount_to_change);
        amountToChangeEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String amount = amountToChangeEditText.getText().toString();
                _currencyConverterAdapter.setAmountToChange((amount == null || amount.isEmpty()) ? 0f : Float.parseFloat(amount));
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        configureSpinner();
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        _tracker = application.getDefaultTracker();
    }

    private void configureSpinner() {
        ArrayAdapter<CurrencyRate> spinnerAdapter = new ArrayAdapter<CurrencyRate>(getApplicationContext(), R.layout.spinner_item, _currencyList);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                _currencyConverterAdapter.setOptionSelected((CurrencyRate) parentView.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
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
        super.onResume();
    }
}
