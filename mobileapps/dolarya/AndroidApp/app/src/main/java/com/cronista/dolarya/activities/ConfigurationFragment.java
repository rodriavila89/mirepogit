package com.cronista.dolarya.activities;

/**
 * Created by Ramiro E. Rinaldi on 02-Oct-15.
 */
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.cronista.dolarya.AnalyticsApplication;
import com.cronista.dolarya.R;
import com.cronista.dolarya.models.Configuration;

public class ConfigurationFragment extends Fragment {
    private String SCREEN_NAME = "Configuracion";
    private String TAG = "ConfigurationFragment";
    private Tracker _tracker;
    private Configuration _configuration = null;
    private Switch _swOpenClose;
    private Switch _swHourly;
    public ConfigurationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        _tracker = application.getDefaultTracker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configuration, container, false);
        RelativeLayout rates = (RelativeLayout) view.findViewById(R.id.layout_rates);
        RelativeLayout converter = (RelativeLayout) view.findViewById(R.id.layout_converter);
        rates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(MainActivity.class);
            }
        });
        converter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(CurrencyConverterActivity.class);
            }
        });

        _configuration = Configuration.Get(getActivity().getApplicationContext());
        _swOpenClose = (Switch) view.findViewById(R.id.switch_apertura_cierre);
        _swHourly = (Switch) view.findViewById(R.id.switch_cada_hora);
        _swOpenClose.setChecked(_configuration.getReceiveOpenClose());
        _swHourly.setChecked(_configuration.getReceiveHourly());
        _swOpenClose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                _tracker.send(new HitBuilders.EventBuilder().setCategory("Action")
                        .setAction((isChecked ? "Suscripcion" : "Desuscripcion") + " Apertura y cierre del mercado")
                        .build());
                _configuration.setReceiveOpenClose(isChecked);
                Configuration.Save(_configuration, getActivity().getApplicationContext());
                SplashActivity.subscribe(getActivity().getApplicationContext());
            }
        });
        _swHourly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                _tracker.send(new HitBuilders.EventBuilder().setCategory("Action")
                        .setAction((isChecked ? "Suscripcion" : "Desuscripcion") + " Cada una hora")
                        .build());
                _configuration.setReceiveHourly(isChecked);
                Configuration.Save(_configuration, getActivity().getApplicationContext());
                SplashActivity.subscribe(getActivity().getApplicationContext());
            }
        });
        TextView tvDeviceId = (TextView) view.findViewById(R.id.device_id);
        tvDeviceId.setText(_configuration.getDeviceId());
        TextView tvRegistrationId = (TextView) view.findViewById(R.id.registration_id);
        tvRegistrationId.setText(_configuration.getRegistrationId());
        return view;
    }

    private void changeActivity(Class activityClass) {
        ShowHideFragment(getFragmentManager(), false);
        Intent intent = new Intent(getActivity(), activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        getActivity().startActivity(intent);
    }

    public static void ShowHideFragment(FragmentManager fm) {
        ShowHideFragment(fm, true);
    }

    public static void ShowHideFragment(FragmentManager fm, boolean animated) {
        Fragment fragment = fm.findFragmentById(R.id.configuration_fragment);
        FragmentTransaction ft = fm.beginTransaction();
        if (animated) {
            ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        }
        ft.hide(fragment);
        if (fragment.isHidden()) {
            ft.show(fragment);
            ft.addToBackStack(null);
            Log.d("hidden", "Show");
        } else {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.hide(fragment);
            Log.d("Shown", "Hide");
        }
        ft.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "Setting screen name: " + SCREEN_NAME);
        _tracker.setScreenName(SCREEN_NAME);
        _tracker.send(new HitBuilders.ScreenViewBuilder().build());
        _configuration = Configuration.Get(getActivity().getApplicationContext());
        _swOpenClose.setChecked(_configuration.getReceiveOpenClose());
        _swHourly.setChecked(_configuration.getReceiveHourly());
    }
}
