package com.cronista.dolarya.helpers;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.cronista.dolarya.activities.MainActivity;
import com.cronista.dolarya.adapters.CustomAdapter;
import com.cronista.dolarya.models.CurrencyRate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ramiro E. Rinaldi on 07-Sep-15.
 */
public class APIHandler {
    private String TAG = "APIHandler";
    private Context _context;
    public List<CurrencyRate> currentCurrencyRateList;

    public APIHandler(Context context) {
        _context = context;
        currentCurrencyRateList = CurrencyRate.GetLatestUpdate(context);
    }

    public void UpdateCurrenciesAsync(final CustomAdapter customAdapter) {
        Log.i(TAG, "Doing UpdateCurrenciesAsync...");
        ListenableFuture<List<CurrencyRate>> getLatestRatesResult = GetLatestRates();
        Futures.addCallback(getLatestRatesResult, GetLatestRatesCallBack(customAdapter));
    }

    public void GetLastWeekCurrenciesAsync(CurrencyRate rate, FutureCallback<List<CurrencyRate>> callBack) {
        Log.i(TAG, "Doing GetLastMonthCurrenciesAsync...");
        ListenableFuture<List<CurrencyRate>> getLastWeekRatesResult = GetLastWeekRates(rate.getCurrencyId());
        Futures.addCallback(getLastWeekRatesResult, callBack);
    }

    public void GetLastWeekCurrenciesAsync() {
        Log.i(TAG, "Doing GetLastMonthCurrenciesAsync...");
        ListenableFuture<List<CurrencyRate>> getLastWeekRatesResult = GetLastWeekRates();
        Futures.addCallback(getLastWeekRatesResult, GetLastMonthRatesCallBack());
    }

    public void GetLastMonthCurrencyAsync(CurrencyRate rate, FutureCallback<List<CurrencyRate>> callBack) {
        Log.i(TAG, "Doing GetLastMonthCurrencyAsync...");
        ListenableFuture<List<CurrencyRate>> getLastMonthRatesResult = GetLastMonthRates(rate.getCurrencyId());
        Futures.addCallback(getLastMonthRatesResult, callBack);
    }

    private ListenableFuture<List<CurrencyRate>> GetLatestRates()
    {
        Log.i(TAG, "Doing GetLatestRates...");
        List<Pair<String, String>> listPair = new ArrayList<>();
        ListenableFuture<JsonElement> getLatestRatesResult = MobileServiceHelper.getInstance(_context).getMobileServiceClientInstance().invokeApi("CurrencyRates", "Get", listPair);
        return ConvertApiResultToListenableFuture(getLatestRatesResult);
    }

    private ListenableFuture<List<CurrencyRate>> GetLastWeekRates() {
        Log.i(TAG, "Doing GetLastWeekRates...");
        Calendar cal = ISO8601.GetLastWeek();
        List<Pair<String, String>> listPair = new ArrayList<>();
        listPair.add(new Pair<>("id", ""));
        String fromDate = cal.get(Calendar.YEAR) +  "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE);
        listPair.add(new Pair<>("fromDate", fromDate));
        ListenableFuture<JsonElement> getLastMonthRatesResult = MobileServiceHelper.getInstance(_context).getMobileServiceClientInstance().invokeApi("CurrencyRates", "Get", listPair);
        return ConvertApiResultToListenableFuture(getLastMonthRatesResult);
    }

    private ListenableFuture<List<CurrencyRate>> GetLastWeekRates(int currencyId) {
        Log.i(TAG, "Doing GetLastWeekRates...");
        Calendar cal = ISO8601.GetLastWeek();
        List<Pair<String, String>> listPair = new ArrayList<>();
        listPair.add(new Pair<>("id", currencyId + ""));
        String fromDate = cal.get(Calendar.YEAR) +  "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE);
        listPair.add(new Pair<>("fromDate", fromDate));
        ListenableFuture<JsonElement> getLastMonthRatesResult = MobileServiceHelper.getInstance(_context).getMobileServiceClientInstance().invokeApi("CurrencyRates", "Get", listPair);
        return ConvertApiResultToListenableFuture(getLastMonthRatesResult);
    }

    private ListenableFuture<List<CurrencyRate>> GetLastMonthRates(int currencyId) {
        Log.i(TAG, "Doing GetLastMonthRates...");
        Calendar cal = ISO8601.GetLastMonth();
        List<Pair<String, String>> listPair = new ArrayList<>();
        listPair.add(new Pair<>("id", currencyId + ""));
        listPair.add(new Pair<>("fromDate", cal.get(Calendar.YEAR) +  "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE)));
        ListenableFuture<JsonElement> getLastMonthRatesResult = MobileServiceHelper.getInstance(_context).getMobileServiceClientInstance().invokeApi("CurrencyRates", "Get", listPair);
        return ConvertApiResultToListenableFuture(getLastMonthRatesResult);
    }

    private ListenableFuture<List<CurrencyRate>> ConvertApiResultToListenableFuture(ListenableFuture<JsonElement> result)
    {
        AsyncFunction<JsonElement, List<CurrencyRate>> transformUserJsonFunction = ParseJsonToCurrencyList();
        Futures.addCallback(result, ApiCallBack());
        return Futures.transform(result, transformUserJsonFunction);
    }

    private AsyncFunction<JsonElement, List<CurrencyRate>> ParseJsonToCurrencyList() {
        AsyncFunction<JsonElement, List<CurrencyRate>> callBack =
                new AsyncFunction<JsonElement, List<CurrencyRate>>() {
                    public ListenableFuture<List<CurrencyRate>> apply(JsonElement ratesJson) throws ParseException {
                        List<CurrencyRate> rates = new ArrayList<>();
                        for (JsonElement je : (JsonArray) ratesJson) {
                            rates.add(new CurrencyRate(je));
                        }
                        return Futures.immediateFuture(rates);
                    }
                };
        return callBack;
    }

    private FutureCallback<List<CurrencyRate>> GetLatestRatesCallBack(final CustomAdapter customAdapter) {
        FutureCallback<List<CurrencyRate>> callBack = new FutureCallback<List<CurrencyRate>>() {
            @Override
            public void onSuccess(List<CurrencyRate> rates) {
                for (CurrencyRate rate : rates) {
                    CurrencyRate.Save(rate, _context);
                }
                customAdapter.setCurrencyRateList(CurrencyRate.GetLatestUpdate(_context));
                MainActivity._lastUpdateValue.setText(CurrencyRate.GetLatestUpdateTime(_context));
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable exc) {
                exc.printStackTrace();
            }
        };
        return callBack;
    }

    private FutureCallback<List<CurrencyRate>> GetLastMonthRatesCallBack() {
        FutureCallback<List<CurrencyRate>> callBack = new FutureCallback<List<CurrencyRate>>() {
            @Override
            public void onSuccess(List<CurrencyRate> rates) {
                CurrencyRate.Save(rates, _context);
            }

            @Override
            public void onFailure(Throwable exc) {
                exc.printStackTrace();
            }
        };
        return callBack;
    }

    private FutureCallback<JsonElement> ApiCallBack() {
        FutureCallback<JsonElement> callBack = new FutureCallback<JsonElement>() {
            @Override
            public void onFailure(Throwable exc) {
                exc.printStackTrace();
            }

            @Override
            public void onSuccess(JsonElement result) {
                Log.i(TAG, "Success GetLatestRates");
            }
        };
        return callBack;
    }
}
