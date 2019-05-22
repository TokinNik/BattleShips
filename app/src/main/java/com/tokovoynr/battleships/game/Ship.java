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
    private int[][] cells;
    private boolean onDesk = false;
    public Ship(int deckCount, ShipDirection direction)
    {
        this.deckCount = deckCount;
        this.direction = direction;
        this.cells = new int[9+3*deckCount-1][2];
    }

    public Ship(int deckCount, int anchorCell, ShipDirection direction)
    {
        this.deckCount = deckCount;
        this.anchorCell = anchorCell;
        this.direction = direction;
        this.cells = new int[9+3*deckCount-1][2];
        fillCellsAround();
    }


    public boolean checkArea(int xGrid, int yGrid)
    {
        if(!onDesk)
            return true;

        for (int[] cell: cells)
        {
            Log.d(TAG, "xG = " + xGrid + " yG = " + yGrid + " xC = " + cell[0] + " yC = " + cell[1]);
            if (cell[1] == xGrid && cell[0] == yGrid)
                return false;
        }

        return true;
    }

    private void fillCellsAround()
    {
        int[] cord = Cell.getCordInt(anchorCell);
        int i = -1, j = -1;
        switch (deckCount)
        {
            case 1:
                break;
            case 2:
                j = -2;
                break;
            case 3:
                i = -2;
                j = -2;
                break;
            case 4:
                i = -2;
                j = -3;
                break;

        }
        for (int[] cell: cells)
        {
            for (int ii = i; ii < 2; ii++)
            {
                for (int jj = j; jj < 2; jj++)
                {
                    if (ii == 0 && jj == 0)
                        continue;
                    else
                    {
                        cell[0] = cord[0] - ii;
                        cell[1] = cord[1] - jj;
                    }
                }
            }
        }
    }

    public void setDirection(ShipDirection direction) {
        this.direction = direction;
    }

    public void setCells(int[][] cells) {
        this.cells = cells;
    }

    public void setAnchorCell(int anchorCell)
    {
        this.anchorCell = anchorCell;
        fillCellsAround();
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

    public int[][] getCells() {
        return cells;
    }
}
