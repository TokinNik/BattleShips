package com.tokovoynr.battleships;

import android.text.Editable;

public class Profile
{
    private int id;
    private String name;
    private int[] availableShipSets;
    private int currentSet;
    private int[] availableAvatars;
    private int currentAvatar;
    private int rating;

    public Profile(int id, String name, int[] availableShipSets,
                   int currentSet, int[] availableAvatars, int currentAvatar, int rating)
    {
        this.id = id;
        this.name = name;
        this.availableShipSets = availableShipSets;
        this.currentSet = currentSet;
        this.availableAvatars = availableAvatars;
        this.currentAvatar = currentAvatar;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int[] getAvailableShipSets() {
        return availableShipSets;
    }

    public int getCurrentSet() {
        return currentSet;
    }

    public int[] getAvailableAvatars() {
        return availableAvatars;
    }

    public int getCurrentAvatar() {
        return currentAvatar;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setName(String name) {
        this.name = name;
    }
}
