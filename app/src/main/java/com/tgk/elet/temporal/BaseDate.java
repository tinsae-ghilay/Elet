package com.tgk.elet.temporal;

import androidx.annotation.NonNull;

import com.tgk.elet.localDate.MonthName;

public abstract class BaseDate implements Comparable<BaseDate>{

    private final int year,  month, date;
    private MonthName monthName;

    protected BaseDate(int year, int month, int date){
        this.year = year;
        this.month = month;
        this.date = date;
    }

    protected void validate(){ // all values should be greater than 0
        String checked = "";
        if (year < 0) checked += "year - ";
        if (month <= 0) checked += "month - ";
        if (date <= 0) checked += "dayOfMonth -";
        if (!checked.equalsIgnoreCase("")){
            throw new IllegalArgumentException(checked+" should be greater than 0");
        }
    }

    public int getDate() {
        return date;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public MonthName getMonthName() {
        return monthName;
    }

    @NonNull
    @Override
    public String toString(){
        return date+" " + MonthName.values()[month -1] +", "+ year;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj==null){ return  false;}
        try{
            BaseDate d = (BaseDate) obj;
            return obj.getClass().isInstance(this)  && d.getDate() == date
                    && d.getMonth() == month  && d.getYear() == year;
        }catch (IllegalArgumentException e){
            String m = "Object "+ obj.getClass().getName()+" Cannot be cast to BaseDate()";
            return false;
        }
    }

    @Override
    public int compareTo(BaseDate toCompare) {
        return (month == toCompare.month)? date - toCompare.date : month - toCompare.month;
    }
}
