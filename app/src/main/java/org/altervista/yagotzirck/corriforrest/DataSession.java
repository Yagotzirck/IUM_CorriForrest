package org.altervista.yagotzirck.corriforrest;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class DataSession implements Serializable, Comparable<DataSession> {
    private String user;
    private Date date;
    private int duration;   // in seconds
    private int distance;   // in meters

    private int userWeight; // in Kg

    public DataSession(String user){
        this.user = user;
        date = new Date();
        duration = 0;
        distance = 0;

        userWeight = Users.getInstance().get(user).getWeight();
    }

    // Constructor for debugging purposes
    public DataSession(String user, Date date, int duration, int distance){
        this(user);
        this.date = date;
        this.duration = duration;
        this.distance = distance;
    }

    // setters
    public void setUser(String username){ this.user = username ;}

    // min is inclusive, max is exclusive
    private int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    // should be externally called each second
    public void update(){
        ++duration;
        distance += getRandomNumber(1, 5);
    }

    public void setDuration(int duration){ this.duration += duration; }
    public void setDistance(int distance){ this.distance += distance; }

    // getters

    public String getUser(){ return user; }

    public Date getDate(){ return date; }

    public int getDuration(){ return duration; }
    public int getDistance(){ return distance; }

    public String getDateAsString(){
        DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormatter.format(date);
    }

    public String getTimeAsString(){
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        return timeFormatter.format(date);
    }

    @SuppressLint("DefaultLocale")
    public String getDurationAsString() {
        int hours = duration / 3600;
        int minutes = (duration - hours * 3600) / 60;
        int seconds = duration % 60;

        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }

    @SuppressLint("DefaultLocale")
    public String getDistanceAsString(){
        int km = distance / 1000;
        int meters = distance % 1000;

        return String.format("%d.%03d", km, meters);
    }

    @SuppressLint("DefaultLocale")
    public String getAvgRhythmAsString(){
        int totalSecondsPerKm = (int)(duration / ((distance / 1000.0)));

        int minutes = totalSecondsPerKm / 60;
        int seconds = totalSecondsPerKm % 60;

        return String.format("%d'%02d'' / km", minutes, seconds);
    }

    @SuppressLint("DefaultLocale")
    public String getAvgSpeedAsString(){
        double kmPerHour = ( (double) distance / duration) * (18.0 / 5);

        return String.format("%.1f km/h", kmPerHour);
    }

    public int getBurnedCalories(){
        final int MET = 10;
        // duration * (MET/3600) * Kg
        double value = (duration * MET * userWeight) / 3600.0;
        int intValue = (int) value;

        return intValue;
    }

    public String getBurnedCaloriesAsString(){
        return getBurnedCalories() + " kcal";
    }

    // methods for list item management
    @Override
    public int hashCode(){ return date.hashCode(); }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;
        DataSession dataSession = (DataSession) obj;
        // field comparison
        return date.equals(dataSession.date);
    }

    @Override
    public int compareTo(DataSession ds) {
        return this.date.compareTo(ds.date);
    }
}
