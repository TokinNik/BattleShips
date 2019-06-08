package com.tokovoynr.battleships.game;

import android.util.Log;

import com.tokovoynr.battleships.UI.PreGame.Cell;
import com.tokovoynr.battleships.UI.PreGame.Cell.CellType;

public class GameLogic
{
    public static final String TAG = "GAME_LOGIC";
    private static final int MAX_SHIP_1_COUNT = 4;
    private static final int MAX_SHIP_2_COUNT = 3;
    private static final int MAX_SHIP_3_COUNT = 2;
    private static final int MAX_SHIP_4_COUNT = 1;
    private static final int MAX_MINE_COUNT = 10;
    private static final int MAX_SHIP_COUNT = MAX_SHIP_1_COUNT + MAX_SHIP_2_COUNT + MAX_SHIP_3_COUNT + MAX_SHIP_4_COUNT;
    private Ship[] playerShips = new Ship[MAX_SHIP_COUNT + MAX_MINE_COUNT];
    private Ship[] enemyShips = new Ship[MAX_SHIP_COUNT + MAX_MINE_COUNT];
    private LogicCell[][] playerCells = new LogicCell[12][12];
    private LogicCell[][] enemyCells = new LogicCell[12][12];
    private boolean playerTurn = true;
    private boolean minePause = false;


    public GameLogic()
    {
        int i = 0;
        for (;i < MAX_SHIP_1_COUNT; i++)
        {
            playerShips[i] = new Ship(1, Ship.ShipDirection.UP);
            enemyShips[i] = new Ship(1, Ship.ShipDirection.UP);
        }
        for (; i < MAX_SHIP_1_COUNT + MAX_SHIP_2_COUNT; i++)
        {
            playerShips[i] = new Ship(2, Ship.ShipDirection.UP);
            enemyShips[i] = new Ship(2, Ship.ShipDirection.UP);
        }
        for (; i < MAX_SHIP_1_COUNT + MAX_SHIP_2_COUNT + MAX_SHIP_3_COUNT; i++)
        {
            playerShips[i] = new Ship(3, Ship.ShipDirection.UP);
            enemyShips[i] = new Ship(3, Ship.ShipDirection.UP);
        }
        for (; i < MAX_SHIP_COUNT; i++)
        {
            playerShips[i] = new Ship(4, Ship.ShipDirection.UP);
            enemyShips[i] = new Ship(4, Ship.ShipDirection.UP);
        }
        for (; i < MAX_SHIP_COUNT +  MAX_MINE_COUNT; i++)
        {
            playerShips[i] = new Ship(5, Ship.ShipDirection.UP);
            enemyShips[i] = new Ship(5, Ship.ShipDirection.UP);
        }
        for (int j = 0; j < 12; j++)
        {
            for (i = 0; i < 12; i++)
            {
                playerCells[i][j] = new LogicCell(12 * i + j +1);
            }
        }
        for (int j = 0; j < 12; j++)
        {
            for (i = 0; i < 12; i++)
            {
                enemyCells[i][j] = new LogicCell(12 * i + j +1);
            }
        }

    }

    public GameLogic(Ship[] playerShips)
    {
        this.playerShips = playerShips;
    }

    public GameLogic(Ship[] playerShips, Ship[] enemyShips)
    {
        this.playerShips = playerShips;
        this.enemyShips = enemyShips;
    }

