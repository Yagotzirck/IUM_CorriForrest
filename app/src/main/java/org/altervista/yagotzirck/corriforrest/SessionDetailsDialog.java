package org.altervista.yagotzirck.corriforrest;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SessionDetailsDialog extends DialogFragment {

    private DataSession dataSession;

    private TextView date;
    private TextView startingTime;
    private TextView duration;
    private TextView distance;
    private TextView avgRhythm;
    private TextView avgSpeed;
    private TextView burnedCalories;

    private Button map;
    private Button backToSessionsList;

    public static SessionDetailsDialog newInstance(DataSession dataSession) {
        SessionDetailsDialog f = new SessionDetailsDialog();

        f.setDataSession(dataSession);
        //f.initFields();

        return f;
    }

    private void setDataSession(DataSession dataSession){ this.dataSession = dataSession; }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true); // deprecated
        
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_session_details, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        initFields();
        setListeners();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void findViews(View view){
        date = view.findViewById(R.id.recap_date);
        startingTime = view.findViewById(R.id.recap_startingTime);
        duration = view.findViewById(R.id.recap_duration);
        distance = view.findViewById(R.id.recap_distance);
        avgRhythm = view.findViewById(R.id.recap_avgRhythm);
        avgSpeed = view.findViewById(R.id.recap_avgSpeed);
        burnedCalories = view.findViewById(R.id.recap_burnedCalories);

        map = view.findViewById(R.id.recap_map);
        backToSessionsList = view.findViewById(R.id.sessionDetails_backToSessionsList);
    }

    private void initFields(){
        date.setText(dataSession.getDateAsString());
        startingTime.setText(dataSession.getTimeAsString());
        duration.setText(dataSession.getDurationAsString());
        distance.setText(dataSession.getDistanceAsString() + " km");
        avgRhythm.setText(dataSession.getAvgRhythmAsString());
        avgSpeed.setText(dataSession.getAvgSpeedAsString());
        burnedCalories.setText(dataSession.getBurnedCaloriesAsString());
    }

    private void setListeners() {
        map.setOnClickListener(v -> new SessionDetailsMapDialog().show(getChildFragmentManager(), null));
        backToSessionsList.setOnClickListener(v -> dismiss());
    }

}
