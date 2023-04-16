package com.tgk.elet.geezDate;

import androidx.annotation.NonNull;

import com.tgk.elet.temporal.Year;

abstract class GeezYear extends Year {
    private final int  bahti, ameteAlem;
    GeezYear(int year){
        super(year);
        this.ameteAlem = BahreHasab.EPOCH +year;
        this.bahti = (ameteAlem + ( ameteAlem / 4 )) % 7;
    }
    boolean isLeapYear(){ return getYear() % 4 == 3; }

    @Override
    public int getYear() {
        return super.getYear();
    }

    public int getAmeteAlem() {
        return ameteAlem;
    }

    public int getBahti() {
        return bahti;
    }

    /**
     * @return the era this year is of
     */
    public Apostles isEraOf(){ return Apostles.values()[ indexOfEra() ]; }

    public int indexOfEra() {
        return ameteAlem%4;
    }
    /**
     * @return The day of the week the year starts
     */
    /*Day getBahtiDay(){ return Day.values()[ ( bahti + 1 ) % 7 ]; }*/

    @NonNull
    @Override
    public String toString(){
        return "ዘመነ : "+isEraOf()+" -> ሊፕ ዪር"+ (isLeapYear()? " ከአ እዩ። " : " ግን አይኮነን።\nባሕቲ መስከረም ከአ : "+getBahti()+ "ይውዕል።");
    }
}
