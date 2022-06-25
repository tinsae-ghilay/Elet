package com.tgk.Elet;


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
    CalendarMonth(int month_index,int span,boolean eritrean,boolean tigraian,boolean gregorian)  {
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
    DateParser(int y,int m,boolean eritrean,boolean tigraian,boolean gregorian)  {
        super();
        this.m=m;
        this.y=y;
        this.eritrean=eritrean;
        this.tigraian=tigraian;
        this.gregorian=gregorian;
        populateMonth();
    }
    void populateMonth()  {

        GeezDate dateTime=GeezDate.of(y,m,1);
        int first_day= dateTime.dayOfTheWeek();//dateTime.getDayOfWeek()%7;
        dateTime= dateTime.plusDays(-first_day);
        int i=0;
        while (i<42){
            int d2=0;
            if (gregorian){
                DateLocal gtGre = dateTime.to();
                d2=gtGre.dayOfMonth;
            }
            boolean over_flow=dateTime.getMonth()!=m;
            int d=dateTime.getDayOfMonth();
            boolean isHoliday=HolidaysInTheMonth().contains(d);
            this.month_table[i]=new DateItem(d,d2,over_flow,isHoliday);
            dateTime=dateTime.plusDays(1);
            i++;
        }
    }

    ArrayList<Integer> HolidaysInTheMonth(){
        int gregYear= GeezDate.of(y,m,1).to().year;
        return new ArrayList<>(HolyDays.updatedHolyDates(m, y, gregYear,eritrean,tigraian));
    }

    public DateItem[] getMonth_table() {
        return month_table;
    }
}
