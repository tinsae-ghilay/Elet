package com.tgk.elet.geezDate;

import android.os.Build;

import java.util.*;

public class HolyYear extends GeezYear{

    public  HolyYear(int year) {
        super(year);
    }

    public static HolyDay[] ofMonth(GeezMonth geezMonth) {// holidays with fixed dates except Leap years
        int month = geezMonth.getMonth();
        int year = geezMonth.getYear();

        HolyDay[] holyDays;
        ArrayList<HolyDay> holyDaysList = new ArrayList<>(List.of(new BahreHasab(year).ofMonth(month)));
        switch (month) {

            case 1 : holyDays = new HolyDay[]{
                    new HolyDay(year, month, 1, 15), new HolyDay(year, month, 17, 16)};
                break;
            case 3 : holyDays = new HolyDay[]{
                    new HolyDay(year, month, 15, 17)
                    , new HolyDay(year, month, 6, 29)
                    , new HolyDay(year, month, 21, 30)};
                break;
            case 4 : {
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

            case 5 : holyDays = new HolyDay[]{
                    new HolyDay(year, month, 6, 21)
                    , new HolyDay(year, month, 10, 28)
                    , new HolyDay(year, month, 11, 22)
                    , new HolyDay(year, month, 12, 23)
                    , new HolyDay(year, month, 21, 32)};
                break;

            case 6 : holyDays = new HolyDay[]{new HolyDay(year, month, 16, 33)};
                break;

            case 8 : holyDays = new HolyDay[]{new HolyDay(year, month, 23, 24)};
                break;

            case 9 : holyDays = new HolyDay[]{new HolyDay(year, month, 1, 34)};
                break;

            case 12 : holyDays = new HolyDay[]{
                    new HolyDay(year, month, 1, 25)
                    , new HolyDay(year, month, 7, 35)
                    , new HolyDay(year, month, 13, 26)
                    , new HolyDay(year, month, 16, 27)};
                break;

            case 13 : holyDays = new HolyDay[]{new HolyDay(year, month, 3, 36)};
                break;

            default : holyDays = new HolyDay[0];
        }
        holyDaysList.addAll(List.of(holyDays));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holyDaysList.sort(Comparator.naturalOrder());
        }

        return holyDaysList.toArray(new HolyDay[0]);
    }

    public static HolyDay[] ofYear(int year){
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
}
