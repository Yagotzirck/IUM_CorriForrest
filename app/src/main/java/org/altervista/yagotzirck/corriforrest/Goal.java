package org.altervista.yagotzirck.corriforrest;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Goal {
    public enum GoalPeriod{
        DAY,
        MONTH,
        YEAR,
        CUSTOM_INTERVAL
    };

    public enum GoalType{
        CALORIES,
        DISTANCE,
        DURATION
    };

    private String user;
    private GoalPeriod goalPeriod;
    private GoalType goalType;

    private Date date1;
    private Date date2;     // used only for CUSTOM_INTERVAL

    

    
    // only one of these variables will be used, but Java doesn't support C-style union so whatever
    private int targetDuration;   // in seconds
    private int targetDistance;   // in meters
    private int targetKcals;

    public Goal(String user, GoalPeriod goalPeriod, GoalType goalType, Date date1, Date date2, int targetDuration, int targetDistance, int targetKcals){
        this.user = user;
        this.goalPeriod = goalPeriod;
        this.goalType = goalType;
        this.date1 = date1;
        this.date2 = date2;

        // each of these values will work as default values when switching from a goal type to another
        this.targetDuration =   targetDuration;
        this.targetDistance =   targetDistance;
        this.targetKcals =      targetKcals;
    }

    // getters

    public String getUser() { return user; }


    public int getGoalProgressPercent(){
        int progressValue = getProgressValue();
        double completionRatePercent = ( (double)(progressValue * 100) ) / getTargetValue();
        return (int) completionRatePercent;
    }



    public String getGoalProgressAsString(){
        String outString = "Completamento: " + getGoalProgressPercent() + "% (";
        String goalProgressString = "";

        int goalProgress = getProgressValue();

        switch(goalType){
            case CALORIES: goalProgressString = goalProgress + " kcal"; break;
            case DISTANCE: goalProgressString = getDistanceAsString(goalProgress); break;
            case DURATION: goalProgressString = getDurationAsString(goalProgress); break;
        }

        outString += goalProgressString + ")";

        return outString;
    }

    public String getFormulatedGoal() {
        String outString ="";

        switch (goalType) {
            case CALORIES:
                outString = "Bruciare " + getTargetValue() + " kcal";
                break;
            case DISTANCE:
                outString = "Percorrere " + getGoalDistanceAsString(getTargetValue());
                break;
            case DURATION:
                outString = "Correre per " + getDurationAsString(getTargetValue());
                break;
        }

        switch (goalPeriod) {
            case DAY:
                outString += " nel giorno " + getDayAsString(date1);
                break;
            case MONTH:
                outString += " nel mese " + getMonthAsString(date1);
                break;
            case YEAR:
                outString += " nell'anno " + getYearAsString(date1);
                break;
            case CUSTOM_INTERVAL:
                outString += " tra il " + getDayAsString(date1) + " ed il " + getDayAsString(date2);
                break;
        }

        return outString;
    }

    public boolean isGoalActive(){
        Date currDate = Calendar.getInstance().getTime();

        switch(goalPeriod){
            case DAY: return isSameDay(date1, currDate);
            case MONTH: return isSameMonth(date1, currDate);
            case YEAR: return isSameYear(date1, currDate);
            case CUSTOM_INTERVAL: return isInsideInterval(currDate, date1, date2);
        }
        return false;
    }

    private boolean isGoalPast(){
        Date currDate = Calendar.getInstance().getTime();

        switch(goalPeriod){
            case DAY: return !isSameDay(date1, currDate) && date1.compareTo(currDate) < 0;
            case MONTH: return !isSameMonth(date1, currDate) && date1.compareTo(currDate) < 0;
            case YEAR: return !isSameYear(date1, currDate) && date1.compareTo(currDate) < 0;
            case CUSTOM_INTERVAL: return !isSameDay(date2, currDate) && date2.compareTo(currDate) < 0;
        }
        return false;
    }

    public boolean isGoalCompleted(){
        return getGoalProgressPercent() >= 100;
    }

    public boolean isGoalOngoing(){
        return !isGoalPast() &&  getGoalProgressPercent() < 100;
    }

    public boolean isGoalFailed(){
        return isGoalPast() && getGoalProgressPercent() < 100;
    }




    // setters
    public void setUser(String username){ this.user = username; }
    public void setGoalPeriod(GoalPeriod goalPeriod) { this.goalPeriod = goalPeriod; }
    public void setGoalType(GoalType goalType) { this.goalType = goalType; }
    public void setDate1(Date date) { date1 = date; }
    public void setDate2(Date date) { date2 = date; }

    public void setTargetValue(int value){
        switch(goalType){
            case DURATION: targetDuration = value; break;
            case DISTANCE: targetDistance = value; break;
            case CALORIES: targetKcals = value; break;
        }
    }






    // helper functions
    private ArrayList<DataSession> getSessionsInGoalPeriod(){
        ArrayList<DataSession> dataSessionsFiltered = null;

        DataSessions dataSessions = DataSessions.getInstance();

        switch(goalPeriod){
            case DAY:
                dataSessionsFiltered = dataSessions.getSessionsByDay(user, date1);
                break;
            case MONTH:
                dataSessionsFiltered = dataSessions.getSessionsByMonth(user, date1);
                break;
            case YEAR:
                dataSessionsFiltered = dataSessions.getSessionsByYear(user, date1);
                break;
            case CUSTOM_INTERVAL:
                dataSessionsFiltered = dataSessions.getSessionsByInterval(user, date1, date2);
                break;
        }

        return dataSessionsFiltered;
    }

    private int getProgressValue(){
        ArrayList<DataSession> dataSessionsFiltered = getSessionsInGoalPeriod();

        Statistics stats = new Statistics(user, dataSessionsFiltered);
        int progressValue = 0;

        switch(goalType){
            case CALORIES: progressValue = stats.getBurnedCalories(); break;
            case DISTANCE: progressValue = stats.getDistance(); break;
            case DURATION: progressValue = stats.getDuration(); break;
        }

        return progressValue;
    }

    private int getTargetValue() {
        switch(goalType){
            case DURATION: return targetDuration;
            case DISTANCE: return targetDistance;
            case CALORIES: return targetKcals;
        }

        return 0;   // useless, but Android Studio gets annoying without this statement
    }

    @SuppressLint("DefaultLocale")
    private String getDistanceAsString(int distance){
        int km = distance / 1000;
        int meters = distance % 1000;

        return String.format("%d.%03d km", km, meters);
    }

    @SuppressLint("DefaultLocale")
    public String getGoalDistanceAsString(int distance){
        int km = distance / 1000;

        return String.format("%d km", km);
    }


    @SuppressLint("DefaultLocale")
    public String getDurationAsString(int duration){
        int hours = duration / 3600;
        int minutes = (duration - hours * 3600) / 60;

        return String.format("%dh : %02dmin", hours, minutes);
    }

    private String getDayAsString(Date date){
        DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormatter.format(date);
    }

    private String getMonthAsString(Date date){
        DateFormat dateFormatter = new SimpleDateFormat("MM/yyyy");
        return dateFormatter.format(date);
    }

    private String getYearAsString(Date date){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy");
        return dateFormatter.format(date);
    }

    private static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }

    private static boolean isSameMonth(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMM");
        return fmt.format(date1).equals(fmt.format(date2));
    }

    private static boolean isSameYear(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy");
        return fmt.format(date1).equals(fmt.format(date2));
    }

    private static boolean isInsideInterval(Date dateCheck, Date date1, Date date2){
        return  (isSameDay(dateCheck, date1)) ||
                (isSameDay(dateCheck, date2)) ||
                (dateCheck.compareTo(date1) >= 0  && dateCheck.compareTo(date2) <= 0);
    }

}
