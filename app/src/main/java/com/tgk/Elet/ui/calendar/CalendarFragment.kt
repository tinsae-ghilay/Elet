package com.tgk.Elet.ui.calendar

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tgk.Elet.R
import com.tgk.Elet.databinding.FragmentCalendarBinding
import com.tgk.Elet.calendars.CalendarViewDelegator
import com.tgk.Elet.common.CommonViewModel
import com.tgk.Elet.common.Preferences.getPreferences
import com.tgk.Elet.geezDate.GeezDate
import com.tgk.Elet.geezDate.GeezMonth
import com.tgk.Elet.geezDate.HolyDay
import com.tgk.Elet.geezDate.HolyMonth
import com.tgk.Elet.geezDate.HolyYear
import com.tgk.Elet.temporal.BaseDate
import com.tgk.Elet.temporal.Month
import com.tgk.Elet.ui.adapters.HolidayAdapter

class CalendarFragment : Fragment(), CalendarViewDelegator.OnMonthChangedListener {

    private var _binding: FragmentCalendarBinding? = null
    private var snackBar:Snackbar? = null
    private var focusedMonth:Month? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val month get() = binding.calendarView
    private val holidaysList get() = binding.holidaysList

    private val calendarViewModel by viewModels<CalendarViewModel>()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View {

        val showGregorian  = getShowGregorian()
        // common view model
        val cViewModel = ViewModelProvider(requireActivity())[CommonViewModel::class.java]
        cViewModel.setShowNavButtons(View.VISIBLE)
        //*** end common view model

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)


        month.typeface = ResourcesCompat.getFont(requireContext(), R.font.abyssinica)
        month.setOnMonthChangedListener(this)
        month.showGregorianDates = getShowGregorian()


        // Monthly Holiday list
        val vertical = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        //val decoration = DividerItemDecoration(requireActivity(),DividerItemDecoration.VERTICAL)
        //holidaysList.addItemDecoration(decoration)

        val holidayAdapter = HolidayAdapter(HolyYear.ofMonth(GeezMonth(2015,6)))
        holidaysList.layoutManager = vertical
        holidaysList.adapter = holidayAdapter

        // CalendarViewModel
        calendarViewModel.months.observe(requireActivity()){
            month.months = it
        }

        calendarViewModel.calendrMonth.observe(requireActivity()){
            holidayAdapter.setMonth(it)
        }

        binding.fab.setOnClickListener {
            focusedMonth?.let { showSnack("event will be added for ") }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        snackBar = null
    }

    private fun showSnack(message:String){
        if (snackBar == null){
            snackBar = Snackbar.make(binding.fab,message,Snackbar.LENGTH_SHORT)
        }
        if(snackBar?.isShown == true){
            snackBar?.setText(message)
        }else{
            snackBar?.setText(message)
        }
        snackBar?.show()
    }

    override fun onMothChanged(month: Month?) {
        month?.let {
            focusedMonth = it
            calendarViewModel.setCalendarMonth(it)
        }
    }
    private fun getShowGregorian(): Boolean{
        val pref:SharedPreferences = requireActivity().getPreferences()
        return pref.getBoolean("gregorian",false)
    }
}