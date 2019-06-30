package com.tokovoynr.battleships.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokovoynr.battleships.R;


public class SettingsFragment extends Fragment
{
    public static final String TAG = "SETTINGS_FRAGMENT";
    private View view;
    private OnSettingsFragmentInteractionListener listener;


    public SettingsFragment()
    {

    }

    public static SettingsFragment newInstance()
    {
        SettingsFragment fragment = new SettingsFragment();



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
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        ((EditText)view.findViewById(R.id.editText_name)).setText(MainActivity.getProfile().getName());
        ((EditText)view.findViewById(R.id.editText_name)).addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                MainActivity.getProfile().setName(((EditText)view.findViewById(R.id.editText_name)).getText().toString());
            }
        });
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
        if (context instanceof OnSettingsFragmentInteractionListener)
        {
            listener = (OnSettingsFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnSettingsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        listener = null;
    }


    public interface OnSettingsFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);


    }

}
