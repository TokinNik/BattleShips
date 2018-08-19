package com.tokovoynr.battleships.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tokovoynr.battleships.R;


public class ShopFragment extends Fragment implements View.OnClickListener
{
    public static final String TAG = "SHOP_FRAGMENT";
    private View rootView;
    private OnShopFragmentInteractionListener listener;

    public ShopFragment()
    {

    }

    public static ShopFragment newInstance()
    {
        ShopFragment fragment = new ShopFragment();



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
        rootView = inflater.inflate(R.layout.fragment_shop, container, false);
        Button button1 = rootView.findViewById(R.id.button_test_1);
        Button button2 = rootView.findViewById(R.id.button_test_2);
        Button button3 = rootView.findViewById(R.id.button_test_3);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnShopFragmentInteractionListener)
        {
            listener = (OnShopFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnShopFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(View view)
    {
        listener.onShopClick(view);
    }


    public interface OnShopFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);

        void onShopClick(View view);
    }
}
