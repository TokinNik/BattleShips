package com.tokovoynr.battleships.UI.PreGame;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.tokovoynr.battleships.game.Ship;


public class Shadow extends android.support.v7.widget.AppCompatImageView
{
    public Cell.CellType type;
    public int deckCount;
    public Ship.ShipDirection direction;

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

    public void setDeckCount(int deckCount) {
        this.deckCount = deckCount;
    }

    public Ship.ShipDirection getDirection() {
        return direction;
    }

    public void setDirection(Ship.ShipDirection direction) {
        this.direction = direction;
    }
}
