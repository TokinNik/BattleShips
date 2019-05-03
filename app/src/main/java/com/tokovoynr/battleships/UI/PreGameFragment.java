package com.tokovoynr.battleships.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import com.tokovoynr.battleships.R;
import com.tokovoynr.battleships.UI.Lobby.Cell;


public class PreGameFragment extends Fragment implements View.OnTouchListener
{
    public static final String TAG = "PRE_GAME_FRAGMENT";
    private View view;
    private OnPreGameFragmentInteractionListener listener;
    private ImageView selected_item = null;
    private int offsetX = 0;
    private int offsetY = 0;
    private boolean touchFlag = false;
    private boolean targetFlag = false;
    private int borderX1, borderY1, borderX2, borderY2;
    private boolean firstFlag = true;

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
        final int displayWidth = displaymetrics.widthPixels;
        final int displayHeight = displaymetrics.heightPixels;

        view.findViewById(R.id.main_field).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getActionMasked())
                {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "DOWN TABLE " + v.getId());
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG, "MOVE TABLE");
                        return true;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "UP TABLE");
                        return false;
                }
                return false;
            }
        });

        final RelativeLayout root = view.findViewById(R.id.relative_main);
        root.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN)
                    Log.d(TAG, "DOWN ROOT " + v.getId());
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

                            if (firstFlag)
                            {
                                firstFlag = false;
                                TableLayout mainField = view.findViewById(R.id.main_field);
                                int[] cord = new int[2];
                                mainField.getLocationOnScreen(cord);
                                borderX1 = cord[0];
                                borderY1 = cord[1];
                                borderX2 = mainField.getWidth() + cord[0];
                                borderY2 = mainField.getHeight() + cord[1];
                            }

                            if (x < borderX2 && x > borderX1 && y < borderY2 && y > borderY1)
                            {
                                targetFlag = true;
                                selected_item.setAlpha(1f);
                                if (checkCellsBorder(event.getX(), event.getY(), lp))
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
                                selected_item.setAlpha(1f);
                                targetFlag = false;
                            }
                            else
                            {
                                root.removeView(selected_item);
                            }
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
                touchFlag = true;
                offsetX = (int) event.getX();
                offsetY = (int) event.getY();

                ImageView shadow = new ImageView(view.getContext());

                switch(v.getId())
                {
                    case R.id.dnd_ship_1:
                        shadow.setImageDrawable(getResources().getDrawable(R.drawable.red_box));
                        break;
                    case R.id.dnd_ship_2:
                        shadow.setImageDrawable(getResources().getDrawable(R.drawable.yellow_box));
                        break;
                    case R.id.dnd_ship_3:
                        shadow.setImageDrawable(getResources().getDrawable(R.drawable.green_box));
                        break;
                    case R.id.dnd_ship_4:
                        shadow.setImageDrawable(getResources().getDrawable(R.drawable.blue_box));
                        break;
                    case R.id.dnd_ship_5:
                        shadow.setImageDrawable(getResources().getDrawable(R.drawable.white_box));
                        break;
                    default:
                        shadow.setImageDrawable(getResources().getDrawable(R.drawable.red_box));
                        break;

                }
                shadow.setAlpha(0.5f);
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


    private boolean checkCellsBorder(float eX, float eY, RelativeLayout.LayoutParams lp)
    {
        int left = 1, right = 12, mid = (left + right)/2;
        int i = ((mid - 1) * 12) + 1;
        while(true)//fixme vremenno
        {
            int[] cord = new int[2];
            Cell cell = view.findViewWithTag(String.valueOf(i));
            cell.getLocationOnScreen(cord);
            int size = cell.getWidth();

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
                        size = cell.getWidth();

                        if(cord[0] <= eX)
                        {
                            if(cord[0] + size >= eX)
                            {
                                lp.setMargins(cord[0], cord[1], 0, 0);
                                selected_item.setLayoutParams(lp);
                                return true;
                            }
                            else
                            {
                                left = mid;
                                mid = (right + mid)/2;

                                if (mid == 12) return false;//fixme costil'?
                                if (mid == 11) mid = 12;

                                i = iY + mid - 1;
                            }
                        }
                        else
                        {
                            right = mid;
                            mid = (left + mid)/2;

                            if (left == 12 && right == 12) return false;//fixme costil'?

                            i = iY + mid - 1;
                        }
                    }
                }
                else
                {
                    left = mid;
                    mid = (right + mid)/2;


                    if (mid == 12) return false;//fixme costil'?
                    if (mid == 11) mid = 12;

                    i = ((mid - 1) * 12) + 1;
                }

            }
            else
            {
                right = mid;
                mid = (left + mid)/2;

                if (left == 1 && right == 1) return false;//fixme costil'?

                i = ((mid - 1) * 12) + 1;
            }
        }
    }
}
