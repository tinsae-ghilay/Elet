package com.tgk.Elet.ui.bahre_hassab

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tgk.Elet.common.Util
import com.tgk.Elet.geezDate.BahreHasab
import com.tgk.Elet.geezDate.HolyDay

class HassabViewModel : ViewModel() {

    private val _holidays = MutableLiveData<Array<HolyDay>>().apply { value = BahreHasab.getAll(Util.today.year) }

    val holidays:LiveData<Array<HolyDay>> = _holidays

    fun setYear(year:Int){
        _holidays.postValue(BahreHasab.getAll(year))
    }
}