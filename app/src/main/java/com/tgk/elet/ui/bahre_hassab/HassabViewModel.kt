package com.tgk.elet.ui.bahre_hassab

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tgk.elet.common.Util
import com.tgk.elet.geezDate.BahreHasab
import com.tgk.elet.geezDate.HolyDay

class HassabViewModel : ViewModel() {

    private val _holidays = MutableLiveData<Array<HolyDay>>()
    private val _bahti = MutableLiveData<Int>()
    private val _era = MutableLiveData<Int>()
    private val _year = MutableLiveData<Int>()

    val holidays:LiveData<Array<HolyDay>> = _holidays
    val bahti: LiveData<Int> = _bahti
    val era: LiveData<Int> = _era
    val year: LiveData<Int> = _year

    init {
        setYear(Util.today.year)
    }
    fun setYear(year:Int){
        _year.postValue(year)
        val h = BahreHasab(year)
        _bahti.postValue(h.bahti)
        _era.postValue(h.indexOfEra())
        _holidays.postValue(h.ofYear())
    }
}