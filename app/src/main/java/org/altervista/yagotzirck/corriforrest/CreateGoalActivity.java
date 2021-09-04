package org.altervista.yagotzirck.corriforrest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.altervista.yagotzirck.corriforrest.Goal.GoalPeriod;
import org.altervista.yagotzirck.corriforrest.Goal.GoalType;

public class CreateGoalActivity extends AppCompatActivity implements DatePickersCallbacks, TargetValuePickersCallbacks {
    @Override
    public void dayCallback(Date date) {
        DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        switch(clickedDayButton){
            case INTERVAL_FROM:
                if(date.compareTo(dateTo) <= 0 || isSameDay(date, dateTo)) {
                    dateFrom = date;
                    fromButton.setText(dateFormatter.format(date));
                }
                else{
                    makeToast("La data \"Dal\" dev'essere inferiore o uguale alla data \"al\"!").show();

                }
                break;

            case INTERVAL_TO:
                if (date.compareTo(dateFrom) >= 0) {
                    dateTo = date;
                    toButton.setText(dateFormatter.format(date));
                }
                else{
                    makeToast("La data \"al\" dev'essere superiore o uguale alla data \"Dal\"!").show();
                }
                break;

            case DAY:
                day = date;
                dayButton.setText(dateFormatter.format(date));

                break;
        }

        updateGoal();
    }


    @Override
    public void monthYearCallback(Date monthYearDate) {
        DateFormat dateFormatter = new SimpleDateFormat("MM/yyyy");
        monthYear = monthYearDate;
        monthButton.setText(dateFormatter.format(monthYearDate));

        updateGoal();
    }

    @Override
    public void yearCallback(Date yearDate) {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy");
        year = yearDate;
        yearButton.setText(dateFormatter.format(yearDate));

        updateGoal();
    }

    @Override
    public void targetValueCallback(int value) {
        goal.setTargetValue(value);

        switch (goalType) {
            case CALORIES: caloriesButton.setText(value + " kcal"); break;
            case DISTANCE: distanceButton.setText(goal.getGoalDistanceAsString(value)); break;
            case DURATION: durationButton.setText(goal.getDurationAsString(value)); break;
        }

        updateGoal();
    }


    private Goal goal;




    private enum ClickedDayButton {
        DAY,
        INTERVAL_FROM,
        INTERVAL_TO
    };

    private RadioButton radioCustom;
    private RadioButton radioDay;
    private RadioButton radioMonth;
    private RadioButton radioYear;


    private LinearLayout customSelectionLayout;
    private LinearLayout daySelectionLayout;
    private LinearLayout monthSelectionLayout;
    private LinearLayout yearSelectionLayout;


    private Button fromButton;
    private Button toButton;

    private Button dayButton;
    private Button monthButton;
    private Button yearButton;

    private RadioButton radioCalories;
    private RadioButton radioDistance;
    private RadioButton radioDuration;


    private Button caloriesButton;
    private Button distanceButton;
    private Button durationButton;


    private Button createGoalButton;
    private Button cancelButton;

    private TextView formulatedGoalMsg;


    private Date dateFrom, dateTo, day, monthYear, year;
    private ClickedDayButton clickedDayButton;

