package com.tgk.elet

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.tgk.Elet.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}