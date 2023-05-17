package com.tgk.elet.temporal;

import androidx.annotation.NonNull;

import com.tgk.elet.geezDate.BahreHasab;
import com.tgk.elet.localDate.MonthName;

import java.util.Objects;

public abstract class Month {
    private int year;
    private int month;
    private MonthName monthName;
    private final BaseDate[] dates = new BaseDate[42];

    public Month(int year, int month){
        this.month = month;
        this.year = year;
        validate();
    }

    protected void validate(){
        if (this.month < 0 ) throw new IllegalArgumentException("Value of month should be greater than zero");
        if (this.year < 0) throw new IllegalArgumentException("Value of year should be greater than zero");
    }

    public int getBahtiIndex(){
        int ameteAlem = BahreHasab.EPOCH + year;
        return (((ameteAlem+(ameteAlem/4)-1)%7)+(getMonth())*30)%7;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void addYears(int years){
        this.year = this.year+years;
        this.month = 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Month month1 = (Month) o;
        return this.year == month1.year && this.month == month1.month;
    }

    public MonthName getMonthName(){
        return monthName;
    }

    public BaseDate[] getDates() {
        return dates;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month);
    }

    @NonNull
    @Override
    public String toString() {
        return getMonthName()+", "+getYear();
    }
}
