package com.tgk.elet.geezDate;

import android.content.Context;

import com.tgk.Elet.R;

/**
 * this will be replaced by String resources and accessor in a singleton
 */
public class HolyMonth extends GeezMonth {
    public static String[] daySaint = {"ልደታ ማርያም", "ኢዮብ ጻድቅ", "ባኣታ ማርያም", "ዮሓንስ ወልደ ነጎድጓድ", "ጋብር", "የሱስ/ቁስቁዋም", "ስላሴ", "አባ ኪሮስ"
            ,"ቶማስ ሃዋርያ", "መስቀል", "ኢያቄም ወ ሃና", "ሚካኤል መልኣኽ", "እግዚአብሔር አብ", "አቡነ አረጋዊ", "ቄርቆስ", "ኪዳነ ምህረት"
            ,"ቅዱስ እስጢፋኖስ", "ፊልጶስ ሐዋርያ", "ገብሪኤል", "ጽንሰታ", "ማርያም", "ኡራኤል", "ጊዮርጊስ", "አቡነ ተኽለ ሃይማኖት", "መርቆሬዎስ"
            ,"ቅዱስ ዮሴፍ", "መድሃኔ ዓለም", "ቆስጠንጢኖስ", "ባዓለ እግዚኣብሄር(የሱስ)", "ማርቆስ ወንጌላዊ"};

    public static String[] pagumieSaints = {"ጳጉሜን","ጳጉሜን","ኡፋኤል","ጳጉሜን","ጳጉሜን","ጳጉሜን"};

    public HolyMonth(int year, int month) {
        super(year, month);
    }

     public HolyDay[] getSaints(Context context){
        String[] s = context.getResources().getStringArray(R.array.daily_events);
        int size = getSize();
        HolyDay[] saints = new HolyDay[size];
        if (this.isPagumien()){
            for ( int i = 0; i < size; i++ ){
                saints[i] = new HolyDay( getYear(), getMonth(), i+1, "ጳጉሜን" );
            }
        }else {
            for ( int i =0; i < size; i++ ){
                saints[i] = new HolyDay( getYear(), getMonth(), i+1, s[ i+1] );
            }
        }
        return saints;
    }
    public HolyDay[] getHolyDays(){
        return HolyYear.ofMonth(this);
    }
}
