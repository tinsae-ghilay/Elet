package com.tgk.elet.calendars

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import com.tgk.Elet.R
import com.tgk.elet.common.Util.format
import com.tgk.elet.localDate.DateLocal
import com.tgk.elet.temporal.Month


/**
 * DatePicker that extends CalendarViewDelegator
 * @author Tinsae Ghilay
 * @see CalendarViewDelegator
 * @since March 19,2023 Gregorian / March 10, 2015 Geez Calendar
 */

class DatePicker: CalendarViewDelegator {

    // if this is set to false, calendar months will be assumed as
    // gregorian calendar and current date will be indicated as such
    var isGeezPicker: Boolean = true

    // text view for displaying month and year of current month
    private val title: TextView

    override var months : List<Month>? = null
        set(value) {
            field = value

            // we declare adapter here because we are sure months list is set
            adapter = DatePickerAdapter().also { a ->
                a.months = field
                a.pickedDateListener = pickedDateListener
            }
            month.adapter = adapter
            resetToCurrentMonth()
        }

    // you know what you are looking at here.
    constructor(context: Context)
            :this(context,null)
    constructor(context: Context, attributeSet: AttributeSet?)
            :this(context,attributeSet,0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int)
            :super(context,attributeSet,defStyle){

        this.clipChildren = false
        // Month Title and navigation
        val titleLayout = LinearLayout(context).also {
            it.weightSum =7f
            it.setPadding(10)
            it.setBackgroundResource(R.color.super_lightGrey)
            }

        // navigation buttons
        val navButtonParams = LayoutParams(0,LayoutParams.MATCH_PARENT,1f)

        // back button
        val goToPreviousMonth = ImageButton(context).also { btn ->
            btn.setImageResource(R.drawable.back)
            btn.setColorFilter(Color.DKGRAY)
            btn.setBackgroundResource(R.drawable.ripple)
            btn.layoutParams = navButtonParams
            btn.setOnClickListener {
                month.currentItem--
            }
        }
        // next button
        val goToNextMonth = ImageButton(context).also { btn ->
            btn.setImageResource(R.drawable.next)
            btn.setBackgroundResource(R.drawable.ripple)
            btn.layoutParams = navButtonParams
            btn.setColorFilter(Color.DKGRAY)
            btn.setOnClickListener {
                month.currentItem++
            }
        }

        // back to present button
        val goToCurrentMonth = ImageButton(context).also { btn ->
            btn.setImageResource(R.drawable.now)
            btn.setBackgroundColor(Color.TRANSPARENT)
            btn.layoutParams = navButtonParams
            btn.setColorFilter(Color.DKGRAY)
            btn.setBackgroundResource(R.drawable.ripple)
            btn.setOnClickListener {
                resetToCurrentMonth()
            }
        }

        // Month title text
        val titleParam = LayoutParams(0,LayoutParams.WRAP_CONTENT,4f)
        title = TextView(context).also {
            it.layoutParams = titleParam
            it.gravity = Gravity.START
            it.typeface = typeface
            it.textSize = textSize * 0.65f
            it.setPadding(20,10,20,10)
        }
        titleLayout.addView(title)
        titleLayout.addView(goToPreviousMonth)
        titleLayout.addView(goToCurrentMonth)
        titleLayout.addView(goToNextMonth)
        this.addView(titleLayout)

        // Days of the week
        val weekView = WeekDaysView(context).also {
            it.textSize = textSize
            it.typeface = typeface
            it.setBackgroundResource(R.color.super_lightGrey)
            it.weekEndColor = weekEndColor
            it.weekTextColor = insetColor
            weekDays?.let { days -> it.weekDays = days }
        }
        this.addView(weekView)

        this.addView(month)

    }

    override fun monthScrolled(month: Month?) {
        super.monthScrolled(month)
        title.text = month?.format()
    }
    /**
     * DatePicker adapter
     */
    inner class DatePickerAdapter: MonthAdapter() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthAdapter.Holder {

            val param = ViewGroup.LayoutParams(-1,-1)

            val view =  MonthView(parent.context)
            view.isGeezMonth = isGeezPicker
            view.textSize = textSize
            view.weekEndColor = weekEndColor
            view.insetColor = insetColor
            view.offsetColor = offsetColor
            view.indicatorColor = indicatorColor
            view.selectorColor = selectorColor

            if (!isGeezPicker){
                view.today = DateLocal.now()
            }
            view.layoutParams = param

            return Holder(view)
        }
    }
}