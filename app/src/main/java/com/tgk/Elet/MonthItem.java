package com.tgk.Elet;

import org.joda.time.DateTime;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.GregorianChronology;

import java.util.ArrayList;

// month with year,dates and month index
class MonthItem {
    DateItem[] dates;
    int month;
    int year;
    MonthItem(int year, int month, DateItem[] dates){
        this.year=year;
        this.month=month;
        this.dates=dates;
    }

    public int getMonth() {
        return month;
    }

    public DateItem[] getDates() {
        return dates;
    }


    public int getYear() {
        return year;
    }
}
// date item with info of it being a holiday or not, and if it is a date in this month or not
class DateItem {
    int gregorian_date;
    int date;
    boolean overFlow,holiday;
    DateItem(int date,int gregorian_date, boolean overFlow, boolean holiday){
        this.date=date;
        this.gregorian_date =gregorian_date;
        this.overFlow=overFlow;
        this.holiday=holiday;
    }

    public int getDate(){
        return date;
    }

    public boolean isOverFlow() {
        return overFlow;
    }

    public boolean isHoliday() {
        return holiday;
    }

    public int getGregorian_date() {
        return gregorian_date;
    }
}
class CalendarMonth{
    MonthItem monthItem;
    ArrayList<Integer> holidays;
    boolean eritrean,tigraian,gregorian;
    CalendarMonth(int month_index,int span,boolean eritrean,boolean tigraian,boolean gregorian){
        this.eritrean=eritrean;
        this.tigraian=tigraian;
        this.gregorian=gregorian;
        ScrollWatcher watcher=new ScrollWatcher(month_index,span);
        int year=watcher.getGeezYear();
        int month=watcher.getMonth();
        DateParser parser=new DateParser(year,month,eritrean,tigraian,gregorian);
        monthItem= new MonthItem(year,month,parser.getMonth_table());
        //
        this.holidays=parser.HolidaysInTheMonth();
    }

    public MonthItem getMonthItem() {
        return monthItem;
    }

    public ArrayList<Integer> getHolidays() {
        return holidays;
    }
}

// populate dates that fall in a given calendar table.
class DateParser {
    int y,m;
    DateItem[] month_table = new DateItem[42];
    boolean eritrean,tigraian,gregorian;
    // may be pass just the index and get year and m from there.
    DateParser(int y,int m,boolean eritrean,boolean tigraian,boolean gregorian){
        super();
        this.m=m;
        this.y=y;
        this.eritrean=eritrean;
        this.tigraian=tigraian;
        this.gregorian=gregorian;
        populateMonth();
    }
    void populateMonth(){
        EthiopicChronology chronology;
        chronology=EthiopicChronology.getInstance();
        GregorianChronology gregorianChronology=GregorianChronology.getInstance();
        DateTime dateTime = new DateTime(y, m, 1, 0, 0, 0,chronology);
        int first_day=dateTime.getDayOfWeek()%7;
        dateTime= dateTime.minusDays(first_day);
        int i=0;
        while (i<42){
            DateTime date=dateTime.plusDays(i);
            int d2=0;
            if (gregorian){
                DateTime dtGregorian = date.withChronology(gregorianChronology);
                d2=dtGregorian.getDayOfMonth();
            }
            boolean over_flow=date.getMonthOfYear()!=m;
            int d=date.getDayOfMonth();
            boolean isHoliday=HolidaysInTheMonth().contains(d);
            this.month_table[i]=new DateItem(d,d2,over_flow,isHoliday);
            i++;
        }
    }

    ArrayList<Integer> HolidaysInTheMonth(){
        return new ArrayList<>(HolyDays.updatedHolyDates(m, y, new ConvertDate(1,m,y,"Geez").ferenji_year,eritrean,tigraian));
    }

    public DateItem[] getMonth_table() {
        return month_table;
    }
}
