package com.tgk.Elet.ui.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tgk.Elet.common.Util
import com.tgk.Elet.geezDate.GeezMonth
import com.tgk.Elet.localDate.MonthLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
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



    private val _text = MutableLiveData<String>().apply {
        value = "This is Converter Fragment"
    }
    val text: LiveData<String> = _text
    val geezCalendar: LiveData<List<GeezMonth>> = _geezCalendar
    val gregorianCalendar: LiveData<List<MonthLocal>> = _gregorianCalendar


    private suspend fun getDates(): List<GeezMonth> = withContext(Dispatchers.Default){Util.generateCalendar()}
    private suspend fun getGregorian():List<MonthLocal> = withContext(Dispatchers.Default){Util.generateGregorianCalendar()}

}