package com.tokovoynr.battleships.UI.PreGame;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokovoynr.battleships.R;
import com.tokovoynr.battleships.UI.MainActivity;
import com.tokovoynr.battleships.game.GameLogic;
import com.tokovoynr.battleships.game.Ship;
import com.tokovoynr.battleships.game.ShootResult;


public class PreGameFragment extends Fragment implements View.OnTouchListener, Cell.OnCellListener, View.OnClickListener
{
    public static final String TAG = "PRE_GAME_FRAGMENT";
    private View view;
    private OnPreGameFragmentInteractionListener listener;
    private Shadow selectedItem = null;
    private int offsetX = 0;
    private int offsetY = 0;
    private boolean touchFlag = false;
    private boolean targetFlag = false;
    private boolean relocateFlag = false;
    private int borderX1, borderY1, borderX2, borderY2;
    private int activeShip;
    private Cell selectedCell;
    private Cell relocatedCell;
    private float scale;
    private int displayWidth;
    private int displayHeight;


    public PreGameFragment()
    {

    }

    public static PreGameFragment newInstance()
    {
        PreGameFragment fragment = new PreGameFragment();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);



    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_pre_game, container, false);

        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        displayWidth = displaymetrics.widthPixels;
        displayHeight = displaymetrics.heightPixels;

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

        final RelativeLayout root = view.findViewById(R.id.relative_main);
        root.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                if (touchFlag)
                {
                    switch (event.getActionMasked())
                    {
                        case MotionEvent.ACTION_DOWN:
                            Log.d(TAG, "DOWN ROOT");
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            int x = (int) event.getX();
                            int y = (int) event.getY();
                            //Log.d(TAG, "MOVE ROOT " + x + "  " + y);
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                    new ViewGroup.MarginLayoutParams((int)getResources().getDimension(R.dimen.cell_size),
                                                            (int)getResources().getDimension(R.dimen.cell_size) * selectedItem.getDeckCount()));

                            if (x < borderX2 && x > borderX1 && y < borderY2 && y > borderY1)
                            {
                                targetFlag = true;
                                selectedItem.setAlpha(1f);
                                if (checkCellsBorder(x, y, lp))
                                {
                                    if (selectedItem.getType() != Cell.CellType.MINE)
                                        if (!MainActivity.getGameLogic().checkPosition(selectedCell.getIntTag(), selectedItem.getDeckCount(), selectedItem.getDirection()))
                                        {
                                            selectedItem.enableErr();
                                            //Log.d(TAG, "checkPosition false");
                                        }
                                        else
                                        {
                                            selectedItem.disableErr();
                                            //Log.d(TAG, "checkPosition true");
                                        }
                                    return false;
                                }
                            }
                            else if (targetFlag)
                            {
                                targetFlag = false;
                                selectedItem.setAlpha(0.5f);
                            }

                            lp.setMargins(x - offsetX/2, y - offsetY, 0, 0);
                            selectedItem.setLayoutParams(lp);
                            return false;
                        case MotionEvent.ACTION_UP:
                            Log.d(TAG, "UP ROOT");
                            touchFlag = false;
                            if (targetFlag)
                            {
                                targetFlag = false;

                                if (selectedCell.getType() == Cell.CellType.EMPTY && !selectedItem.isErrrMode())
                                {
                                    if(relocateFlag)
                                    {
                                        relocateFlag = false;
                                        relocatedCell = null;
                                    }
                                    setShip(selectedCell.getIntTag(), selectedItem.getType().ordinal(), selectedItem.getDirection());
                                }
                                else if (relocateFlag)
                                {
                                    relocateFlag = false;
                                    Log.d(TAG, "onTouch: errmode+relocate");
                                    setShip(relocatedCell.getIntTag(), relocatedCell.getType().ordinal(), relocatedCell.getDirection());
                                    relocatedCell = null;
                                }
                            }
                            else
                            {
                                if (relocateFlag)
                                {
                                    relocateFlag = false;
                                    relocatedCell = null;
                                    //setShip(relocatedCell.getIntTag(), relocatedCell.getType().ordinal(), relocatedCell.getDirection());
                                }
                            }

                            root.removeView(selectedItem);
                            selectedItem = null;
                            break;
                        default:
                            break;
                    }
                    return !targetFlag;
                }
                return false;
            }
        });

        view.findViewById(R.id.dnd_ship_1).setOnTouchListener(this);
        view.findViewById(R.id.dnd_ship_2).setOnTouchListener(this);
        view.findViewById(R.id.dnd_ship_3).setOnTouchListener(this);
        view.findViewById(R.id.dnd_ship_4).setOnTouchListener(this);
        view.findViewById(R.id.dnd_ship_5).setOnTouchListener(this);
        view.findViewById(R.id.button_rotate).setOnTouchListener(this);
        view.findViewById(R.id.button_ready).setOnTouchListener(this);
        view.findViewById(R.id.button_exit).setOnTouchListener(this);

        return view;
    }

    private void mathMainFieldBorder()
    {
        TableLayout mainField = view.findViewById(R.id.main_field);
        int[] cord = new int[2];
        mainField.getLocationOnScreen(cord);
        int borderX1 = cord[0];
        int borderY1 = cord[1];
        scale =  mainField.getScaleX();
        int borderX2 = (int)(mainField.getWidth() * scale) + cord[0];
        int borderY2 = (int)(mainField.getHeight() * scale) + cord[1];

        RelativeLayout mainFieldFrame = view.findViewById(R.id.main_field_frame);
        int[] cordFrame = new int[2];
        mainFieldFrame.getLocationOnScreen(cordFrame);
        int borderX1F = cordFrame[0];
        int borderY1F = cordFrame[1];
        int borderX2F = mainFieldFrame.getWidth() + cordFrame[0];
        int borderY2F = mainFieldFrame.getHeight() + cordFrame[1];

        this.borderX1 = (borderX1 < 0) ? borderX1F : borderX1;
        this.borderY1 = (borderY1 < 0) ? borderY1F : borderY1;
        this.borderX2 = (borderX2 > borderX2F) ? borderX2F : borderX2;
        this.borderY2 = (borderY2 > borderY2F) ? borderY2F : borderY2;

        //Log.d(TAG, "!!!!!!!! " + this.borderX1 + " " + this.borderY1 + " " + this.borderX2 + " " + this.borderY2 + " " + mainField.getWidth() + " " + mainFieldFrame.getHeight() + " " + mainField.getScaleX());
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
        if (context instanceof OnPreGameFragmentInteractionListener)
        {
            listener = (OnPreGameFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnPreGameFragmentInteractionListener");
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        for (int i = 1; i < 145; i++)
        {
            ((Cell)view.findViewWithTag(String.valueOf(i))).setListener(this);
        }

        if (MainActivity.getGameLogic().getGameMode() == GameLogic.GameMode.PvP)
        {
            createWaitDialog();
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        listener = null;
    }

    public interface OnPreGameFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);

        void onPreGameClick(View view);

        void onWaitCancel();

    }

    @Override
    public void onClick(View view)
    {
        listener.onPreGameClick(view);
        Log.d(TAG, ": !!!!!!!!!!");
    }

    @Override
    public void onCellTouch(View v, MotionEvent event)
    {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
            ((TextView)view.findViewById(R.id.textView_selected_cell)).setText( ((Cell)v).getCordString(((Cell) v).getIntTag()) );
        }

        onTouch(v, event);
    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouch(View v, MotionEvent event)
    {
       switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "DOWN DnDView");
                if (v.getId() == R.id.button_rotate && activeShip != 0)
                {
                    rotateShip();
                    return false;
                }
                if (v.getId() == R.id.button_ready)
                {
                    if (MainActivity.getGameLogic().isCorrectPlacement(true))
                    {
                        listener.onPreGameClick(v);
                        return false;
                    }
                    else
                    {
                        Toast.makeText(getContext(),"Расставлены не все корабли или мины", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                if (v.getId() == R.id.button_exit)
                {
                    listener.onPreGameClick(v);
                    return false;
                }

                mathMainFieldBorder();
                touchFlag = true;
                offsetX = (int) event.getX();
                offsetY = (int) event.getY();
                int scaleY = 1;
                Shadow shadow = new Shadow(view.getContext());

                switch(v.getId())
                {
                    case R.id.dnd_ship_1:
                        shadow.setImageDrawable(getResources().getDrawable(R.drawable.ship_1));
                        shadow.setType(Cell.CellType.SHIP_1);
                        break;
                    case R.id.dnd_ship_2:
                        shadow.setImageDrawable(getResources().getDrawable(R.drawable.ship_2));
                        shadow.setType(Cell.CellType.SHIP_2);
                        offsetY += (int)getResources().getDimension(R.dimen.cell_size);
                        scaleY = 2;
                        break;
                    case R.id.dnd_ship_3:
                        shadow.setImageDrawable(getResources().getDrawable(R.drawable.ship_3));
                        shadow.setType(Cell.CellType.SHIP_3);
                        offsetY += (int)getResources().getDimension(R.dimen.cell_size);
                        scaleY = 3;
                        break;
                    case R.id.dnd_ship_4:
                        shadow.setImageDrawable(getResources().getDrawable(R.drawable.ship_4));
                        shadow.setType(Cell.CellType.SHIP_4);
                        offsetY += (int)getResources().getDimension(R.dimen.cell_size) * 2;
                        scaleY = 4;
                        break;
                    case R.id.dnd_ship_5:
                        shadow.setImageDrawable(getResources().getDrawable(R.drawable.mine_active));
                        shadow.setType(Cell.CellType.MINE);
                        break;
                    default:
                        if (v.getTag() != null)
                        {
                            if (((Cell)v).getType() != Cell.CellType.EMPTY)
                            {
                                switch (((Cell) v).getType())
                                {
                                    case SHIP_1:
                                        shadow.setImageDrawable(((Cell) v).getDrawable());
                                        break;
                                    case SHIP_2:
                                        shadow.setImageDrawable(getResources().getDrawable(R.drawable.ship_2));
                                        scaleY = 2;
                                        break;
                                    case SHIP_3:
                                        shadow.setImageDrawable(getResources().getDrawable(R.drawable.ship_3));
                                        scaleY = 3;
                                        break;
                                    case SHIP_4:
                                        shadow.setImageDrawable(getResources().getDrawable(R.drawable.ship_4));
                                        scaleY = 4;
                                        break;
                                    case MINE:
                                        shadow.setImageDrawable(((Cell) v).getDrawable());
                                        break;
                                }
                                shadow.setType(((Cell) v).getType());
                                shadow.setDirection(((Cell) v).getDirection());
                                relocateFlag = true;
                                targetFlag = true;
                                relocatedCell = new Cell(view.getContext());
                                relocatedCell.setDirection(((Cell) v).getDirection());
                                relocatedCell.setType(((Cell) v).getType());
                                relocatedCell.setTag(v.getTag());
                                ShootResult[] results = MainActivity.getGameLogic().removeShip(relocatedCell.getIntTag());
                                if(results.length != 0)
                                {
                                    for (ShootResult result : results)
                                    {
                                        ((Cell)view.findViewWithTag(String.valueOf(result.getNumArg1()))).clear();
                                    }
                                    Log.d(TAG, "rremoveShip true");
                                }
                                else
                                {
                                    Log.d(TAG, "removeShip false");
                                }
                            }
                            else
                            {
                                activeShip = 0;
                                touchFlag = false;
                                return false;
                            }
                        }
                        else
                        {
                            shadow.setImageDrawable(getResources().getDrawable(R.drawable.red_box));
                            shadow.setType(Cell.CellType.ERR);
                        }
                        break;

                }
                shadow.setAlpha(0.5f);
                shadow.setPivotX(0);
                shadow.setPivotY(0);
                shadow.setScaleX(scale);
                shadow.setScaleY(scale);
                shadow.setDeckCount(scaleY);
                shadow.setScaleType(ImageView.ScaleType.FIT_XY);
                shadow.setBackgroundColor(Color.BLACK);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        new ViewGroup.MarginLayoutParams((int)getResources().getDimension(R.dimen.cell_size),
                                                 (int)getResources().getDimension(R.dimen.cell_size) * scaleY));
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.findViewById(R.id.ship_buttons_layout).getLayoutParams();
                if (!relocateFlag)
                {
                    layoutParams.setMargins(v.getLeft() + v.getWidth() / 2, 0, 0, marginLayoutParams.bottomMargin);
                }
                else
                {
                    int[] cord = new int[2];
                    v.getLocationOnScreen(cord);
                    layoutParams.setMargins(cord[0], 0, 0, displayHeight - cord[1] - (int)getResources().getDimension(R.dimen.cell_size) * scaleY / 2);
                }

                shadow.setLayoutParams(layoutParams);
                RelativeLayout l = view.findViewById(R.id.relative_main);
                l.addView(shadow);
                selectedItem = shadow;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "MOVE DnDView");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "UP DnDView");
                break;
            default:
                break;
        }
        return false;
    }

    private void setShip(int anchorCell, int deckCount, Ship.ShipDirection direction)
    {
        Log.d(TAG, "setShip: " + anchorCell + " " + deckCount);
        ShootResult[] results = MainActivity.getGameLogic().setShip(anchorCell, deckCount, direction, true);
        if(results.length != 0)
        {
            Log.d(TAG, "setShip true");
            Cell cell = null;
            for (ShootResult result : results)
            {

                cell = view.findViewWithTag(String.valueOf(result.getNumArg1()));
                cell.setType(result.getType());
                cell.setPartNum(result.getNumArg2());
                cell.setDirection(result.getDirection());
            }
            activeShip = anchorCell;
        }
        else
        {
            Log.d(TAG, "setShip false");
        }
    }

    private  void rotateShip()
    {
        int deckCount;
        Ship.ShipDirection direction;

        deckCount = ((Cell)view.findViewWithTag(String.valueOf(activeShip))).getType().ordinal();
        direction = Ship.rotate(((Cell)view.findViewWithTag(String.valueOf(activeShip))).getDirection());

        ShootResult[] results = MainActivity.getGameLogic().removeShip(activeShip);
        if(results.length != 0)
        {
            for (ShootResult result : results)
            {
                ((Cell)view.findViewWithTag(String.valueOf(result.getNumArg1()))).clear();
            }
            Log.d(TAG, "rremoveShip true");
        }
        else
        {
            Log.d(TAG, "rremoveShip false");
        }

        setShip(activeShip, deckCount, direction);
    }


    private boolean checkCellsBorder(int eX, int eY, RelativeLayout.LayoutParams lp)
    {
        int left = 1, right = 12, mid = (left + right)/2;
        int i = ((mid - 1) * 12) + 1;
        int size = 0;
        boolean first = true;
        while(true)
        {
            int[] cord = new int[2];
            Cell cell = view.findViewWithTag(String.valueOf(i));
            cell.getLocationOnScreen(cord);
            if (first)
            {
                float scale = view.findViewById(R.id.main_field).getScaleX();
                //Log.d(TAG, " ||||| " + scale);
                if (scale > 1.0)
                    size = (int)(cell.getWidth() * scale);
                else
                    size = cell.getWidth();
                first = false;
            }

            //Log.d(TAG, "--- " + size + " x " + cord[0] + " y " + cord[1] + " size " + size);

            if(cord[1] <= eY)
            {
                if(cord[1] + size >= eY)
                {
                    int iY = i;
                    left = 1;
                    right = 12;
                    mid = (left + right)/2;
                    i = iY + mid - 1;
                    while (true)
                    {
                        cell = view.findViewWithTag(String.valueOf(i));
                        cell.getLocationOnScreen(cord);

                        if(cord[0] <= eX)
                        {
                            if(cord[0] + size >= eX)
                            {
                                int shift = 0;
                                Cell.CellType type = selectedItem.getType();
                                if (type == Cell.CellType.SHIP_2 || type == Cell.CellType.SHIP_3)
                                    shift = (int)(getResources().getDimension(R.dimen.cell_size) * scale);
                                else if (type == Cell.CellType.SHIP_4)
                                    shift = (int)(getResources().getDimension(R.dimen.cell_size) * scale) * 2;

                                //lp.setMargins(cord[0], cord[1] - shift, 0, 0);
                                switch (selectedItem.getDirection())//fixme kostil' pzdc, ewe i kosachni'
                                {
                                    case UP:
                                        lp.setMargins(cord[0], cord[1] - shift, 0, 0);
                                        break;
                                    case RIGHT:
                                        lp.setMargins(cord[0] + ((type == Cell.CellType.SHIP_4) ? (int)(shift * 1.5) : shift * 2), cord[1], 0, 0);
                                        break;
                                    case DOWN:
                                        lp.setMargins(cord[0] + ((type == Cell.CellType.SHIP_4) ? (int)(shift * 0.5) : shift), cord[1] + ((type == Cell.CellType.SHIP_4) ? (int)(shift * 1.5) : shift * 2), 0, 0);
                                        break;
                                    case LEFT:
                                        lp.setMargins(cord[0] - shift, cord[1] + ((type == Cell.CellType.SHIP_4) ? (int)(shift * 0.5) : shift), 0, 0);
                                        break;
                                }
                                //Log.d(TAG, "---- " + cord[0] + " " + cord[1]);
                                selectedItem.setLayoutParams(lp);
                                selectedCell = view.findViewWithTag(String.valueOf(i));
                                return true;
                            }
                            else
                            {
                                left = mid;
                                mid = (right + mid)/2;

                                if (mid == 12) return false;
                                if (mid == 11) mid = 12;

                                i = iY + mid - 1;
                                //Log.d(TAG,"eX > cordX+size | left = " + left + " right = " + right + " mid = " + mid + " i = " + i + " cX = " + cord[0] + " size = " + size + " eX = " + eX);
                            }
                        }
                        else
                        {
                            right = mid;
                            mid = (left + mid)/2;

                            if (left == 12 && right == 12) return false;

                            i = iY + mid - 1;
                            //Log.d(TAG,"eX < cordX | left = " + left + " right = " + right + " mid = " + mid + " i = " + i + " cX = " + cord[0] + " size = " + size + " eX = " + eX);
                        }
                    }
                }
                else
                {
                    left = mid;
                    mid = (right + mid)/2;


                    if (mid == 12) return false;
                    if (mid == 11) mid = 12;

                    i = ((mid - 1) * 12) + 1;
                    //Log.d(TAG,"eY > cordY+size | left = " + left + " right = " + right + " mid = " + mid + " i = " + " cY = " + cord[1] + " size = " + size + " eY = " + eY);
                }

            }
            else
            {
                right = mid;
                mid = (left + mid)/2;

                if (left == 1 && right == 1) return false;

                i = ((mid - 1) * 12) + 1;
                //Log.d(TAG,"eY < cordY | left = " + left + " right = " + right + " mid = " + mid + " i = " + i + " cY = " + cord[1] + " size = " + size + " eY = " + eY);
            }
        }
    }

    private void createWaitDialog()
    {
        ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setLayoutParams(new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                listener.onWaitCancel();
                dialog.cancel();
            }
        });
        builder.setTitle(R.string.wait_other_player);
        builder.setMessage(R.string.wait_other_player_message);
        builder.setCancelable(false);
        builder.setView(progressBar);
        final AlertDialog wimDialog = builder.create();
        wimDialog.show();

        while(true)
        {
            try
            {
                Thread.sleep(2000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            if (MainActivity.getConnection().readyLobby())
            {
                wimDialog.cancel();
                return;
            }
        }
    }
}
