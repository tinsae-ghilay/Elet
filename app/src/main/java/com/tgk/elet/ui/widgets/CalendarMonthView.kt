package com.tgk.elet.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.tgk.elet.common.Preferences
import com.tgk.elet.geezDate.GeezDate
import com.tgk.elet.geezDate.HolyDay
import com.tgk.elet.geezDate.HolyMonth
import com.tgk.elet.localDate.DateLocal
import com.tgk.elet.temporal.BaseDate


/**
 * CalendarMonthView Extends MonthView
 * @see MonthView
 * @author Tinsae Ghilay
 * @since March 19,2023 Gregorian / March 10, 2015 Geez Calendar
 */
open class CalendarMonthView : MonthView {


    private val grid = Paint().also {
        it.color = offsetColor
        it.isAntiAlias = true
    }
    var showGregorianDates: Boolean = true
    val holidays:Array<HolyDay> get() = (month as HolyMonth).getHolyDays(Preferences.showEritrean,Preferences.showTigraian)

    constructor(context: Context): this(context,null)
    constructor(context: Context,attributeSet: AttributeSet?): this(context,attributeSet,0)
    constructor(context: Context,attributeSet: AttributeSet?,defStyle: Int)
            : super(context,attributeSet,defStyle){
        this.weekEndColor = Color.CYAN
        this.textSize = 50f
    }

    override fun drawBackGround(canvas: Canvas?, paint: Paint, x: Float, y: Float, r: Float) {
        if (showGregorianDates){
            canvas?.drawRect(x+2,y+2,x+cellWidth-2,y+cellHeight-2,paint)
        }else{
            super.drawBackGround(canvas, paint, x, y, r)
        }
    }

    override fun drawGeezDate(canvas: Canvas?, paint: Paint, date: BaseDate, x: Float, y: Float) {
        if (date.isHoliday() && date.isNotOffset()) {
            paint.color = indicatorColor
        }
        if (showGregorianDates){
            canvas?.drawText(date.date.toString(),x+(cellWidth/4f),y+(cellHeight*0.9f),paint)
        }else{
            super.drawGeezDate(canvas, paint, date, x, y)
        }
    }

    override fun drawGregorianDates(canvas: Canvas?,date: BaseDate,x: Float, y: Float, paint: Paint) {
        // if we have to show gregorian dates
        if (showGregorianDates) {
            paint.textSize = textSize*0.7f
            paint.color = textColor
            canvas?.drawText(
                DateLocal.fromJdn(
                    (date as GeezDate).julianDay).date.toString(),
                x+(cellWidth*0.8f),
                y+(cellHeight*0.3f),
                paint)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        // vertical lines
        if(showGregorianDates){
            for (d in 0..weeksInAMonth){
                canvas?.drawLine(0f,d*cellHeight,cellWidth*daysInAWeek,d*cellHeight,grid)
            }
            // horizontal lines
            for (d in 0 .. daysInAWeek){
                canvas?.drawLine(d*cellWidth,0f,cellWidth*d,cellHeight*weeksInAMonth,grid)
            }
        }
        super.onDraw(canvas)
    }
    private fun BaseDate.isHoliday():Boolean{
        if (holidays.isNotEmpty()){
            for (d in holidays){
                if (d.date == this.date) return true
            }
            return false
        }else return false
    }
}