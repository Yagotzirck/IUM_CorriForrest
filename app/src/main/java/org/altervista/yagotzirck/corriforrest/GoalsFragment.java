package org.altervista.yagotzirck.corriforrest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class GoalsFragment extends Fragment implements GoalDelete {

    private enum GoalsToDisplay {
        COMPLETED,
        ONGOING,
        FAILED
    };


    Button createNewGoalButton;

    TextView titleCompleted;
    TextView titleOngoing;
    TextView titleFailed;

    TextView noGoalsPresent;

    ListView listView;


    GoalsToDisplay selectedGoals;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

        BottomNavigationView bottomNav = view.findViewById(R.id.goals_bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.nav_goals_ongoing);

        selectedGoals = GoalsToDisplay.ONGOING;

        createNewGoalButton.setOnClickListener(v -> createNewGoal());
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    titleCompleted.setVisibility(View.GONE);
                    titleOngoing.setVisibility(View.GONE);
                    titleFailed.setVisibility(View.GONE);

                    switch(item.getItemId()){
                        case R.id.nav_goals_completed:
                            titleCompleted.setVisibility(View.VISIBLE);
                            displaySelectedGoals(GoalsToDisplay.COMPLETED);

                            break;

                        case R.id.nav_goals_ongoing:
                            titleOngoing.setVisibility(View.VISIBLE);
                            displaySelectedGoals(GoalsToDisplay.ONGOING);

                            break;

                        case R.id.nav_goals_failed:
                            titleFailed.setVisibility(View.VISIBLE);
                            displaySelectedGoals(GoalsToDisplay.FAILED);

                            break;

                    }

                    return true;
                }
            };

    private void findViews(View view){
        createNewGoalButton = view.findViewById(R.id.goals_createGoal);

        titleCompleted = view.findViewById(R.id.goals_titleCompleted);
        titleOngoing = view.findViewById(R.id.goals_titleOngoing);
        titleFailed = view.findViewById(R.id.goals_titleFailed);

        noGoalsPresent = view.findViewById(R.id.goals_noGoalsPresent);

        listView = view.findViewById(R.id.goals_listView);
    }

    private void displaySelectedGoals(GoalsToDisplay goalsToDisplay){
        Goals goalsInstance = Goals.getInstance();
        ArrayList<Goal> goalsFiltered;
        String user = LoggedUser.getInstance().get();

        selectedGoals = goalsToDisplay;


        switch(goalsToDisplay){
            case COMPLETED:
                goalsFiltered = goalsInstance.getCompletedGoals(user);
                break;

            case ONGOING:
                goalsFiltered = goalsInstance.getOngoingGoals(user);
                break;

            case FAILED:
                goalsFiltered = goalsInstance.getFailedGoals(user);
                break;

            default:
                goalsFiltered = new ArrayList<>();
        }

        if(goalsFiltered.isEmpty()) {
            noGoalsPresent.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        else{
            noGoalsPresent.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            GoalsAdapter adapter = new GoalsAdapter(getContext(), goalsFiltered, this);
            listView.setAdapter(adapter);
        }
    }


    private void createNewGoal(){
        Intent createGoal = new Intent(getContext(), CreateGoalActivity.class);
        startActivity(createGoal);
    }


    @Override
    public void deleteGoal(Goal goal) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
        adb.setTitle("Elimina obiettivo");
        adb.setMessage("Vuoi eliminare l'obiettivo\n\"" + goal.getFormulatedGoal() + "\"?");

        adb.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Goals.getInstance().remove(goal);
                displaySelectedGoals(selectedGoals);
            }
        });

        adb.setNegativeButton("Annulla", null);
        adb.show();

    }

}
