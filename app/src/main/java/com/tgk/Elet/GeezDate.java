package com.tgk.Elet;


import androidx.annotation.NonNull;

public class GeezDate {
    static final long JDN_OFFSET = 1723856;
    static final long JDN_AT_EPOCH = 2440588;
    static final long EPOCH_DAY = 86400000;
    int year,month,dayOfMonth,dayOfYear;
    long julianDay;
    GeezDate(int year,
             int month,
             int dayOfMonth,
             int dayOfYear,
             long julianDay) {

        validate(dayOfMonth,month,year);
        this.year=year;
        this.month=month;
        this.dayOfMonth=dayOfMonth;
        this.dayOfYear=dayOfYear;
        this.julianDay=julianDay;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public int getDayOfYear() {
        return dayOfYear;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public long getJulianDay() {
        return julianDay;
    }

    /**
     * Validates all parameters to specs with in the Geez Date format
     * @param day int
     * @param month int
     * @param year int
     */
    private void validate(int day, int month, int year)  {
        Month[] months = Month.values();
        boolean validMonth=(month > 0 && month < 14);
        int maxValidDate = (month==13)?(year%4==3)? 6 : 5 : 30;
        boolean datesValid = day > 0 && day <= maxValidDate;
        String exception="Given date " + year + " - " + month + " - " + day + " unrepresentable in Geez Calendar";
        String info=(validMonth)? months[month-1]+" has between 1 and "+maxValidDate+" days for the year "+year
                : "Months can only have 1 to 13 value";
        if ( !validMonth || !datesValid ){
            throw new Error(exception+" \n -> "+info);
        }
    }

    //Addition starts here
    /**
     * adds any given number of days to instantiated calendar object
     * @param days number of days to be added
     * @return GeezDate
     */
    GeezDate plusDays(int days) {
        return jdnToGeezDate(this.julianDay+days);
    }
    /**
     * adds any given number of 'Geez Calendar years' to instantiated calendar object
     * @param years number of days to be added
     * @return GeezDate
     */
    GeezDate plusYears(int years) {return jdnToGeezDate((long) (this.julianDay+((years*365.25))));}
    /**
     * adds any given number of 'Months' to instantiated calendar object
     * @param months number of days to be added
     * @param pagumieAsFullMonth
     * if true, Pagumie will be considered as full month else a month will be considered as just 30 says .
     * False will return same date within a year, but date will be offset when year changes.
     * true will return the same dayOfMonth every year. on Pagumie it will change on leap years
     * @return GeezDate
     */
    GeezDate plusMonths(int months, boolean pagumieAsFullMonth) {
        if (pagumieAsFullMonth){
            int sum = this.month+months;
            int jumpedYears = (sum-1)/13;
            int targetYear = this.year+jumpedYears;
            int targetMonth = (sum%13==0)? 13 : sum%13;
            int targetDayOfMonth = (targetMonth==13)? 1 : dayOfMonth;
            return of(targetYear,targetMonth,targetDayOfMonth);
        } else { return jdnToGeezDate(this.julianDay+(months* 30L));}
    }

    //convert GeezDate to Date
    public DateLocal to(){
        int f = (int) (this.julianDay + 1401 + (((4 * julianDay + 274277) / 146097) * 3) / 4 - 38);
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
        return (this.month==13)?(this.year%4==3)? 6 : 5 : 30;
    }

    /**
     * calculates the day on which a date falls.
     * example: june 9, 2022 returns 4, i.e. Thursday
     * @return dayOfWeek 0 = sunday, 6 = saturday.
     */
    int  dayOfTheWeek() {
        return ((int)(this.julianDay+1)%7);
    }

    @NonNull
    @Override
    public String toString() {
        return year + "-" + month + "-" + dayOfMonth ;
    }

    /**
     * Formats GeezDate to a desired pattern
     * @return String
     */
    public String format(DateFormat format){
        String res;
        switch (format) {
            case DOTTED :
                res = this.dayOfMonth + "." + this.month + "." + this.year;
                break;
            case DOT_SPACED :
                res = this.dayOfMonth + ". " + this.month + ". " + this.year;
                break;
            case SLASHED :
                res = dayOfMonth + "/" + this.month + "/" + this.year;
                break;
            case SLASH_SPACED :
                res = this.dayOfMonth + "/ " + this.month + "/ " + this.year;
                break;
            case SPACED :
                res = this.dayOfMonth + "  " + this.month + "  " + this.year;
                break;
            case SPACED_WITH_COMA :
                res = this.dayOfMonth + ",  " + this.month + ",  " + this.year;
                break;
            case WITH_COMMA :
                res = this.dayOfMonth + "," + this.month + "," + this.year;
                break;
            case MONTH_NAMED :
                res = this.dayOfMonth + "  " + Month.values()[this.month - 1] + ", " + this.year;
                break;
            case MONTH_NAMED_ISO :
                res = Month.values()[this.month - 1] + " " + this.dayOfMonth + " " + this.year;
                break;
            case DAY_NAMED :
                res = DaysOfWeek.values()[dayOfTheWeek()] + ", " + Month.values()[month - 1] + " " + this.dayOfMonth + ", " + this.year;
                break;
            default :
            {
                res = this.toString();
                break;
            }
        }
        return res;
    }


    /**
     * returns a GeezDate object from gregorian date
     * @param year gregorian year
     * @param month Gregorian month
     * @param dayOfMonth Gregorian day of the month(date)
     * @return GeezDate
     */
    static GeezDate from(int year, int month,int dayOfMonth) {

    return jdnToGeezDate(gregorianToJdn(year, month, dayOfMonth));
    }

    /**
     *  returns a GeezDate object from Current epoch time in millis
     *  @return GeezDate
     */
    static GeezDate now(){
        long  dayOfEpoch =System.currentTimeMillis()/(EPOCH_DAY);
        long jdn=dayOfEpoch+ JDN_AT_EPOCH;
        return jdnToGeezDate(jdn);
    }

    /**
     * @param year geez year
     * @param month geez month
     * @param dayOfMonth geez day of the month (date)
     * @return GeezDate
     */
    static GeezDate of(int year, int month,int dayOfMonth)  {
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
        int dayOfYear = (month*30)+dayOfMonth;
        return new GeezDate(year, month, dayOfMonth, dayOfYear, jdn);
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
    private static long gregorianToJdn(int year, int month,int dayOfMonth){
        long a = (1461L * (year + 4800 + (month - 14)/12))/4;
        long b = (367 * (month - 2 - 12 * ((month - 14)/12)))/12;
        long c = (3 * ((year + 4900 + (month - 14)/12)/100))/4;
        return (a+ b - c + dayOfMonth - 32075);
    }
}
enum  DateFormat{
    SLASHED,SLASH_SPACED,DOTTED,
    DOT_SPACED,SPACED,WITH_COMMA,
    SPACED_WITH_COMA,MONTH_NAMED,
    MONTH_NAMED_ISO,DAY_NAMED,ISO
}


enum  Month{

    MESKEREM,TIKIMTI,HIDAR,TAHSAS
    ,TIRI,LEKATIT,MEGABIT,MIYAZYA
    ,GINBOT,SENE,HAMLE,NAHASE,PAGUMIE
}
enum  DaysOfWeek{
    SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY
}
class DateLocal{

    int year,month,dayOfMonth;
    long julianDay;
    DateLocal(int year,int month,int dayOfMonth,long julianDay){
        this.year=year;
        this.month=month;
        this.dayOfMonth=dayOfMonth;
        this.julianDay=julianDay;
    }
    DateLocal(long jdn){
        int f = (int) (jdn + 1401 + (((4 * jdn + 274277) / 146097) * 3) / 4 - 38);
        int e = 4 * f + 3;
        int g = (e % 1461) / 4;
        int h = 5 * g + 2;
        this.julianDay=jdn;
        this.dayOfMonth = ((h% 153) / 5 + 1);
        this.month = ((h / 153 + 2)% 12 + 1);
        this.year = ((e / 1461) - 4716 + (12 +2 - month) / 12);
    }

    @NonNull
    @Override
    public String toString() {
        return year+"-"+month+"-"+dayOfMonth;
    }
    // for now this is what I need.
    static DateLocal now(){
        long jdn=(System.currentTimeMillis()/GeezDate.EPOCH_DAY)+GeezDate.JDN_AT_EPOCH;
        return new DateLocal(jdn);
    }
}
