package com.tokovoynr.battleships.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokovoynr.battleships.R;
import com.tokovoynr.battleships.UI.dummy.DummyContent;
import com.tokovoynr.battleships.UI.dummy.DummyContent.DummyItem;

public class LobbyFragment extends Fragment
{

    public static final String TAG = "LOBBY_FRAGMENT";
    private View view;
    private int columnCount = 2;
    private OnLobbyFragmentInteractionListener listener;

    public LobbyFragment()
    {

    }

    public static LobbyFragment newInstance(int columnCount)
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
        View view = inflater.inflate(R.layout.fragment_lobby_list, container, false);

        if (view instanceof RecyclerView)
        {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (columnCount <= 1)
            {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else
            {
                recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
            }
            recyclerView.setAdapter(new MyLobbyRecyclerViewAdapter(DummyContent.ITEMS, listener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnLobbyFragmentInteractionListener)
        {
            listener = (OnLobbyFragmentInteractionListener) context;
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

        void onListFragmentInteraction(DummyItem item);
    }
}
