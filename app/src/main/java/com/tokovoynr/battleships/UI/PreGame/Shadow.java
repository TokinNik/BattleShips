package com.tokovoynr.battleships.UI.PreGame;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;


public class Shadow extends android.support.v7.widget.AppCompatImageView
{
    public Cell.CellType type;

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
}
