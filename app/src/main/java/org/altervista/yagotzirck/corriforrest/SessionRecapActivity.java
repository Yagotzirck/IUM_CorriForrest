package org.altervista.yagotzirck.corriforrest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class SessionRecapActivity extends AppCompatActivity {

    private DataSession dataSession;

    private TextView date;
    private TextView startingTime;
    private TextView duration;
    private TextView distance;
    private TextView avgRhythm;
    private TextView avgSpeed;
    private TextView burnedCalories;

    private Button map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_recap);

        dataSession = (DataSession)getIntent().getSerializableExtra("dataSession");

        findViews();
        initFields();
        setListeners();



    }

    private void findViews(){
        date = findViewById(R.id.recap_date);
        startingTime = findViewById(R.id.recap_startingTime);
        duration = findViewById(R.id.recap_duration);
        distance = findViewById(R.id.recap_distance);
        avgRhythm = findViewById(R.id.recap_avgRhythm);
        avgSpeed = findViewById(R.id.recap_avgSpeed);
        burnedCalories = findViewById(R.id.recap_burnedCalories);

        map = findViewById(R.id.recap_map);
    }

    private void initFields(){
        date.setText(dataSession.getDate());
        startingTime.setText(dataSession.getTime());
        duration.setText(dataSession.getDuration());
        distance.setText(dataSession.getDistance() + " km");
        avgRhythm.setText(dataSession.getAvgRhythm());
        avgSpeed.setText(dataSession.getAvgSpeed());
        burnedCalories.setText(dataSession.getBurnedCalories());
    }

    private void setListeners(){
        map.setOnClickListener( v -> new SessionDetailsMapDialog().show(getSupportFragmentManager(), null) );


    }
}