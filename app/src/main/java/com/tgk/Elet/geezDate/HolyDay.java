package com.tgk.Elet.geezDate;

import androidx.annotation.NonNull;

import com.tgk.Elet.localDate.MonthName;
import com.tgk.Elet.temporal.BaseDate;

public class HolyDay extends BaseDate {

    private final String name;
    private int nameIndex;
    
    // +15
    /**
     * this will be retrieved from String resources and may be put in to a singleton
     */
    private final String[] holiday_names = new String[]{"ወንጌላዊ", "ባሕቲ መስከረም", "አበቅቴ", "መጥቅዕ ", "ጾመ ነነዌ"
            , "ምጅማር ጾመ-ኣርባዓ", "ደብረ ዘይት", "ሆሳዕና", "ዓርቢ ስቕለት", "ፋሲካ", "ርክበ ካህናት", "ዕርገት", "ጰራቅሊጦስ", "ጾመ ሃዋርያት"
            , "ጾመ ድሕነት", "ሓድሽ ዓመት (ቅዱስ ዮሓንስ)", "መስቀል", "ጾመ ነብያት", "ልደት ፈረንጂ", "ሓድሽ ዓመት ፈረንጂ", "ልደት", "ግዝረት"
            , "ጥምቀት", "ቃና ዘ ገሊላ", "መዓልቲ ሰራሕተኛታት", "ጾመ ፍልሰታ", "ደብረ ታቦር", "ፍልሰታ (ኣሸንዳ / ማርያ )", "ጾመ ገሃድ", "ቁስቛም"
            , "ማርያም ጽዮን", "ባኣታ ማርያም", "ኣስተርእዮ", "ኪዳነ ምሕረት", "ልደታ", "ፅንሰታ", "ሩፋኤል","ዝተፈልጠ በዓል የለን"};
    
    
    HolyDay(int year, int month, int date,String name) {
        super(year, month, date);
        this.name = name;
    }
    HolyDay(int year, int month, int date,int nameIndex){
        super(year, month, date);
        this.nameIndex=nameIndex;
        this.name=holiday_names[nameIndex];
    }


    @Override
    public MonthName getMonthName(){
        return MonthName.values()[getMonth()-1];
    }

    public String getName(){
        return (name==null)? holiday_names[nameIndex] : name;
    }
    @NonNull
    @Override
    public String toString(){
        return name+" "+ super.toString()+" ይውዕል።";
    }

}
