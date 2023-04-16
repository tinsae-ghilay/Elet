package com.tgk.elet.calendars

import com.tgk.elet.temporal.BaseDate

interface OnDateSelectedListener{
    fun selectedDate(date: BaseDate)
}