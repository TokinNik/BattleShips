package com.tokovoynr.battleships.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokovoynr.battleships.R;


public class GameFragment extends Fragment {
    public static final String TAG = "GAME_FRAGMENT";
    private View view;
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
    public void onDetach()
    {
        super.onDetach();
        listener = null;
    }

    public interface OnGameFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);

    }
}
