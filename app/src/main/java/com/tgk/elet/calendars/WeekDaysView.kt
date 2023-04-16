package com.tgk.elet.calendars

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.tgk.elet.calendars.Unit.sp

/**
 * WeekView extends View
 * @author Tinsae Ghilay
 * @see View
 */
class WeekDaysView: View {

    var typeface: Typeface? = Typeface.DEFAULT
        set(value) {
            if (field!=value){
                field = value
                dayPaint.typeface = typeface
                invalidate()
            }
        }
    private val dayPaint = TextPaint().also {
        it.textAlign = Paint.Align.CENTER
    }
    var textSize = 18.sp
        set(value) {
            field = value
            dayPaint.textSize = textSize
            invalidate()
        }
    // if no array for week day names is passed to attributes 
    // or set in code while instantiating the widget
    // the default value will be taken.
    var weekDays = arrayOf("ሰን","ሰኑ","ሰሉ","ረቡ","ሓሙ","ዓር","ቀዳ")
        set(value) {
            field = value
            invalidate()
        }
    var weekEndColor = Color.argb(255,37,150,190)
        set(value) {
            field = value
            invalidate()
        }
    var weekTextColor = Color.DKGRAY
        set(value) {
            field = value
            invalidate()
        }

    private val dayWidth get() = this.width/7f
    private val medianX get() = dayWidth/2f

    constructor(context: Context):this(context,null)
    constructor(context: Context,attrs:AttributeSet?):this(context,attrs,0)
    constructor(context: Context,attrs: AttributeSet?,defStyle: Int):super(context,attrs,defStyle){
        dayPaint.textSize = textSize
        dayPaint.typeface = typeface
    }

    override fun onDraw(canvas: Canvas?) {
        //super.onDraw(canvas)
        var xAxis: Float
        var yAxis: Float
        val offset = getTextHeight(dayPaint,weekDays[0])
        for ((column, day) in weekDays.withIndex()){
            xAxis = column.times(dayWidth).plus(medianX)+paddingStart
            yAxis = textSize+paddingTop.toFloat()+offset
            dayPaint.color = if (column == 0 || column == 6) weekEndColor else weekTextColor
            canvas?.drawText(day,xAxis,yAxis,dayPaint)
        }
    }

    private fun getTextHeight(textPaint: Paint, text: String):Float {
        val rect = Rect()
        textPaint.getTextBounds(text,0,1,rect)
        val height = rect.height()
        return height/2f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val preferredHeight: Int =
            (suggestedMinimumHeight+textSize.toInt().times(2) + paddingTop + paddingBottom)
        val preferredWidth: Int = widthMeasureSpec + paddingStart + paddingEnd
        val resolvedWidth = resolveSize(preferredWidth, widthMeasureSpec)
        val resolvedHeight = resolveSize(preferredHeight, heightMeasureSpec)
        setMeasuredDimension(resolvedWidth, resolvedHeight)
    }
}