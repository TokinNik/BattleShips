package com.tokovoynr.battleships.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TableLayout;

import com.tokovoynr.battleships.R;

public class TestView extends View
{
    public static final String TAG = "TEST_VIEW";
    private float posLeft;
    private float posTop;
    private float posTopMax;
    private float posLeftMax;
    private float posLeftStart;
    private float posTopStart;
    private GestureDetector gestureDetector;

    public TestView(Context context, AttributeSet attr)
    {
        super(context, attr);
        posLeftStart = 100;
        posTopStart = 100;
        posLeft = posLeftStart;
        posTop = posTopStart;
        gestureDetector = new GestureDetector(context, new MyGestureListener());

       
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (gestureDetector.onTouchEvent(event))
            return true;
        return true;
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

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
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.red_box);
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);

        canvas.drawColor(paint.getColor());
        canvas.drawBitmap(tempBitmap, posLeft, posTop, null);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
        {
            posLeft -= distanceX;
            posTop -= distanceY;
            invalidate();
            return true;
        }


    }
}
