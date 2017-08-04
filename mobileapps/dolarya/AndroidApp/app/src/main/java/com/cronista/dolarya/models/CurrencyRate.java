package com.cronista.dolarya.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.cronista.dolarya.data.DolarYaContract;
import com.cronista.dolarya.data.DolarYaDbHelper;
import com.cronista.dolarya.helpers.ISO8601;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Ramiro E. Rinaldi on 07-Sep-15.
 */
public class CurrencyRate {
    private long id;
    private int currencyId;
    private String nombre;
    private Float variacionPorcentual;
    private Float compra;
    private Float venta;
    private Date ultimaActualizacion;
    private int orden;

    public CurrencyRate() {
    }

    public CurrencyRate(JsonElement jsonElement) throws ParseException {
        try {
            setCurrencyId(((JsonObject) jsonElement).get("id").getAsInt());
            setNombre(((JsonObject) jsonElement).get("nombre").getAsString());
            setCompra(((JsonObject) jsonElement).get("compra").getAsFloat());
            setVenta(((JsonObject) jsonElement).get("venta").getAsFloat());
            if (((JsonObject) jsonElement).get("variacionPorcentual") != null) {
                setVariacionPorcentual(((JsonObject) jsonElement).get("variacionPorcentual").getAsFloat());
            } else {
                setVariacionPorcentual(0f);
            }
            String dateStr = ((JsonObject) jsonElement).get("ultimaActualizacion").getAsString();
            Calendar cal = null;
            try {
                cal = ISO8601.toCalendar(dateStr, true);
            } catch (ParseException e) {
                cal = ISO8601.toCalendar(dateStr, false);
            }
            setUltimaActualizacion(cal.getTime());
            setOrden(((JsonObject) jsonElement).get("orden").getAsInt());
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

    public CurrencyRate(Cursor cursor) throws ParseException {
        setId(cursor.getLong(0));
        setCurrencyId(cursor.getInt(1));
        setNombre(cursor.getString(2));
        setCompra(cursor.getFloat(3));
        setVenta(cursor.getFloat(4));
        setVariacionPorcentual(cursor.getFloat(5));
        String dateStr = (cursor.getString(6));
        Calendar cal = ISO8601.toCalendar(dateStr, false);
        setUltimaActualizacion(cal.getTime());
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setVariacionPorcentual(Float variacionPorcentual) {
        this.variacionPorcentual = variacionPorcentual;
    }

    public void setCompra(Float compra) {
        this.compra = compra;
    }

    public void setVenta(Float venta) {
        this.venta = venta;
    }

    public void setUltimaActualizacion(Date ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public long getId() {
        return this.id;
    }

    public int getCurrencyId() {
        return this.currencyId;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Float getVariacionPorcentual() {
        return this.variacionPorcentual;
    }

    public Float getCompra() {
        return this.compra;
    }

    public Float getVenta() {
        return this.venta;
    }

    public Date getUltimaActualizacion() {
        return this.ultimaActualizacion;
    }

    public int getOrden() {
        return this.orden;
    }

    private static String GetLatestUpdateDate(Context context) {
        SQLiteDatabase db = DolarYaDbHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = db.query(
                DolarYaContract.CurrencyRateEntry.TABLE_NAME,
                new String[]{DolarYaContract.CurrencyRateEntry.COLUMN_NAME_LAST_UPDATED},
                null,
                null,
                null,
                null,
                DolarYaContract.CurrencyRateEntry.COLUMN_NAME_LAST_UPDATED + " DESC",
                "1");
        cursor.moveToFirst();
        String lastUpdateDate = null;
        if (!cursor.isAfterLast()) {
            lastUpdateDate = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return lastUpdateDate;
    }

    public static String GetLatestUpdateTime(Context context) {
        String latestUpdateDate = GetLatestUpdateDate(context);
        if (latestUpdateDate == null) {
            return "";
        }
        try {
            latestUpdateDate = ISO8601.getTimefromString(latestUpdateDate);
        } catch (ParseException e) {
            e.printStackTrace();
            latestUpdateDate = "";
        }
        return latestUpdateDate;
    }

    public static List<CurrencyRate> GetLatestUpdate(Context context) {
        List<CurrencyRate> currencyRateList = new ArrayList<>();
        String latestUpdateDate = GetLatestUpdateDate(context);
        if (latestUpdateDate != null) {
            SQLiteDatabase db = DolarYaDbHelper.getInstance(context).getWritableDatabase();
            Cursor cursor = db.query(
                    DolarYaContract.CurrencyRateEntry.TABLE_NAME,
                    DolarYaContract.CurrencyRateEntry.Projection,
                    DolarYaContract.CurrencyRateEntry.COLUMN_NAME_LAST_UPDATED + " = ?",
                    new String[]{latestUpdateDate},
                    null, null,
                    DolarYaContract.CurrencyRateEntry.COLUMN_NAME_ORDER);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                try {
                    currencyRateList.add(new CurrencyRate(cursor));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
        }
        return currencyRateList;
    }

    private static CurrencyRate GetLatestUpdate(int currencyId, Context context) {
        List<CurrencyRate> list = GetLatestUpdate(context);
        for (CurrencyRate rate : list) {
            if (rate.getCurrencyId() == currencyId) {
                return rate;
            }
        }
        return null;
    }
    private static CurrencyRate GetByDate(int currencyId, Date date, Context context) {
        SQLiteDatabase db = DolarYaDbHelper.getInstance(context).getWritableDatabase();
        CurrencyRate rate = GetByDate(currencyId, date, db);
        db.close();
        return rate;
    }

    private static CurrencyRate GetByDate(int currencyId, Date date, SQLiteDatabase db) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.AM_PM, Calendar.AM);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        String strDateFrom = ISO8601.fromCalendar(cal);
        cal.add(Calendar.DATE, 1);
        String strDateTo = ISO8601.fromCalendar(cal);
        Cursor cursor = db.query(
                DolarYaContract.CurrencyRateEntry.TABLE_NAME,
                DolarYaContract.CurrencyRateEntry.Projection,
                DolarYaContract.CurrencyRateEntry.COLUMN_NAME_CURRENCY_ID + " = ? AND " + DolarYaContract.CurrencyRateEntry.COLUMN_NAME_LAST_UPDATED + " BETWEEN ? AND ?",
                new String[]{String.valueOf(currencyId), strDateFrom, strDateTo},
                null, null,
                DolarYaContract.CurrencyRateEntry.COLUMN_NAME_Name,
                "1");
        cursor.moveToFirst();
        CurrencyRate currencyRate = null;
        if (!cursor.isAfterLast()) {
            try {
                currencyRate = new CurrencyRate(cursor);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return currencyRate;
    }

    public static Boolean Save(CurrencyRate currencyRate, Context context) {
        SQLiteDatabase db = DolarYaDbHelper.getInstance(context).getWritableDatabase();
        Boolean result = Save(currencyRate, db);
        db.close();
        return result;
    }

    public static Boolean Save(CurrencyRate currencyRate, SQLiteDatabase db) {
        CurrencyRate latestRate = CurrencyRate.GetByDate(currencyRate.getCurrencyId(), currencyRate.getUltimaActualizacion(), db);
        Boolean saved = true;
        ContentValues values = new ContentValues();
        values.put(DolarYaContract.CurrencyRateEntry.COLUMN_NAME_CURRENCY_ID, currencyRate.getCurrencyId());
        values.put(DolarYaContract.CurrencyRateEntry.COLUMN_NAME_Name, currencyRate.getNombre());
        values.put(DolarYaContract.CurrencyRateEntry.COLUMN_NAME_BUY, currencyRate.getCompra());
        values.put(DolarYaContract.CurrencyRateEntry.COLUMN_NAME_SELL, currencyRate.getVenta());
        values.put(DolarYaContract.CurrencyRateEntry.COLUMN_NAME_PERCENTAGE_CHANGE, currencyRate.getVariacionPorcentual());
        values.put(DolarYaContract.CurrencyRateEntry.COLUMN_NAME_ORDER, currencyRate.getOrden());
        Calendar c = Calendar.getInstance();
        c.setTime(currencyRate.getUltimaActualizacion());
        values.put(DolarYaContract.CurrencyRateEntry.COLUMN_NAME_LAST_UPDATED, ISO8601.fromCalendar(c));
        float epsilon = (float) 0.00001;
        if (latestRate != null) {
            currencyRate.setId(latestRate.getId());
            values.put(DolarYaContract.CurrencyRateEntry._ID, currencyRate.getId());
            if(latestRate.getUltimaActualizacion().compareTo(currencyRate.getUltimaActualizacion()) != 0 ||
                    Math.abs(latestRate.getCompra() - currencyRate.getCompra()) > epsilon ||
                    Math.abs(latestRate.getVenta() - currencyRate.getVenta()) > epsilon) {
                int rowCount = db.update(DolarYaContract.CurrencyRateEntry.TABLE_NAME, values,
                        DolarYaContract.CurrencyRateEntry._ID + " = ? ",
                        new String[]{String.valueOf(currencyRate.getId())});
                if (rowCount <= 0)
                    saved = false;
            }
        } else {
            long id = db.insert(DolarYaContract.CurrencyRateEntry.TABLE_NAME, null, values);
            if (id <= 0)
                saved = false;
        }
        return saved;
    }

    public static void Save(List<CurrencyRate> currencyRateList, Context context)
    {
        SQLiteDatabase db = DolarYaDbHelper.getInstance(context).getWritableDatabase();
        for (CurrencyRate currencyRate:currencyRateList) {
            Save(currencyRate, db);
        }
        db.close();
    }

    public List<CurrencyRate> getLatestWeek(Context context) {
        return getLatest(ISO8601.GetLastWeek(), context);
    }

    public List<CurrencyRate> getLatestMonth(Context context) {
        return getLatest(ISO8601.GetLastMonth(), context);
    }


    private List<CurrencyRate> getLatest(Calendar dateFrom, Context context) {
        String strDateFrom = ISO8601.fromCalendar(dateFrom);
        SQLiteDatabase db = DolarYaDbHelper.getInstance(context).getWritableDatabase();
        List<CurrencyRate> currencyRateList = new ArrayList<>();
        Cursor cursor = db.query(
                DolarYaContract.CurrencyRateEntry.TABLE_NAME,
                DolarYaContract.CurrencyRateEntry.Projection,
                DolarYaContract.CurrencyRateEntry.COLUMN_NAME_CURRENCY_ID + " = ? AND " + DolarYaContract.CurrencyRateEntry.COLUMN_NAME_LAST_UPDATED + " > ?",
                new String[]{String.valueOf(getCurrencyId()), strDateFrom},
                null, null,
                DolarYaContract.CurrencyRateEntry.COLUMN_NAME_LAST_UPDATED);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            try {
                currencyRateList.add(new CurrencyRate(cursor));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return currencyRateList;
    }

    @Override
    public String toString() {
        return getNombre();
    }
}