    public ShootResult[] setShip(int anchorCell, int deckCount)//Этап расстановки
    {

        for (int i = 0; i < MAX_SHIP_COUNT + MAX_MINE_COUNT; i++)
        {
            if (!playerShips[i].isOnDesk() && playerShips[i].getDeckCount() == deckCount)
            {
                int[] cells;
                LogicCell cell;
                ShootResult[] result = new ShootResult[0];
                cell = findCell(anchorCell, true);
                switch (deckCount)
                {
                    case 1:
                        cell.setType(CellType.SHIP_1);
                        cell.setShip(playerShips[i]);
                        cells = new int[]{anchorCell};
                        playerShips[i].setCells(cells);
                        result = new ShootResult[]{new ShootResult(ShootResult.ResultType.SHIP_PART, CellType.SHIP_1, anchorCell, 1)};
                        break;
                    case 2:
                        if((anchorCell - 12) > 0)
                        {
                            cell.setType(CellType.SHIP_2);
                            cell.setShip(playerShips[i]);
                            findCell(anchorCell - 12, true).setShip(playerShips[i]);
                            cells = new int[]{anchorCell, anchorCell - 12};
                            playerShips[i].setCells(cells);
                            result = new ShootResult[]{new ShootResult(ShootResult.ResultType.SHIP_PART, CellType.SHIP_2, anchorCell, 2),
                                                        new ShootResult(ShootResult.ResultType.SHIP_PART, CellType.SHIP_2, anchorCell-12, 1)};
                        }
                        else
                            return new ShootResult[0];
                        break;
                    case 3:
                        if((anchorCell - 12) > 0 && (anchorCell + 12) < 145)
                        {
                            cell.setType(CellType.SHIP_3);
                            cell.setShip(playerShips[i]);
                            findCell(anchorCell - 12, true).setShip(playerShips[i]);
                            findCell(anchorCell + 12, true).setShip(playerShips[i]);
                            cells = new int[]{anchorCell, anchorCell - 12, anchorCell + 12};
                            playerShips[i].setCells(cells);
                            result = new ShootResult[]{new ShootResult(ShootResult.ResultType.SHIP_PART, CellType.SHIP_3, anchorCell, 2),
                                    new ShootResult(ShootResult.ResultType.SHIP_PART, CellType.SHIP_3, anchorCell-12, 1),
                                    new ShootResult(ShootResult.ResultType.SHIP_PART, CellType.SHIP_3, anchorCell+12, 3)};
                        }
                        else
                            return new ShootResult[0];
                        break;
                    case 4:
                        if((anchorCell - 24) > 0 && (anchorCell + 12) < 145)
                        {
                            cell.setType(CellType.SHIP_4);
                            cell.setShip(playerShips[i]);
                            findCell(anchorCell - 12, true).setShip(playerShips[i]);
                            findCell(anchorCell + 12, true).setShip(playerShips[i]);
                            findCell(anchorCell - 24, true).setShip(playerShips[i]);
                            cells = new int[]{anchorCell, anchorCell - 12, anchorCell - 24, anchorCell + 12};
                            playerShips[i].setCells(cells);
                            result = new ShootResult[]{new ShootResult(ShootResult.ResultType.SHIP_PART, CellType.SHIP_4, anchorCell, 3),
                                    new ShootResult(ShootResult.ResultType.SHIP_PART, CellType.SHIP_4, anchorCell-12, 2),
                                    new ShootResult(ShootResult.ResultType.SHIP_PART, CellType.SHIP_4, anchorCell-24, 1),
                                    new ShootResult(ShootResult.ResultType.SHIP_PART, CellType.SHIP_4, anchorCell+12, 4)};
                        }
                        else
                            return new ShootResult[0];
                        break;
                    case 5:
                        cell.setType(CellType.MINE);
                        cell.setShip(playerShips[i]);
                        cells = new int[]{anchorCell};
                        playerShips[i].setCells(cells);
                        result = new ShootResult[]{new ShootResult(ShootResult.ResultType.SHIP_PART, CellType.MINE, anchorCell, 1)};
                        break;
                }
                cell.setShip(playerShips[i]);
                playerShips[i].setAnchorCell(anchorCell);
                playerShips[i].setOnDesk(true);
                return result;
            }

        }
        return new ShootResult[0];
    }

    public ShootResult[] removeShip(int removeCell)//Этап расстановки
    {
        ShootResult[] results;
        int anchorCell;
        try
        {
            anchorCell = findCell(removeCell, true).getShip().getAnchorCell();
        }
        catch (NullPointerException c)
        {
            Log.e(TAG, "LogicCell with id " + removeCell + " don't have ship");
            return new ShootResult[0];
        }
        for (int i = 0; i < MAX_SHIP_COUNT + MAX_MINE_COUNT; i++)
        {
            if (playerShips[i].isOnDesk() && playerShips[i].getAnchorCell() == anchorCell)
            {
                playerShips[i].clear();
                results = new ShootResult[(playerShips[i].getDeckCount() > 4 ? 1: playerShips[i].getDeckCount())];//fixme если время будет
                int j = 0;
                for (int cellId : playerShips[i].getCells())
                {
                    LogicCell cell = findCell(cellId, true);
                    if (cell == null)
                        continue;
                    results[j] = new ShootResult(ShootResult.ResultType.SHIP_DESTROY, CellType.EMPTY, cellId, 0);
                    cell.clear();
                    j++;
                }
                return results;
            }
        }
        return new ShootResult[0];
    }

