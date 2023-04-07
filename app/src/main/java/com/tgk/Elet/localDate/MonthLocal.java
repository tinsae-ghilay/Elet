package com.tgk.Elet.localDate;

import com.tgk.Elet.temporal.Month;

public class MonthLocal extends Month {

    public MonthLocal(int year, int month) {
        super(year, month);
        populateDates();
    }

    @Override
    protected void validate() {
        super.validate();
        if (this.getMonth() > 12) throw new IllegalArgumentException("Month should not exceed 12");
    }
    public MonthLocal addMonths(int months){
        int plusYears = months/12;
        int plusMonths = months % 12;
        //int year = this.getYear()+plusYears;
        int m = this.getMonth()+plusMonths;
        if (m>12){
            plusYears++;
            m = m-12;
        }
        int year = getYear()+plusYears;
        return new MonthLocal(year, m);

    }

    private void populateDates() {
        DateLocal date = DateLocal.of(getYear(),getMonth(),1);
        date = date.plusDays(-getBahtiIndex());
        for(int i = 0; i < getDates().length; i++){
            getDates()[i] = date;
            date = date.plusDays(1);
        }
    }

    /**
     * Sakamoto's method from Wikipedia to calculate
     * dayOfWeek a dateOfMonth falls.
     * @return Int
     */
    @Override
    protected int getBahtiIndex() {
        int y = getYear();
        int m = getMonth();
        int d = 1;
        int[] t = {0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4};
        if ( getMonth() < 3 )
        {
            y -= 1;
        }
        return (y + y/4 - y/100 + y/400 + t[m-1] + d) % 7;
    }

    @Override
    public MonthName getMonthName() {
        if (this.getMonth() < 9) return MonthName.values()[getMonth()+3]; else return MonthName.values()[getMonth()-9];
        //return MonthName.values()[getMonth()-1];

    }
}
