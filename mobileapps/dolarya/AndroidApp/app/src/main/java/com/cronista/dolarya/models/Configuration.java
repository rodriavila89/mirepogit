package com.cronista.dolarya.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cronista.dolarya.data.DolarYaContract;
import com.cronista.dolarya.data.DolarYaDbHelper;

import java.text.ParseException;

/**
 * Created by Ramiro E. Rinaldi on 07-Sep-15.
 */
public class Configuration {
    private long id;
    private boolean receiveOpenClose;
    private boolean receiveHourly;
    private String deviceId;
    private String registrationId;

    public Configuration() {
    }

    public Configuration(Cursor cursor) throws ParseException {
        setId(cursor.getLong(0));
        setReceiveOpenClose(cursor.getInt(1) > 0);
        setReceiveHourly(cursor.getInt(2) > 0);
        setDeviceId(cursor.getString(3));
        setRegistrationId(cursor.getString(4));
    }


    public void setId(long id) { this.id = id; }
    public void setReceiveOpenClose(boolean receiveOpenClose) { this.receiveOpenClose = receiveOpenClose; }
    public void setReceiveHourly(boolean receiveHourly) { this.receiveHourly = receiveHourly; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public void setRegistrationId(String registrationId) { this.registrationId = registrationId; }

    public long getId() { return this.id; }
    public boolean getReceiveOpenClose() {
        return this.receiveOpenClose;
    }
    public boolean getReceiveHourly() {
        return this.receiveHourly;
    }
    public String getDeviceId() {
        return this.deviceId;
    }
    public String getRegistrationId() {
        return this.registrationId;
    }
    public static Configuration Get(Context context) {
        SQLiteDatabase db = DolarYaDbHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = db.query(
                DolarYaContract.ConfigurationEntry.TABLE_NAME,
                DolarYaContract.ConfigurationEntry.Projection,
                null,
                null,
                null,
                null,
                null,
                "1");
        cursor.moveToFirst();
        Configuration configuration = null;
        if (!cursor.isAfterLast()) {
            try {
                configuration = new Configuration(cursor);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        db.close();
        return configuration;
    }


    public static Boolean Save(Configuration configuration, Context context) {
        Configuration currentConfig = Configuration.Get(context);
        SQLiteDatabase db = DolarYaDbHelper.getInstance(context).getWritableDatabase();
        Boolean saved = true;
        ContentValues values = new ContentValues();
        values.put(DolarYaContract.ConfigurationEntry.COLUMN_NAME_RECEIVE_OPEN_CLOSE, configuration.getReceiveOpenClose());
        values.put(DolarYaContract.ConfigurationEntry.COLUMN_NAME_RECEIVE_HOURLY, configuration.getReceiveHourly());
        values.put(DolarYaContract.ConfigurationEntry.COLUMN_NAME_DEVICE_ID, configuration.getDeviceId());
        values.put(DolarYaContract.ConfigurationEntry.COLUMN_NAME_REGISTRATION_ID, configuration.getRegistrationId());

        if (currentConfig != null) {
            configuration.setId(configuration.getId());
            values.put(DolarYaContract.ConfigurationEntry._ID, configuration.getId());
            int rowCount = db.update(DolarYaContract.ConfigurationEntry.TABLE_NAME, values,
                    DolarYaContract.ConfigurationEntry._ID + " = ? ",
                    new String[]{String.valueOf(configuration.getId())});
            if (rowCount <= 0)
                saved = false;
        } else {
            long id = db.insert(DolarYaContract.ConfigurationEntry.TABLE_NAME, null, values);
            if (id <= 0)
                saved = false;
        }
        db.close();
        return saved;
    }
}
