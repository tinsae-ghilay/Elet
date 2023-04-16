package com.tgk.elet.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tgk.Elet.R
import com.tgk.Elet.databinding.HolidayItemBinding
import com.tgk.elet.common.Util.format
import com.tgk.elet.geezDate.HolyDay
import com.tgk.elet.geezDate.HolyMonth
import com.tgk.elet.localDate.DateFormat
import com.tgk.elet.temporal.Month

class HolidayAdapter(private var holidays: Array<HolyDay>) : RecyclerView.Adapter<HolidayAdapter.Holder>() {

    var isForAnnualList = true

    private var month: Month? = null
    inner class Holder(binding: HolidayItemBinding): RecyclerView.ViewHolder(binding.root){
        private val dates = binding.date
        private val events = binding.events
        private val  holidays = this.itemView.context.resources.getStringArray(R.array.holy_days)
        private val saints = this.itemView.context.resources.getStringArray(R.array.daily_events)
        init {
            binding.root.setOnClickListener {  }
        }
        fun bind(holiday: HolyDay?){

            dates.text = holiday?.format(DateFormat.DAY_OF_MONTH)
            //events.text = holiday?.name  // replace this with the below code-- but update resources first
            if (isForAnnualList){
                holiday?.nameIndex?.let {
                    events.text = holidays[it]
                }
            } else{
                holiday?.let {
                    events.text = saints[it.date]
                }
            }
        }

        fun bindNull(){
            if (month!=null){
                dates.text = month?.format()
            }
            else dates.text = ""
            events.text = this.itemView.context.getString(R.string.no_holidays)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = HolidayItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (holidays.isNotEmpty()){
            holder.bind(holidays[position])
        }else{
            holder.bindNull()
        }
    }

    override fun getItemCount(): Int {
        return if (holidays.isNotEmpty()) holidays.size else 1
    }

    fun setHolidays(holidays: Array<HolyDay>){
        this.holidays = holidays
        this.notifyDataSetChanged()
    }
    fun setMonth(month: HolyMonth){
        this.month = month
        this.holidays = month.holyDays
        notifyDataSetChanged()
    }
}