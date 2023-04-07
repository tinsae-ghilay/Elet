package com.tgk.Elet.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tgk.Elet.R
import com.tgk.Elet.databinding.HolidayItemBinding
import com.tgk.Elet.common.Util.format
import com.tgk.Elet.geezDate.HolyDay
import com.tgk.Elet.geezDate.HolyMonth
import com.tgk.Elet.localDate.DateFormat
import com.tgk.Elet.temporal.Month

class HolidayAdapter(private var holidays: Array<HolyDay>) : RecyclerView.Adapter<HolidayAdapter.Holder>() {

    private var month: Month? = null
    inner class Holder(binding: HolidayItemBinding): RecyclerView.ViewHolder(binding.root){
        private val dates = binding.date
        private val events = binding.events
        init {
            binding.root.setOnClickListener {  }
        }
        fun bind(holiday: HolyDay?){

            dates.text = holiday?.format(DateFormat.DAY_OF_MONTH)
            events.text = holiday?.name
        }

        fun bindNull(){
            if (month!=null){
                dates.text = month?.monthName.toString()
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