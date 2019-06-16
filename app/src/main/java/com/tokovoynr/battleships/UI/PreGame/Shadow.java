package com.tokovoynr.battleships.UI.PreGame;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.tokovoynr.battleships.R;
import com.tokovoynr.battleships.game.Ship;


public class Shadow extends android.support.v7.widget.AppCompatImageView
{
    private Cell.CellType type;
    private int deckCount;
    private Ship.ShipDirection direction = Ship.ShipDirection.UP;
    private boolean errrMode = false;

    public Shadow(Context context) {
        super(context);
    }

    public Shadow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Shadow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Cell.CellType getType() {
        return type;
    }

    public void setType(Cell.CellType type) {
        this.type = type;
    }

    public int getDeckCount() {
        return deckCount;
    }

    public void setDeckCount(int deckCount)
    {
        this.deckCount = deckCount;
    }

    public Ship.ShipDirection getDirection() {
        return direction;
    }

    public void setDirection(Ship.ShipDirection direction)
    {
        this.direction = direction;
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
    }

    public void enableErr()
    {
        errrMode = true;

        switch (deckCount)
        {
            case 1:
                setImageDrawable(getResources().getDrawable(R.drawable.ship_1_err));
                break;
            case 2:
                setImageDrawable(getResources().getDrawable(R.drawable.ship_2_err));
                break;
            case 3:
                setImageDrawable(getResources().getDrawable(R.drawable.ship_3_err));
                break;
            case 4:
                setImageDrawable(getResources().getDrawable(R.drawable.ship_4_err));
                break;
        }
    }
    public void disableErr()
    {
        if(errrMode)
        {
            switch (deckCount)
            {
                case 1:
                    setImageDrawable(getResources().getDrawable(R.drawable.ship_1));
                    break;
                case 2:
                    setImageDrawable(getResources().getDrawable(R.drawable.ship_2));
                    break;
                case 3:
                    setImageDrawable(getResources().getDrawable(R.drawable.ship_3));
                    break;
                case 4:
                    setImageDrawable(getResources().getDrawable(R.drawable.ship_4));
                    break;
            }
            errrMode = false;
        }
    }

    public boolean isErrrMode() {
        return errrMode;
    }
}
