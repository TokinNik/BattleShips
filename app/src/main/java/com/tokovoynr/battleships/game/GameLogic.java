package com.tokovoynr.battleships.game;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tokovoynr.battleships.UI.GameFragment;
import com.tokovoynr.battleships.UI.MainActivity;
import com.tokovoynr.battleships.UI.PreGame.Cell;
import com.tokovoynr.battleships.UI.PreGame.Cell.CellType;
import com.tokovoynr.battleships.network.TestConnection;

public class GameLogic
{
    public enum GameMode
    {
        PvP,
        PvE
    }

    public static final String TAG = "GAME_LOGIC";
    public static final int MAX_SHIP_1_COUNT = 1;
    public static final int MAX_SHIP_2_COUNT = 1;
    public static final int MAX_SHIP_3_COUNT = 1;
    public static final int MAX_SHIP_4_COUNT = 1;
    public static final int MAX_MINE_COUNT = 0;
    public static final int MAX_SHIP_COUNT = MAX_SHIP_1_COUNT + MAX_SHIP_2_COUNT + MAX_SHIP_3_COUNT + MAX_SHIP_4_COUNT;
    private Ship[] playerShips = new Ship[MAX_SHIP_COUNT + MAX_MINE_COUNT];
    private Ship[] enemyShips = new Ship[MAX_SHIP_COUNT + MAX_MINE_COUNT];
    private LogicCell[][] playerCells = new LogicCell[12][12];
    private LogicCell[][] enemyCells = new LogicCell[12][12];
    private boolean playerTurn = true;
    private boolean minePause = false;
    private Bot enemyBot = new Bot(Bot.BotMode.EASY);
    private GameMode gameMode = GameMode.PvP;
    private TestConnection connector;
    private AppCompatActivity mainActivity;


