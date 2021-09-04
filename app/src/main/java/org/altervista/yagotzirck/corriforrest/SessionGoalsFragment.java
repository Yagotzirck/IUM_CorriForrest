package org.altervista.yagotzirck.corriforrest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class SessionGoalsFragment extends Fragment{


    TextView noActiveGoals;

    ListView listView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_session_goals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        displayGoals();
    }




    private void findViews(View view){

        noActiveGoals = view.findViewById(R.id.session_goals_noActiveGoals);
        listView = view.findViewById(R.id.session_goals_listView);
    }

    private void displayGoals(){
        Goals goalsInstance = Goals.getInstance();
        String user = LoggedUser.getInstance().get();

        ArrayList<Goal> goalsFiltered = goalsInstance.getActiveGoals(user);


        if(goalsFiltered.isEmpty()) {
            noActiveGoals.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        else{
            noActiveGoals.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            SessionGoalsAdapter adapter = new SessionGoalsAdapter(getContext(), goalsFiltered);
            listView.setAdapter(adapter);
        }
    }

}
