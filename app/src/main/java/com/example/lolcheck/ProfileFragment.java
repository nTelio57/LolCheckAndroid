package com.example.lolcheck;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setSummonerInformation();
        setMatchHistory();
    }

    void setSummonerInformation()
    {
        TextView usernameText = getView().findViewById(R.id.profile_username);
        TextView levelText = getView().findViewById(R.id.profile_level);
        TextView divisionText = getView().findViewById(R.id.profile_division);
        TextView winrateText = getView().findViewById(R.id.profile_winrate);
        ImageView iv = getView().findViewById(R.id.profile_icon);

        usernameText.setText(Summoner.name);
        levelText.setText(Summoner.summonerLevel+"");
        divisionText.setText(LeagueSolo.tier+" "+LeagueSolo.rank);
        float winrateTemp = (float)LeagueSolo.wins / (float)((float)LeagueSolo.wins+(float)LeagueSolo.losses)*100;
        winrateText.setText(Math.round(winrateTemp)+"% "+LeagueSolo.wins+"/"+LeagueSolo.losses);
        iv.setImageDrawable(new BitmapDrawable(getResources(), Summoner.iconBitmap));
    }

    void setMatchHistory()
    {
        for(int i = 0; i < MatchInfo.loadedMathesInfo.size(); i++)
        {
            AddMatchHistoryTab(MatchInfo.loadedMathesInfo.get(i));
        }
    }

    void AddMatchHistoryTab(MatchInfo mi)
    {
        LinearLayout linearLayout = getView().findViewById(R.id.profile_matchHistoryLayout);
        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.match_history_tab, linearLayout, false);

        TextView champNameText = myLayout.findViewById(R.id.matchHistory_champName);
        TextView kdaText = myLayout.findViewById(R.id.matchHistory_champKda);
        TextView csText = myLayout.findViewById(R.id.matchHistory_champMinnions);
        ImageView champIcon = myLayout.findViewById(R.id.matchHistory_champIcon);

        Match m = Match.loadedMatches.get(mi.gameId);
        MatchParticipant p = new MatchParticipant();//player of search
        for(int i = 0; i < m.participants.size(); i++)
        {
            if(m.participants.get(i).championId == mi.champion)
            {
                p = m.participants.get(i);
                break;
            }
        }

        int totalCs = p.matchParticipantStats.totalMinionsKilled + p.matchParticipantStats.neutralMinionsKilled;
        float csPerMin = (float)totalCs/((float)m.gameDuration/60);

        champNameText.setText(Champion.allChampions.get(mi.champion).name);
        kdaText.setText(p.matchParticipantStats.kills +"/"+ p.matchParticipantStats.deaths +"/"+ p.matchParticipantStats.assists);
        String csPerMinFormatted = String.format("%.2f", csPerMin);
        csText.setText(totalCs+"cs ("+csPerMinFormatted+"cs/min)");
        champIcon.setImageDrawable(Champion.allChampions.get(mi.champion).iconSquare);

        CardView cw = myLayout.findViewById(R.id.matchHistory_cardView);
        if(p.matchParticipantStats.win) {
            cw.setCardBackgroundColor(getResources().getColor(R.color.victory));
        }else
            cw.setCardBackgroundColor(getResources().getColor(R.color.defeat));

        linearLayout.addView(myLayout);
    }

}