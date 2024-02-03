package com.tgk.elet.common

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.tgk.Elet.R
import com.tgk.elet.localDate.DateFormat
import com.tgk.elet.temporal.BaseDate
import com.tgk.elet.geezDate.*
import com.tgk.elet.localDate.DateLocal
import com.tgk.elet.localDate.MonthLocal
import com.tgk.elet.temporal.Month

object Util {
    // 20 years * 13 months in a year for geez calendar
    const val SPAN = 520
    // 20 years * 12 months in a year for Gregorian calendar
    const val SPAN_LOCAL = 480

    val today: GeezDate = GeezDate.now()
    val thisDay: DateLocal = DateLocal.now()
    // need all calendar arrays here and set them from context
    // months
    // week days
    private var _months = emptyArray<String>()
    private var _geez_months = emptyArray<String>()
    private var _week_Days = emptyArray<String>()
    private var _apostles = emptyArray<String>()
    //******************************
    val months get() = _months
    val geezMonths get() = _geez_months
    val weekDays get() = _week_Days
    val apostles get() = _apostles

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
     * Formats Date to a desired pattern
     * @param format enum
     * @return String
     */
    fun BaseDate.format(format: DateFormat):String{

        val m = if (this is DateLocal) months else geezMonths
        val res: String = when (format) {
            DateFormat.DOTTED -> "$date.$month.$year"
            DateFormat.DOT_SPACED -> "$date. $month. $year"
            DateFormat.SLASHED -> "$date/$month/$year"
            DateFormat.SLASH_SPACED -> "$date/ $month/ $year"
            DateFormat.SPACED -> "$date  $month  $year"
            DateFormat.SPACED_WITH_COMA -> "$date,  $month,  $year"
            DateFormat.WITH_COMMA -> "$date,$month,$year"
            DateFormat.MONTH_NAMED -> "$date ${m[month-1]}, $year"
            DateFormat.MONTH_NAMED_ISO -> "${m[month-1]}  $date $year"
            DateFormat.MONT_OF_YEAR -> "${m[month-1]}, $year"
            DateFormat.DAY_OF_MONTH -> "$date ${m[month-1]}"
            else -> {
                this.toString()
            }
        }
        return res
    }

    fun Month.format(): String{
        val m = if(this is MonthLocal) months else geezMonths
        return "${m[this.month-1]} $year"
    }
    fun Month.name():String = if (this is MonthLocal) months[this.month-1] else geezMonths[this.month-1]

    // Geez Calendar months asan List

    fun generateCalendar(): List<HolyMonth>{

        val months = arrayListOf<HolyMonth>()
        var date = today.plusYears(-20)

        for (i in 0 until SPAN){
            months.add(HolyMonth(date.year,date.month))
            date = date.plusMonths(1,true)
        }
        return months
    }

    // Gregorian Calendar months as a List

    fun generateGregorianCalendar():List<MonthLocal>{

        val months = arrayListOf<MonthLocal>()
        var cMonth = MonthLocal(thisDay.year-20, thisDay.month)

        for (i in 0 until SPAN_LOCAL){
            months.add(cMonth)
            cMonth = cMonth.addMonths(1)
        }
        return months
    }

    fun setTemporalArrays(context: Context){
        val res = context.resources
        _months = res.getStringArray(R.array.months)
        _geez_months = res.getStringArray(R.array.monthsList)
        _week_Days = res.getStringArray(R.array.week_days)
        _apostles = res.getStringArray(R.array.evangelists)
    }

    fun Int.isGregorianLeapYear():Boolean {
        return if (this % 100 == 0) this % 400 == 0
        else this % 4 == 0;
    }

    // get day of week from date object.

    fun BaseDate.dayOfWeek():Int{
        return this.julianDay % 7;
    }

}