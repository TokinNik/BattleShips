package com.tokovoynr.battleships.UI.Lobby;

import com.tokovoynr.battleships.UI.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample lobbyName for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class LobbyContent
{

    /**
     * An array of sample (dummy) items.
     */
    public static final List<LobbyItem> ITEMS = new ArrayList<LobbyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, LobbyItem> ITEM_MAP = new HashMap<String, LobbyItem>();

    private static final int COUNT = 25;

    static
    {
        // Add some sample items.

        resetItems();

        /*
        addItem(new LobbyItem("123", "test1", "D test1"));
        addItem(new LobbyItem("234", "test2", "D test2"));
        addItem(new LobbyItem("345", "test3", "D test3"));
        addItem(new LobbyItem("645", "test4", "D test4"));
        addItem(new LobbyItem("367", "test5", "D test5"));
        addItem(new LobbyItem("368", "test6", "D test6"));
        addItem(new LobbyItem("999999", "<=$uPeR_N@GiB@ToR_228_UlTr@=>", "D test6,5"));
        addItem(new LobbyItem("2345", "test7", "D test7"));
        addItem(new LobbyItem("3345", "test8", "D test8"));
        addItem(new LobbyItem("34345", "test9", "D test9"));
        addItem(new LobbyItem("67345", "It's_very_very_very_very_very_very_very_very_very_very_very_very_long_nickname", "D test10"));
        addItem(new LobbyItem("63345", "test11", "D test11"));
        addItem(new LobbyItem("67385", "test12", "D test12"));
        addItem(new LobbyItem("92565", "test13", "D test13"));
        */
    }

    private static void addItem(LobbyItem item)
    {
        ITEMS.add(item);
        ITEM_MAP.put(item.reit, item);
    }

    public static void resetItems()
    {
        if (ITEMS.size() > 0)
            ITEMS.clear();
        if(MainActivity.getConnection().testConnection())
        {
            String[] lobbyItems = MainActivity.getConnection().getLobby();
            for(int i = 0; i < lobbyItems.length; i++)
            {
                LobbyItem item = new LobbyItem("0", lobbyItems[i].trim(), "test");
                addItem(item);
            }
        }
    }

    private static LobbyItem createDummyItem(int position)
    {
        return new LobbyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++)
        {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of lobbyName.
     */
    public static class LobbyItem
    {
        public final String reit;
        public final String lobbyName;
        public final String details;

        public LobbyItem(String reit, String content, String details)
        {
            this.reit = reit;
            this.lobbyName = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return lobbyName;
        }
    }
}
