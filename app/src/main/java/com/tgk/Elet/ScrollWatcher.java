
package com.tgk.Elet;
/**
 * takes an index of a recyclerView as int and
 * Span of months the calendar has to show from
 * and calculates the year and month of displayed month in recyclerView
 * @author Tinsae ghilay
 * */
public class ScrollWatcher {
    int mid;
    int gy,position,mIndex,yStand;

    /**
     * @param position index of recyclerView
     * @param span amount of months calendar has to show
     */
    ScrollWatcher(int position,int span)  {
        this.gy=GeezDate.now().getYear();
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
}

