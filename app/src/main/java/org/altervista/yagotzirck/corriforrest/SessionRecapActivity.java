package org.altervista.yagotzirck.corriforrest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
    private Button saveAndClose;
    private Button exitWithoutSaving;

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
        saveAndClose = findViewById(R.id.recap_saveAndClose);
        exitWithoutSaving = findViewById(R.id.recap_exitWithoutSaving);
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

    private void setListeners(){
        map.setOnClickListener( v -> new SessionDetailsMapDialog().show(getSupportFragmentManager(), null) );
        saveAndClose.setOnClickListener( v -> saveAndClose() );
        exitWithoutSaving.setOnClickListener(v -> confirmExitWithoutSaving() );


    }

    private void confirmExitWithoutSaving(){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Chiudi senza salvare");
        adb.setMessage("Vuoi scartare la sessione?");

        adb.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                goToHistory();
            }
        });

        adb.setNegativeButton("Annulla", null);
        adb.show();

    }

    private void saveAndClose(){
        DataSessions.getInstance().add(dataSession);
        goToHistory();
    }

    private void goToHistory(){
        Intent mainActivity_sessionsHistory = new Intent(this, MainActivity.class);
        mainActivity_sessionsHistory.putExtra("navId", R.id.drawer_session_history);
        startActivity(mainActivity_sessionsHistory);
    }
}