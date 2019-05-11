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

    public CharSequence getCordString(int id)
    {
        String coordinate = "";
        int x = id%12;
        switch (x)
        {
            case 1:
                coordinate = getResources().getString(R.string.a);
                break;
            case 2:
                coordinate = getResources().getString(R.string.b);
                break;
            case 3:
                coordinate = getResources().getString(R.string.v);
                break;
            case 4:
                coordinate = getResources().getString(R.string.g);
                break;
            case 5:
                coordinate = getResources().getString(R.string.d);
                break;
            case 6:
                coordinate = getResources().getString(R.string.e);
                break;
            case 7:
                coordinate = getResources().getString(R.string.j);
                break;
            case 8:
                coordinate = getResources().getString(R.string.z);
                break;
            case 9:
                coordinate = getResources().getString(R.string.i);
                break;
            case 10:
                coordinate = getResources().getString(R.string.k);
                break;
            case 11:
                coordinate = getResources().getString(R.string.l);
                break;
            case 0:
                coordinate = getResources().getString(R.string.m);
                break;
        }

        coordinate += String.valueOf(x == 0 ? (id / 12) : ((id / 12) + 1));

        return coordinate;
    }
}
