package com.tgk.Elet.common

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object Preferences {

    fun Context.getPreferences():SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
}