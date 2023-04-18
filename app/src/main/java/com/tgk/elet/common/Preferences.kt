package com.tgk.elet.common

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.preference.PreferenceManager

object Preferences {

    fun Context.getPreferences():SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

    fun setAppLocale(preferences: SharedPreferences){
        val appLocale = LocaleListCompat
            .forLanguageTags(preferences.getString("lang", "ti"))
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}