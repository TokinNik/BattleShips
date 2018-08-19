package com.tokovoynr.battleships.UI.Lobby;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tokovoynr.battleships.R;
import com.tokovoynr.battleships.UI.Lobby.LobbyContent.LobbyItem;

import java.util.List;

import me.grantland.widget.AutofitTextView;


/**
 * {@link RecyclerView.Adapter} that can display a {@link LobbyItem} and makes a call to the
 * specified {@link ListFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyLobbyRecyclerViewAdapter extends RecyclerView.Adapter<MyLobbyRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "LOBBY_RECYCLER_ADAPTER";
    private final List<LobbyItem> mValues;
    private final ListFragment.OnListFragmentInteractionListener mListener;

    public MyLobbyRecyclerViewAdapter(List<LobbyItem> items, ListFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.lobbyItem = mValues.get(position);
        holder.textView.setText(mValues.get(position).id);
        holder.autofitTextView.setText(mValues.get(position).content);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener)
                {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.lobbyItem);
                }
            }
        });
        holder.buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mListener.onListFragmentJoin(holder.lobbyItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        final View view;
        final TextView textView;
        final AutofitTextView autofitTextView;
        final Button buttonJoin;
        public LobbyItem lobbyItem;

        ViewHolder(View view)
        {
            super(view);
            this.view = view;
            textView =  view.findViewById(R.id.item_number);
            autofitTextView =  view.findViewById(R.id.content);
            buttonJoin = view.findViewById(R.id.button_join);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + autofitTextView.getText() + "'";
        }
    }
}
