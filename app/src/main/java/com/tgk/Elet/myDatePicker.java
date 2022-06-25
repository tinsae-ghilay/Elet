package com.tgk.Elet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;

public class myDatePicker extends FrameLayout {
    NumberPicker dPicker,mPicker,yPicker;
    String[] months;
    Context context;
    int d,m,y,YEAR;
    GeezDate date = GeezDate.now();
    DateLocal dateLocal=DateLocal.now();
    private OnDateChangedListener dateListener;
    boolean set_selector_to_geez;

    public myDatePicker(@NonNull Context context)  {
        super(context);
        this.context=context;
        initView(context);
    }

    public myDatePicker(@NonNull Context context, @Nullable AttributeSet attrs)  {
        super(context, attrs);
        this.context=context;
        initView(context);
    }

    public myDatePicker(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)  {
        super(context, attrs, defStyleAttr);
        this.context=context;
        initView(context);
    }
    private void initView(Context context) {
        View.inflate(context, R.layout.picker_layout, this);
        this.context=context;
        mPicker = findViewById(R.id.months_picker);
        yPicker = findViewById(R.id.year_picker);
        dPicker = findViewById(R.id.dates_picker);
        setViews();
    }
    void setViews(){
        // gregorian.
        setMonthList();
        this.d=dateLocal.dayOfMonth;
        this.m=dateLocal.month;
        this.y=dateLocal.year;
        this.YEAR=y;
        // initiate Number Pickers
        // Date Picker
        initDatePicker();
        // Month Picker
        initMonthPicker();
        // years picker
        initYearPicker();
    }

    private void setMonthList() {
        if (set_selector_to_geez){
            this.months=context.getResources().getStringArray(R.array.monthsList);
        }else{
            this.months=context.getResources().getStringArray(R.array.months);
        }
    }

    public void setOnChangedDateListener(OnDateChangedListener listener){
        this.dateListener=listener;
    }

    private void initYearPicker() {
        yPicker.setDisplayedValues(null);
        setYEAR(y);
        String[] years=yearsList(YEAR);
        yPicker.setMaxValue(years.length-1);
        yPicker.setDisplayedValues(years);
        yPicker.setValue(100);
        yPicker.setOnValueChangedListener(listener);
    }

    private void initMonthPicker() {
        mPicker.setDisplayedValues(null);
        mPicker.setMaxValue(months.length-1);
        mPicker.setDisplayedValues(shortenedMonths());
        mPicker.setValue(m-1);
        mPicker.setOnValueChangedListener(listener);
    }

    public void setSelectorToGeez(boolean set_selector_to_geez) {
        this.set_selector_to_geez = set_selector_to_geez;
    }

    private void initDatePicker() {
        dPicker.setDisplayedValues(null);
        String[] dates=datesArray(m,y);
        dPicker.setMaxValue(dates.length-1);
        dPicker.setDisplayedValues(dates);
        dPicker.setValue(d-1);
        dPicker.setOnValueChangedListener(listener);
    }

    // populate list of years for geez date picker.
    String[] yearsList(int gy){
        int year;
        String[] list=new String[200];
        int middle=100;
        for (int i=0;i<200;i++){
            year=gy+(i-middle);
            list[i]=year+"";
        }
        return list;
    }

    // date values for geez date picker
    private void setDayNumPicker(int month,int year){
        String[] dates=datesArray(month,year);
        dPicker.setDisplayedValues(null);
        dPicker.setMaxValue(dates.length-1);
        dPicker.setDisplayedValues(dates);
        this.d=dPicker.getValue()+1;
        setDates(d,m,y);
    }
    // months for geez date picker
    private String[] shortenedMonths(){
        ArrayList<String> list=new ArrayList<>();
        boolean isInTigrinya=((months[0].equalsIgnoreCase("መስከረም"))||(months[8].equalsIgnoreCase("መስከረም")));
        int size;
        if (isInTigrinya){
            size=2;
        }else {
            size=3;
        }
        for (String month : months) {
            list.add(month.substring(0, size));
        }
        return list.toArray(new String[months.length]);
    }
    // list of dates converted to String so that they be used in number picker.
    private String[] datesArray(int m,int year){
        int size;
        if (set_selector_to_geez){
            if (m==13){
                if (isGeezLeapYear(year)){
                    size=6;
                }else{
                    size=5;
                }

            }else {
                size=30;
            }
        }else{
            if (m!=2){
                if ((m<8&&m%2!=0)||(m>7&&m%2==0)){
                    size=31;
                }else{
                    size=30;
                }
            }else if (isLeapYear(year)){
                size=29;
            }else {
                size=28;
            }
        }
        String[] dates=new String[size];
        for (int i=0;i<size;i++){
            int date=i+1;
            dates[i]=date+"";
        }
        return dates;
    }
    //leap year
    private boolean isLeapYear(int year){
        if (year % 4 != 0) {
            return false;
        } else if (year % 400 == 0) {
            return true;
        } else return year % 100 != 0;
    }
    //geez leap year
    private  boolean isGeezLeapYear(int year){
        return year%4==3;
    }
    // set date to new Value if d,m or year changed.
    // then notify listener for changes in date.
    public void setDates(int d,int m,int y) {
        this.d = d;
        this.m = m;
        this.y = y;
        //notify listener.
        if (dateListener!=null){
            dateListener.onDateChanged(m,d,y);
        }
    }

    public void setYEAR(int YEAR) {
        this.YEAR = YEAR;
    }

    NumberPicker.OnValueChangeListener listener=new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            d=dPicker.getValue()+1;
            m=mPicker.getValue()+1;
            y=Integer.parseInt(yearsList(YEAR)[yPicker.getValue()]);
        if (dPicker.equals(picker)) {
            d=newVal+1;
            setDates(d,m,y);
        }else if (mPicker.equals(picker)){
            m = newVal+1;
            setDayNumPicker(m,y);
        }else {
            y=Integer.parseInt(yearsList(YEAR)[newVal]);
            setDayNumPicker(m,y);
        }
    }};
    //reset date picker
    void resetWheels(){
        setMonthList();
        if (set_selector_to_geez){
            setDates( date.dayOfMonth, date.month, date.year);
        }else{
            setDates( dateLocal.dayOfMonth, dateLocal.month, dateLocal.year );
        }
        initDatePicker();
        initMonthPicker();
        initYearPicker();
    }
}
