package org.altervista.yagotzirck.corriforrest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class HistorySessionsAdapter extends ArrayAdapter<DataSession> {

    // usata per aggiornare la pagina dell'activity UsersManagementActivity
    private HistoryOpenSessionDetails historyOpenSessionDetails;

    public HistorySessionsAdapter(Context context, ArrayList<DataSession> dataSessions, HistoryOpenSessionDetails historyOpenSessionDetails) {
        super(context, 0, dataSessions);
        this.historyOpenSessionDetails = historyOpenSessionDetails;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position

        DataSession dataSession = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_list_item, parent, false);

        }

        // Lookup view for data population

        RelativeLayout historyListItem = convertView.findViewById(R.id.historyListItem);
        TextView distance =   convertView.findViewById(R.id.history_item_distance);
        TextView duration = convertView.findViewById(R.id.history_item_duration);
        TextView date = convertView.findViewById(R.id.history_item_date);

        historyListItem.setTag(position);


        // Populate the data into the template view using the data object
        distance.setText(dataSession.getDistanceAsString() + " km");
        duration.setText(dataSession.getDurationAsString());
        date.setText(dataSession.getDateAsString());

        // Impostazione degli event listeners
        historyListItem.setOnClickListener( v -> showDetails(v) );

        return convertView;
    }

    private void showDetails(View view) {
        int position = (Integer) view.getTag();
        // Access the row position here to get the correct data item
         DataSession ds = getItem(position);


        // Avvisa dell'avvenuta promozione ed aggiorna la pagina
        historyOpenSessionDetails.openSessionDetailsCallback(ds);
    }
}
