package com.tokovoynr.battleships.game;

import com.tokovoynr.battleships.UI.PreGame.Cell;

public class ShootResult
{
    public static final String TAG = "SHOOT_RESULT";

    public  enum ResultType
    {
        EMPTY,
        MINE,
        SHIP_PART,
        SHIP_DESTROY,
        ENEMY_WIN,
        PLAYER_WIN
    }
    private ResultType result = ResultType.EMPTY;
    private Cell.CellType type = Cell.CellType.EMPTY;
    private Ship.ShipDirection direction = Ship.ShipDirection.UP;
    private int numArg1 = 0;
    private int numArg2 = 0;
    private boolean boolArg = false;

    public ResultType getResult() {
        return result;
    }

    public ShootResult(ResultType result, Cell.CellType type, int numArg1, int numArg2)
    {
        this.result = result;
        this.type = type;
        this.numArg1 = numArg1;
        this.numArg2 = numArg2;
    }
    public ShootResult(Ship.ShipDirection direction, Cell.CellType type, int numArg1, int numArg2)
    {
        this.type = type;
        this.direction = direction;
        this.numArg1 = numArg1;
        this.numArg2 = numArg2;
    }

    public ShootResult(Ship.ShipDirection direction, Cell.CellType type, int numArg1, int numArg2, boolean boolArg)
    {
        this.type = type;
        this.direction = direction;
        this.numArg1 = numArg1;
        this.numArg2 = numArg2;
        this.boolArg = boolArg;
    }

    public ShootResult(ResultType result, Cell.CellType type, Ship.ShipDirection direction, int numArg1, int numArg2, boolean boolArg)
    {
        this.result = result;
        this.type = type;
        this.direction = direction;
        this.numArg1 = numArg1;
        this.numArg2 = numArg2;
        this.boolArg = boolArg;
    }

    public static ShootResult parseForNum(int i)
    {
        switch (i)
        {
            case -3:
                return new ShootResult(ResultType.ENEMY_WIN, Cell.CellType.ERR, 0, 0);
            case -2:
                return new ShootResult(ResultType.PLAYER_WIN, Cell.CellType.ERR, 0, 0);
            case -1:
                return new ShootResult(ResultType.EMPTY, Cell.CellType.ERR, 0, 0);
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return new ShootResult(ResultType.EMPTY, Cell.CellType.EMPTY, i, 1);
            case 9:
                return new ShootResult(ResultType.MINE, Cell.CellType.MINE, 1, 1);
            case 10:
                return new ShootResult(ResultType.SHIP_PART, Cell.CellType.SHIP_4, 1, 1);
            case 11:
                return new ShootResult(ResultType.SHIP_DESTROY, Cell.CellType.SHIP_4, 1, 1);
            default:
                return new ShootResult(ResultType.EMPTY, Cell.CellType.ERR, 0, 0);
        }
    }

    int convertToNum()
    {
        switch (result)
        {
            case EMPTY:
                return numArg1;
            case MINE:
                return 9;
            case SHIP_PART:
                return 10;
            case SHIP_DESTROY:
                return 11;
            case PLAYER_WIN:
                return -2;
            case ENEMY_WIN:
                return -3;
            default:
                return -1;
        }

    }

    public void setResult(ResultType result) {
        this.result = result;
    }

    public Cell.CellType getType() {
        return type;
    }

    public void setType(Cell.CellType type) {
        this.type = type;
    }

    public int getNumArg1() {
        return numArg1;
    }

    public void setNumArg1(int numArg1) {
        this.numArg1 = numArg1;
    }

    public int getNumArg2() {
        return numArg2;
    }

    public void setNumArg2(int numArg2) {
        this.numArg2 = numArg2;
    }

    public Ship.ShipDirection getDirection() {
        return direction;
    }

    public void setDirection(Ship.ShipDirection direction) {
        this.direction = direction;
    }

    public boolean isBoolArg() {
        return boolArg;
    }

    public void setBoolArg(boolean boolArg) {
        this.boolArg = boolArg;
    }
}
