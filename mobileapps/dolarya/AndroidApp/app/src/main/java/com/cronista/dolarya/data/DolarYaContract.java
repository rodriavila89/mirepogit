package com.cronista.dolarya.data;

import android.provider.BaseColumns;

/**
 * Created by Ramiro E. Rinaldi on 07-Sep-15.
 */
public final class DolarYaContract {
    public DolarYaContract(){}
    public static abstract class CurrencyRateEntry implements BaseColumns {
        public static final String TABLE_NAME = "CurrencyRate";
        public static final String COLUMN_NAME_CURRENCY_ID = "CurrencyId";
        public static final String COLUMN_NAME_Name = "Name";
        public static final String COLUMN_NAME_BUY = "Buy";
        public static final String COLUMN_NAME_SELL = "Sell";
        public static final String COLUMN_NAME_PERCENTAGE_CHANGE = "PercentageChange";
        public static final String COLUMN_NAME_LAST_UPDATED = "LastUpdated";
        public static final String COLUMN_NAME_ORDER = "Sort";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + CurrencyRateEntry.TABLE_NAME + " (" +
                        CurrencyRateEntry._ID + " INTEGER PRIMARY KEY," +
                        CurrencyRateEntry.COLUMN_NAME_CURRENCY_ID + SQLiteConst.INTEGER_TYPE + SQLiteConst.COMMA_SEP +
                        CurrencyRateEntry.COLUMN_NAME_Name + SQLiteConst.TEXT_TYPE + SQLiteConst.COMMA_SEP +
                        CurrencyRateEntry.COLUMN_NAME_BUY + SQLiteConst.REAL_TYPE + SQLiteConst.COMMA_SEP +
                        CurrencyRateEntry.COLUMN_NAME_SELL + SQLiteConst.REAL_TYPE + SQLiteConst.COMMA_SEP +
                        CurrencyRateEntry.COLUMN_NAME_PERCENTAGE_CHANGE + SQLiteConst.REAL_TYPE + SQLiteConst.COMMA_SEP +
                        CurrencyRateEntry.COLUMN_NAME_LAST_UPDATED + SQLiteConst.TEXT_TYPE + SQLiteConst.COMMA_SEP +
                        CurrencyRateEntry.COLUMN_NAME_ORDER + SQLiteConst.INTEGER_TYPE +
                " )";

        public static final String SQL_ADD_ORDER_COLUMN =
                "ALTER TABLE " + CurrencyRateEntry.TABLE_NAME + " ADD COLUMN " +
                        CurrencyRateEntry.COLUMN_NAME_ORDER + " " + SQLiteConst.INTEGER_TYPE;

        public static final String SQL_UPDATE_ORDER_COLUMN =
                "UPDATE " + CurrencyRateEntry.TABLE_NAME + " SET " + CurrencyRateEntry.COLUMN_NAME_ORDER +
                        "=" + CurrencyRateEntry.COLUMN_NAME_CURRENCY_ID;

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + CurrencyRateEntry.TABLE_NAME;

        public static final String[] Projection = {
                CurrencyRateEntry._ID,
                CurrencyRateEntry.COLUMN_NAME_CURRENCY_ID,
                CurrencyRateEntry.COLUMN_NAME_Name,
                CurrencyRateEntry.COLUMN_NAME_BUY,
                CurrencyRateEntry.COLUMN_NAME_SELL,
                CurrencyRateEntry.COLUMN_NAME_PERCENTAGE_CHANGE,
                CurrencyRateEntry.COLUMN_NAME_LAST_UPDATED
        };
    }

    public static abstract class ConfigurationEntry implements BaseColumns {
        public static final String TABLE_NAME = "Configuration";
        public static final String COLUMN_NAME_RECEIVE_OPEN_CLOSE = "ReceiveOpenClose";
        public static final String COLUMN_NAME_RECEIVE_HOURLY = "ReceiveHourly";
        public static final String COLUMN_NAME_DEVICE_ID = "DeviceId";
        public static final String COLUMN_NAME_REGISTRATION_ID = "RegistrationId";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + ConfigurationEntry.TABLE_NAME + " (" +
                        ConfigurationEntry._ID + " INTEGER PRIMARY KEY," +
                        ConfigurationEntry.COLUMN_NAME_RECEIVE_OPEN_CLOSE + SQLiteConst.INTEGER_TYPE + SQLiteConst.COMMA_SEP +
                        ConfigurationEntry.COLUMN_NAME_RECEIVE_HOURLY + SQLiteConst.INTEGER_TYPE + SQLiteConst.COMMA_SEP +
                        ConfigurationEntry.COLUMN_NAME_DEVICE_ID + SQLiteConst.TEXT_TYPE + SQLiteConst.COMMA_SEP +
                        ConfigurationEntry.COLUMN_NAME_REGISTRATION_ID + SQLiteConst.TEXT_TYPE +
                        " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + ConfigurationEntry.TABLE_NAME;

        public static final String[] Projection = {
                ConfigurationEntry._ID,
                ConfigurationEntry.COLUMN_NAME_RECEIVE_OPEN_CLOSE,
                ConfigurationEntry.COLUMN_NAME_RECEIVE_HOURLY,
                ConfigurationEntry.COLUMN_NAME_DEVICE_ID,
                ConfigurationEntry.COLUMN_NAME_REGISTRATION_ID
        };

        public static final String SQL_RESET_CONFIGURATION =
                "UPDATE " + ConfigurationEntry.TABLE_NAME + " SET " +
                        ConfigurationEntry.COLUMN_NAME_RECEIVE_OPEN_CLOSE + " = 1" + SQLiteConst.COMMA_SEP +
                        ConfigurationEntry.COLUMN_NAME_RECEIVE_HOURLY + " = 0";

        public static final String SQL_ADD_DEVICE_ID_COLUMN =
                "ALTER TABLE " + ConfigurationEntry.TABLE_NAME + " ADD COLUMN " +
                        ConfigurationEntry.COLUMN_NAME_DEVICE_ID + " " + SQLiteConst.TEXT_TYPE;

        public static final String SQL_ADD_REGISTRATION_ID_COLUMN =
                "ALTER TABLE " + ConfigurationEntry.TABLE_NAME + " ADD COLUMN " +
                        ConfigurationEntry.COLUMN_NAME_REGISTRATION_ID + " " + SQLiteConst.TEXT_TYPE;
    }
}
