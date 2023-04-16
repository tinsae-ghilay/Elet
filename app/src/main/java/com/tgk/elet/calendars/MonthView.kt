package com.tgk.elet.calendars

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.tgk.elet.geezDate.GeezDate
import com.tgk.elet.geezDate.HolyMonth
import com.tgk.elet.localDate.DateLocal
import com.tgk.elet.temporal.BaseDate
import com.tgk.elet.temporal.Month

/**
 * MonthView Extends View and puts a List of dates in a grid of 7 days and 6 weeks
 * @author Tinsae Ghilay
 * @since March 19,2023 Gregorian / March 10, 2015 Geez Calendar
 * @see View
 */
open class MonthView : View {

    private val rect = Rect()
    private val superText:TextPaint = TextPaint().also {
        it.textAlign = Paint.Align.CENTER
    }

    //********* booleans **********
    var isGeezMonth: Boolean = true
        set(value) {
            field = value
            invalidate()
        }

    //********** Temporal ************
    var today:BaseDate = GeezDate.now()
        set(value) {
            field = value
            invalidate()
        }
    open var month:Month = HolyMonth(today.year,today.month)
        set(value) {
            field = value
            invalidate()
        }
    var selectedDate:BaseDate? = null
       set(value) {
           field = value
           invalidate()
       }

    //******* constants ********
    protected val daysInAWeek = 7
    protected val weeksInAMonth= 6

    //*********** Paints **************
    private val datePaint = TextPaint()
    private val selector = Paint()
    private val indicator = Paint()

    //************************ listener ******************************
    var onDateSelectedListener: OnDateSelectedListener?= null

    //***************** cell dimens ********************
    protected val cellWidth get() = (this.width/ daysInAWeek).toFloat()
    protected val cellHeight get() = (this.height-paddingTop-paddingBottom)/6f
    private val radius get() = cellHeight/2f
    private val median get() = cellWidth/2f

    //************* dimens *****************
    var textSize:Float = 40f
        set(value) {
            field = value
            invalidate()
        }

    //******************************** Colors *************************************
    var weekEndColor: Int = Color.argb(255,37,150,190)
    var insetColor: Int = Color.DKGRAY
        set(value) {
            field = value
            invalidate()
        }
    protected var textColor = insetColor
    var offsetColor: Int = Color.LTGRAY
        set(value) {
            field = value
            invalidate()
        }
    var indicatorColor: Int = Color.argb(255,255,67,11)
        set(value) {
            field = value
            indicator.color = field
            invalidate()
        }
    var selectorColor: Int = Color.LTGRAY
        set(value) {
            field = value
            invalidate()
        }

    constructor(context: Context):this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context,attrs,0)
    constructor(context: Context,attrs: AttributeSet?,defStyle: Int)
            : super(context, attrs,defStyle){

        initPaints()
        today = if (isGeezMonth) GeezDate.now() else DateLocal.now()
        this.setOnClickListener{
            this.performClick()

        }
    }

    override fun onDraw(canvas: Canvas?) {
        var column = 0
        var row = 0
        datePaint.textSize = textSize

        for (day in month.dates){

            //val date = day.date.toString()
            // paint anchor coordinates
            val dayStart = column*cellWidth
            val weekStart = row*cellHeight
            if (day.isNotOffset()){

                // indicating selected date
                if (day.equals(selectedDate))
                    drawBackGround(canvas,selector,dayStart,weekStart,radius)

                // indicate current date
                if (day.equals(today))
                    drawBackGround(canvas,indicator,column*cellWidth,row*cellHeight,radius)
            }

            // date text color
            textColor = if (!day.isNotOffset()) offsetColor
            else if (column.isWeekEnd()) weekEndColor
            else insetColor

            datePaint.color = textColor
            drawGeezDate(canvas,datePaint,day,dayStart,weekStart)
            drawGregorianDates(canvas,day,dayStart,weekStart,superText)

            // update column and row
            if (column < weeksInAMonth) column++
            else{
                column = 0
                row++
            }
        }
    }

    private fun initPaints(){
        datePaint.textAlign = Paint.Align.CENTER
        datePaint.style = Paint.Style.FILL
        selector.color = selectorColor
        selector.style = Paint.Style.FILL
        indicator.color = indicatorColor
        indicator.style = Paint.Style.STROKE
        indicator.strokeWidth = 3f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val calculatedHeight = (widthMeasureSpec*0.75).toInt()+paddingBottom+paddingTop
        setMeasuredDimension(widthMeasureSpec,calculatedHeight)
    }

    private fun Int.isWeekEnd(): Boolean = this == 0 || this == 6

    protected fun BaseDate.isNotOffset():Boolean = this.month == this@MonthView.month.month

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event?.action == MotionEvent.ACTION_UP){
            val xCoordinate = event.x.div(cellWidth).toInt()
            val yCoordinate = event.y.div(cellHeight).toInt()
            val index = getIndexFromCoordinates(xCoordinate,yCoordinate)
            index?.let {
                try {
                    month.dates[it].also { date->
                        return if (date.isNotOffset()){
                            selectedDate = date
                            onDateSelectedListener?.selectedDate(month.dates[index])
                            true
                        }else
                            false
                    }
                }catch (e: Exception){
                    //Log.d("MonthView", "On touch event returned an exception: ${e.message}")
                    return false
                }
            }
        }
        return super.onTouchEvent(event)
    }

    // gives us the cell index from touch coordinates
    private fun getIndexFromCoordinates(x: Int?, y: Int?) :Int?{

        return if (x!=null && y!=null) (y* daysInAWeek)+x else null
    }

    private fun getTextHeight():Float {
        datePaint.getTextBounds("0",0,1,rect)
        val height = rect.height()
        return height/2f
    }

    protected open fun drawBackGround(canvas: Canvas?, paint: Paint, x: Float, y: Float, r: Float){

        canvas?.drawCircle(x+median,y+radius,r-2,paint)
    }

    protected open fun drawGeezDate(canvas: Canvas?,paint: Paint,date:BaseDate,x:Float,y: Float){
        val day = date.date.toString()
        val xAxis = x+median
        val yAxis = y+radius+getTextHeight()
        canvas?.drawText(day,xAxis,yAxis,paint)
    }

    protected open fun drawGregorianDates(canvas: Canvas?, date: BaseDate,x: Float,y: Float,paint: Paint){
        // this is here so that we can override it in sub-classes
    }
}