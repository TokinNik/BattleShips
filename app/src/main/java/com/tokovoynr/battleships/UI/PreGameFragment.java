package com.tokovoynr.battleships.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokovoynr.battleships.R;


public class PreGameFragment extends Fragment {
    public static final String TAG = "PRE_GAME_FRAGMENT";
    private View view;
    private OnPreGameFragmentInteractionListener listener;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_pre_game, container, false);


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
}
