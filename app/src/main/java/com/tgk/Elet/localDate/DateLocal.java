package com.tgk.Elet.localDate;

import androidx.annotation.NonNull;

import com.tgk.Elet.geezDate.GeezDate;
import com.tgk.Elet.temporal.BaseDate;

public class DateLocal extends BaseDate {

    long julianDay;

    public DateLocal(int year, int month, int dayOfMonth, long julianDay) {
        super(year,month,dayOfMonth);
        this.julianDay = julianDay;
    }

    public DateLocal(int year,int month, int dayOfMonth){
        super(year,month,dayOfMonth);
    }

    public DateLocal plusDays(int days){
        return DateLocal.fromJdn(this.julianDay+days);
    }

    @Override
    public MonthName getMonthName() {
        if (this.getMonth() < 9) return MonthName.values()[getMonth()+3]; else return MonthName.values()[getMonth()-9];

    }

    @NonNull
    @Override
    public String toString() {
        return this.getYear()+ "-" + this.getMonth() + "-" + this.getDate();
    }

    // for now this is what I need.
    public static DateLocal now() {
        long jdn = (System.currentTimeMillis() / GeezDate.EPOCH_DAY) + com.tgk.Elet.geezDate.GeezDate.JDN_AT_EPOCH;
        return DateLocal.fromJdn(jdn);
    }

    public static DateLocal fromJdn(long jdn){
        int f = (int) (jdn + 1401 + (((4 * jdn + 274277) / 146097) * 3) / 4 - 38);
        int e = 4 * f + 3;
        int g = (e % 1461) / 4;
        int h = 5 * g + 2;
        //this.julianDay = jdn;
        int dayOfMonth = ((h % 153) / 5 + 1);
        int month = ((h / 153 + 2) % 12 + 1);
        int year = ((e / 1461) - 4716 + (12 + 2 - month) / 12);
        return new DateLocal(year,month,dayOfMonth,jdn);
    }

    public static DateLocal of(int year, int month, int dayOfMonth){
        return DateLocal.fromJdn(GeezDate.gregorianToJdn(year, month, dayOfMonth));
    }

}
