package com.tgk.Elet;

class FormatString {
    String string;
    FormatString(String string){
        this.string=string;
    }
    public String[] format(){
        return new String[] {string.substring(0,string.indexOf(",")),string.substring(string.indexOf(",")+1)};
    }
}
