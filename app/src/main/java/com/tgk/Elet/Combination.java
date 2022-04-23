package com.tgk.Elet;

import androidx.annotation.NonNull;

import java.util.Comparator;

public class Combination{
    protected int a;
    private final String string;
    Combination(int a, String string){
        this.a=a;
        this.string=string;
    }
    @NonNull
    public String toString(){
        return a+","+string;
    }
}
class SortByInt implements Comparator<Combination> {
    // Used for sorting in ascending order of
    // roll number
    @Override
    public int compare(Combination o1, Combination o2) {
        return o1.a -o2.a;
    }
}
