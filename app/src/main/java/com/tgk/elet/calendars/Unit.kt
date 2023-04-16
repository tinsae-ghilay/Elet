package com.tgk.elet.calendars

import android.content.res.Resources
import kotlin.math.roundToInt

object Unit {

    // int to dp
    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

    // int to sp
    val Int.sp:Float get() = this*Resources.getSystem().displayMetrics.scaledDensity
}