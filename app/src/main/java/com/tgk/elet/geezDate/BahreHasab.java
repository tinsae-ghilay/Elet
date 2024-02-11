package com.tgk.elet.geezDate;

import java.util.ArrayList;

/**
 * Calculates Bahre hasab of a given Geez year.
 * @version 1.0.1
 * @author by Tinsae Ghilay.(tinsaekahsay@gmail.com)
 */
public class BahreHasab extends GeezYear {
    public static final int EPOCH = 5500;// Constants
    private final int negar, dayOfNegarit, wember, mebajaHamer;
    private final boolean startsFromBahti, mebajaHamerOverFlows;
    /**
     * @param year Geez year of which Bahre Hasab is to be calculated
     */
    public BahreHasab(int year){
        super(year);
        int TINTE_METQIE = 19;
        int MEDEB = getAmeteAlem() % TINTE_METQIE;
        this.wember = (MEDEB != 0) ? MEDEB - 1 : 18;
        this.negar =(wember * TINTE_METQIE) % 30;
        this.startsFromBahti = negar > 14;
        this.dayOfNegarit =(startsFromBahti) ? (getBahti() - 1 + negar) % 7
                : (getBahti() - 1 + 30 + negar) % 7;
        int TEWSAK = determineTewsak();
        this.mebajaHamerOverFlows = negar + TEWSAK > 30;
        this.mebajaHamer =(mebajaHamerOverFlows)?
                (negar + TEWSAK) % 30 : negar + TEWSAK;
    }

    /**
     * @return date of Abeqtie of the year
     */
    int getAbeqtie(){
        int TINTE_ABEQTIE = 11;
        return (wember * TINTE_ABEQTIE) % 30; }

    /**
     * @return Holiday object of Metqie / Negarit
     */
    HolyDay getMetqie(){
        int month = (startsFromBahti)? 1 : 2;
        return new HolyDay(getYear(),month, negar, 3);
    }

    /**
     * @param tewsak amount of days that span from Nineveh to the requested Holiday
     * @param holiday name of the required Holiday
     * @return a Holiday object of the day the requested Holiday falls.
     * @see HolyDay
     */
    HolyDay getHoliday(int tewsak,String holiday){
        return new HolyDay(getYear(), calculateMonth(tewsak),calculateDate(tewsak),holiday);
    }

    /**
     * Call this for specific Holidays.
     * @param tewsak amount of days that span from Nineveh to the requested Holiday
     * @param nameIndex index of the name of Holiday in an array
     * @return a Holiday object of the day the requested Holiday falls.
     * @see HolyDay
     */
    HolyDay getHoliday(int tewsak,int nameIndex){
        return new HolyDay(getYear(), calculateMonth(tewsak),calculateDate(tewsak),nameIndex);
    }

    /**
     * @return a Holiday Object of the date Nineveh starts on
     * @see HolyDay
     */
    HolyDay getNineveh(){ return new HolyDay(getYear(),getIndexOfNineveh(), mebajaHamer, 4); }

    /**
     * @return correcting the day of the week a Holiday must fall on
     */
    private int determineTewsak(){
        switch (dayOfNegarit) {
            case 0:
                return 6;
            case 1:
                return 5;
            case 2:
                return 4;
            case 3:
                return 3;
            case 4:
                return 2;
            case 5:
                return 8;
            default:
                return 7;
        }
    }

    /**
     * @return the month of year when Fast of Nineveh starts
     */
    private int getIndexOfNineveh(){ return (!startsFromBahti || mebajaHamerOverFlows)? 6 : 5; }

    /**
     * @param to_add = number of days from inveigh up to specific holiday.
     * @return  Month on which a holiday falls
     * */
    private int calculateMonth(int to_add){
        int jump= (mebajaHamer + to_add - 1) / 30;
        return getIndexOfNineveh() + jump;
    }

    protected HolyDay[] ofMonth(int month){
        ArrayList<HolyDay> ofMonth = new ArrayList<>();
        for (HolyDay holiday: ofYear()) {
            if (holiday.getMonth()==month){
                ofMonth.add(holiday);
            }
        }
        return ofMonth.toArray(new HolyDay[]{});
    }
    /**
     * @param tewsak = number of days from nineveh to specified holiday.
     * @return  date on which a holiday falls
     * */
    private int calculateDate(int tewsak){ return ((mebajaHamer + tewsak) % 30 == 0)? 30 : (mebajaHamer +tewsak) % 30; }

    /**
     * Call this if an array of all the Holidays calculated from this class is needed.
     * good for displaying them in a list
     * @return Array of variable Holidays of the year
     * @see HolyDay
     */
    public HolyDay[] ofYear(){
        int GREAT_FAST = 14;
        int MOUNT_OF_OLIVES = 41;
        int PALM_SUNDAY = 62;
        int GOOD_FRIDAY = 67;
        int EASTER = 69;
        int CONGRESS_OF_THE_SAGES = 93;
        int ASCENSION = 108;
        int PARACLETE = 118;
        int FAST_OF_APOSTLES = 119;
        int FAST_OF_SALVATION = 121;
        return new HolyDay[]{getMetqie(),getNineveh(),getHoliday(GREAT_FAST,5)
                ,getHoliday(MOUNT_OF_OLIVES,6),getHoliday(PALM_SUNDAY,7)
                ,getHoliday(GOOD_FRIDAY,8), getHoliday(EASTER, 9)
                ,getHoliday(CONGRESS_OF_THE_SAGES,10),getHoliday(ASCENSION,11)
                ,getHoliday(PARACLETE,12),getHoliday(FAST_OF_APOSTLES,13)
                ,getHoliday(FAST_OF_SALVATION,14)};
    }

    public static HolyDay[] getAnnual(int year){
        BahreHasab hassab = new BahreHasab(year);
        return hassab.ofYear();
    }
}