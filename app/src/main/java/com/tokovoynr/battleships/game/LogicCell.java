package com.tokovoynr.battleships.game;

import com.tokovoynr.battleships.UI.PreGame.Cell;

public class LogicCell
{
    public static final String TAG = "LOGIC_CELL";
    private int id;

    private Cell.CellType type;
    private Ship ship;
    private int partNum = -1;

    LogicCell(int id)
    {
        type = Cell.CellType.EMPTY;
        this.id = id;
    }

    public LogicCell(Cell.CellType type, int id) {
        this.type = type;
        this.id = id;
    }

    public LogicCell(Cell.CellType type, int id, Ship ship)
    {
        this.type = type;
        this.id = id;
        this.ship = ship;
    }

    void clear()
    {
        type = Cell.CellType.EMPTY;
        partNum = -1;
        ship = null;
    }

    public Cell.CellType getType() {
        return type;
    }

    public void setType(Cell.CellType type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship)
    {
        this.ship = ship;
    }

    public int getPartNum() {
        return partNum;
    }

    public void setPartNum(int partNum) {
        this.partNum = partNum;
    }
}
