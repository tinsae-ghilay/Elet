package com.tgk.elet.ui.widgets

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tgk.elet.geezDate.GeezDate
import com.tgk.elet.temporal.BaseDate
import com.tgk.elet.temporal.Month

/**
 * MonthAdapter Extends Recyclerview Adapter
 * @see RecyclerView.Adapter
 * @author tinsae Ghilay
 * @since March 19,2023 Gregorian / March 10, 2015 Geez Calendar
 */

open class MonthAdapter : RecyclerView.Adapter<MonthAdapter.Holder>(){

    var selectedDate:BaseDate = GeezDate.now()
    var months: List<Month>? = null
    var pickedDateListener: OnDatePicked? = null

    inner class Holder(view: MonthView): RecyclerView.ViewHolder(view){

        val monthView = view.also {v ->
            v.onDateSelectedListener = object: OnDateSelectedListener {
                override fun selectedDate(date: BaseDate) {
                    pickedDateListener?.datePicked(date,adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
       return super.createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        months?.get(position)?.also {
            holder.monthView.month = it
            holder.monthView.selectedDate = selectedDate
        }
    }

    override fun getItemCount(): Int {
        return months?.size?:0
    }

    interface OnDatePicked{
        fun datePicked(date: BaseDate, position: Int)
    }
}