    public ShootResult[] rotateShip(int rotateCell)//Этап расстановки
    {
        ShootResult[] results = new ShootResult[0];
        int anchorCell;
        try
        {
            anchorCell = findCell(rotateCell, true).getShip().getAnchorCell();
        }
        catch (NullPointerException c)
        {
            Log.e(TAG, "LogicCell with id " + rotateCell + " don't have ship");
            return new ShootResult[0];
        }
        for (int i = 0; i < MAX_SHIP_COUNT + MAX_MINE_COUNT; i++)
        {
            if (playerShips[i].isOnDesk() && playerShips[i].getAnchorCell() == anchorCell)
            {
                switch (playerShips[i].getDeckCount())
                {
                    case 1:
                        playerShips[i].rotate();
                        results = new ShootResult[1];
                        results[0] = new ShootResult(ShootResult.ResultType.SHIP_PART, CellType.SHIP_1, playerShips[i].getDirection(), anchorCell);
                        break;
                    case 2:
                        switch (playerShips[i].getDirection())
                        {
                            case UP:

                                break;
                            case RIGHT:

                                break;
                            case DOWN:

                                break;
                            case LEFT:

                                break;
                        }
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }



                return results;
            }
        }

        return new ShootResult[0];
    }

    public void switchTurn()//Этап игры
    {
        if(playerTurn)
        {
            //Enemy.turn();
            playerTurn = false;
        }
        else
        {
            if(minePause)
            {
                minePause = false;
                //Enemy.turn();
                switchTurn();//вместо хода противника
            }
            else
            {
                playerTurn = true;
            }
        }
    }

    public ShootResult shoot(int targetCell, boolean playerTurn)//Этап игры
    {
        ShootResult shootResult;
        CellType type = CellType.ERR;
        LogicCell lc = findCell(targetCell, playerTurn);;
        try
        {
            type = lc.getType();
        }
        catch (NullPointerException c)
        {
            Log.d(TAG, "Can't find LogicCell with id " + targetCell);
            return new ShootResult(ShootResult.ResultType.EMPTY, type, 0,0);
        }
        switch(type)
        {
            case EMPTY:
                int num = mathCountAroundCell(lc);
                shootResult = new ShootResult(ShootResult.ResultType.EMPTY, type, num,0);
                switchTurn();
                break;
            case MINE:
                minePause = true;
                shootResult = new ShootResult(ShootResult.ResultType.MINE, type, 0,0);
                break;
            case SHIP_1:
                if (minePause) minePause = false;
                shootResult = new ShootResult(ShootResult.ResultType.SHIP_DESTROY, type, 0,0);
                break;
            case SHIP_2:
                if (minePause) minePause = false;
                if(lc.getShip() != null)
                {
                    if (lc.getShip().destroyPart(lc.getId()))
                        shootResult = new ShootResult(ShootResult.ResultType.SHIP_DESTROY, type, 0,0);
                    else
                        shootResult = new ShootResult(ShootResult.ResultType.SHIP_PART, type, 0,0);
                }
                else
                    shootResult = new ShootResult(ShootResult.ResultType.EMPTY, CellType.ERR, 0,0);
                break;
            case SHIP_3:
                if (minePause) minePause = false;
                if(lc.getShip() != null)
                {
                    if (lc.getShip().destroyPart(lc.getId()))
                        shootResult = new ShootResult(ShootResult.ResultType.SHIP_DESTROY, type, 0,0);
                    else
                        shootResult = new ShootResult(ShootResult.ResultType.SHIP_PART, type, 0,0);
                }
                else
                    shootResult = new ShootResult(ShootResult.ResultType.EMPTY, CellType.ERR, 0,0);
                break;
            case SHIP_4:
                if (minePause) minePause = false;
                if(lc.getShip() != null)
                {
                    if (lc.getShip().destroyPart(lc.getId()))
                        shootResult = new ShootResult(ShootResult.ResultType.SHIP_DESTROY, type, 0,0);
                    else
                        shootResult = new ShootResult(ShootResult.ResultType.SHIP_PART, type, 0,0);
                }
                else
                    shootResult = new ShootResult(ShootResult.ResultType.EMPTY, CellType.ERR, 0,0);
                break;
            case ERR:
                shootResult = new ShootResult(ShootResult.ResultType.EMPTY, type, 0,0);
                break;
            default:
                shootResult = new ShootResult(ShootResult.ResultType.EMPTY, CellType.ERR, 0,0);
                break;
        }
        return shootResult;
    }

    private LogicCell findCell(int tag, boolean playerTurn)//Любой этап
    {
        if (playerTurn)
        {
            for (LogicCell[] lc : playerCells)//TODO сделать улучшенный поиск
            {
                for (int i = 0; i < 12; i++)
                {
                    if (lc[i].getId() == tag)
                        return lc[i];
                }
            }
        }
        else
        {
            for (LogicCell[] lc : enemyCells)
            {
                for (int i = 0; i < 12; i++)
                {
                    if (lc[i].getId() == tag)
                        return lc[i];
                }
            }
        }
        return null;
    }

