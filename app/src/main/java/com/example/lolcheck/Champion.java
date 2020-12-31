package com.example.lolcheck;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Champion {
    public String id;
    public int key;
    public String name;
    public Drawable iconSquare;

    public static HashMap<Integer, Champion> allChampions;

    public Champion(){}

    public Champion(String _id, int _key, String _name)
    {
        id = _id;
        key = _key;
        name = _name;
    }

    public Champion(String _id, int _key, String _name, Drawable _iconSquare)
    {
        id = _id;
        key = _key;
        name = _name;
        iconSquare = _iconSquare;
    }

    public static Champion[] getAllChampions()
    {
        Champion newArray[] = new Champion[allChampions.size()];
        Collection<Champion> allChamps = allChampions.values();
        Iterator<Champion> allChampsIter = allChamps.iterator();
        int index = 0;
        while(allChampsIter.hasNext())
        {
            Champion c = allChampsIter.next();
            newArray[index++] = c;
        }
        return newArray;
    }
}
