/*
 * takes an index which will be divided by 13(months in a geez calendar year)
 * reminder is the month index (i%13)
 * dividend is the year difference (i/13)
 * */
package com.tgk.Elet;

public class ScrollWatcher {
    int mid;
    int gy,position,mIndex,yStand;
    ScrollWatcher(int position,int span){
        this.gy=new CurrentDate().getCurrentGeezYear();
        this.position=position;
        this.mIndex=position%13;
        this.yStand=position/13;
        this.mid=(span/13)/2;
    }
    // calendar table's translated month.
    int getMonth(){
        return mIndex+1;
    }
    // calendar table's geez year.
    int getGeezYear(){
        int dif=yStand-mid;
        return gy+dif;
    }
    // calendar months gregorian year.
    /*int getGregorianYear(){
        return new ConvertDate(1,mIndex+1,getGeezYear(),"Geez").ferenji_year;
    }*/
}

