package com.cronista.dolarya.adapters;

import android.content.Context;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.cronista.dolarya.R;
import com.cronista.dolarya.helpers.CustomTextView;
import com.cronista.dolarya.models.CurrencyRate;

import java.text.SimpleDateFormat;

/**
 * Created by Ramiro E. Rinaldi on 21-Sep-15.
 */
public class CustomMarkerView extends MarkerView {
    private CustomTextView tvContentRates;
    private CustomTextView tvContentDates;

    public CustomMarkerView (Context context, int layoutResource) {
        super(context, layoutResource);
        tvContentRates = (CustomTextView) findViewById(R.id.custom_marker_rates);
        tvContentDates = (CustomTextView) findViewById(R.id.custom_marker_date);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        CurrencyRate rate = (CurrencyRate)e.getData();
        tvContentRates.setText(rate.getCompra() + " - " + rate.getVenta());
        tvContentDates.setText(formatter.format(rate.getUltimaActualizacion()));
    }

    @Override
    public int getXOffset() {
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset() {
        return -getHeight();
    }
}