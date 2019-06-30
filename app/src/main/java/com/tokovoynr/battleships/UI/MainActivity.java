package com.tokovoynr.battleships.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.tokovoynr.battleships.Profile;
import com.tokovoynr.battleships.R;
import com.tokovoynr.battleships.UI.Lobby.ListFragment;
import com.tokovoynr.battleships.UI.Lobby.LobbyContent;
import com.tokovoynr.battleships.UI.Lobby.LobbyFragment;
import com.tokovoynr.battleships.UI.PreGame.PreGameFragment;
import com.tokovoynr.battleships.game.GameLogic;
import com.tokovoynr.battleships.network.TestConnection;

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
    private static TestConnection connection = new TestConnection();
    private static Profile profile = new Profile(1, "Name", new int[]{1}, 1, new int[]{1}, 1, 1);


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

        gameLogic = new GameLogic(this);
        gameLogic.setConnector(connection);

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
                gameLogic.setGameMode(GameLogic.GameMode.PvE);
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
                if (!connection.testConnection())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setTitle(R.string.connection_lost);
                    builder.setMessage(R.string.connection_lost_message);
                    builder.setCancelable(false);
                    AlertDialog wimDialog = builder.create();
                    wimDialog.show();
                }
                else
                {
                    fragment = new LobbyFragment();
                    fragmentManager.beginTransaction()
                            .add(R.id.container, fragment, LobbyFragment.TAG)
                            .commit();
                    mainLayout.setVisibility(View.INVISIBLE);
                    currentFragment = tag;
                }
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setFragment(MainActivity.TAG);
                        gameLogic.clearAll();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setTitle(R.string.exit_error);
                if(gameLogic.getGameMode() == GameLogic.GameMode.PvE)
                    builder.setMessage(R.string.exit_error_pve);
                else
                    builder.setMessage(R.string.exit_error_pvp);
                builder.setCancelable(false);
                AlertDialog wimDialog = builder.create();
                wimDialog.show();

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
    public void onListFragmentInteraction(LobbyContent.LobbyItem item)
    {

    }

    @Override
    public void onListFragmentJoin(LobbyContent.LobbyItem item)
    {
        if(connection.joinToLobby(item.lobbyName))
        {
            setFragment(PreGameFragment.TAG);
            gameLogic.setGameMode(GameLogic.GameMode.PvP);
        }
        else
        {
            Toast.makeText(this,R.string.lobby_owerflow, Toast.LENGTH_SHORT).show();
            //((ListFragment)fragmentManager.findFragmentById(R.id.fragment_lobby_list)).resetListItems();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onLobbyClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button_room_settings:
                //gameLogic.getConnector().executeGet("123 test 321");
                break;
            case R.id.button_new_lobby:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        connection.createLobby();
                        gameLogic.setGameMode(GameLogic.GameMode.PvP);
                        setFragment(PreGameFragment.TAG);
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setTitle(R.string.create_lobby_question);
                builder.setMessage(R.string.create_lobby_message);
                builder.setCancelable(false);
                AlertDialog wimDialog = builder.create();
                wimDialog.show();
                break;
            default:
                break;

        }
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
    public void onWaitCancel()
    {
        connection.closeLobby();
        setFragment(LobbyFragment.TAG);
    }

    @Override
    public void onPreGameClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button_ready:
                if (gameLogic.getGameMode() == GameLogic.GameMode.PvP)
                {
                    connection.readyToPlay();

                    while(true)
                    {
                        try
                        {
                            Thread.sleep(2000);
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        if(connection.checkEnemyReady())
                        {
                            gameLogic.setPlayerTurn(connection.getQueue());
                            Toast.makeText(this, (gameLogic.isPlayerTurn()? "true" : "false"), Toast.LENGTH_SHORT).show();
                            setFragment(GameFragment.TAG);
                            return;
                        }
                    }
                }
                else
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
    public void onGameEnd(boolean playerWin)
    {
        if (playerWin)
        {
            profile.setRating(profile.getRating() + 1);
        }
        else if (profile.getRating() != 1)
        {
            profile.setRating(profile.getRating() - 1);
        }
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

    public static TestConnection getConnection() {
        return connection;
    }

    public static Profile getProfile() {
        return profile;
    }
}
