package com.cronista.dolarya.adapters;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cronista.dolarya.activities.SplashActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.cronista.dolarya.R;
import com.cronista.dolarya.helpers.CustomTextView;
import com.cronista.dolarya.helpers.Utils;
import com.cronista.dolarya.models.CurrencyRate;
import com.google.common.util.concurrent.FutureCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Ramiro E. Rinaldi on 08-Sep-15.
 */
public class CustomAdapter extends BaseAdapter {
    static class ViewHolder {
        CustomTextView currencyName;
        CustomTextView buy;
        CustomTextView sell;
        CustomTextView percentageChange;
        ImageView percentageChangeCaret;
        FrameLayout chartContainerLayout;
        RelativeLayout chartButtonsContainerLayout;
        RelativeLayout shareLayout;
        ImageButton plusBtn;
        FrameLayout shareContainerLayout;
        ImageView shareOpenerBtn;
        CustomTextView weekBtn;
        CustomTextView monthBtn;
        ImageView facebookBtn;
        ImageView twitterBtn;
        ImageView whatsappBtn;
    }

    private class CustomAdapterRow
    {
        public CurrencyRate currencyRate;
        public boolean isSharedOpen;
        public boolean isChartOpen;
        int chartId;
    }

    private Context _context;
    private List<CurrencyRate> _currencyRateList;
    public void setCurrencyRateList(List<CurrencyRate> rateList)
    {
        _currencyRateList = rateList;
        customAdapterRowList = new ArrayList<>();
        for (CurrencyRate rate : _currencyRateList) {
            CustomAdapterRow newItem = new CustomAdapterRow();
            newItem.currencyRate = rate;
            newItem.isChartOpen = false;
            newItem.isSharedOpen = false;
            customAdapterRowList.add(newItem);
        }
    }
    public List<CustomAdapterRow> customAdapterRowList;

    public CustomAdapter(Context context) {
        _context = context;
        setCurrencyRateList(CurrencyRate.GetLatestUpdate(_context));
    }

    @Override
    public int getCount() {
        return customAdapterRowList.size();
    }

    @Override
    public Object getItem(int position) {
        return customAdapterRowList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return customAdapterRowList.get(position).currencyRate.getCurrencyId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final CustomAdapterRow customAdapterRow = (CustomAdapterRow) getItem(position);
        final CurrencyRate currencyRate = customAdapterRow.currencyRate;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_list_item, parent, false);
            holder = new ViewHolder();
            holder.currencyName = (CustomTextView) convertView.findViewById(R.id.item_currency_name);
            holder.buy = (CustomTextView) convertView.findViewById(R.id.item_buy_value);
            holder.sell = (CustomTextView) convertView.findViewById(R.id.item_sell_value);
            holder.percentageChange = (CustomTextView) convertView.findViewById(R.id.item_percentage_change_value);
            holder.percentageChangeCaret = (ImageView) convertView.findViewById(R.id.item_percentage_change_caret);
            holder.chartContainerLayout = (FrameLayout) convertView.findViewById(R.id.item_data_container);
            holder.chartButtonsContainerLayout = (RelativeLayout) convertView.findViewById(R.id.item_data_buttons_container);
            holder.shareLayout = (RelativeLayout) convertView.findViewById(R.id.item_share);
            holder.plusBtn = (ImageButton) convertView.findViewById(R.id.item_plus_btn);
            holder.shareContainerLayout = (FrameLayout) convertView.findViewById(R.id.item_share_container);
            holder.shareOpenerBtn = (ImageView) convertView.findViewById(R.id.item_share_opener);
            holder.weekBtn = (CustomTextView) convertView.findViewById(R.id.item_week_btn);
            holder.monthBtn = (CustomTextView) convertView.findViewById(R.id.item_month_btn);
            holder.facebookBtn = (ImageView) convertView.findViewById(R.id.item_facebook_btn);
            holder.twitterBtn = (ImageView) convertView.findViewById(R.id.item_twitter_btn);
            holder.whatsappBtn = (ImageView) convertView.findViewById(R.id.item_whatsapp_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.currencyName.setText(currencyRate.getNombre());
        holder.buy.setText(String.format("%.4f", currencyRate.getCompra()));
        holder.sell.setText(String.format("%.4f", currencyRate.getVenta()));
        holder.percentageChange.setText(String.format("%.4f", currencyRate.getVariacionPorcentual()));
        holder.percentageChangeCaret.setImageResource(currencyRate.getVariacionPorcentual() >= 0 ? R.drawable.caret_up : R.drawable.caret_down);

        final int chartId;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            chartId = Utils.generateViewId();
        } else {
            chartId = View.generateViewId();
        }
        ViewGroup.LayoutParams shareParams = holder.shareLayout.getLayoutParams();
        shareParams.height = dpToPx(85);
        holder.shareLayout.setLayoutParams(shareParams);

