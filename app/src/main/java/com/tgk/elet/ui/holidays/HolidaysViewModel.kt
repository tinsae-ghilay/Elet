package com.tgk.elet.ui.holidays

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tgk.elet.geezDate.GeezDate

class HolidaysViewModel : ViewModel() {

    private val _date = MutableLiveData<GeezDate>().apply { value = GeezDate.now() }
}