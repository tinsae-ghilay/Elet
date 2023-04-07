package com.tgk.Elet.ui.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tgk.Elet.databinding.FragmentConverterBinding
import com.tgk.Elet.calendars.OnDateSelectedListener
import com.tgk.Elet.common.CommonViewModel
import com.tgk.Elet.common.Util
import com.tgk.Elet.common.Util.format
import com.tgk.Elet.geezDate.GeezDate
import com.tgk.Elet.localDate.DateFormat
import com.tgk.Elet.localDate.DateLocal
import com.tgk.Elet.temporal.BaseDate

class ConverterFragment : Fragment() {

    private var _binding: FragmentConverterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var selectedDate:BaseDate = Util.today

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val converterViewModel =
            ViewModelProvider(this)[ConverterViewModel::class.java]

        val cViewModel = ViewModelProvider(requireActivity())[CommonViewModel::class.java]
        cViewModel.setFragmentTitle(requireContext().getString(R.string.convert))
        cViewModel.setShowNavButtons(View.GONE)

        _binding = FragmentConverterBinding.inflate(inflater, container, false)
        binding.selectedDate.text =Util.thisDay.format(DateFormat.MONTH_NAMED)
        val geezPicker = binding.geezPicker.also {
            it.selectedDate = Util.today
            it.onDateSelectedListener = listener
        }
        val gregorianPicker = binding.gregorianPicker.also {
            it.isGeezPicker = false
            it.selectedDate = selectedDate
            it.onDateSelectedListener = listener
        }
        converterViewModel.geezCalendar.observe(requireActivity()){
            geezPicker.months = it
        }
        converterViewModel.gregorianCalendar.observe(requireActivity()){
            gregorianPicker.months = it
        }
        binding.geez.also {
            it.setOnClickListener { v -> toggleChronology(v) }
        }
        binding.gregorian.also {
            it.setOnClickListener { v -> toggleChronology(v) }
        }
        return binding.root
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun toggleChronology(view:View){

        view.setBackgroundResource(R.color.main)
        if (view == binding.geez){
            binding.gregorian.setBackgroundResource(R.color.white)
            if (binding.switcher.currentView != binding.geezPicker){
                binding.switcher.showNext()
            }

        }else{
            if (binding.switcher.currentView != binding.gregorianPicker){
                binding.switcher.showPrevious()
            }
            binding.geez.setBackgroundResource(R.color.white)
        }

    }
    private val listener: OnDateSelectedListener = object : OnDateSelectedListener{
        override fun selectedDate(date: BaseDate) {
            selectedDate = date
            val dateRequested = if (date is GeezDate){
                DateLocal.fromJdn((selectedDate as GeezDate).julianDay)
            }else {
                GeezDate.from(selectedDate.year,selectedDate.month,selectedDate.date)
            }
            binding.selectedDate.text = dateRequested.format(DateFormat.MONTH_NAMED)
        }
    }
}