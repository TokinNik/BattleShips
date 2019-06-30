package com.tokovoynr.battleships.game;

import android.util.Log;

class BotCell
{
    public static final String TAG = "BOT_CELL";
    private int id = 0;
    private int x = 0;
    private int y = 0;
    private int value = -1;
    private Group group;
    private float chance;

    public BotCell() {
    }

    public BotCell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public BotCell(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public float getChance() {
        return chance;
    }

    public void setChance(float chance) {
        this.chance = chance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
