package com.tokovoynr.battleships.UI.Lobby;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokovoynr.battleships.R;


public class LobbyFragment extends Fragment {
    public static final String TAG = "LOBBY_FRAGMENT";
    private View view;
    private OnLobbyFragmentInteractionListener listener;

    public LobbyFragment()
    {

    }

    public static LobbyFragment newInstance()
    {
        LobbyFragment fragment = new LobbyFragment();


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
        view = inflater.inflate(R.layout.fragment_lobby, container, false);


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
        if (context instanceof LobbyFragment.OnLobbyFragmentInteractionListener)
        {
            listener = (LobbyFragment.OnLobbyFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnLobbyFragmentInteractionListener");
        }


    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        listener = null;
    }

    public interface OnLobbyFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);

    }
}
