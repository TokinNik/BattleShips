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
        SHIP_DESTROY;
    }
    private ResultType result;
    private Cell.CellType type;
    private int numArg1;
    private int numArg2;

    public ResultType getResult() {
        return result;
    }

    public ShootResult(ResultType result, Cell.CellType type, int num1, int num2)
    {
        this.result = result;
        this.type = type;
        this.numArg1 = num1;
        this.numArg2 = num2;
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
}
