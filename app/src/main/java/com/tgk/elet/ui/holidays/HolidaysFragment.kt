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
import com.tgk.elet.common.Preferences
import com.tgk.elet.common.Preferences.setPreferenceValues
import com.tgk.elet.common.Preferences.showEritrean
import com.tgk.elet.common.Preferences.showTigraian
import com.tgk.elet.common.Util
import com.tgk.elet.geezDate.HolyMonth
import com.tgk.elet.geezDate.HolyYear
import com.tgk.elet.ui.adapters.HolidayAdapter
import com.tgk.elet.ui.widgets.SwitchView

class HolidaysFragment : Fragment() {

    private var _binding: FragmentHolidaysBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val holidayAdapter by lazy { HolidayAdapter(HolyYear.ofYear(Util.today.year,showEritrean,showTigraian))}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // we have to look for preferences whenever we navigate back here
        requireActivity().setPreferenceValues()

        _binding = FragmentHolidaysBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val vertical = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        val holidays = binding.holidays
        //val decoration = DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        //holidays.addItemDecoration(decoration)
        holidays.layoutManager = vertical
        holidays.adapter = holidayAdapter
        binding.switches.onSwitchAction = object : SwitchView.OnSwitchAction{
            override fun switchTo(state: SwitchView.SwitchState) {
                if (state == SwitchView.SwitchState.LEFT) showAnnual()
                else showMonthly()
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showAnnual(){
        holidayAdapter.isForAnnualList = true
        holidayAdapter.setHolidays(HolyYear.ofYear(Util.today.year,showEritrean,showTigraian))

    }

    private fun showMonthly(){
        holidayAdapter.setHolidays(HolyMonth(Util.today.year,Util.today.month).getSaints(requireActivity()))
        holidayAdapter.isForAnnualList = false
    }
}