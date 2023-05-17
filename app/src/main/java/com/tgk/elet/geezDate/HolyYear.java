package com.tgk.elet.geezDate;

import android.os.Build;

import com.tgk.elet.common.NationalHolidays;
import com.tgk.elet.common.Preferences;

import java.util.*;

// TODO his class should not extend GeezYear. will make it a singleton
// and possibly migrate to Kotlin
public class HolyYear extends GeezYear{

    //boolean showEritrean = false, showTigraian = false;

    public  HolyYear(int year) {
        super(year);
    }

    public static HolyDay[] ofMonth(GeezMonth geezMonth) {// holidays with fixed dates except Leap years
        int month = geezMonth.getMonth();
        int year = geezMonth.getYear();

        HolyDay[] holyDays;
        ArrayList<HolyDay> holyDaysList = new ArrayList<>(List.of(new BahreHasab(year).ofMonth(month)));
        switch (month) {

            case 1 : holyDays = new HolyDay[]{ // September
                    new HolyDay(year, month, 1, 15), new HolyDay(year, month, 17, 16)};
                break;
            case 3 : holyDays = new HolyDay[]{ // November
                    new HolyDay(year, month, 15, 17)
                    , new HolyDay(year, month, 6, 29)
                    , new HolyDay(year, month, 21, 30)};
                break;
            case 4 : { // December
                int x_mass = 16, new_year = 23, gehad = 28, lidet = 29;
                if (year % 4 == 0) {
                    x_mass = 15;
                    new_year = 22;
                    gehad = 27;
                    lidet = 28;
                }

                holyDays = new HolyDay[]{
                        new HolyDay(year, month, 3, 31)
                        , new HolyDay(year, month, x_mass, 18)
                        , new HolyDay(year, month, new_year, 19)
                        , new HolyDay(year, month, gehad, 28)
                        , new HolyDay(year, month, lidet, 20)};
                break;
            }

            case 5 : holyDays = new HolyDay[]{ // January
                    new HolyDay(year, month, 6, 21)
                    , new HolyDay(year, month, 10, 28)
                    , new HolyDay(year, month, 11, 22)
                    , new HolyDay(year, month, 12, 23)
                    , new HolyDay(year, month, 21, 32)};
                break;

            case 6 : holyDays = new HolyDay[]{ //February
                    new HolyDay(year, month, 16, 33)};
                break;

            case 8 : { // April
                int mothersDay = mothersDay(year);
                if (mothersDay == -1){// Mothers day falls on April?
                    holyDays = new HolyDay[]{
                            new HolyDay(year, month, 23, 24)
                            ,new HolyDay(year,month,30,37)
                            ,new HolyDay(year,month,30,38)
                    };
                }else{
                    holyDays = new HolyDay[]{ // April
                            new HolyDay(year, month, 23, 24)
                            ,new HolyDay(year,month,30,37)
                    };
                }
            }
                break;

            case 9 :{ // May
                int mothersDay = mothersDay(year);
                if (mothersDay!=-1){ // Mothers day falls on May?
                    holyDays = new HolyDay[]{
                            new HolyDay(year, month, 1, 34)
                            ,new HolyDay(year,month,mothersDay,38)
                    };
                }else{
                    holyDays = new HolyDay[]{ // May
                            new HolyDay(year, month, 1, 34)
                    };
                }
                break;
            }

            case 12 : holyDays = new HolyDay[]{ // August
                    new HolyDay(year, month, 1, 25)
                    , new HolyDay(year, month, 7, 35)
                    , new HolyDay(year, month, 13, 26)
                    , new HolyDay(year, month, 16, 27)};
                break;

            case 13 : holyDays = new HolyDay[]{ // Pagumien
                    new HolyDay(year, month, 3, 36)};
                break;

            default : holyDays = new HolyDay[0];
        }
        holyDaysList.addAll(List.of(holyDays));
        NationalHolidays nationals = NationalHolidays.INSTANCE;
        if(Preferences.INSTANCE.getShowEritrean()) holyDaysList.addAll(List.of(nationals.getForEri(year,month)));
        if(Preferences.INSTANCE.getShowTigraian()) holyDaysList.addAll(List.of(nationals.getForTigray(year,month)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holyDaysList.sort(Comparator.naturalOrder());
        }

        return holyDaysList.toArray(new HolyDay[0]);
    }

    public static HolyDay[] ofYear(int year,boolean showEritrean, boolean showTigraian){
        ArrayList<HolyDay> annual = new ArrayList<>();
        for (int i = 1; i < 14; i++){
            HolyDay[] monthly = ofMonth(new GeezMonth(year,i));
            annual.addAll(List.of(monthly));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            annual.sort(Comparator.naturalOrder());
        }
        return annual.toArray(new HolyDay[]{});
    }

    static int mothersDay(int year) {
        GeezMonth month = new GeezMonth(year,9);
        int index = month.getBahtiIndex();
        if (index == 1) {
            return -1;
        } else if (index == 0) {
            return month.getDates()[0].getDate();
        } else {
            return month.getDates()[7].getDate();
        }
    }
}
