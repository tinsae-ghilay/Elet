package com.tgk.elet.ui.converter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.tgk.Elet.databinding.FragmentConverterBinding
import com.tgk.elet.common.Util
import com.tgk.elet.common.Util.convert
import com.tgk.elet.common.Util.format
import com.tgk.elet.localDate.DateFormat
import com.tgk.elet.temporal.BaseDate
import com.tgk.elet.temporal.Month
import com.tgk.elet.ui.widgets.DatePickerWidget
import com.tgk.elet.ui.widgets.SwitchView

class ConverterFragment : Fragment() {

    private var _binding: FragmentConverterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var months: List<Month>? = null
    private var gmonths: List<Month>? = null
    // default status selected date = GeezDate today, and target Gregorian
    private var target:SwitchView.SwitchState = SwitchView.SwitchState.LEFT
    private var selectedDate:BaseDate = Util.today

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // converter View model
        val converterViewModel:ConverterViewModel by viewModels()

        _binding = FragmentConverterBinding.inflate(inflater, container, false)
        // seting the default selected date to date at instance
        converterViewModel.setSelected(selectedDate)
        // show date picker dialog button
        binding.showDialog.setOnClickListener(onClick)
        // Geez calendar months
        converterViewModel.geezCalendar.observe(viewLifecycleOwner){
            months = it
        }
        // Gregorian Calendar months
        converterViewModel.gregorianCalendar.observe(viewLifecycleOwner){
            gmonths = it
        }
        // observe selected date
        converterViewModel.selected.observe(viewLifecycleOwner){
            selectedDate = it
            binding.selectedDate.text = it.format(DateFormat.MONTH_NAMED)
        }
        // Observing Converted date
        converterViewModel.converted.observe(viewLifecycleOwner){
            binding.convertedDate.text = it.format(DateFormat.MONTH_NAMED)
        }
        // switch conversion to and fro
        binding.switches.onSwitchAction = object: SwitchView.OnSwitchAction {
            override fun switchTo(state: SwitchView.SwitchState) {
                /***
                * state/ target is needed for choosing datePicker mode.
                * and shift selected date to the opposite
                ***/
                target = state
                converterViewModel.setSelected(selectedDate.convert())
            }
        }

        return binding.root
    }

    /***
    * OnClick handles click event for button that trigers DatePicker
    * if target is Gregorian DatePicker is of Geez
    * and vice versa
    ***/
    private val onClick = View.OnClickListener {

        val  fr = if(target == SwitchView.SwitchState.LEFT){ // selected date is Geez
            DatePickerWidget(true,months)
        }else{
            DatePickerWidget(true,gmonths)
        }
        fr.show(childFragmentManager,"picker")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

