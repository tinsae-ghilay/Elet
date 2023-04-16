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
        if (month < 0) checked += "month - ";
        if (date < 0) checked += "dayOfMonth -";
        if (checked.equalsIgnoreCase("")){
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

    /**
     * Formats GeezDate to a desired pattern
     * @return String
     * moved to another class
     */
    /*public String format(DateFormat format){
        String res;
        switch (format) {
            case DOTTED :
                res = getDate() + "." + getMonth() + "." + getYear();
                break;
            case DOT_SPACED :
                res = getDate() + ". " + getMonth() + ". " + getYear();
                break;
            case SLASHED :
                res = getDate() + "/" + getMonth() + "/" + getYear();
                break;
            case SLASH_SPACED :
                res = getDate() + "/ " + getMonth() + "/ " + getYear();
                break;
            case SPACED :
                res = getDate() + "  " + getMonth() + "  " + getYear();
                break;
            case SPACED_WITH_COMA :
                res = getDate() + ",  " + getMonth() + ",  " + getYear();
                break;
            case WITH_COMMA :
                res = getDate() + "," + getMonth() + "," + getYear();
                break;
            case MONTH_NAMED :
                res = getDate() + " " + MonthName.values()[getMonth() - 1] + ", " + getYear();
                break;
            case MONTH_NAMED_ISO :
                res = MonthName.values()[getMonth() - 1] + " " + getDate() + " " + getYear();
                break;
            /*case DAY_NAMED :
                res = DaysOfWeek.values()[dayOfTheWeek()] + ", " + Month.values()[getMonth() - 1] + " " + getDate() + ", " + getYear();
                break;*/
            /*default :
            {
                res = this.toString();
                break;
            }
        }
        return res;
    }*/
}
