package com.tgk.elet.common

import com.tgk.elet.common.Util.isGregorianLeapYear
import com.tgk.elet.geezDate.HolyDay

object NationalHolidays {

    // 37 is womens day
    // 38 is mothers day

    fun getForEri(year: Int, month: Int): Array<HolyDay> {
        return when(month){
            6 -> {
                val date = if ((year+8 /*choosing the easy way out here*/).isGregorianLeapYear()) 2 else 3
                arrayOf(HolyDay(year,month,date,39)) }
            9  -> arrayOf(HolyDay(year,month,16,40))
            10 -> arrayOf(HolyDay(year,month,13,41))
            12 -> arrayOf(HolyDay(year,month,26, 42))
            else -> return emptyArray()
        }
    }

    fun getForTigray(year: Int, month: Int): Array<HolyDay> {
        return when(month){
            6 -> arrayOf(
                    HolyDay(year,month,11, 43)
                    , HolyDay(year,month,23,44))
            //8 -> arrayOf(HolyDay(year,month,27,45))
            9 -> arrayOf( HolyDay(year,month, 20, 45))
            else -> emptyArray()
        }
    }
}