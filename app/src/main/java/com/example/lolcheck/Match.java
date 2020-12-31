package com.example.lolcheck;

import java.util.ArrayList;
import java.util.HashMap;

public class Match {
    public long gameId;
    public long gameDuration;
    public ArrayList<MatchParticipant> participants;

    public static HashMap<Long, Match> loadedMatches;

    public Match(){}

    public Match(long _gameId, long _gameDuration, ArrayList<MatchParticipant> _participants)
    {
        gameId = _gameId;
        gameDuration = _gameDuration;
        participants = _participants;
    }
}