        holder.chartButtonsContainerLayout.setVisibility(View.GONE);
        holder.chartContainerLayout.setVisibility(View.GONE);
        holder.plusBtn.setImageResource(R.drawable.expand_button);
        LineChart chart = (LineChart)holder.chartContainerLayout.findViewById(customAdapterRow.chartId);
        if(chart != null)
        {
            holder.chartContainerLayout.removeView(chart);
        }
        holder.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LineChart chart =  (LineChart)holder.chartContainerLayout.findViewById(customAdapterRow.chartId);
                if (holder.chartContainerLayout.getVisibility() == View.GONE) {
                    customAdapterRow.isChartOpen = true;
                    ViewGroup.LayoutParams shareParams = holder.shareLayout.getLayoutParams();
                    shareParams.height = dpToPx(335);
                    holder.shareLayout.setLayoutParams(shareParams);
                    holder.chartButtonsContainerLayout.setVisibility(View.VISIBLE);
                    holder.chartContainerLayout.setVisibility(View.VISIBLE);
                    holder.plusBtn.setImageResource(R.drawable.close_button);

                    if (chart == null)
                    {
                        chart = new LineChart(_context);
                        chart.setId(chartId);
                        customAdapterRow.chartId = chartId;
                        holder.chartContainerLayout.addView(chart); // add the programmatically created chart
                        ConfigureChart(currencyRate, true, chart);
                    }
                } else {
                    customAdapterRow.isChartOpen = false;
                    ViewGroup.LayoutParams shareParams = holder.shareLayout.getLayoutParams();
                    shareParams.height = dpToPx(85);
                    holder.shareLayout.setLayoutParams(shareParams);

                    holder.chartButtonsContainerLayout.setVisibility(View.GONE);
                    holder.chartContainerLayout.setVisibility(View.GONE);
                    if (chart == null) {
                        holder.chartContainerLayout.removeView(chart);
                    }
                    holder.plusBtn.setImageResource(R.drawable.expand_button);
                }
            }
        });
        if(customAdapterRow.isSharedOpen)
        {
            holder.shareContainerLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.shareContainerLayout.setVisibility(View.GONE);
        }
        holder.shareOpenerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.shareContainerLayout.getVisibility() == View.GONE) {
                    customAdapterRow.isSharedOpen = true;
                    holder.shareContainerLayout.setVisibility(View.VISIBLE);
                } else {
                    customAdapterRow.isSharedOpen = false;
                    holder.shareContainerLayout.setVisibility(View.GONE);
                }
            }
        });
        holder.weekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LineChart chart =  (LineChart)holder.chartContainerLayout.findViewById(customAdapterRow.chartId);
                ConfigureChart(currencyRate, true, chart);
            }
        });
        holder.monthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LineChart chart =  (LineChart)holder.chartContainerLayout.findViewById(customAdapterRow.chartId);
                ConfigureChart(currencyRate, false, chart);
            }
        });
        holder.facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appInstalledOrNot("com.facebook.katana"))// Check android app is installed or not
                {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "http://bit.ly/appDolar");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "El Cronista - DólarYa");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.facebook.katana");
                    _context.startActivity(intent);
                    return;
                } else {
                    showMessage("App Not Installed");
                }
            }
        });
        holder.twitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appInstalledOrNot("com.twitter.android"))// Check android app is installed or not
                {
                    Intent intent = createShareIntent(currencyRate);
                    intent.setComponent(new ComponentName("com.twitter.android", "com.twitter.android.composer.ComposerActivity"));
                    intent.setPackage("com.twitter.android");
                    _context.startActivity(intent);
                    return;
                } else {
                    showMessage("App Not Installed");
                }
            }
        });
        holder.whatsappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appInstalledOrNot("com.whatsapp"))// Check android app is installed or not
                {
                    Intent intent = createShareIntent(currencyRate);
                    intent.setPackage("com.whatsapp");
                    _context.startActivity(intent);
                    return;
                } else {
                    showMessage("App Not Installed");
                }
            }
        });
        convertView.setOnTouchListener(new View.OnTouchListener() {
            int downX, upX;

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    downX = (int) event.getX();
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    upX = (int) event.getX();
                    if (upX - downX > 80) {
                        holder.shareContainerLayout.setVisibility(View.VISIBLE);
                        customAdapterRow.isSharedOpen = true;
                    } else if (downX - upX > -80) {
                        holder.shareContainerLayout.setVisibility(View.GONE);
                        customAdapterRow.isSharedOpen = false;
                    }
                    else
                    {
                        Toast.makeText(_context, "downX: " + downX + " upX:" + upX, Toast.LENGTH_SHORT).show();
                    }

                    return true;

                }
                return false;
            }
        });
        return convertView;
    }

    public void showMessage(String msg) {
        Toast.makeText(_context, msg, Toast.LENGTH_SHORT).show();
    }

    private Intent createShareIntent(CurrencyRate currencyRate) {
        DateFormat dateFormat = new SimpleDateFormat("hh.mm a");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareText = currencyRate.getNombre() + ": $" + currencyRate.getCompra() + "/$" + currencyRate.getVenta() + ".";
        shareText += " Hora: " + dateFormat.format(currencyRate.getUltimaActualizacion());
        shareText += " Descargá la app DólarYa de Cronista.com ingresando en: http://bit.ly/appDolar";
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        intent.putExtra(Intent.EXTRA_SUBJECT, "El Cronista - DólarYa");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = _context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void setupChart(LineChart chart, LineData data)
    {
        chart.setDescription("");
        chart.setNoDataTextDescription("You need to provide data for the chart.");
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
        chart.setBackgroundColor(Color.rgb(203, 228, 233));
        chart.setViewPortOffsets(10, 0, 10, 0);
        chart.setData(data);
        Legend l = chart.getLegend();
        l.setEnabled(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setStartAtZero(false);
        leftAxis.setSpaceTop(30);
        leftAxis.setDrawAxisLine(false);
        chart.getAxisRight().setEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(false);
        xAxis.setAvoidFirstLastClipping(true);
        CustomMarkerView mv = new CustomMarkerView(_context, R.layout.custom_marker);
        chart.setMarkerView(mv);
        chart.animateX(1000);
    }

    private LineData GetData(CurrencyRate currencyRate, boolean week, LineChart chart) {
        List<CurrencyRate> currencyRateList;
        if (week) {
            currencyRateList = currencyRate.getLatestWeek(_context);
            if(currencyRateList.size() < 3)
            {
                SplashActivity._apiHandler.GetLastWeekCurrenciesAsync(currencyRate, GetCurrenciesFromAPICallBack(currencyRate, week, chart));
            }
        } else {
            currencyRateList = currencyRate.getLatestMonth(_context);
            if(currencyRateList.size() < 15)
            {
                SplashActivity._apiHandler.GetLastMonthCurrencyAsync(currencyRate, GetCurrenciesFromAPICallBack(currencyRate, week, chart));
            }
        }
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> entryList = new ArrayList<Entry>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM");

        for (int i = 0; i < currencyRateList.size(); i++) {
            CurrencyRate rate = currencyRateList.get(i);
            entryList.add(new Entry(rate.getCompra(), i, rate));
            String format = formatter.format(rate.getUltimaActualizacion());
            xVals.add(format);
        }
        LineDataSet lineDataSet = new LineDataSet(entryList, currencyRate.getNombre());

        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleSize(6f);
        lineDataSet.setColor(Color.WHITE);
        lineDataSet.setCircleColor(Color.rgb(54, 106, 117));
        lineDataSet.setCircleColorHole(Color.rgb(54, 106, 117));
        lineDataSet.setHighLightColor(Color.WHITE);
        lineDataSet.setDrawValues(false);
        LineData data = new LineData(xVals, lineDataSet);
        return data;
    }

    private void ConfigureChart(CurrencyRate currencyRate, boolean week, LineChart chart)
    {
        LineData data = GetData(currencyRate, week, chart);
        setupChart(chart, data);
    }

    private FutureCallback<List<CurrencyRate>> GetCurrenciesFromAPICallBack(final CurrencyRate currencyRate, final boolean week, final LineChart chart) {
        FutureCallback<List<CurrencyRate>> callBack = new FutureCallback<List<CurrencyRate>>() {
            @Override
            public void onSuccess(List<CurrencyRate> rates) {
                Log.i("CustomAdapter", "Success GetCurrenciesFromAPICallBack");
                CurrencyRate.Save(rates, _context);
                ConfigureChart(currencyRate, week, chart);
            }

            @Override
            public void onFailure(Throwable exc) {
                exc.printStackTrace();
                Log.e("CustomAdapter", "Failure GetCurrenciesFromAPICallBack");
            }
        };
        return callBack;
    }
}
