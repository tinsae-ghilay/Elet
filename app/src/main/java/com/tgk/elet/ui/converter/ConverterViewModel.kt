package com.tgk.elet.ui.converter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tgk.elet.common.Util
import com.tgk.elet.common.Util.format
import com.tgk.elet.geezDate.GeezMonth
import com.tgk.elet.localDate.DateFormat
import com.tgk.elet.localDate.MonthLocal
import com.tgk.elet.temporal.BaseDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConverterViewModel : ViewModel() {

    private val _geezCalendar: MutableLiveData<List<GeezMonth>> = MutableLiveData<List<GeezMonth>>().apply {
        viewModelScope.launch {
            value = getDates()
        }
    }

    private val _gregorianCalendar: MutableLiveData<List<MonthLocal>> = MutableLiveData<List<MonthLocal>>().apply {
        viewModelScope.launch{
            value = getGregorian()
        }
    }

    private val _converted:MutableLiveData<BaseDate> = MutableLiveData<BaseDate>()

    private val _dayInfo = MutableLiveData<List<String>>()
    val geezCalendar: LiveData<List<GeezMonth>> = _geezCalendar
    val gregorianCalendar: LiveData<List<MonthLocal>> = _gregorianCalendar
    val converted:LiveData<BaseDate> = _converted
    val day_info:LiveData<List<String>> = _dayInfo

    private suspend fun getDates(): List<GeezMonth> = withContext(Dispatchers.Default){Util.generateCalendar()}
    private suspend fun getGregorian():List<MonthLocal> = withContext(Dispatchers.Default){Util.generateGregorianCalendar()}

    fun setConverted(date: BaseDate){
        Log.d("Converter V:"," got selected Date as ${date.format(DateFormat.MONTH_NAMED)}")
        _converted.postValue(date)
    }

}