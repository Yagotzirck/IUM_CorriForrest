package org.altervista.yagotzirck.corriforrest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SessionGoalsAdapter extends ArrayAdapter<Goal> {

    public SessionGoalsAdapter(Context context, ArrayList<Goal> goals) {
        super(context, 0, goals);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position

        Goal goal = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.session_goal_list_item, parent, false);

        }

        // Lookup view for data population

        ImageView checkedImg = convertView.findViewById(R.id.session_goal_item_checked);
        TextView formulatedGoal =   convertView.findViewById(R.id.session_goal_item_formulatedGoal);
        TextView progress = convertView.findViewById(R.id.session_goal_item_progress);


        if(goal.getGoalProgressPercent() >= 100)
            checkedImg.setVisibility(View.VISIBLE);


        // Populate the data into the template view using the data object
        formulatedGoal.setText(goal.getFormulatedGoal());
        progress.setText(goal.getGoalProgressAsString());


        return convertView;
    }

}
