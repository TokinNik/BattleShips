package com.tokovoynr.battleships.game;

import android.util.Log;

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
    private ShipDirection direction = ShipDirection.UP;;
    private int[] cells;
    private boolean onDesk = false;
    private boolean destroyed = false;

    public Ship(int deckCount)
    {
        this.deckCount = deckCount;
        this.cells = new int[deckCount];
    }

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

    public static ShipDirection rotate(ShipDirection oldDirection)
    {
        switch (oldDirection)
        {
            case UP:
                return ShipDirection.RIGHT;
            case RIGHT:
                return ShipDirection.DOWN;
            case DOWN:
                return ShipDirection.LEFT;
            case LEFT:
                return ShipDirection.UP;
            default:
                return ShipDirection.UP;
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
                if(onDesk)
                    num++;
                else
                {
                    cells[i] = id;
                    return false;
                }
            }
        }

        return num == deckCount;
    }

    public boolean isDestroy()
    {
        for (int i = 0; i < cells.length; i++)
        {
            if (cells[i] != 0)
            {
                return false;
            }
        }
        destroyed = true;
        return true;
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

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
