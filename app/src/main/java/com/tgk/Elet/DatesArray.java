package com.tgk.Elet;

public class DatesArray {
    int month;
    int y;
    DatesArray(int month, int y){
        this.month=month;
        this.y=y;
    }

    // dates
    int[] datesInAMonth(){
        boolean is_pagumien=month==13;
        boolean isGeezLeapYear=(y % 4 == 3);
        int[] MonthDates;
        if (is_pagumien){
            if (isGeezLeapYear){
                MonthDates= new int[]{1,2,3,4,5,6};
            }else {
                MonthDates=new int[]{1,2,3,4,5};
            }
        }else{
            MonthDates=new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30};
        }
        return MonthDates;
    }
}

