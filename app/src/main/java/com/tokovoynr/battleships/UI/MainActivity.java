package com.tokovoynr.battleships.UI;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.tokovoynr.battleships.R;
import com.tokovoynr.battleships.UI.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements SettingsFragment.OnSettingsFragmentInteractionListener,
        LobbyFragment.OnLobbyFragmentInteractionListener,
        PreGameFragment.OnPreGameFragmentInteractionListener,
        GameFragment.OnGameFragmentInteractionListener
{
    public static final String TAG = "MAIN_ACTIVITY";
    private static String currentFragment;
    private FragmentManager fragmentManager;
    private LinearLayout mainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        mainLayout = findViewById(R.id.mainLayout);
        currentFragment = TAG;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button_settings:
                setFragment(SettingsFragment.TAG);
                break;
            case R.id.button_online_game:
                setFragment(LobbyFragment.TAG);
                break;
            case R.id.button_one_app_game:
                setFragment(PreGameFragment.TAG);
                break;
            case R.id.button_settings2:
                setFragment(GameFragment.TAG);
                break;
            default:
                break;

        }
    }

    private void setFragment(String tag)
    {
        Fragment fragment;
        switch (tag)
        {
            case SettingsFragment.TAG:
                fragment = new SettingsFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.container, fragment, SettingsFragment.TAG)
                        .commit();
                mainLayout.setVisibility(View.INVISIBLE);
                currentFragment = tag;
                Log.d(TAG, "set fragment " + tag);
                break;
            case LobbyFragment.TAG:
                fragment = new LobbyFragment();
                if (!currentFragment.equals(TAG))
                fragmentManager.beginTransaction()
                        .remove(fragmentManager.findFragmentByTag(currentFragment))
                        .commit();
                fragmentManager.beginTransaction()
                        .add(R.id.container, fragment, LobbyFragment.TAG)
                        .commit();
                mainLayout.setVisibility(View.INVISIBLE);
                currentFragment = tag;
                Log.d(TAG, "set fragment " + tag);
                break;
            case PreGameFragment.TAG:
                fragment = new PreGameFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.container, fragment, PreGameFragment.TAG)
                        .commit();
                mainLayout.setVisibility(View.INVISIBLE);
                currentFragment = tag;
                Log.d(TAG, "set fragment " + tag);
                break;
            case GameFragment.TAG:
                fragment = new GameFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.container, fragment, GameFragment.TAG)
                        .commit();
                mainLayout.setVisibility(View.INVISIBLE);
                currentFragment = tag;
                Log.d(TAG, "set fragment " + tag);
                break;
            case MainActivity.TAG:
                fragmentManager.beginTransaction()
                        .remove(fragmentManager.findFragmentByTag(currentFragment))
                        .commit();
                mainLayout.setVisibility(View.VISIBLE);
                currentFragment = tag;
                Log.d(TAG, "set fragment " + tag);
                break;
            default:
                break;

        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public void onBackPressed()
    {
        if(mainLayout.getVisibility() == View.INVISIBLE)
        {
            if (fragmentManager.findFragmentByTag(SettingsFragment.TAG) != null && fragmentManager.findFragmentByTag(SettingsFragment.TAG).isVisible())
            {
                setFragment(MainActivity.TAG);
                Log.d(TAG, "back to fragment " + TAG);
            }
            else if (fragmentManager.findFragmentByTag(LobbyFragment.TAG) != null && fragmentManager.findFragmentByTag(LobbyFragment.TAG).isVisible())
            {
                setFragment(MainActivity.TAG);
                Log.d(TAG, "back to fragment " + TAG);
            }
            else if (fragmentManager.findFragmentByTag(PreGameFragment.TAG) != null && fragmentManager.findFragmentByTag(PreGameFragment.TAG).isVisible())
            {
                setFragment(LobbyFragment.TAG);
                Log.d(TAG, "back to fragment " + LobbyFragment.TAG);
            }
            else if (fragmentManager.findFragmentByTag(GameFragment.TAG) != null && fragmentManager.findFragmentByTag(GameFragment.TAG).isVisible())
            {
                setFragment(LobbyFragment.TAG);
                Log.d(TAG, "back to fragment " + LobbyFragment.TAG);
            }
        }
        else
        {
            super.onBackPressed();
        }
    }
}
