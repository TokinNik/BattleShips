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
import com.tokovoynr.battleships.UI.MainActivity;

public class Cell extends android.support.v7.widget.AppCompatImageView implements View.OnTouchListener
{
    public enum CellType
    {
        EMPTY,
        SHIP_1,
        SHIP_2,
        SHIP_3,
        SHIP_4,
        MINE
    }
    public static final String TAG = "CELL_VIEW";
    private OnCellListener listener;
    private CellType type = CellType.EMPTY;
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
                Log.d(TAG, "DOWN CELL " + getTag() + " " + type);
                listener.onCellTouch(v, event);
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.d(TAG, "MOVE CELL " + getId());
                return false;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "UP CELL " + getTag());

                break;
            default:
                break;
        }
        return false;
    }

    public interface OnCellListener
    {
        void onCellTouch(View v, MotionEvent event);
    }

    public void setType(CellType type)
    {
        this.type = type;
        switch (type)
        {
            case EMPTY:
                Drawable img = getResources().getDrawable(R.drawable.cell);
                setImageDrawable(img);
                Log.d(TAG, "EMPTY");
                break;
            case SHIP_1:
                setImageDrawable(getResources().getDrawable(R.drawable.green_box));
                Log.d(TAG, "SHIP_1");
                break;
            case SHIP_2:
                setImageDrawable(getResources().getDrawable(R.drawable.green_box));
                Log.d(TAG, "SHIP_1");
                break;
            case SHIP_3:
                setImageDrawable(getResources().getDrawable(R.drawable.green_box));
                Log.d(TAG, "SHIP_1");
                break;
            case SHIP_4:
                setImageDrawable(getResources().getDrawable(R.drawable.green_box));
                Log.d(TAG, "SHIP_1");
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

    public CellType getType() {
        return type;
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

    public int[] getCordInt(int id)
    {
        int[] coordinate = new int[2];
        coordinate[0] = id%12;
        if(coordinate[0] == 0)
            coordinate[0] = 12;

        coordinate[1] = coordinate[0] == 12 ? (id / 12) : ((id / 12) + 1);

        return coordinate;
    }

    public void setListener(OnCellListener listener) {
        this.listener = listener;
    }
}
