package com.tgk.elet.ui.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.card.MaterialCardView
import com.tgk.Elet.R
import com.tgk.elet.ui.widgets.Unit.sp

class SwitchView: MaterialCardView{

    var onSwitchAction: OnSwitchAction? = null
    private var leftSwitch: TextView
    private var rightSwitch: TextView
    private var highlightedColor:Int
    private var hintedColor:Int
    private var switchState = SwitchState.RIGHT
    private val abyss by lazy {
        ResourcesCompat.getFont(context,R.font.abyssinica)
    }

    constructor(context: Context) :this(context,null)
    constructor(context: Context, attributeSet: AttributeSet?) :this(context,attributeSet,0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) :super(context,attributeSet,defStyle) {

        // ******************************* reading attrs *******************************
        val tArray = context.obtainStyledAttributes(attributeSet, R.styleable.SwitchView)
        val leftText = tArray.getString(R.styleable.SwitchView_left_button_text)
        val rightText = tArray.getString(R.styleable.SwitchView_right_button_text)
        highlightedColor = tArray.getColor(R.styleable.SwitchView_highlighted_color,Color.DKGRAY)
        hintedColor = tArray.getColor(R.styleable.SwitchView_hinted_color, Color.LTGRAY)

        tArray.recycle()


        val switchParam = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        leftSwitch = TextView(context).also {
            it.layoutParams = switchParam
            it.text = leftText
            it.textSize = 8.sp
            it.gravity = Gravity.CENTER
            it.typeface = abyss
            it.setOnClickListener {
                toggleState(SwitchState.LEFT)
            }
        }
        rightSwitch = TextView(context).also {
            it.layoutParams = switchParam
            it.text = rightText
            it.textSize = 8.sp
            it.gravity = Gravity.CENTER
            it.typeface = abyss
            it.setOnClickListener {
                toggleState(SwitchState.RIGHT)
            }
        }
        val container = LinearLayout(context).also {
            it.orientation = LinearLayout.HORIZONTAL
            it.weightSum = 2F
            it.addView(leftSwitch)
            it.addView(rightSwitch)
        }
        this.addView(container)
        leftSwitch.performClick()

    }

    private fun toggleState(state:SwitchState){
        if (state != switchState){
            when(state){
                SwitchState.RIGHT -> {// switch to right
                    rightSwitch.highlight()
                    leftSwitch.hint()

                }
                else ->{// switch to left
                    leftSwitch.highlight()
                    rightSwitch.hint()

                }
            }
            onSwitchAction?.switchTo(state)
            switchState = state
        }
    }

    private fun TextView.hint(){
        this.setTextColor(highlightedColor)
        this.setBackgroundColor(hintedColor)
    }
    private fun TextView.highlight(){
        this.setTextColor(hintedColor)
        this.setBackgroundColor(highlightedColor)
    }

    interface OnSwitchAction{
        fun switchTo(state: SwitchState)
    }
    enum class SwitchState{
        LEFT, RIGHT
    }
}