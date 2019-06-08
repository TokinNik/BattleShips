package com.tokovoynr.battleships.game;

import android.util.Log;

import com.tokovoynr.battleships.UI.PreGame.Cell;

public class Ship
{
    public enum ShipDirection
    {
        UP,
        DOWN,
        RIGHT,
        LEFT;
    }
    public static final String TAG = "SHIP";
    private int deckCount;

    private int anchorCell;
    private ShipDirection direction;
    private int[] cells;
    private boolean onDesk = false;
    public Ship(int deckCount, ShipDirection direction)
    {
        this.deckCount = deckCount;
        this.direction = direction;
        this.cells = new int[deckCount];
    }
    public Ship(int deckCount, int anchorCell, ShipDirection direction)
    {
        this.deckCount = deckCount;
        this.anchorCell = anchorCell;
        this.direction = direction;
        this.cells = new int[deckCount];
    }

    void clear()
    {
        anchorCell = 0;
        direction = ShipDirection.UP;
        for (int cell: cells)
        {
            cell = 0;
        }
        onDesk = false;
    }

    void rotate()
    {
        switch (direction)
        {
            case UP:
                direction = ShipDirection.RIGHT;
                break;
            case RIGHT:
                direction = ShipDirection.DOWN;
                break;
            case DOWN:
                direction = ShipDirection.LEFT;
                break;
            case LEFT:
                direction = ShipDirection.UP;
                break;
        }
    }

    public boolean destroyPart(int id)
    {
        int num = 0;
        for (int i = 0; i < cells.length; i++)
        {
            if(cells[i] == id)
            {
                cells[i] = 0;

            }
            if (cells[i] == 0)
            {
                num++;
            }
        }

        if(num == deckCount)
            return true;
        else
            return false;
    }

    public void setDirection(ShipDirection direction) {
        this.direction = direction;
    }

    public void setCells(int[] cells) {
        this.cells = cells;
    }

    public void setAnchorCell(int anchorCell)
    {
        this.anchorCell = anchorCell;
    }

    public void setOnDesk(boolean onDesk) {
        this.onDesk = onDesk;
    }

    public boolean isOnDesk() {
        return onDesk;
    }

    public int getDeckCount() {
        return deckCount;
    }

    public int getAnchorCell() {
        return anchorCell;
    }

    public ShipDirection getDirection() {
        return direction;
    }

    public int[] getCells() {
        return cells;
    }
}
