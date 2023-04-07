package com.tgk.Elet

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tgk.Elet.R
import com.tgk.Elet.databinding.ActivityMainBinding
import com.tgk.Elet.common.CommonViewModel
import com.tgk.Elet.common.Util.format
import com.tgk.Elet.localDate.DateFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private val cViewModel: CommonViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val toolBar = binding.toolbar
        toolBar.datum.text = cViewModel.geezDate.format(DateFormat.MONTH_NAMED)
        toolBar.apostle.text = cViewModel.era.toString()
        setContentView(binding.root)

        cViewModel.navButtonsVisibility.observe(this){
            //setMonthNavVisibility(it)
        }
        /*binding.next.setOnClickListener {
            cViewModel.setNavDirection(1)
        }
        binding.back.setOnClickListener {
            cViewModel.setNavDirection(-1)
        }
        cViewModel.fragmentTitle.observe(this){
            binding.fragmentTitle.text = it
        }*/
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

    }


    private fun setMonthNavVisibility(visibility: Int){
        //binding.back.visibility = visibility
        //binding.next.visibility = visibility
    }
}