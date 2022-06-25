package com.tgk.Elet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

public class GridViewItem extends androidx.appcompat.widget.AppCompatTextView {

    Paint paint = new Paint();

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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw border for each cell
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

    }

    private void initTextView(Context context) {
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(context.getResources().getColor(R.color.lightGrey));
    }

}
