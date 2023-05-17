package com.tgk.elet.common

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.preference.PreferenceManager

object Preferences {


    private var showEritreanH = false
    private var showTigraianH = false
    val showEritrean get() = showEritreanH
    val showTigraian get() = showTigraianH

    fun Context.getPreferences():SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

    fun Context.getValue(key: String): Boolean{
        return this.getPreferences().getBoolean(key,false)
    }
    fun setAppLocale(preferences: SharedPreferences){
        val appLocale = LocaleListCompat
            .forLanguageTags(preferences.getString("lang", "ti"))
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    fun Context.setPreferenceValues(){
        showEritreanH = this.getValue("eritrean")
        showTigraianH = this.getValue("tigraian")
    }
}