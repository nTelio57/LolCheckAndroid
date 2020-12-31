package com.example.lolcheck;

public class MatchParticipantStats {
    public int kills;
    public int deaths;
    public int assists;
    public int totalMinionsKilled;
    public int neutralMinionsKilled;
    public boolean win;

    public MatchParticipantStats(){}

    public MatchParticipantStats(int _kills, int _deaths, int _assists, int _totalMinionsKilled, int _neutralMinionsKilled, boolean _win)
    {
        kills = _kills;
        deaths = _deaths;
        assists = _assists;
        totalMinionsKilled = _totalMinionsKilled;
        neutralMinionsKilled = _neutralMinionsKilled;
        win = _win;
    }
}
