package com.tgk.Elet;

import java.util.Calendar;
import java.util.GregorianCalendar;

class CurrentDate {
    private Calendar cal;
    private int d;
    private int m;
    private int y;
    ConvertDate converted;
    CurrentDate(){
        if (cal==null){
            cal=Calendar.getInstance();
        }
        this.d=cal.get(Calendar.DAY_OF_MONTH);
        this.m=cal.get(Calendar.MONTH)+1;
        this.y=cal.get(Calendar.YEAR);

        this.converted=new ConvertDate(d,m,y,"ferenji");
    }

    int getTodayInGeez(){
        //return String.valueOf(geezCalConverter.geezDate(d,m,geezCalConverter.dateVar(d,m,y),y));
        return converted.day_of_the_week;
    }
    // there really is no more need for this.
    public int numberOfDaysInMonth(int month) {
        Calendar monthStart = new GregorianCalendar(y, month-1, 1);
        return monthStart.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public int getCurrentGregorianYear() {
        return y;
    }
    int getCurrentGregorianMonth(){
        return m;
    }
    int getCurrentGregorianDate(){
        return d;
    }
    int getCurrentGeezYear(){
        return converted.geez_year;
    }
    int getCurrentGeezDate(){
        return converted.geez_date;
    }
    int getCurrentGeezMonth(){
        return converted.geez_month;
    }

    public void setCal(Calendar cal) {
        this.cal = cal;
    }
    public String getTimeNow(){
        return cal.getTime().toString();
    }
}

