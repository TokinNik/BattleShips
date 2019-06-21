package com.tokovoynr.battleships.game;

import android.util.Log;

import com.tokovoynr.battleships.UI.PreGame.Cell;

import java.util.Random;

public class Bot
{
    public enum BotMode
    {
        RANDOM,
        EASY
    }
    public static final String TAG = "BOT";
    private BotMode mode = BotMode.RANDOM;
    private int[][] botField = new int[12][12];

    public Bot()
    {
        clear();
    }

    public Bot(BotMode mode)
    {
        this.mode = mode;
        clear();
    }

    int turn()
    {
        int step = 0;
        switch (mode)
        {
            case RANDOM:
                step = getRandStep();
                int[] cord = Cell.getCordInt(step);
                cord[0]--;
                cord[1]--;
                while (cord[1] < 12 && cord[1] > -1 && cord[0] < 12 && cord[0] > -1 && botField[cord[1]][cord[0]] == 1)
                {
                    step = getRandStep();
                    cord = Cell.getCordInt(step);
                    cord[0]--;
                    cord[1]--;
                }
                Log.d(TAG, "turn: " + step);
                break;
            case EASY:
                break;
            default:
                break;
        }

        return step;
    }

    int getRandStep()
    {
        Random random = new Random();
        int step = random.nextInt(144);
        step ++;
        return step;
    }

    void clear()
    {
        for (int[] x: botField)
        {
            for (int z: x)
            {
                z = -1;
            }
        }
    }

    public BotMode getMode() {
        return mode;
    }

    public void setMode(BotMode mode) {
        this.mode = mode;
    }
}
