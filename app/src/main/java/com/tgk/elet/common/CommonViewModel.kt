package com.tgk.elet.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tgk.elet.geezDate.GeezDate
import com.tgk.elet.geezDate.HolyYear

class CommonViewModel: ViewModel() {

    val geezDate: GeezDate = GeezDate.now()
    private val hYear: HolyYear = HolyYear(geezDate.year)
    val era = hYear.indexOfEra()


    private val _navButtonsVisibility:MutableLiveData<Int> = MutableLiveData()
    private  val _fragmentTitle: MutableLiveData<String> = MutableLiveData()

    val navButtonsVisibility:LiveData<Int> = _navButtonsVisibility


    fun setShowNavButtons(visibility: Int) {
        _navButtonsVisibility.postValue(visibility)
    }

    fun setFragmentTitle(title: String){
        _fragmentTitle.postValue(title)
    }
}