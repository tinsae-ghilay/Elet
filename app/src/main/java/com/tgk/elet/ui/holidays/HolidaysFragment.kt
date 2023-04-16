package com.tgk.elet.ui.holidays

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tgk.Elet.R
import com.tgk.Elet.databinding.FragmentHolidaysBinding
import com.tgk.elet.common.CommonViewModel
import com.tgk.elet.common.Util
import com.tgk.elet.geezDate.HolyMonth
import com.tgk.elet.geezDate.HolyYear
import com.tgk.elet.ui.adapters.HolidayAdapter

class HolidaysFragment : Fragment() {

    private var _binding: FragmentHolidaysBinding? = null

    private lateinit var currentPage:String

    companion object {
        const val ANNUAL ="annual"
        const val LUNAR ="lunar"
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val holidayAdapter by lazy { HolidayAdapter(HolyYear.ofYear(Util.today.year))}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val holidaysViewModel = ViewModelProvider(this)[HolidaysViewModel::class.java]
        val cViewModel = ViewModelProvider(requireActivity())[CommonViewModel::class.java]
        cViewModel.setFragmentTitle(requireContext().getString(R.string.annual))
        cViewModel.setShowNavButtons(View.GONE)

        _binding = FragmentHolidaysBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val vertical = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        val holidays = binding.holidays
        //val decoration = DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        //holidays.addItemDecoration(decoration)
        holidays.layoutManager = vertical
        holidays.adapter = holidayAdapter
        showAnnual()
        binding.annual.setOnClickListener {
            if (currentPage == LUNAR) showAnnual()
        }
        binding.monthly.setOnClickListener {
            if(currentPage == ANNUAL) showMonthly()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showAnnual(){
        binding.monthly.setBackgroundResource(R.color.white)
        binding.annual.setBackgroundResource(R.color.Neon)
        holidayAdapter.isForAnnualList = true
        holidayAdapter.setHolidays(HolyYear.ofYear(Util.today.year))
        currentPage = ANNUAL

    }

    private fun showMonthly(){
        holidayAdapter.setHolidays(HolyMonth(Util.today.year,Util.today.month).getSaints(requireActivity()))
        holidayAdapter.isForAnnualList = false
        binding.monthly.setBackgroundResource(R.color.Neon)
        binding.annual.setBackgroundResource(R.color.white)
        currentPage = LUNAR
    }
}