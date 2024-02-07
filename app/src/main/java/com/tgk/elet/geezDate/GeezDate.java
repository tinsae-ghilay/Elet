package com.tgk.elet.geezDate;

import com.tgk.elet.localDate.DateLocal;
import com.tgk.elet.localDate.MonthName;
import com.tgk.elet.temporal.BaseDate;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.TimeZone;

public class GeezDate extends BaseDate {
    static final long JDN_OFFSET = 1723856;
    public static final long JDN_AT_EPOCH = 2440588;
    public static final long EPOCH_DAY = 86400000;
    private final long julianDay;
    GeezDate(int year,
             int month,
             int dayOfMonth,
             long julianDay) {
        super(year,month,dayOfMonth);
        this.julianDay = julianDay;

    }

    @Override
    protected void validate(){
        super.validate();
        // then verify if representable in Geez date
        MonthName[] monthNames = MonthName.values();
        boolean validMonth=(this.getMonth() < 14);
        int maxValidDate = (getMonth()== 13)?(getYear() %4==3)? 6 : 5 : 30;
        boolean datesValid = getDate() <= maxValidDate;
        String exception="Given date " + getYear() + " - " + getMonth() + " - " + getDate() + " unrepresentable in Geez CalendarMonthView";
        String info=(validMonth)? monthNames[getMonth()-1]+" has between 1 and "+maxValidDate+" days for the year "+getYear()
                : "Months can only have 1 to 13 value";
        if ( !validMonth || !datesValid ){
            throw new Error(exception+" \n -> "+info);
        }

    }

    public long getJulianDay() {
        return julianDay;
    }

    @Override
    public MonthName getMonthName(){
        return MonthName.values()[getMonth()-1];
    }


    protected String getSaint(){ // every day has saints assigned to it
        return (getMonth() == 13) ? HolyMonth.pagumieSaints[getDate()] : HolyMonth.daySaint[getDate() - 1];
    }

    //Addition starts here
    /**
     * adds any given number of days to instantiated calendar object
     * @param days number of days to be added
     * @return GeezDate
     */
    public GeezDate plusDays(int days) {
        return jdnToGeezDate(julianDay+days);
    }
    /**
     * adds any given number of 'Geez CalendarMonthView years' to instantiated calendar object
     * @param years number of days to be added
     * @return GeezDate
     */
    public GeezDate plusYears(int years) {return jdnToGeezDate((long) (julianDay+((years*365.25))));}
    /**
     * adds any given number of 'Months' to instantiated calendar object
     * @param months number of days to be added
     * @param pagumieAsFullMonth
     * if true, Pagumie will be considered as full month else a month will be considered as just 30 says .
     * False will return same date within a year, but date will be offset when year changes.
     * true will return the same dayOfMonth every year. on Pagumie it will change on leap years
     * @return GeezDate
     */
    public GeezDate plusMonths(int months, boolean pagumieAsFullMonth) {
        if (pagumieAsFullMonth){
            int sum = this.getMonth()+months;
            int jumpedYears = (sum-1)/13;
            int targetYear = this.getYear()+jumpedYears;
            int targetMonth = (sum%13==0)? 13 : sum%13;
            int targetDayOfMonth = (targetMonth==13)? 1 : this.getDate();
            return of(targetYear,targetMonth,targetDayOfMonth);
        } else { return jdnToGeezDate(julianDay+(months* 30L));}
    }

    //convert GeezDate to LocalDate
    DateLocal toLocalDate(){
        int f = (int) (julianDay + 1401 + (((4 * julianDay + 274277) / 146097) * 3) / 4 - 38);
        int e = 4 * f + 3;
        int g = (e % 1461) / 4;
        int h = 5 * g + 2;
        int day = ((h% 153) / 5 + 1);
        int month = ((h / 153 + 2)% 12 + 1);
        int year = ((e / 1461) - 4716 + (12 +2 - month) / 12);
        return new DateLocal(year, month, day,julianDay);
    }
    /**
     * @return maximum number of days a month can have
     */
    private int getMaxDate() {
        return (this.getMonth()==13)?(this.getYear()%4==3)? 6 : 5 : 30;
    }


    /**
     * returns a GeezDate object from gregorian date
     * @param year gregorian year
     * @param month Gregorian month
     * @param dayOfMonth Gregorian day of the month(date)
     * @return GeezDate
     */
    public static GeezDate from(int year, int month, int dayOfMonth) {

        return jdnToGeezDate(gregorianToJdn(year, month, dayOfMonth));
    }

    /**
     *  returns a GeezDate object from Current epoch time in millis
     *  @return GeezDate
     */
    public static GeezDate now(){
        // TODO("consider accounting for offsetting from UTC")
        long  dayOfEpoch = System.currentTimeMillis()/(EPOCH_DAY);
        long jdn=dayOfEpoch+ JDN_AT_EPOCH;
        return jdnToGeezDate(jdn);
    }

    /**
     * @param year geez year
     * @param month geez month
     * @param dayOfMonth geez day of the month (date)
     * @return GeezDate
     */
    public static GeezDate of(int year, int month, int dayOfMonth)  {
        return jdnToGeezDate(geezToJdn(dayOfMonth,month,year));
    }


    /**
     * @author Dr.Berhanu Beyene &
     * @author Dr.Manfred Kudlek 's Formula (Algorithm) found
     * <a href="https://www.geez.org/Calendars"> here</a>
     * @param jdn Julian Day
     */
    private static GeezDate jdnToGeezDate(long jdn)  {
        long r = (jdn - JDN_OFFSET) % 1461;
        long n = r%365 + 365*(r/1460);
        int year = (int)(4 * ((jdn - JDN_OFFSET) / 1461) + r / 365 - r / 1460);
        int month = (int)(n/30 + 1);
        int dayOfMonth = (int)(n%30 + 1);
        //int dayOfYear = (month*30)+dayOfMonth;
        return new GeezDate(year, month, dayOfMonth,jdn);
    }

    /**
     * Based on a formula by Dr.Berhanu Beyene & Dr.Manfred Kudlek
     * @param day geez date
     * @param month geez month
     * @param year geez year
     * @return Long
     * Algorithm can be found <a href="https://www.geez.org/Calendars">here</a>
     */
    private static long geezToJdn(int day, int month, int year){
        return (( JDN_OFFSET + 365 )+ 365L * ( year - 1 )+( year/4 )+ 30L * month+ day - 31);
    }

    /**
     * formula from Wikipedia found <a href="https://en.wikipedia.org/wiki/Julian_day#Converting_Gregorian_calendar_date_to_Julian_Day_Number">here</a>
     * @param year Gregorian year
     * @param month Gregorian month
     * @param dayOfMonth Gregorian day of the month(date)
     * @return Long
     * possibly valid for dates after November 23, 4713 BC
     */
    public static long gregorianToJdn(int year, int month,int dayOfMonth){
        long a = (1461L * (year + 4800 + (month - 14)/12))/4;
        long b = (367 * (month - 2 - 12 * ((month - 14)/12)))/12;
        long c = (3 * ((year + 4900 + (month - 14)/12)/100))/4;
        return (a+ b - c + dayOfMonth - 32075);
    }
}


