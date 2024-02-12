package com.tgk.elet

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tgk.Elet.R
import com.tgk.Elet.databinding.ActivityMainBinding
import com.tgk.elet.common.CommonViewModel
import com.tgk.elet.common.Preferences.getPreferences
import com.tgk.elet.common.Preferences.setAppLocale
import com.tgk.elet.common.Util
import com.tgk.elet.common.Util.format
import com.tgk.elet.localDate.DateFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val cViewModel: CommonViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // first and for most , we get locale here
        setAppLocale(pref)
        Util.setTemporalArrays(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val toolBar = binding.toolbar
        toolBar.datum.text = cViewModel.geezDate.format(DateFormat.MONTH_NAMED)
        toolBar.apostle.text = Util.apostles[cViewModel.era]
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

    }
    private val pref:SharedPreferences by lazy { this.getPreferences() }

    override fun onResume() {
        super.onResume()
        // have to register this
        pref.registerOnSharedPreferenceChangeListener (prefListener)
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
    }

    override fun onPause() {
        // have to unregister this
        super.onPause()
        pref.unregisterOnSharedPreferenceChangeListener(prefListener)
    }

    private val prefListener:OnSharedPreferenceChangeListener =
        OnSharedPreferenceChangeListener { p, key ->
            if (key == "lang") {
                val appLocale = LocaleListCompat
                    .forLanguageTags(p.getString(key, "ti"))
                AppCompatDelegate.setApplicationLocales(appLocale)
            }
        }

}
