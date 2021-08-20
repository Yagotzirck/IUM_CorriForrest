package org.altervista.yagotzirck.corriforrest;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class DataSession {
    private Date date;
    private int duration;   // in seconds
    private int distance;   // in meters

    private int userWeight; // in Kg

    public DataSession(){
        date = new Date();
        duration = 0;
        distance = 0;

        String loggedUser = LoggedUser.getInstance().get();
        userWeight = Users.getInstance().get(loggedUser).getWeight();
    }

    // setters

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

    // getters

    public String getDate(){
        DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormatter.format(date);
    }

    public String getTime(){
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        return timeFormatter.format(date);
    }

    @SuppressLint("DefaultLocale")
    public String getDuration() {
        int hours = duration / 3600;
        int minutes = (duration - hours * 3600) / 60;
        int seconds = duration % 60;

        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }

    @SuppressLint("DefaultLocale")
    public String getDistance(){
        int km = distance / 1000;
        int meters = distance % 1000;

        return String.format("%d.%03d", km, meters);
    }

    @SuppressLint("DefaultLocale")
    public String getAvgRhythm(){
        int totalSecondsPerKm = (int)(duration / ((distance / 1000.0)));

        int minutes = totalSecondsPerKm / 60;
        int seconds = totalSecondsPerKm % 60;

        return String.format("%d'%02d'' / km", minutes, seconds);
    }

    @SuppressLint("DefaultLocale")
    public String getAvgSpeed(){
        double kmPerHour = ( (double) distance / duration) * (18.0 / 5);

        return String.format("%.1f km/h", kmPerHour);
    }

    public String getBurnedCalories(){
        final int MET = 10;
        // duration * (MET/3600) * Kg
        double value = (duration * MET * userWeight) / 3600.0;
        int intValue = (int) value;

        return intValue + " kcal";
    }
}