    private GoalPeriod goalPeriod;
    private GoalType goalType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViews();
        setListeners();
        initFieldsAndButtons();
    }







    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backToGoals(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void findViews(){
        radioCustom = findViewById(R.id.createGoal_radioCustom);
        radioDay = findViewById(R.id.createGoal_radioDay);
        radioMonth = findViewById(R.id.createGoal_radioMonth);
        radioYear = findViewById(R.id.createGoal_radioYear);


        customSelectionLayout = findViewById(R.id.createGoal_customSelection);
        daySelectionLayout = findViewById(R.id.createGoal_daySelection);
        monthSelectionLayout = findViewById(R.id.createGoal_monthSelection);
        yearSelectionLayout = findViewById(R.id.createGoal_yearSelection);


        fromButton = findViewById(R.id.createGoal_buttonFrom);
        toButton = findViewById(R.id.createGoal_buttonTo);

        dayButton =  findViewById(R.id.createGoal_buttonDay);
        monthButton = findViewById(R.id.createGoal_buttonMonth);
        yearButton = findViewById(R.id.createGoal_buttonYear);


        radioCalories = findViewById(R.id.createGoal_radioCalories);
        radioDistance = findViewById(R.id.createGoal_radioDistance);
        radioDuration = findViewById(R.id.createGoal_radioDuration);

        caloriesButton = findViewById(R.id.createGoal_buttonCalories);
        distanceButton = findViewById(R.id.createGoal_buttonDistance);
        durationButton = findViewById(R.id.createGoal_buttonDuration);

        formulatedGoalMsg = findViewById(R.id.createGoal_formulatedGoalMsg);

        createGoalButton = findViewById(R.id.createGoal_buttonCreate);
        cancelButton = findViewById(R.id.createGoal_buttonCancel);
    }

    private void setListeners(){
        radioCustom.setOnClickListener( v -> checkGoalPeriod(GoalPeriod.CUSTOM_INTERVAL));
        radioDay.setOnClickListener( v -> checkGoalPeriod(GoalPeriod.DAY));
        radioMonth.setOnClickListener( v -> checkGoalPeriod(GoalPeriod.MONTH));
        radioYear.setOnClickListener( v -> checkGoalPeriod(GoalPeriod.YEAR));


        monthButton.setOnClickListener(v -> {
            MonthYearPickerFragment mypf = new MonthYearPickerFragment();

            // Prevent the date picker from picking a date before the current one
            Bundle args = new Bundle();
            args.putBoolean("blockPastDays", true);
            mypf.setArguments(args);

            mypf.show(getSupportFragmentManager(), null);

        } );
        yearButton.setOnClickListener(v -> new YearPickerFragment().show(getSupportFragmentManager(), null));

        dayButton.setOnClickListener(v -> {
            clickedDayButton = ClickedDayButton.DAY;
            DatePickerFragment dpf = new DatePickerFragment();

            // Prevent the date picker from picking a day before the current one
            Bundle args = new Bundle();
            args.putBoolean("blockPastDays", true);
            dpf.setArguments(args);

            dpf.show(getSupportFragmentManager(), "datePicker");
        });

        fromButton.setOnClickListener(v -> {
            clickedDayButton = ClickedDayButton.INTERVAL_FROM;
            DatePickerFragment dpf = new DatePickerFragment();

            // Prevent the date picker from picking a day before the current one
            Bundle args = new Bundle();
            args.putBoolean("blockPastDays", true);
            dpf.setArguments(args);

            dpf.show(getSupportFragmentManager(), "datePicker");
        });

        toButton.setOnClickListener(v -> {
            clickedDayButton = ClickedDayButton.INTERVAL_TO;
            DatePickerFragment dpf = new DatePickerFragment();

            // Prevent the date picker from picking a day before the current one
            Bundle args = new Bundle();
            args.putBoolean("blockPastDays", true);
            dpf.setArguments(args);

            dpf.show(getSupportFragmentManager(), "datePicker");
        });


        radioCalories.setOnClickListener(v -> checkGoalType(GoalType.CALORIES));
        radioDistance.setOnClickListener(v -> checkGoalType(GoalType.DISTANCE));
        radioDuration.setOnClickListener(v -> checkGoalType(GoalType.DURATION));


        caloriesButton.setOnClickListener(v -> new CaloriesPickerFragment().show(getSupportFragmentManager(), "caloriesPicker"));
        distanceButton.setOnClickListener(v -> new DistancePickerFragment().show(getSupportFragmentManager(), "distancePicker"));
        durationButton.setOnClickListener(v -> new DurationPickerFragment().show(getSupportFragmentManager(), "durationPicker"));


        createGoalButton.setOnClickListener(v -> backToGoals(true));
        cancelButton.setOnClickListener(v -> backToGoals(false));
    }


    private void checkGoalPeriod(GoalPeriod selectedGoalPeriod){
        customSelectionLayout.setVisibility(View.GONE);
        daySelectionLayout.setVisibility(View.GONE);
        monthSelectionLayout.setVisibility(View.GONE);
        yearSelectionLayout.setVisibility(View.GONE);


        switch(selectedGoalPeriod){
            case CUSTOM_INTERVAL: customSelectionLayout.setVisibility(View.VISIBLE); break;
            case DAY: daySelectionLayout.setVisibility(View.VISIBLE); break;
            case MONTH: monthSelectionLayout.setVisibility(View.VISIBLE); break;
            case YEAR: yearSelectionLayout.setVisibility(View.VISIBLE); break;

        }

        goalPeriod = selectedGoalPeriod;

        updateGoal();

    }

    private void checkGoalType(GoalType selectedGoalType){
        caloriesButton.setVisibility(View.GONE);
        distanceButton.setVisibility(View.GONE);
        durationButton.setVisibility(View.GONE);

        switch(selectedGoalType){
            case CALORIES: caloriesButton.setVisibility(View.VISIBLE); break;
            case DISTANCE: distanceButton.setVisibility(View.VISIBLE); break;
            case DURATION: durationButton.setVisibility(View.VISIBLE); break;
        }

        goalType = selectedGoalType;

        updateGoal();
    }

    private void initFieldsAndButtons(){
        Calendar currDateCal = Calendar.getInstance();

        Calendar nextWeekCal = Calendar.getInstance();
        nextWeekCal.add(Calendar.DATE, 7);

        Date currDate = currDateCal.getTime();
        Date nextWeek = nextWeekCal.getTime();

        // dummy initializations in order to avoid accessing null variables
        // on first dayCallback() call inside this method
        dateFrom = currDateCal.getTime();
        dateTo = nextWeekCal.getTime();

        final int DEFAULT_TARGET_DURATION = 3600 * 1 + 0 * 60; // 1h 00 min
        final int DEFAULT_TARGET_DISTANCE = 5000;   // 5 km
        final int DEFAULT_TARGET_CALORIES = 500;   // 500 kcal

        goal = new Goal(LoggedUser.getInstance().get(),
                goalPeriod,
                goalType.CALORIES,
                dateFrom,
                dateTo,
                DEFAULT_TARGET_DURATION,
                DEFAULT_TARGET_DISTANCE,
                DEFAULT_TARGET_CALORIES);



        goalPeriod = GoalPeriod.CUSTOM_INTERVAL;
        goalType = GoalType.CALORIES;


        clickedDayButton = ClickedDayButton.INTERVAL_FROM;
        dayCallback(currDate);

       clickedDayButton = ClickedDayButton.INTERVAL_TO;
        dayCallback(nextWeek);

        clickedDayButton = ClickedDayButton.DAY;
        dayCallback(currDate);

        monthYearCallback(currDate);
        yearCallback(currDate);


        goalType = GoalType.CALORIES;
        targetValueCallback(DEFAULT_TARGET_CALORIES);

        goalType = GoalType.DURATION;
        targetValueCallback(DEFAULT_TARGET_DURATION);

        goalType = GoalType.DISTANCE;
        targetValueCallback(DEFAULT_TARGET_DISTANCE);


        // starting value for goalType, after button initializations are done
        goalType = GoalType.CALORIES;




        updateGoal();
    }

    private void updateGoal(){
        goal.setGoalPeriod(goalPeriod);
        goal.setGoalType(goalType);

        switch(goalPeriod){
            case DAY:
                goal.setDate1(day);
                break;

            case MONTH:
                goal.setDate1(monthYear);
                break;

            case YEAR:
                goal.setDate1(year);
                break;

            case CUSTOM_INTERVAL:
                goal.setDate1(dateFrom);
                goal.setDate2(dateTo);
                break;
        }

        formulatedGoalMsg.setText(goal.getFormulatedGoal());

    }

    private void backToGoals(boolean saveGoal){
        Intent goalFragment = new Intent(this, MainActivity.class);
        goalFragment.putExtra("navId", R.id.drawer_goals);

        if(saveGoal)
            Goals.getInstance().add(goal);

        startActivity(goalFragment);
    }

    private Toast makeToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        View view = toast.getView();
        TextView toastMessage = (TextView) view.findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.WHITE);
        view.setBackgroundColor(Color.RED);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }

    private static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }
}