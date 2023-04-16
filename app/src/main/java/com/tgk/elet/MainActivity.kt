package com.tgk.elet

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tgk.Elet.R
import com.tgk.Elet.databinding.ActivityMainBinding
import com.tgk.elet.common.CommonViewModel
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
        setAppLocale(this)
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

    override fun onResume() {
        super.onResume()
        setAppLocale(this)
    }

}
