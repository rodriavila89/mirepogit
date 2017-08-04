package com.cronista.dolarya.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cronista.dolarya.R;
import com.cronista.dolarya.helpers.CustomTextView;
import com.cronista.dolarya.models.CurrencyRate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ramiro E. Rinaldi on 02-Oct-15.
 */
public class CurrencyConverterAdapter extends BaseAdapter {
    private static String TAG = "CurrencyConverterAdapter";
    private Context _context;
    private List<CurrencyRate> _currencyRateList;
    private List<CurrencyRate> _filteredCurrencyRateList;
    private Float _amountToChange;
    private CurrencyRate _optionSelected;

    static class ViewHolder {
        CustomTextView currencyName;
        CustomTextView exchangeValue;
    }


    public CurrencyConverterAdapter(Context context, List<CurrencyRate> currencyRateList) {
        _context = context;
        _currencyRateList = currencyRateList;
        _filteredCurrencyRateList = currencyRateList;
        _optionSelected = currencyRateList.get(0);
        _amountToChange = 0f;
    }

    @Override
    public int getCount() {
        return _filteredCurrencyRateList.size();
    }

    @Override
    public Object getItem(int position) {
        return _filteredCurrencyRateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return _filteredCurrencyRateList.get(position).getCurrencyId();
    }

    public void setAmountToChange(float amountToChange)
    {
        _amountToChange = amountToChange;
        if (_amountToChange == null) {
            _amountToChange = 0f;
        }
        notifyDataSetChanged();
    }

    public void setOptionSelected(CurrencyRate selectedCurrencyRate)
    {
        _optionSelected = selectedCurrencyRate;
        if (_optionSelected == null) {
            _optionSelected = _currencyRateList.get(0);
        }
        _filteredCurrencyRateList = new ArrayList<CurrencyRate>();
        for (CurrencyRate rate: _currencyRateList) {
            if(rate.getCurrencyId() != _optionSelected.getCurrencyId())
            {
                _filteredCurrencyRateList.add(rate);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final CurrencyRate currencyRate = (CurrencyRate) getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.currency_converter_list_item, parent, false);
            holder = new ViewHolder();
            holder.currencyName = (CustomTextView) convertView.findViewById(R.id.item_currency_converter_name);
            holder.exchangeValue = (CustomTextView) convertView.findViewById(R.id.item_currency_converter_value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.currencyName.setText(currencyRate.getNombre());
        Float pesos = _optionSelected.getCompra() * _amountToChange;
        Float currencyResult = pesos / currencyRate.getCompra();
        holder.exchangeValue.setText(String.format("%.2f", currencyResult));
        return convertView;
    }
}
