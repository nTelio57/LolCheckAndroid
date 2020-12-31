package com.example.lolcheck;

public class MatchParticipant {
    public int participantId;
    public int championId;
    public int teamId;
    public MatchParticipantStats matchParticipantStats;

    //Champion


    public MatchParticipant()
    {

    }

    public MatchParticipant(int _participantId, int _championId, int _teamId, MatchParticipantStats stats)
    {
        participantId = _participantId;
        championId = _championId;
        teamId = _teamId;
        matchParticipantStats = stats;
    }
}
