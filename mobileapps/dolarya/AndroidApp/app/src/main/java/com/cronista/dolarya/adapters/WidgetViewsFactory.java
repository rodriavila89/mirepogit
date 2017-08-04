package com.cronista.dolarya.adapters;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.cronista.dolarya.R;
import com.cronista.dolarya.activities.BoardWidget;
import com.cronista.dolarya.models.CurrencyRate;

import java.util.List;

/**
 * Created by Ramiro E. Rinaldi on 01-Oct-15.
 */
public class WidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context ctxt = null;
    private int appWidgetId;
    private List<CurrencyRate> currencyRateList = null;

    public WidgetViewsFactory(Context ctxt, Intent intent) {
        this.ctxt = ctxt;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        currencyRateList = CurrencyRate.GetLatestUpdate(ctxt);
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return (currencyRateList.size());
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(ctxt.getPackageName(), R.layout.widget_row);
        CurrencyRate rate = currencyRateList.get(position);
        row.setTextViewText(R.id.widget_row_currency_name, rate.getNombre());
        row.setTextViewText(R.id.widget_row_currency_rate, "$" + String.format("%.2f", rate.getCompra()) + " / $" + String.format("%.2f", rate.getVenta()));

        Bundle extras = new Bundle();
        extras.putInt(BoardWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        row.setOnClickFillInIntent(R.id.widget_row_currency_name, fillInIntent);

        return (row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return currencyRateList.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
