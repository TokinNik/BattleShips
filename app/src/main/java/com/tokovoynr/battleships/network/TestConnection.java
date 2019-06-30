package com.tokovoynr.battleships.network;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokovoynr.battleships.UI.Lobby.LobbyContent;
import com.tokovoynr.battleships.UI.MainActivity;
import com.tokovoynr.battleships.UI.PreGame.Cell;
import com.tokovoynr.battleships.game.ShootResult;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class TestConnection
{
    public static final String TAG = "TEST_CONNECTION";
    private String targetURL = "http://46.236.128.153:8080/";
    private String currentQuery = "test";
    private String myGroupId = "";


    public String executeGet(final String query)
    {
        HttpURLConnection connection = null;

        try
        {
            //Create connection
            URL url = new URL(query);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.setUseCaches(false);
            connection.setDoInput(true);

            InputStream in;
            int status = connection.getResponseCode();

            if(status != 200)
                in = connection.getErrorStream();
            else
                in = connection.getInputStream();

            if (in != null)
            {
                BufferedReader rd = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null)
                {
                    response.append(line);
                    response.append('\n');
                }
                rd.close();

                Log.d(TAG, "executeGet: " + response.toString());
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return "";
    }

    class AsyncRequest extends AsyncTask<Void, Void, String>
    {
        @Override
        protected String doInBackground(Void... voids)
        {
            return executeGet(targetURL + currentQuery);
        }

        @Override
        protected void onPostExecute(String str)
        {
            super.onPostExecute(str);

        }
    }

    public boolean testConnection()
    {
        currentQuery = "test";
        Log.d(TAG, "testConnection: " + targetURL + currentQuery);
        AsyncRequest asyncRequest = new AsyncRequest();
        String s = "";

        try
        {
            s += asyncRequest.execute().get();
            Log.d(TAG, "testConnection: " + s);
            return !s.equals("");
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public String[] getLobby()
    {
        currentQuery = "getGroups";
        Log.d(TAG, "getLobby: " + targetURL + currentQuery);
        AsyncRequest asyncRequest = new AsyncRequest();
        String s = "";
        int count = 0;

        try
        {
            s += asyncRequest.execute().get();
            Log.d(TAG, "getLobby: " + s);
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return gson.fromJson(s, String[].class);

        } catch (ExecutionException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return new String[0];
    }

    public void createLobby()
    {
        currentQuery = "createGroup?user=" + MainActivity.getProfile().getName();
        Log.d(TAG, "createLobby: " + targetURL + currentQuery);
        AsyncRequest asyncRequest = new AsyncRequest();
        String s = "";

        try
        {
            s += asyncRequest.execute().get();
            Log.d(TAG, "createLobby: " + s);
            myGroupId = s;
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void closeLobby()
    {
        currentQuery = "removeGroup?group=" + myGroupId;
        Log.d(TAG, "closeLobby: " + targetURL + currentQuery);
        AsyncRequest asyncRequest = new AsyncRequest();
        String s = "";

        try
        {
            s += asyncRequest.execute().get();
            Log.d(TAG, "closeLobby: " + s);
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public boolean readyLobby()
    {
        currentQuery = "ready?group=" + myGroupId;
        Log.d(TAG, "readyLobby: " + targetURL + currentQuery);
        AsyncRequest asyncRequest = new AsyncRequest();
        String s = "";

        try
        {
            s += asyncRequest.execute().get();
            s = s.trim();
            Log.d(TAG, "readyLobby: " + s);
            return s.equals("true");
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean joinToLobby(String id)
    {
        currentQuery = "joinGroup/" +  MainActivity.getProfile().getName() +
                "?group=" + id;
        myGroupId = id;
        Log.d(TAG, "joinToLobby: " + targetURL + currentQuery);
        AsyncRequest asyncRequest = new AsyncRequest();
        String s = "";

        try
        {
            s += asyncRequest.execute().get();
            s = s.trim();
            Log.d(TAG, "joinToLobby: " + s);
            return s.equals("true");
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public void readyToPlay()
    {
        currentQuery = "readyToPlay/" +  MainActivity.getProfile().getName() +
                "?group=" + myGroupId;
        Log.d(TAG, "readyToPlay: " + targetURL + currentQuery);
        AsyncRequest asyncRequest = new AsyncRequest();
        String s = "";

        try
        {
            s += asyncRequest.execute().get();
            s = s.trim();
            Log.d(TAG, "readyToPlay: " + s);
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public boolean checkEnemyReady()
    {
        currentQuery = "sessionReady?group=" + myGroupId;
        Log.d(TAG, "checkEnemyReady: " + targetURL + currentQuery);
        AsyncRequest asyncRequest = new AsyncRequest();
        String s = "";

        try
        {
            s += asyncRequest.execute().get();
            s = s.trim();
            Log.d(TAG, "checkEnemyReady: " + s);
            return s.equals("true");
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean getQueue()
    {
        currentQuery = "getOrder/" + MainActivity.getProfile().getName() + "?group=" + myGroupId;
        Log.d(TAG, "getQueue: " + targetURL + currentQuery);
        AsyncRequest asyncRequest = new AsyncRequest();
        String s = "";

        try
        {
            s += asyncRequest.execute().get();
            s = s.trim();
            Log.d(TAG, "getQueue: " + s);
            return s.equals("true");
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public ShootResult fire(int step)
    {
        currentQuery = "fire/" + MainActivity.getProfile().getName() + "?coord=" + step + "&group=" + myGroupId;
        Log.d(TAG, "fire: " + targetURL + currentQuery);
        AsyncRequest asyncRequest = new AsyncRequest();
        String s = "";

        try
        {
           asyncRequest.execute().get();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        return new ShootResult(ShootResult.ResultType.EMPTY, Cell.CellType.EMPTY, 1, step);
    }

    public int getEnemyTurn()
    {
        currentQuery = "getCoord/" + MainActivity.getProfile().getName() + "?group=" + myGroupId;
        Log.d(TAG, "getEnemyTurn: " + targetURL + currentQuery);
        AsyncRequest asyncRequest = new AsyncRequest();
        String s = "";

        try
        {
            s += asyncRequest.execute().get();
            s = s.trim();
            Log.d(TAG, "getEnemyTurn: " + s);
            Log.d(TAG, "getEnemyTurn: i " + Integer.parseInt(s));
            return Integer.parseInt(s);
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

    public int receiveEnemyTurn()
    {
        currentQuery = "getFireResult/" + MainActivity.getProfile().getName() + "?group=" + myGroupId;
        Log.d(TAG, "receiveEnemyTurn: " + targetURL + currentQuery);
        AsyncRequest asyncRequest = new AsyncRequest();
        String s = "";

        try
        {
            s += asyncRequest.execute().get();
            s = s.trim();
            Log.d(TAG, "receiveEnemyTurn: " + s);
            return Integer.parseInt(s);
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void sendEnemyTurn(int result)
    {
        currentQuery = "setFireResult/" + MainActivity.getProfile().getName() + "?group=" + myGroupId + "&result=" + result;
        currentQuery = currentQuery.replaceAll(System.lineSeparator(), "");
        Log.d(TAG, "sendEnemyTurn: " + targetURL + currentQuery);
        AsyncRequest asyncRequest = new AsyncRequest();
        String s = "";

        try
        {
            s += asyncRequest.execute().get();
            s = s.trim();
            Log.d(TAG, "sendEnemyTurn: " + s);
            return;
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

}
