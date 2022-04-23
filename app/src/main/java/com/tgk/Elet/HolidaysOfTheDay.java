package com.tgk.Elet;

import android.content.Context;


import java.util.ArrayList;

public class HolidaysOfTheDay {
    int d,y;
    int month;
    Context context;
    BahreHasab bahreHasab;
    String[] daily_events;
    boolean eritrean,tigraian;
    HolidaysOfTheDay(Context context,int d, int month, int y,boolean eritrean,boolean tigraian){
        this.context=context;
        this.eritrean=eritrean;
        this.tigraian=tigraian;
        this.daily_events=context.getResources().getStringArray(R.array.daily_events);
        this.d=d;
        this.y=y;
        this.month=month;
        this.bahreHasab=new BahreHasab(y);
    }
    ArrayList<String> getExtendedList(){
        HolyDaysList holidays=new HolyDaysList(context,month,y,false,eritrean,tigraian);
        ArrayList<Integer> dates=holidays.dates;
        ArrayList<String> events=holidays.names;
        ArrayList<String> list=new ArrayList<>();

        for (int i=0;i<dates.size();i++) {
            if (dates.get(i) == d) {
                list.add(events.get(i));
            }
        }
        if (month!=13){
            list.add(daily_events[d]);
        }
        return list;
    }
    ArrayList<String> getShortList(){
        HolyDaysList holidays=new HolyDaysList(context,month,y,false,eritrean,tigraian);
        ArrayList<Integer> dates=holidays.dates;
        ArrayList<String> events=holidays.names;
        ArrayList<String> list=new ArrayList<>();

        for (int i=0;i<dates.size();i++) {
            if (dates.get(i) == d) {
                list.add(events.get(i));
            }
        }
        return list;
    }
}

