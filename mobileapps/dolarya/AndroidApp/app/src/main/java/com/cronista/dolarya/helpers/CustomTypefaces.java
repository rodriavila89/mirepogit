package com.cronista.dolarya.helpers;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by Ramiro E. Rinaldi on 09-Sep-15.
 */
public enum CustomTypefaces {
    INSTANCE;
    private final HashMap<String, Typeface> map = new HashMap<String, Typeface>();
    public Typeface getTypeface(String file, Context context) {
        Typeface result = map.get(file);
        if (result == null) {
            result = Typeface.createFromAsset(context.getAssets(), file);
            map.put(file, result);
        }
        return result;
    }
}
