package com.cronista.dolarya.activities;

/**
 * Created by Ramiro E. Rinaldi on 02-Oct-15.
 */
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.cronista.dolarya.R;
import com.cronista.dolarya.adapters.WidgetService;

public class BoardWidget extends AppWidgetProvider {
    public static final String EXTRA_ITEM = "com.spiritconsultingllc.dolarya.BoardWidget.EXTRA_ITEM";
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews widget = new RemoteViews(context.getPackageName(), R.layout.board_widget);
            widget.setRemoteAdapter(R.id.board_list, intent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], widget);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}