    public GameLogic(AppCompatActivity activity)
    {
        mainActivity = activity;

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
                playerCells[i][j] = new LogicCell(12 * i + j + 1);
            }
        }
        for (int j = 0; j < 12; j++)
        {
            for (i = 0; i < 12; i++)
            {
                enemyCells[i][j] = new LogicCell(12 * i + j + 1);
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

    public ShootResult[] setShip(int anchorCell, int deckCount, Ship.ShipDirection direction, boolean playerTurn)//Этап расстановки
    {

        for (int i = 0; i < MAX_SHIP_COUNT + MAX_MINE_COUNT; i++)
        {
            Ship ship = (playerTurn ? playerShips[i] : enemyShips[i]);
            if (!ship.isOnDesk() && ship.getDeckCount() == deckCount)
            {
                int[] cells;
                int j = 1;
                LogicCell cell;
                ShootResult[] result = new ShootResult[0];
                cell = findCell(anchorCell, playerTurn);
                ship.setDirection(direction);
                switch (deckCount)
                {
                    case 1:
                        cell.setType(CellType.SHIP_1);
                        cell.setShip(ship);
                        cell.setPartNum(1);
                        cells = new int[]{anchorCell};
                        ship.setCells(cells);
                        result = new ShootResult[]{new ShootResult(direction, CellType.SHIP_1, anchorCell, 1)};
                        break;
                    case 2:
                        switch (ship.getDirection())
                        {
                            case UP:
                                if((anchorCell - 12) > 0)
                                {
                                    cells = new int[]{anchorCell - 12, anchorCell};
                                    j = 1;
                                    for (int c: cells)
                                    {
                                        cell = findCell(c, playerTurn);
                                        cell.setType(CellType.SHIP_2);
                                        cell.setShip(ship);
                                        cell.setPartNum(j);
                                        j++;
                                    }
                                    ship.setCells(cells);
                                    result = new ShootResult[]{new ShootResult(direction, CellType.SHIP_2, cells[1], 2),
                                            new ShootResult(direction, CellType.SHIP_2, cells[0], 1)};
                                }
                                else
                                    return new ShootResult[0];
                                break;
                            case RIGHT:
                                if((anchorCell%12) > 0)
                                {
                                    cells = new int[]{anchorCell + 1, anchorCell};
                                    j = 1;
                                    for (int c: cells)
                                    {
                                        cell = findCell(c, playerTurn);
                                        cell.setType(CellType.SHIP_2);
                                        cell.setShip(ship);
                                        cell.setPartNum(j);
                                        j++;
                                    }
                                    ship.setCells(cells);
                                    result = new ShootResult[]{new ShootResult(direction, CellType.SHIP_2, cells[1], 2),
                                            new ShootResult(direction, CellType.SHIP_2, cells[0], 1)};
                                }
                                else
                                    return new ShootResult[0];
                                break;
                            case DOWN:
                                if((anchorCell + 12) < 145)
                                {
                                    cells = new int[]{anchorCell + 12, anchorCell};
                                    j = 1;
                                    for (int c: cells)
                                    {
                                        cell = findCell(c, playerTurn);
                                        cell.setType(CellType.SHIP_2);
                                        cell.setShip(ship);
                                        cell.setPartNum(j);
                                        j++;
                                    }
                                    ship.setCells(cells);
                                    result = new ShootResult[]{new ShootResult(direction, CellType.SHIP_2, cells[1], 2),
                                            new ShootResult(direction, CellType.SHIP_2, cells[0], 1)};
                                }
                                else
                                    return new ShootResult[0];
                                break;
                            case LEFT:
                                if((anchorCell%12) > 1)
                                {
                                    cells = new int[]{anchorCell - 1, anchorCell};
                                    j = 1;
                                    for (int c: cells)
                                    {
                                        cell = findCell(c, playerTurn);
                                        cell.setType(CellType.SHIP_2);
                                        cell.setShip(ship);
                                        cell.setPartNum(j);
                                        j++;
                                    }
                                    ship.setCells(cells);
                                    result = new ShootResult[]{new ShootResult(direction, CellType.SHIP_2, cells[1], 2),
                                            new ShootResult(direction, CellType.SHIP_2, cells[0], 1)};
                                }
                                else
                                    return new ShootResult[0];
                                break;
                        }
                        break;
                    case 3:
                        switch (ship.getDirection())
                        {
                            case UP:
                                if((anchorCell - 12) > 0 && (anchorCell + 12) < 145)
                                {
                                    cells = new int[]{anchorCell - 12, anchorCell, anchorCell + 12};
                                    j = 1;
                                    for (int c: cells)
                                    {
                                        cell = findCell(c, playerTurn);
                                        cell.setType(CellType.SHIP_3);
                                        cell.setShip(ship);
                                        cell.setPartNum(j);
                                        j++;
                                    }
                                    ship.setCells(cells);
                                    result = new ShootResult[]{new ShootResult(direction, CellType.SHIP_3, anchorCell, 2),
                                            new ShootResult(direction, CellType.SHIP_3, anchorCell - 12, 1),
                                            new ShootResult(direction, CellType.SHIP_3, anchorCell + 12, 3)};
                                }
                                else
                                    return new ShootResult[0];
                                break;
                            case RIGHT:
                                if((anchorCell - 1)%12 > 0 && (anchorCell + 1)%12 > 1)
                                {
                                    cells = new int[]{anchorCell + 1, anchorCell, anchorCell - 1};
                                    j = 1;
                                    for (int c: cells)
                                    {
                                        cell = findCell(c, playerTurn);
                                        cell.setType(CellType.SHIP_3);
                                        cell.setShip(ship);
                                        cell.setPartNum(j);
                                        j++;
                                    }
                                    ship.setCells(cells);
                                    result = new ShootResult[]{new ShootResult(direction, CellType.SHIP_3, anchorCell, 2),
                                            new ShootResult(direction, CellType.SHIP_3, anchorCell + 1, 1),
                                            new ShootResult(direction, CellType.SHIP_3, anchorCell - 1, 3)};
                                }
                                else
                                    return new ShootResult[0];
                                break;
                            case DOWN:
                                if((anchorCell - 12) > 0 && (anchorCell + 12) < 145)
                                {
                                    cells = new int[]{anchorCell + 12, anchorCell, anchorCell - 12};
                                    j = 1;
                                    for (int c: cells)
                                    {
                                        cell = findCell(c, playerTurn);
                                        cell.setType(CellType.SHIP_3);
                                        cell.setShip(ship);
                                        cell.setPartNum(j);
                                        j++;
                                    }
                                    ship.setCells(cells);
                                    result = new ShootResult[]{new ShootResult(direction, CellType.SHIP_3, anchorCell, 2),
                                            new ShootResult(direction, CellType.SHIP_3, anchorCell + 12, 1),
                                            new ShootResult(direction, CellType.SHIP_3, anchorCell - 12, 3)};
                                }
                                else
                                    return new ShootResult[0];
                                break;
                            case LEFT:
                                if((anchorCell - 1)%12 > 0 && (anchorCell + 1)%12 > 1)
                                {
                                    cells = new int[]{anchorCell - 1, anchorCell, anchorCell + 1};
                                    j = 1;
                                    for (int c: cells)
                                    {
                                        cell = findCell(c, playerTurn);
                                        cell.setType(CellType.SHIP_3);
                                        cell.setShip(ship);
                                        cell.setPartNum(j);
                                        j++;
                                    }
                                    ship.setCells(cells);
                                    result = new ShootResult[]{new ShootResult(direction, CellType.SHIP_3, anchorCell, 2),
                                            new ShootResult(direction, CellType.SHIP_3, anchorCell + 1, 3),
                                            new ShootResult(direction, CellType.SHIP_3, anchorCell - 1, 1)};
                                }
                                else
                                    return new ShootResult[0];
                                break;
                        }

                        break;
                    case 4:
                        switch (ship.getDirection())
                        {
                            case UP:
                                if((anchorCell - 24) > 0 && (anchorCell + 12) < 145)
                                {
                                    cells = new int[]{anchorCell - 24, anchorCell - 12, anchorCell, anchorCell + 12};
                                    j = 1;
                                    for (int c: cells)
                                    {
                                        cell = findCell(c, playerTurn);
                                        cell.setType(CellType.SHIP_4);
                                        cell.setShip(ship);
                                        cell.setPartNum(j);
                                        j++;
                                    }
                                    ship.setCells(cells);
                                    result = new ShootResult[]{new ShootResult(direction, CellType.SHIP_4, anchorCell, 3),
                                            new ShootResult(direction, CellType.SHIP_4, anchorCell - 12, 2),
                                            new ShootResult(direction, CellType.SHIP_4, anchorCell - 24, 1),
                                            new ShootResult(direction, CellType.SHIP_4, anchorCell + 12, 4)};
                                }
                                else
                                    return new ShootResult[0];
                                break;
                            case RIGHT:
                                if((anchorCell - 1)%12 > 0 && (anchorCell + 2)%12 > 1)
                                {
                                    cells = new int[]{anchorCell + 2, anchorCell + 1, anchorCell, anchorCell - 1};
                                    for (int c: cells)
                                    {
                                        cell = findCell(c, playerTurn);
                                        cell.setType(CellType.SHIP_4);
                                        cell.setShip(ship);
                                        cell.setPartNum(j);
                                        j++;
                                    }
                                    ship.setCells(cells);
                                    result = new ShootResult[]{new ShootResult(direction, CellType.SHIP_4, anchorCell, 3),
                                            new ShootResult(direction, CellType.SHIP_4, anchorCell - 1, 4),
                                            new ShootResult(direction, CellType.SHIP_4, anchorCell + 2, 1),
                                            new ShootResult(direction, CellType.SHIP_4, anchorCell + 1, 2)};
                                }
                                else
                                    return new ShootResult[0];
                                break;
                            case DOWN:
                                if((anchorCell - 12) > 0 && (anchorCell + 24) < 145)
                                {
                                    cells = new int[]{anchorCell + 24, anchorCell + 12, anchorCell, anchorCell - 12};
                                    for (int c: cells)
                                    {
                                        cell = findCell(c, playerTurn);
                                        cell.setType(CellType.SHIP_4);
                                        cell.setShip(ship);
                                        cell.setPartNum(j);
                                        j++;
                                    }
                                    ship.setCells(cells);
                                    result = new ShootResult[]{new ShootResult(direction, CellType.SHIP_4, anchorCell, 3),
                                            new ShootResult(direction, CellType.SHIP_4, anchorCell - 12, 4),
                                            new ShootResult(direction, CellType.SHIP_4, anchorCell + 24, 1),
                                            new ShootResult(direction, CellType.SHIP_4, anchorCell + 12, 2)};
                                }
                                else
                                    return new ShootResult[0];
                                break;
                            case LEFT:
                                if((anchorCell - 2)%12 > 0 && (anchorCell + 1)%12 > 1)
                                {
                                    cells = new int[]{anchorCell - 2, anchorCell - 1, anchorCell, anchorCell + 1};
                                    for (int c: cells)
                                    {
                                        cell = findCell(c, playerTurn);
                                        cell.setType(CellType.SHIP_4);
                                        cell.setShip(ship);
                                        cell.setPartNum(j);
                                        j++;
                                    }
                                    ship.setCells(cells);
                                    result = new ShootResult[]{new ShootResult(direction, CellType.SHIP_4, anchorCell, 3),
                                            new ShootResult(direction, CellType.SHIP_4, anchorCell - 1, 2),
                                            new ShootResult(direction, CellType.SHIP_4, anchorCell - 2, 1),
                                            new ShootResult(direction, CellType.SHIP_4, anchorCell + 1, 4)};
                                }
                                else
                                    return new ShootResult[0];
                                break;
                        }
                        break;
                    case 5:
                        cell.setType(CellType.MINE);
                        cell.setShip(ship);
                        cell.setPartNum(1);
                        cells = new int[]{anchorCell};
                        ship.setCells(cells);
                        result = new ShootResult[]{new ShootResult(direction, CellType.MINE, anchorCell, 1)};
                        break;
                }
                ship.setAnchorCell(anchorCell);
                ship.setOnDesk(true);
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
            Log.e(TAG, "LogicCell with reit " + removeCell + " don't have ship");
            return new ShootResult[0];
        }
        for (int i = 0; i < MAX_SHIP_COUNT + MAX_MINE_COUNT; i++)
        {
            if (playerShips[i].isOnDesk() && playerShips[i].getAnchorCell() == anchorCell)
            {
                results = new ShootResult[(playerShips[i].getDeckCount() > 4 ? 1: playerShips[i].getDeckCount())];//fixme если время будет
                int j = 0;
                for (int cellId : playerShips[i].getCells())
                {
                    LogicCell cell = findCell(cellId, true);
                    if (cell == null)
                        continue;
                    results[j] = new ShootResult(ShootResult.ResultType.SHIP_DESTROY, CellType.EMPTY, cellId, -1);
                    cell.clear();
                    j++;
                }
                playerShips[i].clear();
                return results;
            }
        }
        return new ShootResult[0];
    }

    public ShootResult switchTurn()//Этап игры
    {
        if(playerTurn)
        {
            playerTurn = false;
            Log.d(TAG, "switchTurn: playerTurn true to false");

            ShootResult result;
            if(gameMode == GameMode.PvE)
            {
                int enemyStep = enemyBot.turn();
                result = shoot(enemyStep, true);
                enemyBot.receiver(result);
            }
            else//PvP
            {
                int enemyStep = 0;
                while(true)
                {

                    enemyStep = connector.getEnemyTurn();//передаёт ход к противнику, получает ход противника (ожидание)

                    if (enemyStep != 0)
                    {
                        break;
                    }

                    try
                    {
                        Thread.sleep(2000);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

                result = shoot(enemyStep, true);//отмечает результат на локальных полях
                Log.d(TAG, "switchTurn: " + result.convertToNum() + result.getResult() + " " + result.getType() + " " + result.getNumArg1() + " " + result.getNumArg2());
                if (winCheck(false))
                {
                    result.setResult(ShootResult.ResultType.PLAYER_WIN);
                    ((GameFragment)mainActivity.getSupportFragmentManager().findFragmentByTag(GameFragment.TAG)).endGame(false, 1);
                }
                connector.sendEnemyTurn(result.convertToNum());//отправляет результат высрела противнику, не передаёт ход
            }
/*
                int overlapse = 1000;
                while(overlapse > 0)
                {
                    enemyStep = enemyBot.fire();
                    result = shoot(enemyStep, true);
                    enemyBot.receiver(result);
                    if (winCheck(false))
                    {
                        return new ShootResult(ShootResult.ResultType.ENEMY_WIN, CellType.ERR, enemyBot.getStepCount(), 0);
                    }
                    overlapse--;
                    Log.d(TAG, "switchTurn: overlapse = " + overlapse);
                }
*/

                ShootResult res = null;
                if (result.getResult() == ShootResult.ResultType.SHIP_DESTROY || result.getResult() == ShootResult.ResultType.SHIP_PART)
                {
                    if (!winCheck(false))
                    {
                        playerTurn = true;
                        Log.d(TAG, "switchTurn: playerTurn1 foase to true");
                        switchTurn();
                    }
                    else
                    {
                        ((GameFragment)mainActivity.getSupportFragmentManager().findFragmentByTag(GameFragment.TAG)).endGame(false, 1);
                        //res = new ShootResult(ShootResult.ResultType.ENEMY_WIN, CellType.ERR, 0, 0);
                    }
                }

                if (res != null)
                {
                    return  res;
                }

                if(minePause)
                {
                    minePause = false;
                    playerTurn = true;
                    Log.d(TAG, "switchTurn: playerTurn2 foase to true");
                    switchTurn();
                }
                else
                {
                    playerTurn = true;
                    Log.d(TAG, "switchTurn: playerTurn3 foase to true");
                }

                return result;


        }
        else
        {
            if(minePause)
            {
                minePause = false;
            }
            else
            {
                playerTurn = true;
                Log.d(TAG, "switchTurn: playerTurn foase to true");
            }
        }
        return new ShootResult(ShootResult.ResultType.EMPTY, CellType.ERR, 0, 0);
    }

    public ShootResult shoot(int targetCell, boolean playerTurn)//Этап игры
    {
        ShootResult shootResult = new ShootResult(ShootResult.ResultType.EMPTY, CellType.EMPTY, 7, 0);;
        LogicCell lc = findCell(targetCell, playerTurn);
        int num = 0;
        CellType type = CellType.ERR;
        if (gameMode == GameMode.PvP && this.playerTurn)
        {
            connector.fire(targetCell);

            while(true)
            {

                num = connector.receiveEnemyTurn();//возврвщает резкльтат хода (спрашивает через сервер)
                if (num != -1)
                {
                    shootResult = ShootResult.parseForNum(num);
                    if (shootResult.getResult() == ShootResult.ResultType.SHIP_DESTROY || shootResult.getResult() == ShootResult.ResultType.SHIP_PART)
                         this.playerTurn = false;
                    if(num == -2)
                        return shootResult;
                    Log.d(TAG, "shoot: s " + shootResult.getResult() + " " + shootResult.getType() + " " + shootResult.getNumArg1() + " " + shootResult.getNumArg2());
                   break;
                }

                try
                {
                    Thread.sleep(2000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            type = shootResult.getType();
        }
        else
        {
            try
            {
                type = lc.getType();
            } catch (NullPointerException c)
            {
                Log.d(TAG, "Can't find LogicCell with reit " + targetCell);
                return new ShootResult(ShootResult.ResultType.EMPTY, type, 0, 0);
            }
        }

        Log.d(TAG, "shoot: type = " + type);
        switch(type)
        {
            case EMPTY:
                if (gameMode == GameMode.PvE || !this.playerTurn)
                {
                    num = mathCountAroundCell(lc);
                    Log.d(TAG, "shoot: num = " + num);
                }
                shootResult = new ShootResult(ShootResult.ResultType.EMPTY, CellType.EMPTY, num, targetCell);
                break;
            case MINE:
                //minePause = !minePause;
                shootResult = new ShootResult(ShootResult.ResultType.MINE, type, 1, targetCell);
                if (gameMode == GameMode.PvP)
                {
                    setShip(targetCell, 5, Ship.ShipDirection.UP, playerTurn);
                }
                lc.setDestroyed(true);
                break;
            case SHIP_1:
                if (gameMode == GameMode.PvP)
                {
                    setShip(targetCell, 1, shootResult.getDirection(), playerTurn);
                }
                shootResult = new ShootResult(ShootResult.ResultType.SHIP_DESTROY, type, 1, targetCell);
                lc.getShip().destroyPart(lc.getId());
                lc.getShip().isDestroy();
                lc.setDestroyed(true);
                break;
            case SHIP_2:
                if (gameMode == GameMode.PvP)
                {
                    setShip(targetCell, 2, shootResult.getDirection(), playerTurn);
                }
                if(lc.getShip() != null)
                {
                    if (lc.getShip().destroyPart(lc.getId()))
                        shootResult = new ShootResult(ShootResult.ResultType.SHIP_DESTROY, type, lc.getPartNum(), targetCell);
                    else
                        shootResult = new ShootResult(ShootResult.ResultType.SHIP_PART, type, lc.getPartNum(), targetCell);
                    lc.getShip().destroyPart(lc.getId());
                    lc.getShip().isDestroy();
                    lc.setDestroyed(true);
                }
                else
                    shootResult = new ShootResult(ShootResult.ResultType.EMPTY, CellType.ERR, 1, targetCell);
                break;
            case SHIP_3:
                if (gameMode == GameMode.PvP)
                {
                    setShip(targetCell, 3, shootResult.getDirection(), playerTurn);
                }
                if(lc.getShip() != null)
                {
                    if (lc.getShip().destroyPart(lc.getId()))
                        shootResult = new ShootResult(ShootResult.ResultType.SHIP_DESTROY, type, lc.getPartNum(), targetCell);
                    else
                        shootResult = new ShootResult(ShootResult.ResultType.SHIP_PART, type, lc.getPartNum(), targetCell);
                    lc.getShip().destroyPart(lc.getId());
                    lc.getShip().isDestroy();
                    lc.setDestroyed(true);
                }
                else
                    shootResult = new ShootResult(ShootResult.ResultType.EMPTY, CellType.ERR, 1, targetCell);
                break;
            case SHIP_4:
                if (gameMode == GameMode.PvP)
                {
                    setShip(targetCell, 4, shootResult.getDirection(), playerTurn);
                }
                if(lc.getShip() != null)
                {
                    if (lc.getShip().destroyPart(lc.getId()))
                        shootResult = new ShootResult(ShootResult.ResultType.SHIP_DESTROY, type, lc.getPartNum(), targetCell);
                    else
                        shootResult = new ShootResult(ShootResult.ResultType.SHIP_PART, type, lc.getPartNum(), targetCell);
                    lc.getShip().destroyPart(lc.getId());
                    lc.getShip().isDestroy();
                    lc.setDestroyed(true);
                }
                else
                    shootResult = new ShootResult(ShootResult.ResultType.EMPTY, CellType.ERR, 1, targetCell);
                break;
            case ERR:
                shootResult = new ShootResult(ShootResult.ResultType.EMPTY, type, 0, targetCell);
                break;
            default:
                shootResult = new ShootResult(ShootResult.ResultType.EMPTY, CellType.ERR, -1, targetCell);
                break;
        }
        return shootResult;
    }

    public boolean winCheck(boolean playerTurn)
    {
        for (int i = 0; i < MAX_SHIP_COUNT; i++)
        {
            if(!(playerTurn ? enemyShips[i].isDestroyed() : playerShips[i].isDestroyed()))
            {
                Log.d(TAG, "winCheck: " + (playerTurn ? enemyShips[i].getAnchorCell() : playerShips[i].getAnchorCell()));
                return false;
            }
        }
        return true;
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

    public boolean checkPosition(int intTag, int deckCount, Ship.ShipDirection direction)//Этап расстановки
    {
        int[] cord;
        int[] cells = new int[0];
        switch (deckCount)
        {
            case 1:
                cells = new int[]{intTag};
                break;
            case 2:
                switch (direction)
                {
                    case UP:
                        cells = new int[]{intTag, intTag - 12};
                        break;
                    case RIGHT:
                        cells = new int[]{intTag, intTag + 1};
                        break;
                    case DOWN:
                        cells = new int[]{intTag, intTag + 12};
                        break;
                    case LEFT:
                        cells = new int[]{intTag, intTag - 1};
                        break;
                }
                break;
            case 3:
                switch (direction)
                {
                    case DOWN:
                    case UP:
                        cells = new int[]{intTag, intTag - 12, intTag + 12};
                        break;
                    case RIGHT:
                    case LEFT:
                        cells = new int[]{intTag, intTag + 1, intTag -1};
                        break;
                }
                break;
            case 4:
                switch (direction)
                {
                    case UP:
                        cells = new int[]{intTag, intTag - 12, intTag - 24, intTag + 12};
                        break;
                    case RIGHT:
                        cells = new int[]{intTag, intTag - 1, intTag + 2, intTag + 1};
                        break;
                    case DOWN:
                        cells = new int[]{intTag, intTag + 12, intTag + 24, intTag - 12};
                        break;
                    case LEFT:
                        cells = new int[]{intTag, intTag - 1, intTag - 2, intTag + 1};
                        break;
                }
                break;

        }

        for (int cell: cells)
        {
            if (cell < 0 || cell > 144)
            {
                continue;
            }
            cord = Cell.getCordInt(cell);
            cord[0]--;
            cord[1]--;
            //StringBuilder log = new StringBuilder("||| ");
            for (int j = cord[0] - 1; j <= cord[0] + 1; j++)
            {
                for (int i = cord[1] - 1; i <= cord[1] + 1; i++)
                {
                    //log.append(" i= ").append(i).append(" j= ").append(j).append(" ||| ");
                    if (i < 12 && i > -1 && j < 12 && j > -1 && playerCells[i][j].getShip() != null && playerCells[i][j].getShip().getDeckCount() != 5)
                    {
                        //Log.d(TAG, "checkPosition: " + log.toString());
                        //Log.d(TAG, "checkPosition: i= " + i + " j= " + j);
                        //Log.d(TAG, "checkPosition: shipCells = ( " + Arrays.toString(playerCells[i][j].getShip().getCells()) + " )");
                        //Log.d(TAG, "checkPosition: cells = ( " + Arrays.toString(cells) + " )");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private int mathCountAroundCell(LogicCell logicCell)//Этап игры
    {
        int num = 0;
        int[] cord = Cell.getCordInt(logicCell.getId());
        cord[0]--;
        cord[1]--;
        if (!playerTurn)
        {
            for (int j = cord[0] - 1; j <= cord[0] + 1; j++)
            {
                for (int i = cord[1] - 1; i <= cord[1] + 1; i++)
                {
                    if(i < 12 && i > -1 && j < 12 && j > -1 && playerCells[i][j].getShip() != null)
                    {
                        num++;
                    }
                }
            }
        }
        else
        {
            for (int j = cord[0] - 1; j <= cord[0] + 1; j++)
            {
                for (int i = cord[1] - 1; i <= cord[1] + 1; i++)
                {
                    if(i < 12 && i > -1 && j < 12 && j > -1 && enemyCells[i][j].getShip() != null)
                    {
                        num++;
                    }
                }
            }
        }

        logicCell.setPartNum(num);
        return num;
    }

    public boolean isCorrectPlacement(boolean playerTurn)//Этап расстановки
    {
        for (int i = 0; i < MAX_SHIP_COUNT + MAX_MINE_COUNT; i++)
        {
            if(!(playerTurn ? playerShips[i].isOnDesk() : enemyShips[i].isOnDesk()))
            {
                return false;
            }
        }

        return true;
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
        for (int i = 0; i < MAX_SHIP_COUNT + MAX_MINE_COUNT; i++)
        {
            playerShips[i].clear();
            enemyShips[i].clear();
        }

        //gameMode = GameMode.PvP;
        enemyBot.clear();
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

    public Bot getEnemyBot() {
        return enemyBot;
    }

    public void setEnemyBot(Bot enemyBot) {
        this.enemyBot = enemyBot;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public void setEnemyBotLvL(Bot.BotMode mode)
    {
        enemyBot.setMode(mode);
    }

    public void setConnector(TestConnection connector) {
        this.connector = connector;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public ShootResult[] getPlayerCells()
    {
        ShootResult[] result = new ShootResult[144];
        int i = 0;
        for (LogicCell[] cells: playerCells)
        {
            for (LogicCell cell: cells)
            {
                result[i] = new ShootResult((cell.getShip() == null ? Ship.ShipDirection.UP : cell.getShip().getDirection()), cell.getType(), cell.getId(), cell.getPartNum(), cell.isDestroyed());
                i++;
            }
        }
        return result;
    }

    public ShootResult[] getEnemyCells()
    {
        ShootResult[] result = new ShootResult[144];
        int i = 0;
        for (LogicCell[] cells: enemyCells)
        {
            for (LogicCell cell: cells)
            {
                result[i] = new ShootResult((cell.getShip() == null ? Ship.ShipDirection.UP : cell.getShip().getDirection()),
                        cell.getType(), cell.getId(), cell.getPartNum(), cell.isDestroyed());
                i++;
            }
        }
        return result;
    }

}
