package com.tgk.Elet;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

class HolyDays {
    private static int[] ninweh, great_fast, mount_of_olives,
            palm_sunday, good_friday, easter, sages_congregation, ascension,
            paraclete, apostles_fast, salvation_fast;
    // fixed holiday dates
    public static Integer[] natHolyDates(int month, int y){// common National Holy day dates Ethio-Eri-Tigray
        Integer[] hd;// ዕለታት ናይ በዓላት ኣብ ነብሲወከፍ ወርሒ
        switch (month) {
            case 1: {
                hd = new Integer[]{1, 17};//ሓድሽ ዓመትን መስቀልን
                break;
            }
            case 3:{
                hd=new Integer[]{15,6,21};
                break;
            }
            case 4: {
                int x_mass;
                int lidet;
                int new_year;
                int gehad;
                if (y%4==3){//Try to pass this as a variable?.
                    x_mass=15;
                    new_year=22;
                    lidet=28;
                    gehad=27;
                }else{
                    x_mass=16;
                    new_year=23;
                    lidet=29;
                    gehad=28;
                }
                hd = new Integer[]{3,x_mass,new_year,gehad,lidet};//ልደት!
                break;
            }
            case 5: {
                hd = new Integer[]{6,10,11,12,21}; //ጥምቀት
                break;
            }
            case 6: {
                hd = new Integer[]{16}; //holy days to be calculated.
                break;
            }
                /*case 7: {
                    hd = new Integer[]{};
                    break;
                }*/
            case 8: {
                hd = new Integer[]{23};//may day
                break;
            }
            case 9: {
                hd = new Integer[]{1};
                break;
            }
            case 10:
            case 11: {
                hd = new Integer[]{};
                break;
            }
            case 12:{
                hd=new Integer[]{1,7,13,16};
                break;
            }
            case 13:{
                hd=new Integer[]{3};
                break;
            }
            default:
                hd = new Integer[]{};
                break;
        }
        return hd;

    }
    // fixed holiday names
    public static int[] natHolyDayIndex(int month){ // National Holy day names Eri
        int[] holyDays;// ኣስማት በዓላት ነብሲወከፍ ወርሒ
        switch (month) {
            case 1: {
                holyDays =new int[] {0, 1};
                break;
            }
            case 3:{
                holyDays =new int[] {2,14,15};
                break;
            }
            case 4: {
                holyDays=new int[]{16,3,4,13,5};
                break;
            }
            case 5: {
                holyDays=new int[]{6,13,7,8,17};
                break;
            }
            case 6: {
                holyDays = new int[]{18}; //holy days to be calculated.
                break;
            }
            case 8: {
                holyDays=new int[]{9};
                break;
            }
            case 9: {
                holyDays=new int[]{19};
                break;
            }
            case 10:
            case 11: {
                holyDays=new int[]{};
                break;
            }
            case 12:{
                holyDays=new int[]{10,20,11,12};
                break;
            }
            case 13:{
                holyDays=new int[]{21};
                break;
            }
            default:
                holyDays=new int[]{};
                break;
        }
        return holyDays;
    }
    // updates all variable holidays for each calendar month.
    private static void updateVariableHolyDays(int y){
        BahreHasab hasab=new BahreHasab(y);
        ninweh=hasab.getNinweh();
        great_fast= hasab.getTsome40();
        mount_of_olives= hasab.getDebreZeyt();
        palm_sunday= hasab.getHosaena();
        good_friday= hasab.getArbiSqlet();
        easter= hasab.getFasika();
        sages_congregation= hasab.getRkbeKahnat();
        ascension= hasab.getErget();
        paraclete= hasab.getParaklitos();
        apostles_fast= hasab.getTsomeHawaryat();
        salvation_fast=hasab.getTsomeDhnet();
    }
    // Holiday names updated to include variable and fixed holidays .
    static ArrayList <String> updatedHolidays(Context context, int gm, int y,boolean eritrean,boolean tigraian){
        String[] holy_days = context.getResources().getStringArray(R.array.holy_days);
        updateVariableHolyDays(y);
        String[] national_holidays=context.getResources().getStringArray(R.array.national_holidays);
        int[] array=natHolyDayIndex(gm);
        ArrayList<String> list=new ArrayList<>();
        for (int j : array) {
            list.add(national_holidays[j]);
        }
        if (eritrean){
            int[][] hd=eritreanFestivals(y);
            String[] fests=context.getResources().getStringArray(R.array.eritrean);
            for (int i=0;i< hd.length;i++) {
                if (hd[i][0] == gm) {
                    list.add(fests[i]);
                }
            }
        }
        if (tigraian){
            int[][] hd=tigrayFestivals();
            String[] fests=context.getResources().getStringArray(R.array.tigraian);
            for (int i=0;i< hd.length;i++) {
                if (hd[i][0] == gm) {
                    list.add(fests[i]);
                }
            }
        }
        if (ninweh[0]==gm){
            list.add(holy_days[ninweh[2]]);
        }if (great_fast[0]==gm){
            list.add(holy_days[great_fast[2]]);
        }
        if (mount_of_olives[0]==gm){
            list.add(holy_days[mount_of_olives[2]]);
        }
        if (palm_sunday[0]==gm){
            list.add(holy_days[palm_sunday[2]]);
        }
        if (good_friday[0]==gm){
            list.add(holy_days[good_friday[2]]);
        }
        if (easter[0]==gm){
            list.add(holy_days[easter[2]]);
        }
        if (sages_congregation[0]==gm){
            list.add(holy_days[sages_congregation[2]]);
        }
        if (ascension[0]==gm){
            list.add(holy_days[ascension[2]]);
        }
        if (paraclete[0]==gm){
            list.add(holy_days[paraclete[2]]);
        }
        if (apostles_fast[0]==gm){
            list.add(holy_days[apostles_fast[2]]);
        }
        if (salvation_fast[0]==gm){
            list.add(holy_days[salvation_fast[2]]);
        }
        return list;

    }
    // Holiday dates updated to include variable and fixed holidays
    static ArrayList<Integer> updatedHolyDates(int gm, int gy, int y,boolean eritrean,boolean tigraian){
        updateVariableHolyDays(gy);
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(natHolyDates(gm,y)));
        if (eritrean){
            int[][] hd=eritreanFestivals(y);
            for (int[] ints : hd) {
                if (ints[0] == gm) {
                    list.add(ints[1]);
                }
            }
        }
        if (tigraian){
            int[][] hd=tigrayFestivals();
            for (int[] ints : hd) {
                if (ints[0] == gm) {
                    list.add(ints[1]);
                }
            }
        }
        if (ninweh[0]==gm){
            list.add(ninweh[1]);
        }if (great_fast[0]==gm){
            list.add(great_fast[1]);
        }
        if (mount_of_olives[0]==gm){
            list.add(mount_of_olives[1]);
        }
        if (palm_sunday[0]==gm){
            list.add(palm_sunday[1]);
        }
        if (good_friday[0]==gm){
            list.add(good_friday[1]);
        }
        if (easter[0]==gm){
            list.add(easter[1]);
        }
        if (sages_congregation[0]==gm){
            list.add(sages_congregation[1]);
        }
        if (ascension[0]==gm){
            list.add(ascension[1]);
        }
        if (paraclete[0]==gm){
            list.add(paraclete[1]);
        }
        if (apostles_fast[0]==gm){
            list.add(apostles_fast[1]);
        }
        if (salvation_fast[0]==gm){
            list.add(salvation_fast[1]);
        }

        return list;
    }
    static int[][] eritreanFestivals(int y){
        if (isLeapYear(y)){
            return new int[][]{{6,2},{6,29},{9,16},{10,13},{12,26}};
        }else {
            return new int[][]{{6,3},{6,29},{9,16},{10,13},{12,26}};
        }
    }

    private static boolean isLeapYear(int year){
        if (year % 4 != 0) {
            return false;
        } else if (year % 400 == 0) {
            return true;
        } else return year % 100 != 0;
    }

    static  int[][] tigrayFestivals(){
        return new int[][]{{6,11},{6,23},{8,27},{9,20}};

    }
}
