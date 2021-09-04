package org.altervista.yagotzirck.corriforrest;

import android.annotation.SuppressLint;
import android.provider.ContactsContract;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Statistics {
    private DataSession sessionStats;
    private int numSessions;
    private int burnedCalories;

    public Statistics(String user, ArrayList<DataSession> dataSessions){
        sessionStats = new DataSession(user);
        numSessions = dataSessions.size();

        int totalDuration = 0;
        int totalDistance = 0;
        burnedCalories = 0;

        for (DataSession ds: dataSessions){
            totalDuration += ds.getDuration();
            totalDistance += ds.getDistance();
            burnedCalories += ds.getBurnedCalories();
        }
        sessionStats.setDuration(totalDuration);
        sessionStats.setDistance(totalDistance);
    }


    // getters
    public String getNumSessionsAsString(){ return Integer.toString(numSessions); }
    public String getDurationAsString() { return sessionStats.getDurationAsString(); }
    public String getDistanceAsString(){ return sessionStats.getDistanceAsString(); }
    public String getAvgRhythmAsString(){ return sessionStats.getAvgRhythmAsString(); }
    public String getAvgSpeedAsString(){ return sessionStats.getAvgSpeedAsString(); }
    public String getBurnedCaloriesAsString(){ return burnedCalories + " kcal"; }

    public int getNumSessions(){ return numSessions; }
    public int getDuration() { return sessionStats.getDuration(); }
    public int getDistance(){ return sessionStats.getDistance(); }
    public int getBurnedCalories(){ return burnedCalories; }
}
