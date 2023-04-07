package com.tgk.Elet.calendars

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.tgk.Elet.R
import com.tgk.Elet.calendars.Unit.dp
import com.tgk.Elet.calendars.Unit.sp
import com.tgk.Elet.temporal.BaseDate
import com.tgk.Elet.temporal.Month

/**
 * CalendarViewDelegator.
 * Extends LinearLayout
 * @see LinearLayout
 * @author Tinsae ghilay
 * @since March 19,2023 Gregorian / March 10, 2015 Geez Calendar
 */

open class CalendarViewDelegator: LinearLayout {

    var selectedDate:BaseDate? = null
    protected var onMonthChangeListener: OnMonthChangedListener? = null

    // Text size shouldn't be too small or too big
    private val maxTextSize = 20.sp
    private val minTextSize = 10.sp
    // attributes that we can set in xml
    var textSize = 18.sp
    protected var weekDays:Array<String>? = null
    var weekEndColor: Int = Color.argb(255,37,150,190)
    var insetColor: Int = Color.DKGRAY
    var offsetColor: Int = Color.LTGRAY
    var indicatorColor: Int = Color.argb(255,255,67,11)
    var selectorColor: Int = Color.LTGRAY
    var typeface :Typeface? = Typeface.DEFAULT

    var onDateSelectedListener: OnDateSelectedListener? = null

    // this is the index of the current month in view pager
    protected val currentMonthIndex get() = months?.size?.div(2)

    // view pager and its adapter for scrolling through months
    protected val month:ViewPager2
    protected lateinit  var adapter: MonthAdapter

    // List of months to be shown in datePicker
    open var months : List<Month>? = null

    // position of previously selected view index in adapter
    protected var oldPosition:Int? = null

    // you know what you are looking at here.
    constructor(context: Context): this(context,null)
    constructor(context: Context, attributeSet: AttributeSet?): this(context,attributeSet,0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int)
            :super(context,attributeSet,defStyle){

        // orientation of our layout
        this.orientation = VERTICAL

        // ******************************* reading attrs *******************************
        val tArray = context.obtainStyledAttributes(attributeSet, R.styleable.CalendarViewDelegator)
        textSize = delegateTextSize(tArray.getDimension(R.styleable.CalendarViewDelegator_geezTextSize,textSize))
        typeface = delegateTypeFace(tArray.getResourceId(R.styleable.CalendarViewDelegator_font,0))
        delegateWeekDays(tArray.getResourceId(R.styleable.CalendarViewDelegator_weekDays,0))
        weekEndColor = tArray.getColor(R.styleable.CalendarViewDelegator_weekEndColor,weekEndColor)
        insetColor = tArray.getColor(R.styleable.CalendarViewDelegator_insetColor, insetColor)
        offsetColor = tArray.getColor(R.styleable.CalendarViewDelegator_offsetColor,offsetColor)
        indicatorColor = tArray.getColor(R.styleable.CalendarViewDelegator_indicatorColor,indicatorColor)
        selectorColor = tArray.getColor(R.styleable.CalendarViewDelegator_selectionColor, selectorColor)

        tArray.recycle()
        //********************************************************************************

        // ViewPager for months
        month = ViewPager2(context)
        month.setPadding(5.dp)
        month.clipToPadding = false
        month.clipChildren =false
        month.setPageTransformer(MarginPageTransformer(10.dp))


        // Month scroll
        month.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {

                monthScrolled(months?.get(position))
            }
            override fun onPageScrollStateChanged(state: Int) {

                onMonthChangeListener?.onMothChanged(months?.get(month.currentItem))
            }
        })
    }
    open fun monthScrolled(month:Month?){}

    /**
     * picked date listener from CalendarViewAdapter gets us Month index in adapter
     * selected date from month.
     */
    protected val pickedDateListener = object : MonthAdapter.OnDatePicked{
        override fun datePicked(date: BaseDate, position: Int) {
            if (selectedDate != date){
                selectedDate = date
                adapter.selectedDate = date
                oldPosition?.let {
                    if (oldPosition != position) adapter.notifyItemChanged(it)
                }
                if (oldPosition==null && position!=currentMonthIndex){
                    currentMonthIndex?.let { adapter.notifyItemChanged(it) }
                }
                oldPosition = position
            }
            onDateSelectedListener?.selectedDate(date)
        }
    }

    /**
     * font from attrs
     * if it is not set, we will take the default font
     */
    private fun delegateTypeFace(int: Int): Typeface?{
        return if (int==0) Typeface.DEFAULT
        else ResourcesCompat.getFont(context,int)
    }

    /**
     * text size should not exceed a certain size to avoid layout overflow / overlap
     * if text size is bigger than maximum suggested text size, we set it to maximum
     * if size is smaller than suggested minimum text size, we set it to minimum
     * else we set it with given attribute
     */
    private fun delegateTextSize(providedTextSize: Float): Float {

        return if (providedTextSize in minTextSize .. maxTextSize ) providedTextSize
        else if (providedTextSize > maxTextSize) maxTextSize
        else minTextSize
    }

    /**
     * we can set an array of days
     * we will assume array will always have 7 items and
     * no more no less, so won't check for that here
     */
    private fun delegateWeekDays(id: Int){
        if (id != 0) weekDays = resources.getStringArray(id)
    }
    /**
     * interface to notify parent when month is changed
     */
    interface OnMonthChangedListener{
        fun onMothChanged(month: Month?)
    }

    fun setOnMonthChangedListener(listener: OnMonthChangedListener){
        this.onMonthChangeListener = listener
    }

    protected open fun resetToCurrentMonth(){
        months?.let {
            month.setCurrentItem((it.size.div(2)),false)
        }
    }
}