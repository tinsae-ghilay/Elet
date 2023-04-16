package com.tgk.elet.common

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.preference.PreferenceManager

object Preferences {

    fun Context.getPreferences():SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    fun setAppLocale(context: Context) {
        val pref = context.getPreferences()
        pref.registerOnSharedPreferenceChangeListener { p, key ->
            if (key == "lang") {
                val appLocale = LocaleListCompat
                    .forLanguageTags(p.getString(key, "ti"))
                AppCompatDelegate.setApplicationLocales(appLocale)
                /*val dialog = AlertDialog.Builder(this).create()
                dialog.setMessage(getString(R.string.reboot_message))
                dialog.setButton(AlertDialog.BUTTON_POSITIVE,getString(R.string.yes)) { d, _ ->

                        d.dismiss()
                        this.recreate()
                }
                dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no)){d, _ -> d.dismiss()}
                dialog.show()*/
            }
        }
    }
}