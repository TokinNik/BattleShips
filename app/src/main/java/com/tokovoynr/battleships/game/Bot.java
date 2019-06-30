package com.tokovoynr.battleships.game;

import android.util.Log;

import com.tokovoynr.battleships.UI.PreGame.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private StringBuilder botLog = new StringBuilder("Bot Log\n");
    private BotCell[][] botField = new BotCell[12][12];
    private Ship[] botShips = new Ship[GameLogic.MAX_SHIP_COUNT];
    private ArrayList<Group> groups = new ArrayList<>();
    private int allShipCount = GameLogic.MAX_SHIP_COUNT;
    private int shipOneCount = GameLogic.MAX_SHIP_1_COUNT;
    private int shipTwoCount = GameLogic.MAX_SHIP_2_COUNT;
    private int shipThreeCount = GameLogic.MAX_SHIP_3_COUNT;
    private int shipFourCount = GameLogic.MAX_SHIP_4_COUNT;
    private int mineCount = GameLogic.MAX_MINE_COUNT;
    private int testCount = 4;
    private int stepCount = 0;
    private Random random = new Random();


    Bot()
    {
        for (int i = 0; i < 12; i++)
            for (int j = 0; j < 12; j++)
            {
                botField[i][j] = new BotCell(i,j);
                botField[i][j].setId(i * 12 + j + 1);
            }

        for (int i = 0; i < allShipCount; i++)
        {
            botShips[i] = new Ship(4);
        }
    }

    public Bot(BotMode mode)
    {
        this.mode = mode;
        for (int i = 0; i < 12; i++)
            for (int j = 0; j < 12; j++)
            {
                botField[i][j] = new BotCell(i,j);
                botField[i][j].setId(i * 12 + j + 1);
            }
        for (int i = 0; i < allShipCount; i++)
        {
            botShips[i] = new Ship(4);
        }
    }

    int turn()
    {
        stepCount++;
        int step = 0;
        switch (mode)
        {
            case RANDOM:
                step = getRandStep();
                break;
            case EASY:
                Log.d(TAG, "fire: EASY_BOT");
                setGroups(false);
                correctChances();
                /*
                for (BotCell[] cells: botField)
                {
                    for (BotCell cell: cells)
                    {
                        if (cell.getGroup() != null)
                        {
                            StringBuilder builder = new StringBuilder("[");
                            for (BotCell value: cell.getGroup().getCells())
                            {
                                builder.append(value.getId()).append("(").append(value.getChance()).append(") ");
                            }
                            Log.d(TAG, "fire groups: " + builder.toString() + "], [" + cell.getGroup().getUnitCount() + "]");
                        }
                    }
                }
                */
                switch (testCount)
                {
                    case 0:
                        Ship unfinished = searchUnfinishedShips();

                        if (unfinished != null)
                        {
                            step = finishShip(unfinished);
                            Log.d(TAG, "fire: FINISH_SHIP");
                        }
                        else
                        {
                            int stepCount = 1;
                            float maxChance = 0f;
                            BotCell stepCell = null;
                            for (BotCell[] cells: botField)
                            {
                                for (BotCell cell: cells)
                                {
                                    if(cell.getValue() == -1 && cell.getChance() > maxChance)
                                    {
                                        maxChance = cell.getChance();
                                        stepCell = cell;
                                    }
                                    else if (cell.getValue() == -1 && cell.getChance() == maxChance)
                                    {
                                        stepCount++;
                                    }
                                }
                            }

                            if (stepCount == 1 && stepCell != null)
                            {
                                step = stepCell.getId();
                            }
                            else
                            {
                                ArrayList<BotCell> stepCells = new ArrayList<>();
                                for (BotCell[] cells: botField)
                                {
                                    for (BotCell cell: cells)
                                    {
                                        if(cell.getValue() == -1 && cell.getChance() == maxChance)
                                            stepCells.add(cell);
                                    }
                                }
                                if (stepCells.size() > 0)
                                {
                                    int overflow = 1000;//fixme kostil' op'at'
                                    while (true)
                                    {
                                        step = random.nextInt(stepCells.size());
                                        Log.d(TAG, "fire: while " + stepCells.get(step).getValue());
                                        if (stepCells.get(step).getValue() == -1 || overflow < 0)
                                        {
                                            step = stepCells.get(step).getId();
                                            Log.d(TAG, "fire: UNRANDOM_STEP");
                                            break;
                                        }
                                        overflow--;
                                    }
                                }
                                else
                                {
                                    step = getRandStep();
                                    Log.d(TAG, "fire: RANDOM_STEP");
                                }
                            }

                        }
                        break;
                    case 1:
                        testCount--;
                        step = 29;
                        break;
                    case 2:
                        testCount--;
                        step = 40;
                        break;
                    case 3:
                        testCount--;
                        step = 28;
                        break;
                    case 4:
                        testCount--;
                        step = 16;
                        break;
                    case 5:
                        testCount = 0;
                        step = 89;
                        break;
                }

                break;
            default:
                break;
        }
        botLog.append("Ход № ").append(stepCount).append(" Выстрел в ").append(Cell.getCordString(step)).append(" ");
        Log.d(TAG, "fire: " + step);
        return step;
    }

    void receiver(ShootResult result)
    {
        int[] cord = Cell.getCordInt(result.getNumArg2());
        cord[0]--;
        cord[1]--;
        switch (result.getResult())
        {
            case EMPTY:
                botLog.append(" Результат - Мимо ").append(result.getNumArg1()).append("\n");
                botField[cord[1]][cord[0]].setValue(result.getNumArg1());
                botField[cord[1]][cord[0]].setChance(0);
                if (result.getNumArg1() == 0)
                    for (int x = cord[1] - 1; x <=  cord[1] + 1; x++)
                    {
                        for (int y = cord[0] - 1; y <= cord[0] + 1; y++)
                        {
                            if (x < 12 && x > -1 && y < 12 && y > -1)
                            {
                                botField[x][y].setChance(0f);
                            }
                        }
                    }
                break;
            case SHIP_PART:
                botLog.append(" Результат - Попадание ").append("\n");
                botField[cord[1]][cord[0]].setValue(10);
                botField[cord[1]][cord[0]].setChance(0);
                boolean end = true;//fixme kostil'
                for (Ship ship: botShips)
                {
                    if(end && !ship.isDestroyed())
                    {
                        for (int cell: ship.getCells())
                        {
                            if(end &&((cell - 1) == result.getNumArg2() || (cell + 1) == result.getNumArg2() ||
                                    (cell - 12) == result.getNumArg2() || (cell + 12) == result.getNumArg2()))
                            {
                                ship.destroyPart(result.getNumArg2());
                                end = false;
                            }
                        }
                    }
                }

                if(end)
                {
                    for (Ship ship: botShips)
                    {
                        if(!ship.isDestroyed())
                        {
                            for (int cell: ship.getCells())
                            {
                                if(cell != 0)
                                {
                                    end = false;
                                    break;
                                }
                            }
                            if (end)
                            {
                                ship.destroyPart(result.getNumArg2());
                                break;//fixme sdelay normal'no blt'
                            }
                            end = true;
                        }
                    }
                }
                break;
            case SHIP_DESTROY:
                botLog.append(" Результат - Уничтожил ").append(result.getType()).append(" осталость ").append(allShipCount-1).append(" кораблей\n");
                botField[cord[1]][cord[0]].setValue(11);
                end = true;//fixme kostil'
                for (Ship ship: botShips)
                {
                    if(end && !ship.isDestroyed())
                    {
                        for (int cell: ship.getCells())
                        {
                            if(end &&((cell - 1) == result.getNumArg2() || (cell + 1) == result.getNumArg2() ||
                                    (cell - 12) == result.getNumArg2() || (cell + 12) == result.getNumArg2()))
                            {
                                ship.setDestroyed(true);
                                end = false;
                            }
                        }
                    }
                }

                if (end)
                {
                    for (Ship ship: botShips)
                    {
                        if (!ship.isDestroyed())
                        {
                            for (int cell : ship.getCells())
                            {
                                if (cell != 0)
                                {
                                    end = false;
                                    break;
                                }
                            }
                            if (end)
                            {
                                ship.setDestroyed(true);
                                break;//fixme sdelay normal'no blt'
                            }
                            end = true;
                        }
                    }
                }

                allShipCount--;
                switch (result.getType().ordinal())
                {
                    case 1:
                        shipOneCount--;
                        break;
                    case 2:
                        shipTwoCount--;
                        break;
                    case 3:
                        shipThreeCount--;
                        break;
                    case 4:
                        shipFourCount--;
                        break;
                    default:
                        break;
                }
                break;
            case MINE:
                botLog.append(" Результат - Мина ").append("\n");
                botField[cord[1]][cord[0]].setValue(9);
                botField[cord[1]][cord[0]].setChance(0);
                mineCount--;
                break;
        }
        //Log.d(TAG, "receiver: " + getBotLog());
    }

    private int getRandStep()
    {
        int step = random.nextInt(144);
        step ++;

        int[] cord = Cell.getCordInt(step);
        cord[0]--;
        cord[1]--;
        while (cord[1] < 12 && cord[1] > -1 && cord[0] < 12 && cord[0] > -1 && botField[cord[1]][cord[0]].getValue() >= 0)//todo vozmoj'na dorabotka
        {
            step = random.nextInt(144);
            step ++;
            cord = Cell.getCordInt(step);
            cord[0]--;
            cord[1]--;
        }

        return step;
    }

    private Ship searchUnfinishedShips()
    {
        for (Ship ship: botShips)
        {
            if(!ship.isDestroyed())
            {
                for (int cell: ship.getCells())
                {
                    if(cell != 0)
                    {
                        return ship;
                    }
                }
            }
        }
        return null;
    }

    private int finishShip(Ship ship)
    {
        int currentPartCount = 0;

        for (int cell: ship.getCells())
        {
            Log.d(TAG, "finishShip: cells " + cell + " " + ship.getCells().length);
            if(cell != 0)
            {
                currentPartCount++;
            }
        }

        BotCell[] nearCells = new BotCell[0];
        float maxChance = 0;
        int resultCount = 1;
        int resultCell = 0;

        switch (currentPartCount)
        {
            case 1:
                int shipCell = ship.getCells()[0];
                nearCells = new BotCell[4];
                int[] cord = Cell.getCordInt(shipCell);
                cord[0]--;
                cord[1]--;
                if(cord[1] - 1 >= 0) nearCells[0] = botField[cord[1] - 1][cord[0]];
                if(cord[1] + 1 < 12) nearCells[1] = botField[cord[1] + 1][cord[0]];
                if(cord[0] - 1 >= 0) nearCells[2] = botField[cord[1]][cord[0] - 1];
                if(cord[0] + 1 < 12) nearCells[3] = botField[cord[1]][cord[0] + 1];
                break;
            case 2:
            case 3:
                int[] shipCells = ship.getCells();
                nearCells = new BotCell[2];
                if(shipCells[0] != 0 && shipCells[1] != 0 && (shipCells[0] == shipCells[1] - 1 || shipCells[0] == shipCells[1] + 1))
                {
                    cord = Cell.getCordInt(shipCells[0]);
                    cord[0]--;
                    cord[1]--;
                    if (cord[0] - 1 >= 0)
                    {
                        nearCells[0] = botField[cord[1]][cord[0] - 1];
                        Log.d(TAG, "finishShip: - 0 " + shipCells[0] + " " + nearCells[0].getId());
                    }

                    cord = Cell.getCordInt(shipCells[currentPartCount-1]);
                    cord[0]--;
                    cord[1]--;
                    if (cord[0] + 1 < 12)
                    {
                        nearCells[1] = botField[cord[1]][cord[0] + 1];
                        Log.d(TAG, "finishShip: - 1 " + shipCells[currentPartCount-1] + " " + nearCells[1].getId());
                    }
                }
                else if(shipCells[0] != 0 && shipCells[1] != 0 && (shipCells[0] == shipCells[1] - 12 || shipCells[0] == shipCells[1] + 12))
                {
                    int minShipCell = shipCells[0];
                    for (int i = 1; i < shipCells.length; i ++)
                    {
                        if(shipCells[i] != 0 && shipCells[i] < minShipCell)
                            minShipCell = shipCells[i];
                    }
                    cord = Cell.getCordInt(minShipCell);
                    cord[0]--;
                    cord[1]--;
                    if (cord[1] - 1 >= 0)
                    {
                        nearCells[0] = botField[cord[1] - 1][cord[0]];
                        Log.d(TAG, "finishShip: | 0 " + shipCells[0] + " " + nearCells[0].getId());
                    }
                    for (int i = 0; i < shipCells.length; i ++)
                    {
                        if(shipCells[i] != 0 && shipCells[i] > minShipCell)
                            minShipCell = shipCells[i];
                    }
                    cord = Cell.getCordInt(minShipCell);
                    cord[0]--;
                    cord[1]--;
                    if (cord[1] + 1 < 12)
                    {
                        nearCells[1] = botField[cord[1] + 1][cord[0]];
                        Log.d(TAG, "finishShip: | 1 " + shipCells[currentPartCount-1] + " " + nearCells[1].getId());
                    }
                }
                break;
        }

        if (nearCells.length > 0)
        {
            for (int i = 0; i < nearCells.length; i++)
            {
                if (nearCells[i] != null && nearCells[i].getChance() != 0 && nearCells[i].getValue() == -1)
                {

                    if (nearCells[i].getChance() > maxChance)
                    {
                        maxChance = nearCells[i].getChance();
                        resultCell = i;
                    } else if (nearCells[i].getChance() == maxChance)
                    {
                        resultCount++;
                    }
                }
                else if (nearCells[i] != null && nearCells[i].getChance() == 0 && nearCells[resultCell].getChance() == 0 && i == nearCells.length-1)
                {
                    resultCount = nearCells.length;
                }
            }
            Log.d(TAG, "finishShip: max " + maxChance);

            for (int i = 0; i < nearCells.length; i++)
            {
                if (nearCells[i] != null)
                {
                    Log.d(TAG, "finishShip: --- " + nearCells[i].getValue() + " " + nearCells[i].getChance() + " " + nearCells[i].getId());
                }
            }

            if (resultCount == 1)
            {
                return nearCells[resultCell].getId();
            }
            else
            {
                for (int i = 0; i < nearCells.length; i++)
                {
                    if (nearCells[i] != null && nearCells[i].getChance() != maxChance)
                    {
                        nearCells[i] = null;
                    }
                }
                int overflow = 1000;
                while (true)
                {
                    int step = random.nextInt(resultCount);
                    if (nearCells[step] != null)
                        Log.d(TAG, "finishShip: while " + step + " " + nearCells[step].getValue() + " " + nearCells[step].getId());
                    if ((nearCells[step] != null && nearCells[step].getValue() == -1) || overflow < 0)
                        return nearCells[step].getId();
                    overflow--;
                }
            }
        }
        else
        {
            return 0;
        }
    }

    private void correctChances()
    {
        Map<BotCell,Float> cells= new HashMap<>();
        // цикл устанавливает единое значение вероятности в каждой ячейке, учитывая различные значения вероятностей в ячейке от разных групп
        for (Group group : groups)
        {
            for (BotCell cell: group.getCells())
            {
                Float value;
                if ((value = cells.get(cell)) == null) // если ячейка еще не в мапе
                    cells.put(cell, (float) (group.getUnitCount() / group.size())); // то добавляем ее со значением из группы
                else     //если она уже в мапе, то корректируем ее значение по теории вероятности
                    cells.put(cell, 1 - (1 - value) * (1 - group.getUnitCount()/ group.size()));
            }
        }
        // цикл корректирует значения с учетом того, что сумма вероятностей в группе должна быть равна количеству мин в группе
        boolean repeat;
        do
        {
            repeat=false;
            for (Group group : groups)
            {                      // для каждой группы
                List<Float> chanceList = group.getChanceList(); //  берем список вероятностей всех ячеек в группе в процентах

                Float sum = 0f;
                for (Float elem: chanceList)
                {
                    sum += elem;             //  вычисляем ее сумму
                }
                if (Math.abs(sum - group.getUnitCount()) > 1)
                {                  //  если разница между ними велика, то проводим корректировку
                    repeat = true;
                    //   корректируем список
                    float correctNum = (group.getUnitCount() / sum);
                    for (int i = 0; i < chanceList.size(); i++)
                    {
                        chanceList.set(i, (chanceList.get(i) >= 0 ? chanceList.get(i) : 1) * correctNum);
                    }

                    for (int i = 0; i < group.size(); i++)
                    {       //   заносим откорректированные значения из списка в ячейки
                        float value = chanceList.get(i);
                        group.getCells().get(i).setChance(value);
                    }
                }
            }
        }
        while (repeat);

        for (BotCell cell: cells.keySet())
        {  // перестраховка
            if (cell.getChance() > 1f)
                cell.setChance(1f);
            if (cell.getChance() < 0f)
                cell.setChance(0f);
        }
    }

    private void setGroups(boolean proMode)
    {
        groups.clear();
        for (int x = 0; x < 12; x++)
            for (int y = 0; y < 12; y++)
            {
                if(botField[x][y].getValue() > 0 && botField[x][y].getValue() < 9)
                {
                    Group newGroup = new Group(botField[x][y].getValue());
                    for (int i = x - 1; i <= x + 1; i++)
                        for (int j = y - 1; j <= y + 1; j++)
                        {
                            //if(i < 12 && i > -1 && j < 12 && j > -1)
                                //Log.d(TAG, "setGroups: /// " + botField[x][y].getId() + " " + botField[i][j].getValue() + " " + botField[i][j].getChance());

                            if(i < 12 && i > -1 && j < 12 && j > -1 && botField[i][j].getValue() == -1)// && botField[i][j].getChance() != 0)
                            {
                                newGroup.getCells().add(botField[i][j]);
                            }
                            else if(i < 12 && i > -1 && j < 12 && j > -1 && botField[i][j].getValue() > 9)
                            {
                                newGroup.setUnitCount(newGroup.getUnitCount()-1);
                                if (newGroup.getUnitCount() == 0)
                                {
                                    for (i = x - 1; i <= x + 1; i++)
                                    {
                                        for (j = y - 1; j <= y + 1; j++)
                                        {
                                            if (i < 12 && i > -1 && j < 12 && j > -1)
                                            {
                                                botField[i][j].setChance(0f);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    newGroup.mathChances();
                    //Log.d(TAG, "setGroups: NEW GR??? " + newGroup.size() + " " + newGroup.getUnitCount() );
                    if (newGroup.size() != 0 && newGroup.getUnitCount() != 0)
                    {
                        Log.d(TAG, "setGroups: NEW GR");
                        botField[x][y].setGroup(newGroup); // создание групп
                        groups.add(newGroup);
                    }
                }
            }
        boolean repeat;
        if (proMode)
        {
            do
            {
                repeat=false;
                for (int i = 0; i < groups.size() - 1; i++)
                {  // проходим по списку групп
                    Group groupI = groups.get(i);
                    for (int j = i + 1; j < groups.size(); j++)
                    {   // сравниваем ее с остальными меньшими группами
                        Group groupJ = groups.get(j);

                        if (groupI.equals(groupJ))                  // удаляем одинаковые группы
                        {
                            groups.remove(j--);
                            break;
                        }
                        Group parent;                               // большая группа
                        Group child;                                // меньшая группа

                        if (groupI.size() > groupJ.size())            // определяем большую и меньшую группы по кол-ву ячеек
                        {
                            parent = groupI;
                            child = groupJ;
                        }
                        else
                        {
                            child = groupI;
                            parent = groupJ;
                        }

                        if (parent.contains(child))
                        {               // если большая содержит меньшую
                            parent.subtraction(child);              //  то вычитаем меньшую из большей
                            repeat = true;                            //  фиксируем факт изменения групп
                        }
                        else if (groupI.overlaps(groupJ))
                        {    // иначе если группы пересекаются
                            if (groupI.getUnitCount() > groupJ.getUnitCount())// определяем большую и меньшую группы по кол-ву мин
                            {
                                parent = groupI;
                                child = groupJ;
                            }
                            else
                            {
                                child = groupI;
                                parent = groupJ;
                            }
                            Group overlap = parent.getOverlap(child);// то берем результат пересечения

                            if (overlap != null)
                            {                  //  и если он имеет смысл (в результате пересечения выявились ячейки с 0% или 100%)
                                groups.add(overlap);                //  то вносим соответствующие коррективы в список
                                parent.subtraction(overlap);
                                child.subtraction(overlap);
                                repeat = true;
                            }
                        }
                    }
                }
            }
            while(repeat);
        }
    }



    void clear()
    {
        for (BotCell[] x: botField)
        {
            for (BotCell z: x)
            {
                z.setValue(-1);
            }
        }

        for (Ship ship: botShips)
        {
            ship.clear();
        }

        groups.clear();

        stepCount = 0;

        botLog = new StringBuilder("Bot Log\n");
    }

    public BotMode getMode() {
        return mode;
    }

    public void setMode(BotMode mode) {
        this.mode = mode;
    }

    public String getBotLog()
    {
        return botLog.toString();
    }

    public int getStepCount() {
        return stepCount;
    }
}
