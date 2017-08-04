package com.cronista.dolarya.helpers;

import android.content.Context;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;

/**
 * Created by Ramiro E. Rinaldi on 08-Sep-15.
 */
public class MobileServiceHelper {
    private static MobileServiceHelper _instance;
    private MobileServiceClient _client;
    private MobileServiceHelper(Context context){
        try {
            _client = new MobileServiceClient("https://cronista-dolarya.azure-mobile.net/", "DlxPsJAXKPpqwUOIxSTrHijfiVxlXb32", context);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized MobileServiceHelper getInstance(Context context) {

        if (_instance == null) {
            _instance = new MobileServiceHelper(context.getApplicationContext());
        }
        return _instance;
    }

    public MobileServiceClient getMobileServiceClientInstance()
    {
        return _client;
    }
}
