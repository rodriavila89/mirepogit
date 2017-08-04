package com.cronista.dolarya.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.UUID;

/**
 * Created by Ramiro E. Rinaldi on 07-Sep-15.
 */
public class DolarYaDbHelper extends SQLiteOpenHelper {
    private static DolarYaDbHelper sInstance;
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "DolarYa.db";
    private Context pContext = null;

    private DolarYaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        pContext = context;
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DolarYaContract.CurrencyRateEntry.SQL_CREATE_ENTRIES);
        db.execSQL(DolarYaContract.ConfigurationEntry.SQL_CREATE_ENTRIES);
        ContentValues values = new ContentValues();
        values.put(DolarYaContract.ConfigurationEntry.COLUMN_NAME_RECEIVE_OPEN_CLOSE, true);
        values.put(DolarYaContract.ConfigurationEntry.COLUMN_NAME_RECEIVE_HOURLY, false);
        db.insert(DolarYaContract.ConfigurationEntry.TABLE_NAME, null, values);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("DolarYaDbHelper", "onUpgrade");
        Log.i("DolarYaDbHelper", "oldVersion: " + oldVersion);
        Log.i("DolarYaDbHelper", "newVersion: " + newVersion);
        switch(oldVersion) {
            case 1:
                db.execSQL(DolarYaContract.CurrencyRateEntry.SQL_ADD_ORDER_COLUMN);
                db.execSQL(DolarYaContract.CurrencyRateEntry.SQL_UPDATE_ORDER_COLUMN);
                db.execSQL(DolarYaContract.ConfigurationEntry.SQL_RESET_CONFIGURATION);
            case 2:
                db.execSQL(DolarYaContract.ConfigurationEntry.SQL_ADD_DEVICE_ID_COLUMN);
                db.execSQL(DolarYaContract.ConfigurationEntry.SQL_ADD_REGISTRATION_ID_COLUMN);
        }
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static synchronized DolarYaDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DolarYaDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }
}