package com.tgk.Elet;

import android.content.Context;


import java.util.ArrayList;
/**
 * Calculates Bahre hasab of a given Geez year.
 * geez year must be passed to constructor.
 * each 'get' method returns an array of
 *      index @0,   month the holiday occurs
 *      index @1,   Date of the holiday this year
 *      index @2,   name of the holiday --- ps. this can be changed to whatever index
 *                  you put the name of holiday in your array.
 * @author by Tinsae Ghilay.(tinsaekahsay@gmail.com)
 */
public class BahreHasab {
    // integer variables-> dates and month indexes
    int gy,AMETE_ALEM, METENE_RABIIT,BAHTI_MESKEREM,MEDEB,WEMBER,WENGELAWI,
            ABEQTIE, NEGARIT, DAY_OF_NEGARIT,TEWSAKE_ELET,MEBAJA_HAMER;
    // Holiday dates .
    int NINVEH,XOM_40,DEBRE_ZEYT,HOSAENA,ARBI_SQLET,FASIKA,RKBE_KAHNAT,ERGET,
            PARAKLITOS,XOM_HAWARYAT,XOM_DHNET;
    // holiday months
    int MONTH_NEGARIT,MONTH_NINWEH,MONTH_XOM_40,MONTH_DEBRE_ZEYT,MONTH_HOSAENA,MONTH_ARBI_SQLET,
            MONTH_FASIKA,MONTH_RKBE_KAHNAT,MONTH_ERGET,MONTH_PARAKLITOS,MONTH_XOM_HAWARYAT,
            MONTH_XOM_DHNET;
    boolean STARTS_FROM_BAHTI,MEBAJA_HAMER_IS_MORE;
    BahreHasab(int gy){
        this.gy=gy;
        // ዓመተ ዓለም።
        calculateAmeteAlem();
        // ርብዒት።
        calculateWengelawi();
        // setting day of new year.
        calculateFoundations();
        // setting date-month of Negarit.
        calculateNegarit();
        // ተውሳኽ ዕለተ መጥቅዕ።
        this.TEWSAKE_ELET= determineTewsak();
        // መባጃ ሓመር።
        calculateMebajaHamer();
        //holidays.
        calculateNinweh();// or here----
        //Great fast(40 days fast)
        calculateGreatFast();
        //Mount of Olives
        // ደብረ-ዘይት ዝውዕለሉ ዕለትን ወርሕን።
        calculateDebreZeyt();
        // Palm sunday
        // ዕለትን ወርሕን ሆሳዕና።
        calculatePalmSunday();
        //Good friday
        // ዕለትን ወርሕን ዓርቢ ስቕለት።
        calculateGoodFriday();
        //Easter
        // ዕለትን ወርሕን ፋሲካ።
        calculateEaster();
        // Congregation of the sages
        // ዕለትን ወርሕን ርክበ ካህናት።
        calculateCongregationOfTheSages();
        //Ascension
        // ዕለትን ወርሕን ዕርገት።
        calculateAscension();
        //Paraclete
        // ዕለትን ወርሕን ጵራቅሊጦስ።
        calculateParaclete();
        //Fast of Apostles
        // ዕለትን ወርሕን ጾመ ሓዋርያት።
        calculateFastOfApostles();
        //Fast of salvation
        // ዕለትን ወርሕን ጾመ ድሕነት።
        calculateFastOfSalvation();
    }
    // setting variables.
    void calculateAmeteAlem() {
        int YEAR_OF_CREATION = 5500;
        this.AMETE_ALEM= YEAR_OF_CREATION +gy;
    }
    void calculateWengelawi(){
        this.METENE_RABIIT =AMETE_ALEM/4;
        this.WENGELAWI=AMETE_ALEM%4;
    }
    void calculateFoundations(){
        this.BAHTI_MESKEREM=(AMETE_ALEM + METENE_RABIIT)%7;
        int TINTE_METQIE = 19;
        this.MEDEB=AMETE_ALEM% TINTE_METQIE;
        // ወምበር።
        this.WEMBER=(MEDEB!=0)?MEDEB-1:18;
        int TINTE_ABEQTIE = 11;
        this.ABEQTIE=(WEMBER* TINTE_ABEQTIE)%30;
        this.NEGARIT =(WEMBER* TINTE_METQIE)%30;
    }
    void calculateNegarit(){
        this.STARTS_FROM_BAHTI= NEGARIT >14;
        this.MONTH_NEGARIT=(STARTS_FROM_BAHTI)?1:2;
        this.DAY_OF_NEGARIT =(STARTS_FROM_BAHTI)?(BAHTI_MESKEREM-1+ NEGARIT)%7
                :(BAHTI_MESKEREM-1+30+ NEGARIT)%7;
    }
    void calculateMebajaHamer(){
        this.MEBAJA_HAMER_IS_MORE= NEGARIT +TEWSAKE_ELET>30;
        this.MEBAJA_HAMER=(MEBAJA_HAMER_IS_MORE)?
                (NEGARIT +TEWSAKE_ELET)%30: NEGARIT +TEWSAKE_ELET;
    }
    void calculateNinweh(){
        this.NINVEH=MEBAJA_HAMER;
        this.MONTH_NINWEH=getIndexOfNinweh();
    }
    // from here on methods are similar. may be loop?
    void calculateGreatFast(){
        int GREAT_FAST = 14;
        this.XOM_40=calculateDate(GREAT_FAST);
        this.MONTH_XOM_40=getIndex(GREAT_FAST);
    }
    void calculateDebreZeyt(){
        int MOUNT_OF_OLIVES = 41;
        this.DEBRE_ZEYT=calculateDate(MOUNT_OF_OLIVES);
        this.MONTH_DEBRE_ZEYT=getIndex(MOUNT_OF_OLIVES);
    }
    void calculatePalmSunday(){
        int PALM_SUNDAY = 62;
        this.HOSAENA=calculateDate(PALM_SUNDAY);
        this.MONTH_HOSAENA=getIndex(PALM_SUNDAY);
    }
    void calculateGoodFriday(){
        int GOOD_FRIDAY = 67;
        this.ARBI_SQLET=calculateDate(GOOD_FRIDAY);
        this.MONTH_ARBI_SQLET=getIndex(GOOD_FRIDAY);
    }
    void calculateEaster(){
        int EASTER = 69;
        this.FASIKA=calculateDate(EASTER);
        this.MONTH_FASIKA=getIndex(EASTER);
    }
    void calculateCongregationOfTheSages(){
        int CONGRESS_OF_THE_SAGES = 93;
        this.RKBE_KAHNAT=calculateDate(CONGRESS_OF_THE_SAGES);
        this.MONTH_RKBE_KAHNAT=getIndex(CONGRESS_OF_THE_SAGES);
    }
    void calculateAscension(){
        int ASCENSION = 108;
        this.ERGET=calculateDate(ASCENSION);
        this.MONTH_ERGET=getIndex(ASCENSION);
    }
    void calculateParaclete(){
        int PARACLETE = 118;
        this.PARAKLITOS=calculateDate(PARACLETE);
        this.MONTH_PARAKLITOS=getIndex(PARACLETE);
    }
    void calculateFastOfApostles(){
        int FAST_OF_APOSTLES = 119;
        this.XOM_HAWARYAT=calculateDate(FAST_OF_APOSTLES);
        this.MONTH_XOM_HAWARYAT=getIndex(FAST_OF_APOSTLES);
    }
    void calculateFastOfSalvation(){
        int FAST_OF_SALVATION = 121;
        this.XOM_DHNET=calculateDate(FAST_OF_SALVATION);
        this.MONTH_XOM_DHNET=getIndex(FAST_OF_SALVATION);
    }
    // ተውሳኽ ዕለተ መጥቅዕ።
    int determineTewsak(){
        switch (DAY_OF_NEGARIT){
            case 0:return 6;
            case 1:return 5;
            case 2:return 4;
            case 3:return 3;
            case 4:return 2;
            case 5:return 8;
            default:return 7;
        }
    }
    // to determine the month Ninweh falls on.
    int getIndexOfNinweh(){
        return (!STARTS_FROM_BAHTI||MEBAJA_HAMER_IS_MORE)?6:5;
    }


