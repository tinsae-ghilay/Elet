package com.tgk.elet.calendars

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.tgk.Elet.R
import com.tgk.elet.common.Util.format
import com.tgk.elet.temporal.Month

/**
 * Calendar View extends CalendarViewDelegator that in turn extends Linear layout
 * @author Tinsae Ghilay
 * @see CalendarViewDelegator
 * @since March 19,2023 Gregorian / March 10, 2015 Geez Calendar
 */

class CalendarView : CalendarViewDelegator{

    private val titleText: TextView

    var showGregorianDates: Boolean = false
        set(value) {
            field = value
            (adapter as CalendarViewAdapter).also {
                it.showGregorian = field
                it.notifyDataSetChanged()
            }
        }

    // List of months to be shown in datePicker
    override var months : List<Month>? = null
        set(value) {
            field = value
            adapter.months = field
            adapter.pickedDateListener = pickedDateListener
            // we attach adapter to pager
            month.adapter = adapter
            // and scroll it to current month index
            resetToCurrentMonth()
        }

    // you know what you are looking at here.
    constructor(context: Context):this(context,null)
    constructor(context: Context, attributeSet: AttributeSet?):this(context,attributeSet,0)
    constructor(context: Context,
                attributeSet: AttributeSet?, defStyle: Int):super(context,attributeSet,defStyle){
        this.adapter = CalendarViewAdapter()

        // attribute variables and viewpager are set in supper class
        // so we only need to init views here
        // Month Title and navigation
        val titleLayout = LinearLayout(context).also {
            it.weightSum =7f
            //it.setPadding(2.dp)
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

        // Month title text
        val titleParam = LayoutParams(0,LayoutParams.WRAP_CONTENT,5f)
        titleText = TextView(context).also { v ->
            v.layoutParams = titleParam
            v.gravity = Gravity.CENTER
            v.typeface = typeface
            v.setBackgroundResource(R.drawable.unbound_ripple)
            v.textSize = textSize * 0.65f
            v.setOnClickListener {
                resetToCurrentMonth()
            }
        }

        titleLayout.addView(goToPreviousMonth)
        //titleLayout.addView(showGregorian)
        titleLayout.addView(titleText)
        //titleLayout.addView(goToCurrentMonth)
        titleLayout.addView(goToNextMonth)
        this.addView(titleLayout)

        // Days of the week
        val weekView = WeekDaysView(context).also {
            it.textSize = textSize
            it.typeface = typeface
           // it.setPadding(1.dp,0,1.dp,0)
            it.setBackgroundColor(offsetColor)
            it.weekEndColor = weekEndColor
            it.weekTextColor = insetColor
            it.typeface = typeface
            weekDays?.let { a -> it.weekDays = a }
        }
        this.addView(weekView)

        this.addView(month)

    }

    override fun monthScrolled(month: Month?) {
        super.monthScrolled(month)
        titleText.text = month?.format()
    }

    /**
     * Calendar month adapter.
     * We override onCreateView() method here to inflate CalendarMonthView
     * @author Tinsae Ghilay
     * @see MonthAdapter
     */

    inner class CalendarViewAdapter: MonthAdapter(){

        var showGregorian = false

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

            val param = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            val view =  CalendarMonthView(parent.context)
            view.textSize = textSize
            view.weekEndColor = weekEndColor
            view.insetColor = insetColor
            view.offsetColor = offsetColor
            view.indicatorColor = indicatorColor
            view.selectorColor = selectorColor
            view.layoutParams = param
            view.showGregorianDates = showGregorianDates

            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            (holder.monthView as CalendarMonthView).showGregorianDates = showGregorian
            super.onBindViewHolder(holder, position)
        }
    }
    override fun resetToCurrentMonth(){
        super.resetToCurrentMonth()
        currentMonthIndex?.let {
            onMonthChangeListener?.onMothChanged(months?.get(it))
        }
    }
}