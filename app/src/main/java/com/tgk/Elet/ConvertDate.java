package com.tgk.Elet;


import org.joda.time.DateTime;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.GregorianChronology;

public class ConvertDate {
    int d,m,y;
    int geez_date,geez_year,ferenji_date,ferenji_year,geez_month,ferenji_month,day_of_the_week;
    String target;
    EthiopicChronology chron_eth;
    GregorianChronology chron_greg;
    ConvertDate(int d,int m,int y,String target){
        this.d=d;
        this.m=m;
        this.y=y;
        this.target=target;
        this.chron_eth=EthiopicChronology.getInstance();
        this.chron_greg=GregorianChronology.getInstance();
        if (target.equalsIgnoreCase("geez")){
            setToGregorian();
        }else{
            setToGeez();
        }
    }
    void setToGregorian(){
        DateTime jodaDateTime = new DateTime(y, m, d, 0, 0, 0, chron_eth);
        DateTime dtGregorian = jodaDateTime.withChronology(chron_greg);
        // keep geez
        this.geez_year=y;
        this.geez_date=d;
        this.geez_month=m;
        //set gregorian
        this.ferenji_year=dtGregorian.getYear();
        this.ferenji_month=dtGregorian.getMonthOfYear();
        this.ferenji_date=dtGregorian.getDayOfMonth();
        int joda_day=dtGregorian.getDayOfWeek();
        this.day_of_the_week=(joda_day==7)?0:joda_day;
    }
    void setToGeez(){
        DateTime jodaDateTime = new DateTime(y, m, d, 0, 0, 0, chron_greg);
        DateTime dtGeez = jodaDateTime.withChronology(chron_eth);
        // keep gregorian
        this.ferenji_month=m;
        this.ferenji_year=y;
        this.ferenji_date=d;
        // set geez
        this.geez_date=dtGeez.getDayOfMonth();
        this.geez_month=dtGeez.getMonthOfYear();
        this.geez_year=dtGeez.getYear();
        int joda_day=dtGeez.getDayOfWeek();
        this.day_of_the_week=(joda_day==7)?0:joda_day;
    }
    String convertDate(String[] days){
        switch (target){
            case "geez":
                return toGregorian(days);
            case "ferenji":
                return toGeez(days);
            default:
                return "Please choose target";
        }
    }
    String toGregorian(String[] days){
        return days[day_of_the_week]+" "+ferenji_date+", "+ferenji_month+", "+ferenji_year;
    }
    String toGeez(String[] days){
        return days[day_of_the_week]+" "+geez_date+", "+geez_month+", "+geez_year;
    }
}

