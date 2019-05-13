package com.tokovoynr.battleships.game;

import com.tokovoynr.battleships.UI.PreGame.Cell;

public class Ship
{
    public enum ShipDirection
    {
        UP,
        DOWN,
        RIGHT,
        LEFT
    }

    public static final String TAG = "SHIP";
    private int deskCount;
    private int anchorCell;
    private ShipDirection direction;
    private Cell[] cells;
    private boolean onDesk = false;

    public Ship(int deskCount, ShipDirection direction)
    {
        this.deskCount = deskCount;
        this.direction = direction;
    }

    public Ship(int deskCount, int anchorCell, ShipDirection direction)
    {
        this.deskCount = deskCount;
        this.anchorCell = anchorCell;
        this.direction = direction;
    }

    public Ship(int deskCount, int anchorCell, ShipDirection direction, Cell[] cells)
    {
        this.deskCount = deskCount;
        this.anchorCell = anchorCell;
        this.direction = direction;
        this.cells = cells;
    }

    public void setDirection(ShipDirection direction) {
        this.direction = direction;
    }

    public void setCells(Cell[] cells) {
        this.cells = cells;
    }

    public void setAnchorCell(int anchorCell) {
        this.anchorCell = anchorCell;
    }

    public void setOnDesk(boolean onDesk) {
        this.onDesk = onDesk;
    }

    public boolean isOnDesk() {
        return onDesk;
    }

    public int getDeskCount() {
        return deskCount;
    }

    public int getAnchorCell() {
        return anchorCell;
    }

    public ShipDirection getDirection() {
        return direction;
    }

    public Cell[] getCells() {
        return cells;
    }
}
