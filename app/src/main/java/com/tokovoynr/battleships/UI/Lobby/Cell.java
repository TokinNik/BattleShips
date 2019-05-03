package com.tokovoynr.battleships.UI.Lobby;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.tokovoynr.battleships.R;

public class Cell extends android.support.v7.widget.AppCompatImageView implements View.OnTouchListener
{
    public static final String TAG = "CELL_VIEW";
    public Cell(Context context)
    {
        super(context);
        setOnTouchListener(this);
    }

    public Cell(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);

        canvas.drawColor(paint.getColor());

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
                int[] x = new int[2];
                v.getLocationOnScreen(x);
                Log.d(TAG, "DOWN CELL " + v.getX() + " " + v.getY() + " " + x[0] + " " + x[1]);
                //setImageDrawable(getResources().getDrawable(R.drawable.red_box));
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "MOVE CELL " + this.getId());
                return false;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "UP CELL");

                break;
            default:
                break;
        }
        return true;
    }
}