    /** returns the month of the holiday for a given tewsak
     * determines the month interval of the holiday from ninveh.
     * @param to_add = number of days from ninveh up to specific holiday.
     * */
    int getIndex(int to_add){
        int jump= (NINVEH+to_add-1)/30;
        return getIndexOfNinweh()+jump;
    }


    /** returns the day of the holiday for a given tewsak
     * by adding its interval from Ninveh.
     * @param tewsak elet= number of days from ninveh to specified holiday.
     * */
    int calculateDate(int tewsak){
        return ((NINVEH+tewsak)%30==0)?30:(NINVEH+tewsak)%30;
    }

    /** calculates all variable holidays of the geez year and
     * @return baalat as a 2D array.
     * @param context if we want to get translatable strings from xml in android.
     * we can just declare arrays 'months' and 'days' within method
     * or pass them as parameters if need be.
     */
    ArrayList<String> getArrayOfHolidays(Context context){
        ArrayList<String> baalat= new ArrayList<>();
        // Array of Months starting from September including Pagumien.
        // i.e Geez way of counting months.
        String[] months= context.getResources().getStringArray(R.array.monthsList);
        // days of the week starting from Sunday.
        String[] days=context.getResources().getStringArray(R.array.week_days);
        // Evangelists in the order of John,mathew,mark,luke.
        String[] apostles=context.getResources().getStringArray(R.array.evangelists);
        baalat.add(apostles[WENGELAWI]);
        baalat.add(days[(BAHTI_MESKEREM+1)%7]);
        baalat.add(""+ABEQTIE);
        baalat.add(NEGARIT +", "+months[MONTH_NEGARIT-1]);
        baalat.add(NINVEH+", "+months[MONTH_NINWEH-1]);
        baalat.add(XOM_40+", "+months[MONTH_XOM_40-1]);
        baalat.add(DEBRE_ZEYT+", "+months[MONTH_DEBRE_ZEYT-1]);
        baalat.add(HOSAENA+", "+months[MONTH_HOSAENA-1]);
        baalat.add(ARBI_SQLET+", "+months[MONTH_ARBI_SQLET-1]);
        baalat.add(FASIKA+", "+months[MONTH_FASIKA-1]);
        baalat.add(RKBE_KAHNAT+", "+months[MONTH_RKBE_KAHNAT-1]);
        baalat.add(ERGET+", "+months[MONTH_ERGET-1]);
        baalat.add(PARAKLITOS+", "+months[MONTH_PARAKLITOS-1]);
        baalat.add(XOM_HAWARYAT+", "+months[MONTH_XOM_HAWARYAT-1]);
        baalat.add(XOM_DHNET+", "+months[MONTH_XOM_DHNET-1]);
        return baalat;
    }
    // ዕለትን ወርሕን ምጅማር ጾመ ነነዌ።
    int[] getNinweh(){
        return new int[]{MONTH_NINWEH,NINVEH,4};
    }
    // ዕለትን ወርሕን ምጅማር ጾመ-ርብዓ (ዓቢ ጾም)
    int[] getTsome40(){
        return new int[]{MONTH_XOM_40,XOM_40,5};
    }
    // ደብረ-ዘይት ዝውዕለሉ ዕለትን ወርሕን።
    int[] getDebreZeyt(){
        return new int[]{MONTH_DEBRE_ZEYT,DEBRE_ZEYT,6};
    }
    // ዕለትን ወርሕን ሆሳዕና።
    int[] getHosaena(){
        return new int[]{MONTH_HOSAENA,HOSAENA,7};
    }
    // ዕለትን ወርሕን ዓርቢ ስቕለት።
    int[] getArbiSqlet(){
        return new int[]{MONTH_ARBI_SQLET,ARBI_SQLET,8};
    }
    // ዕለትን ወርሕን ፋሲካ።
    int[] getFasika(){
        return new int[]{MONTH_FASIKA,FASIKA,9};
    }
    // ዕለትን ወርሕን ርክበ ካህናት።
    int[] getRkbeKahnat(){
        return new int[] {MONTH_RKBE_KAHNAT,RKBE_KAHNAT,10};
    }
    // ዕለትን ወርሕን ዕርገት።
    int[] getErget(){
        return new int[] {MONTH_ERGET,ERGET,11};
    }
    // ዕለትን ወርሕን ጵራቅሊጦስ።
    int[] getParaklitos() {
        return new int[] {MONTH_PARAKLITOS,PARAKLITOS,12};
    }
    // ዕለትን ወርሕን ጾመ ሓዋርያት።
    int[] getTsomeHawaryat(){
        return new int[] {MONTH_XOM_HAWARYAT,XOM_HAWARYAT,13};
    }
    // ዕለትን ወርሕን ጾመ ድሕነት።
    int[] getTsomeDhnet(){
        return new int[] {MONTH_XOM_DHNET,XOM_DHNET,14};
    }
    //TODO research calculating Islamic Holidays.
}

