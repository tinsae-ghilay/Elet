package com.tgk.Elet.common

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.tgk.Elet.localDate.DateFormat
import com.tgk.Elet.temporal.BaseDate
import com.tgk.Elet.geezDate.*
import com.tgk.Elet.localDate.DateLocal
import com.tgk.Elet.localDate.MonthLocal

object Util {

    const val SPAN = 520
    const val SPAN_LOCAL = 480

    val today: GeezDate = GeezDate.now()
    val thisDay: DateLocal = DateLocal.now()//DateLocal.fromJdn(today.julianDay)

    @ColorInt
    fun Context.getColorFromAttr(
        @AttrRes attrColor: Int,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
    ): Int {
        theme.resolveAttribute(attrColor, typedValue, resolveRefs)
        return typedValue.data
    }

    fun getDates(): List<String> {
        val m = ArrayList<String>()
        for (i: Int in 1..42){
            m.add("$i")
        }
        return m
    }

    /**
     * Formats GeezDate to a desired pattern
     * @param format enum
     * @return String
     */
    fun BaseDate.format(format: DateFormat):String{
        val res: String = when (format) {
            DateFormat.DOTTED -> "$date.$month.$year"
            DateFormat.DOT_SPACED -> "$date. $month. $year"
            DateFormat.SLASHED -> "$date/$month/$year"
            DateFormat.SLASH_SPACED -> "$date/ $month/ $year"
            DateFormat.SPACED -> "$date  $month  $year"
            DateFormat.SPACED_WITH_COMA -> "$date,  $month,  $year"
            DateFormat.WITH_COMMA -> "$date,$month,$year"
            DateFormat.MONTH_NAMED -> "$date $monthName, $year"
            DateFormat.MONTH_NAMED_ISO -> "$monthName  $date $year"
            DateFormat.MONT_OF_YEAR -> "$monthName, $year"
            DateFormat.DAY_OF_MONTH -> "$date $monthName"
            else -> {
                this.toString()
            }
        }
        return res
    }

    // what is happening here?

    fun generateCalendar(): List<HolyMonth>{

        val months = arrayListOf<HolyMonth>()
        var date = today.plusYears(-20)

        for (i in 0 until SPAN){
            months.add(HolyMonth(date.year,date.month))
            date = date.plusMonths(1,true)
        }
        return months
    }

    fun generateGregorianCalendar():List<MonthLocal>{

        val months = arrayListOf<MonthLocal>()
        var cMonth = MonthLocal(thisDay.year-20, thisDay.month)

        for (i in 0 until SPAN_LOCAL){
            months.add(cMonth)
            cMonth = cMonth.addMonths(1)
        }
        return months
    }
}