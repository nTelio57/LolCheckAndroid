package com.example.lolcheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Champion.allChampions == null || Champion.allChampions.size() == 0)
            LoadChampionData();
        if(getActiveUsername().length() != 0)
        {
            Check(getActiveUsername());
        }else
        {
            setButton();
        }
    }

    void setButton()
    {
        Button but = findViewById(R.id.button1);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameText = findViewById(R.id.username);
                String username = usernameText.getText().toString();
                Check(username);
            }
        });
    }

    void LoadChampionData()
    {
        HashMap<Integer, Champion> newChampions = new HashMap<Integer, Champion>();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://ddragon.leagueoflegends.com/cdn/10.25.1/data/en_US/champion.json";

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject champsData = response.getJSONObject("data");
                    JSONArray champs = champsData.names();
                    for(int i = 0; i < champs.length(); i++)
                    {
                        JSONObject champ = champsData.getJSONObject(champs.get(i).toString());

                        String id = champ.getString("id");
                        String name = champ.getString("name");
                        int key = champ.getInt("key");
                        Champion newChamp = new Champion(id, key, name);
                        newChampions.put(key, newChamp);
                    }
                    Champion.allChampions = newChampions;
                    LoadChampionIcons();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-Riot-Token", RiotAPI.ApiKey);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    void LoadChampionIcons()
    {
        Champion[] allChamps = Champion.getAllChampions();

        InputStream ims;
        Drawable d;

        try{
            for(int i = 0; i < allChamps.length; i++)
            {
                ims = getAssets().open("championSquare/"+allChamps[i].id+".png");
                d = Drawable.createFromStream(ims, null);
                Champion.allChampions.get(allChamps[i].key).iconSquare = d;
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }


        /*Champion[] allChamps = Champion.getAllChampions();

        for(int i = 0; i < allChamps.length; i++)
        {
            final int index = i;
            String url = "https://ddragon.leagueoflegends.com/cdn/10.25.1/img/champion/"+allChamps[i].id+".png";
            RequestQueue queue = Volley.newRequestQueue(this);
            ImageRequest ir = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    Champion.allChampions.get(allChamps[index].key).iconSquare = response;
                }
            }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    //params.put("X-Riot-Token", "RGAPI-19f4175d-18cb-4abb-8ac0-331aeb5e913d");
                    return params;
                }
            };
            queue.add(ir);
        }*/

    }

    void Check(String username)
    {


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/"+username;

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String accountId = response.getString("accountId");
                    String name = response.getString("name");
                    int profileIconId = response.getInt("profileIconId");
                    long revisionDate = response.getLong("revisionDate");
                    String id = response.getString("id");
                    String puuid = response.getString("puuid");
                    long summonerLevel = response.getLong("summonerLevel");

                    Summoner.SetSummoner(accountId, profileIconId, revisionDate, name, id, puuid, summonerLevel);
                    CheckLeagues(id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-Riot-Token", RiotAPI.ApiKey);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    void CheckLeagues(String summonerId)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://eun1.api.riotgames.com/lol/league/v4/entries/by-summoner/"+summonerId;

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    String summonerName = "";
                    for(int i = 0; i < response.length(); i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String leagueId = jsonObject.getString("leagueId");
                        String queueType = jsonObject.getString("queueType");
                        String tier = jsonObject.getString("tier");
                        String rank = jsonObject.getString("rank");
                        String summonerId = jsonObject.getString("summonerId");
                        summonerName = jsonObject.getString("summonerName");
                        int leaguePoints = jsonObject.getInt("leaguePoints");
                        int wins = jsonObject.getInt("wins");
                        int losses = jsonObject.getInt("losses");

                        if(queueType.equals(QueueLol.Solo_5x5))
                        {
                            LeagueSolo.SetLeague(leagueId, summonerId, summonerName, queueType, tier, rank, leaguePoints, wins, losses);
                        }else if(queueType.equals(QueueLol.Flex_5x5))
                        {
                            LeagueFlex.SetLeague(leagueId, summonerId, summonerName, queueType, tier, rank, leaguePoints, wins, losses);
                        }
                    }
                    LoadIcon(Summoner.profileIconId);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-Riot-Token", RiotAPI.ApiKey);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    void LoadIcon(int profileIconId)
    {
        String url = "https://ddragon.leagueoflegends.com/cdn/10.25.1/img/profileicon/"+profileIconId+".png";
        RequestQueue queue = Volley.newRequestQueue(this);
        ImageRequest ir = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Summoner.iconBitmap = response;
                LoadMatchHistory();
                //Load(Summoner.name);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                //params.put("X-Riot-Token", "RGAPI-19f4175d-18cb-4abb-8ac0-331aeb5e913d");
                return params;
            }
        };
        queue.add(ir);
    }

    public void FinalLoad(String username)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Active_Username", username);
        editor.commit();

        Intent i = new Intent(getBaseContext(), ProfileActivity.class);
        startActivity(i);
    }

    void LoadMatchHistory()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://eun1.api.riotgames.com/lol/match/v4/matchlists/by-account/"+Summoner.accountId;

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("matches");
                    ArrayList<MatchInfo> matchInfoArray = new ArrayList<MatchInfo>();

                    for(int i = 0; i < Math.min(MatchInfo.maxMatchHistorySize, jsonArray.length()); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        long gameId = jsonObject.getLong("gameId");
                        String role = jsonObject.getString("role");
                        int season = jsonObject.getInt("season");
                        String platformId = jsonObject.getString("platformId");
                        int champion = jsonObject.getInt("champion");
                        int queue = jsonObject.getInt("queue");
                        String lane = jsonObject.getString("lane");
                        long timestamp = jsonObject.getLong("timestamp");

                        MatchInfo matchInfo = new MatchInfo(gameId, role, season, platformId, champion, queue, lane, timestamp);
                        matchInfoArray.add(matchInfo);
                    }

                    MatchInfo.loadedMathesInfo = matchInfoArray;
                    LoadMatch();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-Riot-Token", RiotAPI.ApiKey);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    void LoadMatch()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<Long, Match> newHashMap = new HashMap<Long, Match>();

        for(int i = 0; i < MatchInfo.loadedMathesInfo.size(); i++)
        {
            final int index = i;
            String url = "https://eun1.api.riotgames.com/lol/match/v4/matches/"+MatchInfo.loadedMathesInfo.get(i).gameId;

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        ArrayList<MatchParticipant> participantArray = new ArrayList<MatchParticipant>();

                        long gameId = response.getLong("gameId");//MATCH INFO HERE
                        long gameDuration = response.getLong("gameDuration");

                        JSONArray participants = response.getJSONArray("participants");
                        for(int j = 0; j < participants.length(); j++)
                        {
                            JSONObject participant = participants.getJSONObject(j);//PARTICIPANT INFO HERE
                            int participantId = participant.getInt("participantId");
                            int championId = participant.getInt("championId");
                            int teamId = participant.getInt("teamId");

                            JSONObject stats = participant.getJSONObject("stats");//PARTICIPANT STATS HERE
                            int kills = stats.getInt("kills");
                            int deaths = stats.getInt("deaths");
                            int assists = stats.getInt("assists");
                            int totalMinionsKilled = stats.getInt("totalMinionsKilled");
                            int neutralMinionsKilled = stats.getInt("neutralMinionsKilled");
                            boolean win = stats.getBoolean("win");
                            MatchParticipantStats newStats = new MatchParticipantStats(kills, deaths, assists, totalMinionsKilled, neutralMinionsKilled, win);

                            MatchParticipant newParticipant = new MatchParticipant(participantId, championId, teamId, newStats);
                            participantArray.add(newParticipant);
                        }
                        Match newMatch = new Match(gameId, gameDuration, participantArray);
                        newHashMap.put(gameId, newMatch);

                        if(index == MatchInfo.loadedMathesInfo.size()-1) {
                            Match.loadedMatches = newHashMap;
                            FinalLoad(Summoner.name);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("X-Riot-Token", RiotAPI.ApiKey);
                    return params;
                }
            };
            queue.add(stringRequest);
        }

    }



    public String getActiveUsername()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        return sp.getString("Active_Username", "");
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


}