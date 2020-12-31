package com.example.lolcheck;

public class LeagueSolo {

    public static String leagueId;
    public static String summonerId;
    public static String summonerName;
    public static String queueType;
    public static String tier;
    public static String rank;
    public static int leaguePoints;
    public static int wins;
    public static int losses;

    public static void SetLeague(String _leagueId, String _summonerId, String _summonerName, String _queueType, String _tier, String _rank, int _leaguePoints, int _wins, int _losses)
    {
        leagueId = _leagueId;
        summonerId = _summonerId;
        summonerName = _summonerName;
        queueType = _queueType;
        tier = _tier;
        rank = _rank;
        leaguePoints = _leaguePoints;
        wins = _wins;
        losses = _losses;
    }
}
