package com.tgk.Elet

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import com.tgk.Elet.R
import com.tgk.Elet.common.CommonViewModel

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val cViewModel = ViewModelProvider(requireActivity())[CommonViewModel::class.java]
        //cViewModel.setFragmentTitle(requireActivity().getString(R.string.settings))
        cViewModel.setShowNavButtons(View.GONE)
    }
}