package com.tokovoynr.battleships.UI;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import com.tokovoynr.battleships.R;
import com.tokovoynr.battleships.UI.Lobby.ListFragment;
import com.tokovoynr.battleships.UI.Lobby.LobbyContent;
import com.tokovoynr.battleships.UI.Lobby.LobbyFragment;
import com.tokovoynr.battleships.UI.PreGame.PreGameFragment;
import com.tokovoynr.battleships.game.GameLogic;

public class MainActivity extends AppCompatActivity implements SettingsFragment.OnSettingsFragmentInteractionListener,
        ListFragment.OnListFragmentInteractionListener,
        PreGameFragment.OnPreGameFragmentInteractionListener,
        GameFragment.OnGameFragmentInteractionListener,
        LobbyFragment.OnLobbyFragmentInteractionListener,
        ShopFragment.OnShopFragmentInteractionListener
{
    public static final String TAG = "MAIN_ACTIVITY";
    public static final float MIN_SCALE = 0.9f;
    public static final float MAX_SCALE = 3f;
    private static String currentFragment;
    private static GameLogic gameLogic;
    private FragmentManager fragmentManager;
    private LinearLayout mainLayout;
    private ScaleGestureDetector scaleDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        mainLayout = findViewById(R.id.mainLayout);
        currentFragment = TAG;
        scaleDetector = new ScaleGestureDetector(this, new ScaleListener());

        gameLogic = new GameLogic();

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
            case R.id.button_settings2://TODO удалить после тестов
                setFragment(GameFragment.TAG);
                break;
            case R.id.button_shop:
                setFragment(ShopFragment.TAG);
                break;
            default:
                break;

        }
    }

    private void setFragment(String tag)
    {
        Fragment fragment;
        if (!currentFragment.equals(TAG))
            fragmentManager.beginTransaction()
                    .remove(fragmentManager.findFragmentByTag(currentFragment))
                    .commit();
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
                gameLogic.clearAll();
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
            case ShopFragment.TAG:
                fragment = new ShopFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.container, fragment, ShopFragment.TAG)
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
                setFragment(MainActivity.TAG);
                Log.d(TAG, "back to fragment " + LobbyFragment.TAG);
            }
            else if (fragmentManager.findFragmentByTag(GameFragment.TAG) != null && fragmentManager.findFragmentByTag(GameFragment.TAG).isVisible())
            {
                setFragment(LobbyFragment.TAG);
                Log.d(TAG, "back to fragment " + LobbyFragment.TAG);
            }
            else if (fragmentManager.findFragmentByTag(ShopFragment.TAG) != null && fragmentManager.findFragmentByTag(ShopFragment.TAG).isVisible())
                {
                    setFragment(MainActivity.TAG);
                    Log.d(TAG, "back to fragment " + MainActivity.TAG);
                }
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public void onListFragmentInteraction(LobbyContent.LobbyItem item) {

    }

    @Override
    public void onListFragmentJoin(LobbyContent.LobbyItem item)
    {
        setFragment(PreGameFragment.TAG);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onShopClick(View view)
    {
        Log.d(TAG, "onShopClick: start");
        switch (view.getId())
        {
            case R.id.button_test_1:
                //TODO вставь сюда что надо
                break;
            case R.id.button_test_2:
                //
                break;
            case R.id.button_test_3:
                //
                break;
            default:
                break;
        }

    }
    @Override
    public void onPreGameClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button_ready:
                setFragment(GameFragment.TAG);
                break;
            case R.id.button_exit:
                setFragment(MainActivity.TAG);
                break;
            default:
                break;
        }
    }

    @Override
    public void onGameEnd()
    {
        setFragment(MainActivity.TAG);
        gameLogic.clearAll();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (currentFragment.equals(PreGameFragment.TAG))
        {
            scaleDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {
        float scale = 0.9f;
        float prewX, prewY, curX, curY;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            prewX = detector.getFocusX();
            prewY = detector.getFocusY();
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            curX = detector.getFocusX();
            curY = detector.getFocusY();

            if (scale > MAX_SCALE)
                scale = MAX_SCALE;

            if (scale < MIN_SCALE)
                scale = MIN_SCALE;

            TableLayout mainField = findViewById(R.id.main_field);
            mainField.setScaleX(scale);
            mainField.setScaleY(scale);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) mainField.getLayoutParams();
            lp.setMargins((int) (mlp.leftMargin + (curX - prewX)), (int) (mlp.topMargin + (curY - prewY)), 0, 0);
            mainField.setLayoutParams(lp);
            prewX = curX;
            prewY = curY;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector)
        {
            super.onScaleEnd(detector);
        }
    }

    public static GameLogic getGameLogic() {
        return gameLogic;
    }
}
