package com.tokovoynr.battleships.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.tokovoynr.battleships.R;

public class TestUnit extends View
{
    final static String TAG = " TEST_UNIT";

    public TestUnit(Context context)
    {
        super(context);
    }

    public TestUnit(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public TestUnit(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        Log.d(TAG, "widthMeasureSpec " +  widthMeasureSpec + " | widthMeasureSpec " + widthMeasureSpec +
                " | widthSize " + widthSize + " | heightMode " + heightMode + " | heightSize  " + heightSize
                + " | orientation " + getResources().getConfiguration().orientation);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {

            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(widthSize, heightSize);
            } else {
                height = widthSize;
            }
            Log.d(TAG, "h " + height);
            setMeasuredDimension(widthSize, height);
        }
        else
        {
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(widthSize, heightSize);
            } else {
                width = heightSize;
            }
            Log.d(TAG, "w " + width);
            setMeasuredDimension(width, heightSize);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        Log.d(TAG, "onLayout: " + changed + " " + left + " " + top + " " + right + " " + bottom);
        super.onLayout(changed, left, top, right, bottom);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas)
    {
        Bitmap tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.red_box);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);

        canvas.drawColor(paint.getColor());
        canvas.drawBitmap(tempBitmap, 0, 0, null);

        super.onDraw(canvas);
    }
}
