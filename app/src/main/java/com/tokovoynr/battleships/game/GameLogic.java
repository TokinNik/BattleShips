package com.tokovoynr.battleships.game;

import com.tokovoynr.battleships.UI.PreGame.Cell;

public class GameLogic
{
    public static final String TAG = "GAME_LOGIC";
    private static final int MAX_SHIP_1_COUNT = 4;
    private static final int MAX_SHIP_2_COUNT = 3;
    private static final int MAX_SHIP_3_COUNT = 2;
    private static final int MAX_SHIP_4_COUNT = 1;
    private static final int MAX_SHIP_COUNT = MAX_SHIP_1_COUNT + MAX_SHIP_2_COUNT + MAX_SHIP_3_COUNT + MAX_SHIP_4_COUNT;
    private Ship[] playerShips = new Ship[MAX_SHIP_COUNT];
    private Ship[] enemyShips = new Ship[MAX_SHIP_COUNT];


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

    public boolean setShip(int anchorCell, int deckCount)
    {
        for (int i = 0; i < MAX_SHIP_COUNT; i++)
        {
            if (!playerShips[i].isOnDesk())
            {
                playerShips[i].setAnchorCell(anchorCell);
                playerShips[i].setOnDesk(true);
                return true;
            }

        }
        return false;
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
