package org.altervista.yagotzirck.corriforrest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class Goals {

    private static Goals instance = null;

    private static ArrayList<Goal> goalsList;

    private Goals(){}

    public static Goals getInstance() {
        if (instance == null) {
            instance = new Goals();
            goalsList = new ArrayList<>();

            // failed goals
            goalsList.add(new Goal("john", Goal.GoalPeriod.DAY, Goal.GoalType.CALORIES, buildDate(12, 2, 2021), buildDate(12, 2, 2021), 0, 0, 500));
            goalsList.add(new Goal("john", Goal.GoalPeriod.MONTH, Goal.GoalType.DISTANCE, buildDate(12, 2, 2021), buildDate(12, 2, 2021), 0, 300_000, 0));

            // ongoing goals
            goalsList.add(new Goal("john", Goal.GoalPeriod.YEAR, Goal.GoalType.DISTANCE, buildDate(4, 9, 2021), buildDate(12, 2, 2021), 0, 400_000, 0));
            goalsList.add(new Goal("john", Goal.GoalPeriod.DAY, Goal.GoalType.CALORIES, buildDate(4, 9, 2021), buildDate(12, 2, 2021), 0, 0, 400));

            goalsList.add(new Goal("john", Goal.GoalPeriod.CUSTOM_INTERVAL, Goal.GoalType.DISTANCE, buildDate(1, 9, 2021), buildDate(12, 9, 2021), 0, 40_000, 0));

            // almost-completed goals
            goalsList.add(new Goal("john", Goal.GoalPeriod.CUSTOM_INTERVAL, Goal.GoalType.DURATION, buildDate(1, 9, 2021), buildDate(7, 9, 2021), 4140, 0, 0));

            // completed goals
            goalsList.add(new Goal("john", Goal.GoalPeriod.DAY, Goal.GoalType.DISTANCE, buildDate(13, 2, 2021), buildDate(13, 2, 2021), 0, 3_000, 0));

        }

        return instance;
    }

    public void add(Goal goal){ goalsList.add(goal); }
    public void remove(Goal goal){ goalsList.remove(goal); }


    // Active goals = goals whose period includes the current date
    // (in other words, goals which can still be updated)
    public ArrayList<Goal> getActiveGoals(String user){
        ArrayList<Goal> activeGoals = new ArrayList<>();

        for(Goal goal : goalsList)
            if(goal.getUser().equals(user) && goal.isGoalActive())
                activeGoals.add(goal);

        Collections.reverse(activeGoals);
        return activeGoals;
    }

    public ArrayList<Goal> getCompletedGoals(String user){
        ArrayList<Goal> completedGoals = new ArrayList<>();

        for(Goal goal : goalsList)
            if(goal.getUser().equals(user) && goal.isGoalCompleted())
                completedGoals.add(goal);

        Collections.reverse(completedGoals);
        return completedGoals;
    }

    public ArrayList<Goal> getOngoingGoals(String user){
        ArrayList<Goal> ongoingGoals = new ArrayList<>();

        for(Goal goal : goalsList)
            if(goal.getUser().equals(user) && goal.isGoalOngoing())
                ongoingGoals.add(goal);

        Collections.reverse(ongoingGoals);
        return ongoingGoals;
    }

    public ArrayList<Goal> getFailedGoals(String user){
        ArrayList<Goal> failedGoals = new ArrayList<>();

        for(Goal goal : goalsList)
            if(goal.getUser().equals(user) && goal.isGoalFailed())
                failedGoals.add(goal);

        Collections.reverse(failedGoals);
        return failedGoals;
    }


    private static Date buildDate(int day, int month, int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month-1);
        c.set(Calendar.DAY_OF_MONTH, day);
        return c.getTime();
    }

}
