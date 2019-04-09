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

import com.tokovoynr.battleships.R;


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
    private int borderX, borderY;
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
                            Log.d(TAG, "DOWN 1");
                            break;
                        case MotionEvent.ACTION_MOVE:
                            int x = (int) event.getX() - offsetX/2;
                            int y = (int) event.getY() - offsetY;
                            Log.d(TAG, "MOVE 1 " + x + "  " + y);

                            if (firstFlag)
                            {
                                firstFlag = false;
                                ImageView mainField = view.findViewById(R.id.main_field);
                                borderX = mainField.getWidth();
                                borderY = mainField.getHeight();
                                Log.d(TAG, "! " + borderX + "  " + borderY);
                            }

                            if (x < borderX && x > 0 && y < borderY && y > 0)
                            {
                                targetFlag = true;
                                selected_item.setAlpha(1f);
                            }
                            else if (targetFlag)
                            {
                                targetFlag = false;
                                selected_item.setAlpha(0.5f);
                            }
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                            lp.setMargins(x, y, 0, 0);
                            selected_item.setLayoutParams(lp);
                            break;
                        case MotionEvent.ACTION_UP:
                            Log.d(TAG, "UP 1");
                            touchFlag = false;
                            if (targetFlag)
                            {
                                selected_item.setAlpha(1f);
                                //selected_item.setImageDrawable(getResources().getDrawable(R.drawable.green_box));
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
                }
                return true;
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
                Log.d(TAG, "DOWN 2");
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
                Log.d(TAG, "MOVE 2");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "UP 2");
                break;
            default:
                break;
        }
        return false;
    }

}
