package com.tgk.Elet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class GridViewItem extends androidx.appcompat.widget.AppCompatTextView {
    //Canvas canvas = new Canvas();
    Paint paint = new Paint();
    //Rect rect=new Rect();

    public GridViewItem(Context context) {
        super(context);
        initTextView(context);
    }

    public GridViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTextView(context);
    }

    public GridViewItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initTextView(context);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int quarter = widthMeasureSpec / 4;
        int height = widthMeasureSpec-quarter;
        super.onMeasure(widthMeasureSpec, height); // This is the key that will make the height equivalent to its width
        setMeasuredDimension(widthMeasureSpec, height);
    }

   /*private void drawRectangleTextBorder(Canvas canvas, Paint paint) {
        Rect rectToDraw = new Rect(1/2, 30, 1/2, 1/2);
        canvas.drawRect(rectToDraw, paint);
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        //drawRectangleTextBorder(canvas, paint);
        //canvas.drawRect(0, 0, getWidth(), getHeight() * 0.01f * 30, paint);
        //canvas.drawRect(0, measuredHeight * 0.01f * topHeight, measuredWidth, measuredHeight, bottomPaint);

    }

    private void initTextView(Context context) {
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(context.getResources().getColor(R.color.lightGrey));
    }

}
