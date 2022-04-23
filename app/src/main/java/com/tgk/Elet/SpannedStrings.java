package com.tgk.Elet;

import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.RelativeSizeSpan;

public class SpannedStrings {
    int a,b;
    SpannableString string;
    SpannedStrings(int a,int b){
        this.a=a;
        this.b=b;
        SpanString();
    }
    void SpanString(){
        SpannableStringBuilder builder=new SpannableStringBuilder();
        SpannableString date=new SpannableString(b+"\n");
        SpannableString date_geez=new SpannableString(a+"");
        date.setSpan(new RelativeSizeSpan(0.65f),0,date.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        date.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE), 0,
                date.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(date);
        builder.append(date_geez);
        this.string= SpannableString.valueOf(builder);
    }

    public SpannableString getString() {
        return string;
    }
}