    public boolean checkPosition(int id)//Этап расстановки
    {
        int checkI = -1;
        for (int i = 0; i < MAX_SHIP_COUNT + MAX_MINE_COUNT; i++)
        {
            if (playerShips[i].getAnchorCell() == id)
            {
                checkI = i;
                break;
            }
        }
        if (checkI == -1)
            return true;
        else
        {
            int[] cord = Cell.getCordInt(playerShips[checkI].getAnchorCell());
            int i1 = -1, j1 = -1;
            int i2 = 2, j2 = 2;
            switch (playerShips[checkI].getDeckCount())
            {
                case 1:
                    break;
                case 2:
                    switch (playerShips[checkI].getDirection())
                    {
                        case UP:
                            j1 = -2;
                            break;
                        case DOWN:
                            j2 = 3;
                            break;
                        case LEFT:
                            i1 = -2;
                            break;
                        case RIGHT:
                            i2 = 3;
                            break;
                    }
                    break;
                case 3:
                    switch (playerShips[checkI].getDirection())
                    {
                        case UP:
                            j1 = -2;
                            j2 = 3;
                            break;
                        case DOWN:
                            j1 = -2;
                            j2 = 3;
                            break;
                        case LEFT:
                            i1 = -2;
                            i2 = 3;
                            break;
                        case RIGHT:
                            i1 = -2;
                            i2 = 3;
                            break;
                    }
                    break;
                case 4:
                    switch (playerShips[checkI].getDirection())
                    {
                        case UP:
                            j1 = -3;
                            j2 = 3;
                            break;
                        case DOWN:
                            j1 = -2;
                            j2 = 4;
                            break;
                        case LEFT:
                            i1 = -3;
                            i2 = 3;
                            break;
                        case RIGHT:
                            i1 = -2;
                            i2 = 4;
                            break;
                    }
                    break;
                default:
                    break;
            }
            for (int ii = i1; ii < 12; ii++)
            {
                for (int jj = j1; jj < j2; jj++)
                {
                    if (playerCells[cord[1]+ii][cord[0]+jj].getShip() != null)
                    {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private int mathCountAroundCell(LogicCell logicCell)//Этап игры
    {
        int num = 0;

        if (!playerTurn)
        {
            for (int i = -1; i < 2; i++)
            {
                for (int j = -1; j < 2; j++)
                {
                    if(playerCells[i][j].getType() != CellType.EMPTY)
                    {
                        num++;
                    }
                }
            }
        }
        else
        {
            for (int i = -1; i < 2; i++)
            {
                for (int j = -1; j < 2; j++)
                {
                    if(enemyCells[i][j].getType() != CellType.EMPTY)
                    {
                        num++;
                    }
                }
            }
        }

        return num;
    }

    public void clearAll()//Любой этап
    {
        for (int j = 0; j < 12; j++)
        {
            for (int i = 0; i < 12; i++)
            {
                playerCells[i][j].clear();
                enemyCells[i][j].clear();
            }
        }
        for (int i = 0; i < 12; i++)
        {
            playerShips[i].clear();
            enemyShips[i].clear();
        }

        minePause = false;
        playerTurn = true;
    }

    public void logAllFields()
    {
        String text = "";
        Log.d(TAG, "PLAYER FIELD\n");
        for (int i = 0; i < 12; i++)
        {
            for (int j = 0; j < 12; j++)
            {
                switch(playerCells[i][j].getType())
                {
                        case EMPTY: text += "0";
                            break;
                        case MINE: text += "*";
                            break;
                        case SHIP_1: text += "1";
                            break;
                        case SHIP_2: text += "2";
                            break;
                        case SHIP_3: text += "3";
                            break;
                        case SHIP_4: text += "4";
                            break;
                        case ERR: text += "E";
                            break;
                        default:
                            break;
                }
            }
            Log.d("", text);
            text = "";

        }
        Log.d(TAG, "ENEMY FIELD\n");
        for (int i = 0; i < 12; i++)
        {
            for (int j = 0; j < 12; j++)
            {
                switch(enemyCells[i][j].getType())
                {
                    case EMPTY: text += "0";
                        break;
                    case MINE: text += "*";
                        break;
                    case SHIP_1: text += "1";
                        break;
                    case SHIP_2: text += "2";
                        break;
                    case SHIP_3: text += "3";
                        break;
                    case SHIP_4: text += "4";
                        break;
                    case ERR: text += "E";
                        break;
                    default:
                        break;
                }
            }
            Log.d("", text);
            text = "";
        }
    }

    public Ship[] getPlayerShips() {
        return playerShips;
    }

    public void setPlayerShips(Ship[] playerShips) {
        this.playerShips = playerShips;
    }

    public Ship[] getEnemyShips() {
        return enemyShips;
    }

    public void setEnemyShips(Ship[] enemyShips) {
        this.enemyShips = enemyShips;
    }
}
