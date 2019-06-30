package com.tokovoynr.battleships.UI.Lobby;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.tokovoynr.battleships.R;
import com.tokovoynr.battleships.UI.MainActivity;

import me.grantland.widget.AutofitTextView;


public class LobbyFragment extends Fragment implements View.OnClickListener
{
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

        ((AutofitTextView)view.findViewById(R.id.AutofitTextView_nik)).setText(MainActivity.getProfile().getName());
        ((TextView)view.findViewById(R.id.textView_reiting)).setText(String.valueOf(MainActivity.getProfile().getRating()));

        view.findViewById(R.id.button_new_lobby).setOnClickListener(this);
        view.findViewById(R.id.button_room_settings).setOnClickListener(this);



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
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

       // ((ListFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_lobby_list)).resetListItems();
    }

    @Override
    public void onClick(View view)
    {
        listener.onLobbyClick(view);
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

        void onLobbyClick(View view);

    }
}
