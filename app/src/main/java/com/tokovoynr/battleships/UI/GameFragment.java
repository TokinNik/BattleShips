package com.tokovoynr.battleships.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokovoynr.battleships.R;
import com.tokovoynr.battleships.UI.PreGame.Cell;
import com.tokovoynr.battleships.game.Ship;
import com.tokovoynr.battleships.game.ShootResult;


public class GameFragment extends Fragment implements View.OnClickListener, Cell.OnCellListener
{
    public static final String TAG = "GAME_FRAGMENT";
    private View view;
    private int displayWidth;
    private int displayHeight;
    private Cell selectedCell;
    private boolean playerDesk = false;
    private ShootResult lastEnemyStep;
    private GameFragment.OnGameFragmentInteractionListener listener;

    public GameFragment()
    {

    }

    public static GameFragment newInstance()
    {
        GameFragment fragment = new GameFragment();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_game, container, false);

        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        displayWidth = displaymetrics.widthPixels;
        displayHeight = displaymetrics.heightPixels;

        RelativeLayout root = view.findViewById(R.id.relative_main);

        if (displayWidth < 780)//fixme nado normalno adaptirovat' razmeri
        {
            TableLayout mainField = view.findViewById(R.id.main_field);
            mainField.setScaleX(0.9f);
            mainField.setScaleY(0.9f);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) mainField.getLayoutParams();
            lp.setMargins(mlp.leftMargin - 20, mlp.topMargin , 0, 0);
            mainField.setLayoutParams(lp);//fixme only on my device!!!!!!
        }

        root.findViewById(R.id.button_fire).setOnClickListener(this);
        root.findViewById(R.id.button_capitulate).setOnClickListener(this);
        root.findViewById(R.id.button_switch_field).setOnClickListener(this);

        return view;
    }

    public void onButtonPressed(Uri uri)
    {
        if (listener != null)
        {
            listener.onFragmentInteraction(uri);
        }


    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof GameFragment.OnGameFragmentInteractionListener)
        {
            listener = (GameFragment.OnGameFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnGameFragmentInteractionListener");
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        for (int i = 1; i < 145; i++)
        {
            ((Cell)view.findViewWithTag(String.valueOf(i))).setListener(this);
        }
        MainActivity.getGameLogic().setShip(1, 1, Ship.ShipDirection.UP, false);
        MainActivity.getGameLogic().setShip(3, 1, Ship.ShipDirection.UP, false);
        MainActivity.getGameLogic().setShip(5, 1, Ship.ShipDirection.UP, false);
        MainActivity.getGameLogic().setShip(7, 1, Ship.ShipDirection.UP, false);
        MainActivity.getGameLogic().setShip(38, 2, Ship.ShipDirection.UP, false);
        MainActivity.getGameLogic().setShip(40, 2, Ship.ShipDirection.UP, false);
        MainActivity.getGameLogic().setShip(42, 2, Ship.ShipDirection.UP, false);
        MainActivity.getGameLogic().setShip(122, 3, Ship.ShipDirection.UP, false);
        MainActivity.getGameLogic().setShip(124, 3, Ship.ShipDirection.UP, false);
        MainActivity.getGameLogic().setShip(95, 4, Ship.ShipDirection.UP, false);
        MainActivity.getGameLogic().setShip(12, 5, Ship.ShipDirection.UP, false);
        MainActivity.getGameLogic().setShip(24, 5, Ship.ShipDirection.UP, false);
        MainActivity.getGameLogic().setShip(36, 5, Ship.ShipDirection.UP, false);
        MainActivity.getGameLogic().setShip(48, 5, Ship.ShipDirection.UP, false);
        MainActivity.getGameLogic().setShip(60, 5, Ship.ShipDirection.UP, false);
        MainActivity.getGameLogic().setShip(72, 5, Ship.ShipDirection.UP, false);
        MainActivity.getGameLogic().setShip(84, 5, Ship.ShipDirection.UP, false);
        MainActivity.getGameLogic().setShip(96, 5, Ship.ShipDirection.UP, false);
        MainActivity.getGameLogic().setShip(108, 5, Ship.ShipDirection.UP, false);
        MainActivity.getGameLogic().setShip(120, 5, Ship.ShipDirection.UP, false);
        setShipsOnDesk(playerDesk);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCellTouch(View v, MotionEvent event)
    {
        ((TextView)view.findViewById(R.id.textView_selected_cell)).setText( ((Cell)v).getCordString(((Cell) v).getIntTag()) );
        if (!playerDesk)
        {
            if (selectedCell != null)
            {
                selectedCell.setType(Cell.CellType.EMPTY);
            }
            selectedCell = ((Cell) v);
            if (selectedCell.getType() == Cell.CellType.EMPTY && selectedCell.getPartNum() < 0)
            {
                ((Cell) v).setType(Cell.CellType.ERR);
            }
            else
            {
                selectedCell = null;
            }
        }
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_capitulate:
                //Log.d(TAG, "onClick: capitulate");
                break;
            case R.id.button_fire:
                Log.d(TAG, "onClick: fire");
                if (selectedCell != null && selectedCell.getType() == Cell.CellType.ERR)
                {
                    ShootResult result = MainActivity.getGameLogic().shoot(selectedCell.getIntTag(), false);
                    selectedCell.setType(result.getType());
                    switch(result.getResult())
                    {
                        case EMPTY:
                            selectedCell.setPartNum(result.getNumArg1());
                            selectedCell.setPlayersField(false);
                            Log.d(TAG, "onClick: EMPTY");
                            lastEnemyStep = MainActivity.getGameLogic().switchTurn();
                            if (lastEnemyStep.getNumArg2() > 0)
                            {
                                switch (lastEnemyStep.getResult())
                                {
                                    case EMPTY:
                                        Toast.makeText(getContext(), "Ход противника: " + selectedCell.getCordString(lastEnemyStep.getNumArg2()) + " - Мимо" , Toast.LENGTH_SHORT).show();
                                        break;
                                    case MINE:
                                        Toast.makeText(getContext(), "Ход противника: " + selectedCell.getCordString(lastEnemyStep.getNumArg2()) + " - Мина", Toast.LENGTH_SHORT).show();
                                        break;
                                    case SHIP_PART:
                                        Toast.makeText(getContext(), "Ход противника: " + selectedCell.getCordString(lastEnemyStep.getNumArg2()) + " - Попадание" , Toast.LENGTH_SHORT).show();
                                        break;
                                    case SHIP_DESTROY:
                                        Toast.makeText(getContext(), "Ход противника: " + selectedCell.getCordString(lastEnemyStep.getNumArg2()) + " - Уничтожен" , Toast.LENGTH_SHORT).show();
                                        break;
                                    case ENEMY_WIN:
                                        endGame(false);
                                        break;
                                }
                            }
                            break;
                        case SHIP_PART:
                            selectedCell.setPartNum(result.getNumArg1());
                            selectedCell.setDestroyed(true);
                            break;
                        case SHIP_DESTROY:
                            selectedCell.setPartNum(result.getNumArg1());
                            selectedCell.setDestroyed(true);
                            if (MainActivity.getGameLogic().winCheck(true))
                            {
                                endGame(true);
                            }
                            break;
                        case MINE:
                            selectedCell.setDestroyed(true);
                            Log.d(TAG, "onClick: MINE");
                            lastEnemyStep = MainActivity.getGameLogic().switchTurn();
                            if (lastEnemyStep.getNumArg2() > 0)
                            {
                                if (lastEnemyStep.getType() == Cell.CellType.EMPTY)
                                {
                                    Toast.makeText(getContext(), "Ход противника: " + selectedCell.getCordString(lastEnemyStep.getNumArg2()) + " - Мимо" , Toast.LENGTH_SHORT).show();
                                }
                                else if (lastEnemyStep.getType() == Cell.CellType.MINE)
                                    Toast.makeText(getContext(), "Ход противника: " + selectedCell.getCordString(lastEnemyStep.getNumArg2()) + " - Мина", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getContext(), "Ход противника: " + selectedCell.getCordString(lastEnemyStep.getNumArg2()) + " - Попадание" , Toast.LENGTH_SHORT).show();
                            }
                            break;
                        default:
                            break;
                    }
                    selectedCell = null;
                }
                break;
            case R.id.button_switch_field:
                //Log.d(TAG, "onClick: switch field");
                playerDesk = !playerDesk;
                setShipsOnDesk(playerDesk);
                if (playerDesk)
                {
                    ((Button) view.findViewById(R.id.button_switch_field)).setText(getResources().getText(R.string.switch_to_enemy_field));
                }
                else
                {
                    ((Button) view.findViewById(R.id.button_switch_field)).setText(getResources().getText(R.string.switch_to_your_field));
                }
                break;
        }
    }



    public interface OnGameFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
        void onGameEnd();

    }

    private void setShipsOnDesk(boolean playerTurn)
    {
        ShootResult[] results;
        if (playerTurn)
        {
            results = MainActivity.getGameLogic().getPlayerCells();
        }
        else
        {
            results = MainActivity.getGameLogic().getEnemyCells();
        }
        if(results.length != 0)
        {
            //Log.d(TAG, "setShipsOnDesk true");
            Cell cell = null;
            for (ShootResult result : results)
            {
                cell = view.findViewWithTag(String.valueOf(result.getNumArg1()));
                cell.setDestroyed(result.isBoolArg());
                cell.setPlayersField(playerTurn);
                if(playerTurn)
                {
                    cell.setType(result.getType());
                    cell.setPartNum(result.getNumArg2());
                    cell.setDirection(result.getDirection());
                }
                else
                {
                    if(result.getType() == Cell.CellType.EMPTY || result.isBoolArg())
                    {
                        cell.setType(result.getType());
                        cell.setPartNum(result.getNumArg2());
                        cell.setDirection(result.getDirection());
                    }
                    else
                    {
                        cell.clear();
                    }
                }
            }
        }
        else
        {
            Log.d(TAG, "setShipsOnDesk false");
        }
    }

    void endGame(boolean playerWin)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onGameEnd();
            }
        });
        builder.setTitle(R.string.game_end);
        if (playerWin)
            builder.setMessage(R.string.winner_message);
        else
            builder.setMessage(R.string.looser_message);
        builder.setCancelable(false);
        AlertDialog wimDialog = builder.create();
        wimDialog.show();
    }
}
