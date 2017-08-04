package com.cronista.dolarya.activities;

/**
 * Created by Ramiro E. Rinaldi on 02-Oct-15.
 */
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cronista.dolarya.R;

public class FooterFragment extends Fragment {
    public static FooterFragment newInstance() {
        FooterFragment fragment = new FooterFragment();
        return fragment;
    }

    public FooterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_footer, container, true);
    }


}
