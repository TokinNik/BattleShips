package com.tokovoynr.battleships.UI.PreGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.tokovoynr.battleships.R;
import com.tokovoynr.battleships.game.Ship;

public class Cell extends android.support.v7.widget.AppCompatImageView implements View.OnTouchListener
{
    public enum CellType
    {
        EMPTY,
        SHIP_1,
        SHIP_2,
        SHIP_3,
        SHIP_4,
        MINE,
        ERR

    }
    public static final String TAG = "CELL_VIEW";
    private OnCellListener listener;
    private CellType type = CellType.EMPTY;
    private Ship.ShipDirection direction = Ship.ShipDirection.UP;
    private int partNum = -1;
    private boolean destroyed = false;
    private boolean playersField = true;


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

    void clear()
    {
        type = CellType.EMPTY;
        direction = Ship.ShipDirection.UP;
        partNum = -1;
        destroyed = false;
        changeCellState(null,null,0);
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
                Log.d(TAG, "DOWN CELL " + getTag() + " " + type);
                if (type != CellType.ERR)
                {
                    listener.onCellTouch(v, event);
                }
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

    public void changeCellState(@Nullable CellType type, @Nullable Ship.ShipDirection direction, int partNum)
    {
        //Log.d(TAG, "changeCellState: type= " + type + " dir= " + direction + " pnum= " + partNum);

        if (type == null)
            type = this.type;
        if (direction == null)
            direction = this.direction;

        //Log.d(TAG, "changeCellState2: type= " + type + " dir= " + direction + " pnum= " + partNum);

        switch (direction)
        {
            case UP:
                setRotation(0f);
                break;
            case LEFT:
                setRotation(270f);
                break;
            case DOWN:
                setRotation(180f);
                break;
            case RIGHT:
                setRotation(90f);
                break;
        }

        switch (type)
        {
            case EMPTY:
                Drawable img = getResources().getDrawable(R.drawable.cell);
                switch (partNum)
                {
                    case -1:
                        break;
                    case 0:
                       img = getResources().getDrawable(R.drawable.cell_num_0);
                        break;
                    case 1:
                        img = getResources().getDrawable(R.drawable.cell_num_1);
                        break;
                    case 2:
                        img = getResources().getDrawable(R.drawable.cell_num_2);
                        break;
                    case 3:
                        img = getResources().getDrawable(R.drawable.cell_num_3);
                        break;
                    case 4:
                        img = getResources().getDrawable(R.drawable.cell_num_4);
                        break;
                    case 5:
                        img = getResources().getDrawable(R.drawable.cell_num_5);
                        break;
                    case 6:
                        img = getResources().getDrawable(R.drawable.cell_num_6);
                        break;
                    case 7:
                        img = getResources().getDrawable(R.drawable.cell_num_7);
                        break;
                    case 8:
                        img = getResources().getDrawable(R.drawable.cell_num_8);
                        break;
                    default:
                        break;
                }
                setImageDrawable(img);
                break;
            case SHIP_1:
                if (destroyed)
                    if(playersField)
                        setImageDrawable(getResources().getDrawable(R.drawable.ship_1));
                    else
                        setImageDrawable(getResources().getDrawable(R.drawable.ship_1));
                else if(playersField)
                    setImageDrawable(getResources().getDrawable(R.drawable.ship_1));
                else
                    setImageDrawable(getResources().getDrawable(R.drawable.ship_1));
                break;
            case SHIP_2:
                //TODO setRotation() вместо switch(direction) проверить
                switch (partNum)
                {
                    case 1:
                        setImageDrawable(getResources().getDrawable(R.drawable.ship_3_head_up));
                        break;
                    case 2:
                        setImageDrawable(getResources().getDrawable(R.drawable.ship_3_stern_up));
                        break;
                    default:
                        break;
                }
                break;
            case SHIP_3:
                switch (partNum)
                {
                    case 1:
                        setImageDrawable(getResources().getDrawable(R.drawable.ship_3_head_up));
                        break;
                    case 2:
                        setImageDrawable(getResources().getDrawable(R.drawable.ship_3_mid_up));
                        break;
                    case 3:
                        setImageDrawable(getResources().getDrawable(R.drawable.ship_3_stern_up));
                        break;
                    default:
                        break;
                }
                break;
            case SHIP_4:
                switch (partNum)
                {
                    case 1:
                        setImageDrawable(getResources().getDrawable(R.drawable.ship_3_head_up));
                        break;
                    case 2:
                        setImageDrawable(getResources().getDrawable(R.drawable.ship_3_mid_up));
                        break;
                    case 3:
                        setImageDrawable(getResources().getDrawable(R.drawable.ship_3_mid_up));
                        break;
                    case 4:
                        setImageDrawable(getResources().getDrawable(R.drawable.ship_3_stern_up));
                        break;
                    default:
                        break;
                }
                break;
            case MINE:
                if (destroyed)
                    if(playersField)
                        setImageDrawable(getResources().getDrawable(R.drawable.mine_active));
                    else
                        setImageDrawable(getResources().getDrawable(R.drawable.mine_active));
                else
                if(playersField)
                    setImageDrawable(getResources().getDrawable(R.drawable.mine_active));
                else
                    setImageDrawable(getResources().getDrawable(R.drawable.mine_active));
                break;
            case ERR:
                if (this.type != CellType.EMPTY)
                    setImageDrawable(getResources().getDrawable(R.drawable.red_box));
                break;
            default:
                setImageDrawable(getResources().getDrawable(R.drawable.white_box));
                break;
        }
        this.type = type;
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

    public static int[] getCordInt(int id)
    {
        int[] coordinate = new int[2];
        coordinate[0] = id%12;
        if(coordinate[0] == 0)
            coordinate[0] = 12;

        coordinate[1] = coordinate[0] == 12 ? (id / 12) : ((id / 12) + 1);

        return coordinate;
    }

    public int getIntTag()
    {
        return Integer.parseInt(getTag().toString());
    }

    public void setType(CellType type)
    {
        this.type = type;
        changeCellState(null, null, partNum);
    }

    public boolean isPlayersField() {
        return playersField;
    }

    public void setPlayersField(boolean playersField) {
        this.playersField = playersField;
    }

    public Ship.ShipDirection getDirection() {
        return direction;
    }

    public void setDirection(Ship.ShipDirection direction)
    {
        this.direction = direction;
        changeCellState(null, null, partNum);
    }

    public int getPartNum() {
        return partNum;
    }

    public void setPartNum(int partNum)
    {
        this.partNum = partNum;
        changeCellState(null, null, partNum);
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed)
    {
        this.destroyed = destroyed;
        changeCellState(null, null, partNum);
    }

    public void setListener(OnCellListener listener) {
        this.listener = listener;
    }
}
