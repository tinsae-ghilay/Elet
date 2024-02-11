package com.tgk.elet.geezDate;

import com.tgk.elet.localDate.MonthName;
import com.tgk.elet.temporal.Month;

public class GeezMonth extends Month {
    public GeezMonth(int year, int month){

        super(year, month);
        populateDates();
    }

    @Override
    public MonthName getMonthName(){
        return MonthName.values()[ getMonth()-1 ];
    }

    // calculates number of days a month has
    protected int getSize(){ return  (!isPagumien())? 30 : (getYear() % 4 == 3)? 6 : 5;}

    // determine if Month is Pagumen
    protected boolean isPagumien(){ return getMonth() == 13; }

    // validate month
    @Override
    protected void validate() {
        super.validate();
        if (this.getMonth() > 13) throw new IllegalArgumentException("Geez month should not exceed 13");
    }
   
    // generats days of this month object
    private void populateDates() {
        GeezDate date = GeezDate.of(getYear(),getMonth(),1);
        date = date.plusDays(-getBahtiIndex());
        for(int i = 0; i < 42; i++){
            getDates()[i] = date;
            date = date.plusDays(1);
        }
    }
}
