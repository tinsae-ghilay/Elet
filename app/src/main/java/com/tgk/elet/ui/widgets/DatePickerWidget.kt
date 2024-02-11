package com.tgk.elet.ui.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tgk.Elet.databinding.DatePickerWidgetBinding
import com.tgk.elet.common.Util.convert
import com.tgk.elet.geezDate.GeezDate
import com.tgk.elet.localDate.DateLocal
import com.tgk.elet.temporal.BaseDate
import com.tgk.elet.temporal.Month
import com.tgk.elet.ui.converter.ConverterViewModel

class DatePickerWidget(private val what: Boolean,
                       private val cal: List<Month>?)
    : BottomSheetDialogFragment() {


       private val vModel: ConverterViewModel by viewModels(ownerProducer = {requireParentFragment()})

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bind = DatePickerWidgetBinding.inflate(inflater)
        val picker = bind.picker
        picker.onDateSelectedListener = this.listener
        picker.isGeezPicker = this.what
        picker.months = cal
        bind.done.setOnClickListener {
            _ -> this.dismiss()
        }
        return bind.root
    }

    private val listener:OnDateSelectedListener = object: OnDateSelectedListener{
        override fun selectedDate(date: BaseDate) {
            vModel.setSelected(date)
        }
    }
}