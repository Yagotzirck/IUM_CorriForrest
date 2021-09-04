package org.altervista.yagotzirck.corriforrest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GoalsAdapter extends ArrayAdapter<Goal> {

    // usata per eliminare un obiettivo selezionato
    private GoalDelete goalDelete;

    public GoalsAdapter(Context context, ArrayList<Goal> goals, GoalDelete goalDelete) {
        super(context, 0, goals);
        this.goalDelete = goalDelete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position

        Goal goal = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.goal_list_item, parent, false);

        }

        // Lookup view for data population

        ImageButton deleteButton = convertView.findViewById(R.id.goal_item_delete);
        TextView formulatedGoal =   convertView.findViewById(R.id.goal_item_formulatedGoal);
        TextView progress = convertView.findViewById(R.id.goal_item_progress);

        deleteButton.setTag(position);


        // Populate the data into the template view using the data object
        formulatedGoal.setText(goal.getFormulatedGoal());
        progress.setText(goal.getGoalProgressAsString());


        // Impostazione degli event listeners
        deleteButton.setOnClickListener( v -> showDetails(v) );

        return convertView;
    }

    private void showDetails(View view) {
        int position = (Integer) view.getTag();
        // Access the row position here to get the correct data item
         Goal goal = getItem(position);


        // Avvisa dell'avvenuta promozione ed aggiorna la pagina
        goalDelete.deleteGoal(goal);
    }
}
