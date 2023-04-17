package com.tgk.elet.ui.widgets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tgk.elet.common.Util
import com.tgk.elet.geezDate.HolyDay
import com.tgk.elet.geezDate.HolyMonth
import com.tgk.elet.temporal.Month
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CalendarViewModel : ViewModel() {

    // Mutable
    private val _calendarMonth:MutableLiveData<HolyMonth> = MutableLiveData()
    private val _holidays:MutableLiveData<Array<HolyDay>> = MutableLiveData()
    private val _months:MutableLiveData<List<HolyMonth>> = MutableLiveData<List<HolyMonth>>().apply {
        viewModelScope.launch {
            value = getCalendar()
            //setCalendarMonth(GeezMonth(Util.today.year, Util.today.month))
        }
    }
    private val _showGregorianDates: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
        value = true
    }


    // LiveData
    val months: LiveData<List<HolyMonth>> = _months
    val holidays:LiveData<Array<HolyDay>> = _holidays
    val showGregorianDates: LiveData<Boolean> = _showGregorianDates
    val calendrMonth: LiveData<HolyMonth> = _calendarMonth

    // getters
    private suspend fun getCalendar():List<HolyMonth> = withContext(Dispatchers.Default){
        Util.generateCalendar()
    }

    // setter
    fun setCalendarMonth(index: Month){
        _calendarMonth.postValue(index as HolyMonth)
        _holidays.postValue(index.holyDays)
    }

    fun setShowGregorian(){
        _showGregorianDates.postValue(!_showGregorianDates.value!!)
    }

    fun toggle(){
        _showGregorianDates.value = !_showGregorianDates.value!!
    }
}