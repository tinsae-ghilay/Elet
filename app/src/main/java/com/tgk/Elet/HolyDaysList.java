package com.tgk.Elet;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Locale;

import static com.tgk.Elet.HolyDays.updatedHolidays;
import static com.tgk.Elet.HolyDays.updatedHolyDates;

public class HolyDaysList {
    int gy,y,gm;
    Context context;
    String current_month;
    ArrayList<Combination> comparable;
    String no_holidays;
    ArrayList<String> holy_dates,holiday_names,names;
    ArrayList<Integer> dates;
    boolean eritrean,tigraian,isForCalendar;
    HolyDaysList(Context context, int gm,int gy,boolean isForCalendar,boolean eritrean,boolean tigraian){
        super();
        this.context=context;
        this.no_holidays=context.getResources().getStringArray(R.array.monthsList)[gm-1]+" "+context.getResources().getString(R.string.no_holidays);
        this.gm=gm;
        this.gy=gy;
        this.eritrean=eritrean;
        this.tigraian=tigraian;
        this.isForCalendar=isForCalendar;
        this.names=updatedHolidays(context,gm, gy,eritrean,tigraian);
        this.comparable=new ArrayList<>();
        // we need y(gregorian year) to get the dates of each holiday in a month.
        setGregorianYear();
        this.dates=updatedHolyDates(gm,gy,y,eritrean,tigraian);
        generateList();
    }
    void generateList() {
        this.comparable = new ArrayList<>();
        this.holy_dates = new ArrayList<>();
        // holiday names .
        this.holiday_names = new ArrayList<>();
        ArrayList<String> names = updatedHolidays(context, gm, gy,eritrean,tigraian);
        if (dates.size() != 0) {
            for (int i = 0; i < dates.size(); i++) {
                int date = dates.get(i);
                String name = names.get(i);
                comparable.add(new Combination(date, name));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                comparable.sort(new SortByInt());
            }
            for (int j = 0; j < comparable.size(); j++) {
                String raw = comparable.get(j).toString();
                String[] array = new FormatString(raw).format();
                String DATE = array[0];
                String NAME = array[1];
                holiday_names.add(NAME);
                holy_dates.add(DATE);
            }
        } else {
            if (isForCalendar){
                holy_dates.add("-");
                holiday_names.add(no_holidays);
            }
        }
    }
    void setGregorianYear(){
        if (!isForCalendar){
            this.y=new CurrentDate().getCurrentGregorianYear();
        }else {
            this.y=new ConvertDate(1, gm, gy,"geez").ferenji_year;
        }
    }
    static boolean[] setBooleans(Context context){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        boolean eritrean= preferences.getBoolean("eritrea",false);
        boolean tigraian=preferences.getBoolean("tigray",false);
        boolean show_gregorian= preferences.getBoolean("gregorian",false);
        return new boolean[]{eritrean,tigraian,show_gregorian};
    }
}

