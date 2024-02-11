package com.tgk.elet.ui.converter

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tgk.Elet.R
import com.tgk.Elet.databinding.FragmentConverterBinding
import com.tgk.elet.ui.widgets.OnDateSelectedListener
import com.tgk.elet.common.CommonViewModel
import com.tgk.elet.common.Util
import com.tgk.elet.common.Util.format
import com.tgk.elet.geezDate.GeezDate
import com.tgk.elet.localDate.DateFormat
import com.tgk.elet.localDate.DateLocal
import com.tgk.elet.temporal.BaseDate
import com.tgk.elet.temporal.Month
import com.tgk.elet.ui.widgets.DatePicker
import com.tgk.elet.ui.widgets.DatePickerWidget
import com.tgk.elet.ui.widgets.SwitchView

class ConverterFragment : Fragment() {

    private var _binding: FragmentConverterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var months: List<Month>? = null
    private var gmonths: List<Month>? = null
    private var target:SwitchView.SwitchState = SwitchView.SwitchState.LEFT

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       // 4 2morrow
       //val converterViewModel:ConverterViewModel by viewModels();
        val converterViewModel by activityViewModels<ConverterViewModel>()

        val cViewModel = ViewModelProvider(requireActivity())[CommonViewModel::class.java]
        cViewModel.setFragmentTitle(requireContext().getString(R.string.convert))
        cViewModel.setShowNavButtons(View.GONE)

        _binding = FragmentConverterBinding.inflate(inflater, container, false)
        converterViewModel.setConverted(Util.thisDay)
        //binding.selectedDate.text =Util.thisDay.format(DateFormat.MONTH_NAMED)

        binding.showDialog.setOnClickListener(onClick)
        converterViewModel.geezCalendar.observe(viewLifecycleOwner){
            months = it
        }
        converterViewModel.gregorianCalendar.observe(viewLifecycleOwner){
            gmonths = it
        }
        converterViewModel.converted.observe(viewLifecycleOwner){
            binding.selectedDate.text = it.format(DateFormat.MONTH_NAMED)
            Log.d("Fragment:","Got $it as selected date to _______________!")
        }
        binding.switches.onSwitchAction = onSwitchAction

        return binding.root
    }

    private val onClick = View.OnClickListener {
        Toast.makeText(activity," trigger clicked",Toast.LENGTH_SHORT).show();
        val  fr = if(target == SwitchView.SwitchState.LEFT){
            DatePickerWidget(true,months)
        }else{
            DatePickerWidget(true,gmonths)
        }
        fr.show(parentFragmentManager,"picker")
    }

    private val onSwitchAction: SwitchView.OnSwitchAction = object: SwitchView.OnSwitchAction{
        override fun switchTo(state: SwitchView.SwitchState) {
            target = state
        }

    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    /*private val listener: OnDateSelectedListener = object : OnDateSelectedListener {
        override fun selectedDate(date: BaseDate) {
            selectedDate = date
            Toast.makeText(requireContext(),"selected date is $selectedDate", Toast.LENGTH_SHORT).show()
            val dateRequested = if (date is GeezDate){
                DateLocal.fromJdn((selectedDate as GeezDate).julianDay)
            }else {
                GeezDate.from(selectedDate.year,selectedDate.month,selectedDate.date)
            }
            binding.selectedDate.text = dateRequested.format(DateFormat.MONTH_NAMED)
        }
    }*/
}