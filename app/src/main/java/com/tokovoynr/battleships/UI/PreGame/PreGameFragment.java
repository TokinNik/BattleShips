package com.tokovoynr.battleships.UI.PreGame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import com.tokovoynr.battleships.R;


public class PreGameFragment extends Fragment implements View.OnTouchListener
{
    public static final String TAG = "PRE_GAME_FRAGMENT";
    private View view;
    private OnPreGameFragmentInteractionListener listener;
    private Shadow selected_item = null;
    private int offsetX = 0;
    private int offsetY = 0;
    private boolean touchFlag = false;
    private boolean targetFlag = false;
    private int borderX1, borderY1, borderX2, borderY2;
    private int selectedShip;
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



        final TableLayout mainField = view.findViewById(R.id.main_field);

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
                            Log.d(TAG, "MOVE ROOT " + x + "  " + y);
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                            if (x < borderX2 && x > borderX1 && y < borderY2 && y > borderY1)
                            {
                                targetFlag = true;
                                selected_item.setAlpha(1f);
                                if (checkCellsBorder(x, y, lp))
                                {
                                    return false;
                                }
                            }
                            else if (targetFlag)
                            {
                                targetFlag = false;
                                selected_item.setAlpha(0.5f);
                            }

                            lp.setMargins(x - offsetX/2, y - offsetY, 0, 0);
                            selected_item.setLayoutParams(lp);
                            return false;
                        case MotionEvent.ACTION_UP:
                            Log.d(TAG, "UP ROOT");
                            touchFlag = false;
                            if (targetFlag)
                            {
                                targetFlag = false;
                                ((Cell)view.findViewWithTag(String.valueOf(selectedShip))).setType(selected_item.getType());
                            }
                            root.removeView(selected_item);
                            selected_item = null;
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

        FrameLayout mainFieldFrame = view.findViewById(R.id.main_field_frame);
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

        Log.d(TAG, "!!!!!!!! " + this.borderX1 + " " + this.borderY1 + " " + this.borderX2 + " " + this.borderY2 + " " + mainField.getWidth() + " " + mainFieldFrame.getHeight() + " " + mainField.getScaleX());
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
    public void onDetach()
    {
        super.onDetach();
        listener = null;
    }

    public interface OnPreGameFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);

    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouch(View v, MotionEvent event)
    {
       switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "DOWN DnDView");
                mathMainFieldBorder();
                touchFlag = true;
                offsetX = (int) event.getX();
                offsetY = (int) event.getY();

                Shadow shadow = new Shadow(view.getContext());

                switch(v.getId())
                {
                    case R.id.dnd_ship_1:
                        shadow.setImageDrawable(getResources().getDrawable(R.drawable.red_box));
                        shadow.setType(Cell.CellType.MINE);
                        break;
                    case R.id.dnd_ship_2:
                        shadow.setImageDrawable(getResources().getDrawable(R.drawable.yellow_box));
                        shadow.setType(Cell.CellType.MINE);
                        break;
                    case R.id.dnd_ship_3:
                        shadow.setImageDrawable(getResources().getDrawable(R.drawable.green_box));
                        shadow.setType(Cell.CellType.SHIP);
                        break;
                    case R.id.dnd_ship_4:
                        shadow.setImageDrawable(getResources().getDrawable(R.drawable.blue_box));
                        shadow.setType(Cell.CellType.MINE);
                        break;
                    case R.id.dnd_ship_5:
                        shadow.setImageDrawable(getResources().getDrawable(R.drawable.white_box));
                        shadow.setType(Cell.CellType.EMPTY);
                        break;
                    default:
                        shadow.setImageDrawable(getResources().getDrawable(R.drawable.red_box));
                        shadow.setType(Cell.CellType.MINE);
                        break;

                }
                shadow.setAlpha(0.5f);
                shadow.setPivotX(0);
                shadow.setPivotY(0);
                shadow.setScaleX(scale);
                shadow.setScaleY(scale);
                shadow.setScaleType(ImageView.ScaleType.FIT_XY);
                shadow.setBackgroundColor(Color.BLACK);
                shadow.setLayoutParams(v.getLayoutParams());
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.findViewById(R.id.ship_buttons_layout).getLayoutParams();

                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.setMargins(v.getLeft() + v.getWidth()/2, 0, 0, marginLayoutParams.bottomMargin);
                shadow.setLayoutParams(layoutParams);

                RelativeLayout l = view.findViewById(R.id.relative_main);
                l.addView(shadow);

                selected_item = shadow;
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
                Log.d(TAG, " ||||| " + scale);
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
                                lp.setMargins(cord[0], cord[1], 0, 0);
                                Log.d(TAG, "---- " + cord[0] + " " + cord[1]);
                                selected_item.setLayoutParams(lp);
                                selectedShip = i;
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

}