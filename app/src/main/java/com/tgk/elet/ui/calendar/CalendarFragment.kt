package com.tgk.elet.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tgk.Elet.R
import com.tgk.Elet.databinding.FragmentCalendarBinding
import com.tgk.elet.common.CommonViewModel
import com.tgk.elet.common.Preferences
import com.tgk.elet.common.Preferences.getValue
import com.tgk.elet.common.Preferences.setPreferenceValues
import com.tgk.elet.common.Preferences.showEritrean
import com.tgk.elet.common.Preferences.showTigraian
import com.tgk.elet.common.Util
import com.tgk.elet.common.Util.format
import com.tgk.elet.common.Util.today
import com.tgk.elet.geezDate.GeezMonth
import com.tgk.elet.geezDate.HolyMonth
import com.tgk.elet.geezDate.HolyYear
import com.tgk.elet.localDate.DateFormat
import com.tgk.elet.temporal.BaseDate
import com.tgk.elet.temporal.Month
import com.tgk.elet.ui.adapters.HolidayAdapter
import com.tgk.elet.ui.widgets.CalendarViewDelegator
import com.tgk.elet.ui.widgets.OnDateSelectedListener

class CalendarFragment : Fragment(), CalendarViewDelegator.OnMonthChangedListener,OnDateSelectedListener {

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

        // we have to look for preferences whenever wo navigate back here
        requireActivity().setPreferenceValues()

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)


        month.typeface = ResourcesCompat.getFont(requireContext(), R.font.abyssinica)
        month.setOnMonthChangedListener(this)
        month.showGregorianDates = requireActivity().getValue("gregorian")


        // Monthly Holiday list
        val vertical = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        //val decoration = DividerItemDecoration(requireActivity(),DividerItemDecoration.VERTICAL)
        //holidaysList.addItemDecoration(decoration)

        val holidayAdapter = HolidayAdapter(HolyMonth(today.year,Util.today.month))/*HolidayAdapter(HolyYear.ofMonth(
                GeezMonth(Util.today.year,Util.today.month))
        )*/
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

    override fun selectedDate(date: BaseDate) {
        showSnack("selected ${date.format(DateFormat.DAY_NAMED)}")
    }
}