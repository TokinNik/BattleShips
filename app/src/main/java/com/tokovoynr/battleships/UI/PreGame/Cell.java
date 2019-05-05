package com.tokovoynr.battleships.UI.PreGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.tokovoynr.battleships.R;

public class Cell extends android.support.v7.widget.AppCompatImageView implements View.OnTouchListener
{
    public enum CellType
    {
        EMPTY,
        SHIP,
        MINE
    }
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
                int[] cord = new int[2];
                getLocationOnScreen(cord);
                Log.d(TAG, "DOWN CELL X = " + cord[0] + " Y = " + cord[1]);
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.d(TAG, "MOVE CELL " + getId());
                return false;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "UP CELL");

                break;
            default:
                break;
        }
        return true;
    }

    public void setType(CellType type)
    {
        switch (type)
        {
            case EMPTY:
                Drawable img = getResources().getDrawable(R.drawable.cell);
                setImageDrawable(img);
                Log.d(TAG, "EMPTY");
                break;
            case SHIP:
                setImageDrawable(getResources().getDrawable(R.drawable.green_box));
                Log.d(TAG, "SHIP");
                break;
            case MINE:
                setImageDrawable(getResources().getDrawable(R.drawable.red_box));
                Log.d(TAG, "MINE");
                break;
            default:
                setImageDrawable(getResources().getDrawable(R.drawable.red_box));
                Log.d(TAG, "DEF");
                break;

        }
    }
}
