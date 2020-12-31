package com.example.lolcheck;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Summoner {

    static public String accountId;
    static public int profileIconId;
    static public long revisionDate;
    static public String name;
    static public String id;
    static public String puuid;
    static public long summonerLevel;
    static public Bitmap iconBitmap;

    public Summoner()
    {

    }

    public static void Clear()
    {
        accountId = "";
        profileIconId = -1;
        revisionDate = -1;
        name = "";
        id = "";
        puuid = "";
        summonerLevel = -1;
    }

    public static void SetSummoner(String _accountId, int _profileIconId, long _revisionDate, String _name, String _id, String _puuid, long _summonerLevel)
    {
        accountId = _accountId;
        profileIconId = _profileIconId;
        revisionDate = _revisionDate;
        name = _name;
        id = _id;
        puuid = _puuid;
        summonerLevel = _summonerLevel;
    }
}
