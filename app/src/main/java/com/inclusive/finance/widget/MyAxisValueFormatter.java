package com.inclusive.finance.widget;

import android.icu.text.DecimalFormat;


import com.github.mikephil.charting.components.AxisBase;

import com.github.mikephil.charting.formatter.IAxisValueFormatter;


public class MyAxisValueFormatter implements IAxisValueFormatter {
    private DecimalFormat mFormat;

    public MyAxisValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.000");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        return (int)value + " 月";
    }
}