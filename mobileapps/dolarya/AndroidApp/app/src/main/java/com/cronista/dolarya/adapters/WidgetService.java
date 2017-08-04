package com.cronista.dolarya.adapters;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Ramiro E. Rinaldi on 01-Oct-15.
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new WidgetViewsFactory(this.getApplicationContext(), intent));
    }
}
