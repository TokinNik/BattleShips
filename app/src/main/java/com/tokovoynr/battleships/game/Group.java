package com.tokovoynr.battleships.game;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Group
{
    public static final String TAG = "GROUP";
    private ArrayList<BotCell> cells = new ArrayList<>();
    private int unitCount = 0;

    public Group(int unitCount) {
        this.unitCount = unitCount;
    }

    public Group(ArrayList<BotCell> cells, int unitCount)
    {
        this.cells = cells;
        this.unitCount = unitCount;
    }

    public ArrayList<BotCell> getCells() {
        return cells;
    }

    public void setCells(ArrayList<BotCell> cells) {
        this.cells = cells;
    }

    public int getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(int unitCount) {
        this.unitCount = unitCount;
    }

    public int size() {
        return cells.size();
    }

    public boolean contains(Group child)
    {
        StringBuilder builder = new StringBuilder("[");
        for (BotCell value: cells)
        {
            builder.append(value.getId()).append(" ");
        }
        Log.d(TAG, "contains1: " + builder.toString() + "], [" + unitCount + "]");
        builder = new StringBuilder("[");
        for (BotCell value: child.getCells())
        {
            builder.append(value.getId()).append(" ");
        }
        Log.d(TAG, "contains2: " + builder.toString() + "], [" + child.getUnitCount() + "]");

        for (BotCell item: child.getCells())
        {
            if(!cells.contains(item))
                return false;
        }

        return true;
    }

    public void subtraction(Group child)
    {

        StringBuilder builder = new StringBuilder("[");
        for (BotCell value: cells)
        {
            builder.append(value.getId()).append(" ");
        }
        Log.d(TAG, "subtraction1: " + builder.toString() + "], [" + unitCount + "]");
        builder = new StringBuilder("[");
        for (BotCell value: child.getCells())
        {
            builder.append(value.getId()).append(" ");
        }
        Log.d(TAG, "subtraction2: " + builder.toString() + "], [" + child.getUnitCount() + "]");

        for (int i = 0; i < child.size(); i++)
        {
            for (int j = 0; j < cells.size(); j++)
            {
                if (cells.get(j).equals(child.getCells().get(i)))
                {
                    cells.remove(j);
                }
            }
        }
        unitCount -= child.getUnitCount();
        builder = new StringBuilder("[");
        for (BotCell value: child.getCells())
        {
            builder.append(value.getId()).append(" ");
        }
        Log.d(TAG, "subtraction3: " + builder.toString() + "], [" + child.getUnitCount() + "]");
    }

    public boolean overlaps(Group groupJ)
    {
        StringBuilder builder = new StringBuilder("[");
        for (BotCell value: cells)
        {
            builder.append(value.getId()).append(" ");
        }
        Log.d(TAG, "overlaps1: " + builder.toString() + "], [" + unitCount + "]");
        builder = new StringBuilder("[");
        for (BotCell value: groupJ.getCells())
        {
            builder.append(value.getId()).append(" ");
        }
        Log.d(TAG, "overlaps2: " + builder.toString() + "], [" + groupJ.getUnitCount() + "]");


        for (BotCell item: groupJ.getCells())
        {
            if(cells.contains(item))
                return true;
        }

        return false;
    }

    public Group getOverlap(Group child)
    {
        StringBuilder builder = new StringBuilder("[");
        for (BotCell value: cells)
        {
            builder.append(value.getId()).append(" ");
        }
        Log.d(TAG, "getOverlap1: " + builder.toString() + "], [" + unitCount + "]");
        builder = new StringBuilder("[");
        for (BotCell value: child.getCells())
        {
            builder.append(value.getId()).append(" ");
        }
        Log.d(TAG, "getOverlap2: " + builder.toString() + "], [" + child.getUnitCount() + "]");

        ArrayList<BotCell> overlapCells = new ArrayList<>();
        int overlapUnitCount;
        for (int i = 0; i < child.size(); i++)
        {
            for (int j = 0; j < cells.size(); j++)
            {
                if (cells.get(j).equals(child.getCells().get(i)))
                {
                    overlapCells.add(cells.get(j));
                }
            }
        }

        if (unitCount > child.getUnitCount())
        {
            overlapUnitCount = unitCount - (child.size() - overlapCells.size());
            Log.d(TAG, "getOverlap: ovUC=" + overlapUnitCount + " unC=" + unitCount + " ch.sz() - ovCells.sz()=" + (child.size() - overlapCells.size()));
            if (overlapUnitCount != child.getUnitCount())
            {
                return null;
            }
        }
        else
        {
            overlapUnitCount = child.getUnitCount() - (size() - overlapCells.size());
            Log.d(TAG, "getOverlap: ovUC=" + overlapUnitCount + " chld.unC=" + child.getUnitCount() + " sz() - ovCells.sz()=" + (size() - overlapCells.size()));
            if (overlapUnitCount != unitCount)
            {
                return null;
            }
        }

        Group resultGroup = new Group(overlapCells, overlapUnitCount);

        subtraction(resultGroup);
        child.subtraction(resultGroup);

        builder = new StringBuilder("[");
        for (BotCell value: cells)
        {
            builder.append(value.getId()).append(" ");
        }
        Log.d(TAG, "getOverlap11: " + builder.toString() + "], [" + unitCount + "]");
        builder = new StringBuilder("[");
        for (BotCell value: child.getCells())
        {
            builder.append(value.getId()).append(" ");
        }
        Log.d(TAG, "getOverlap22: " + builder.toString() + "], [" + child.getUnitCount() + "]");

        builder = new StringBuilder("[");
        for (BotCell value: overlapCells)
        {
            builder.append(value.getId()).append(" ");
        }
        Log.d(TAG, "getOverlap3: " + builder.toString() + "], [" + overlapUnitCount + "]");

        return resultGroup;
    }

    public List<Float> getChanceList()
    {
        List<Float> chanceList = new ArrayList<>();
        for (BotCell cell: cells)
        {
            chanceList.add(cell.getChance());
        }
        return chanceList;
    }

    public void mathChances()
    {
        for (BotCell cell: cells)
        {
            if (cell.getChance() != 0)
            {
                cell.setChance(((float)unitCount / cells.size()));
            }
        }
    }
}
