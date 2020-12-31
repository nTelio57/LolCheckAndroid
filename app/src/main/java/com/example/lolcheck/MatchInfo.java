package com.example.lolcheck;

import java.util.ArrayList;

public class MatchInfo {
    public long gameId;
    public String role;
    public int season;
    public String platformId;
    public int champion;
    public int queue;
    public String lane;
    public long timestamp;

    public static int maxMatchHistorySize = 10;
    public static ArrayList<MatchInfo> loadedMathesInfo;

    public MatchInfo()
    {

    }

    public MatchInfo(long _gameId, String _role, int _season, String _platformId, int _champion, int _queue, String _lane, long _timestamp)
    {
        gameId = _gameId;
        role = _role;
        season = _season;
        platformId = _platformId;
        champion = _champion;
        queue = _queue;
        lane = _lane;
        timestamp = _timestamp;
    }
}
