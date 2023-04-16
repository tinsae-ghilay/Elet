package com.tgk.elet.temporal;

import androidx.annotation.NonNull;

public abstract class Year {

    private final int year;

    public Year(int year){
        this.year = year;
        validate();
    }

    public int getYear() {
        return year;
    }

    void validate(){
        if (year < 0) throw new IllegalArgumentException("Year should be greater than 0");
    }

    @NonNull
    @Override
    public String toString() {
        return ""+year;
    }
}
