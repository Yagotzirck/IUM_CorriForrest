package org.altervista.yagotzirck.corriforrest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.se.omapi.Session;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import java.util.Timer;
import java.util.TimerTask;



public class SessionStatusFragment extends Fragment {
    private static boolean isPaused = false;
    private static boolean isPauseUnlocked = false;

    private static SessionStartedActivity sessionStartedActivity = null;


    private TextView sessionKm;
    private TextView sessionDuration;
    private TextView sessionKcal;
    private TextView sessionAvgSpeed;

    private ImageButton buttonPause;
    private ImageButton buttonResume;
    private ImageButton buttonStop;

    private SwitchCompat switchPause;

    private LinearLayout sessionStartedButtons;
    private RelativeLayout sessionPausedButtons;


    private Timer timer;

    class DisplayStats extends TimerTask {
        private DataSession dataSession;

        public DisplayStats(DataSession ds){ dataSession = ds; }

        public void run() {
            sessionStartedActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    updateStats(dataSession);
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_session_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionKm = view.findViewById(R.id.session_km);
        sessionDuration = view.findViewById(R.id.session_duration);
        sessionKcal = view.findViewById(R.id.session_kcal);
        sessionAvgSpeed = view.findViewById(R.id.session_avgSpeed);

        buttonPause = view.findViewById(R.id.button_pause_session);
        buttonResume = view.findViewById(R.id.button_resume_session);
        buttonStop = view.findViewById(R.id.button_stop_session);

        switchPause = view.findViewById(R.id.switch_pause_session);

        sessionStartedButtons = view.findViewById(R.id.session_started_buttons);
        sessionPausedButtons = view.findViewById(R.id.session_paused_buttons);



        // set listeners
        switchPause.setOnCheckedChangeListener((viewInner, motionEvent) -> pauseUnlockerTouched());

        buttonPause.setOnClickListener( v -> pause()  );
        buttonResume.setOnClickListener( v -> resume() );

        updateStats(sessionStartedActivity.getDataSession());

        if(isPaused)
            pause();
        else {
            resume();
            isPaused = false;
            isPauseUnlocked = false;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (sessionStartedActivity == null && context instanceof SessionStartedActivity)
            sessionStartedActivity = (SessionStartedActivity) context;
    }


    public void startTimer(){
        DataSession dataSession = sessionStartedActivity.getDataSession();
        timer = new Timer();
        timer.schedule(new DisplayStats(dataSession), 0, 1000);

        sessionStartedActivity.startTimer();
    }

    private void updateStats(DataSession dataSession){
        sessionKm.setText(dataSession.getDistance());
        sessionDuration.setText(dataSession.getDuration());
        sessionKcal.setText(dataSession.getBurnedCalories());
        sessionAvgSpeed.setText(dataSession.getAvgSpeed());
    }


    public void stopTimer(){
        if(timer != null)
            timer.cancel();
        sessionStartedActivity.stopTimer();
    }


    private boolean pauseUnlockerTouched(){
        if(switchPause.isChecked()){
            isPauseUnlocked = true;
            DrawableCompat.setTint(buttonPause.getDrawable(), ContextCompat.getColor(sessionStartedActivity.getApplicationContext(), R.color.button_orange));
        }
        else{
            isPauseUnlocked = false;
            DrawableCompat.setTint(buttonPause.getDrawable(), ContextCompat.getColor(sessionStartedActivity.getApplicationContext(), R.color.grey));
        }

        return false;   // to make the listener happy
    }



    private void pause(){
        if(isPauseUnlocked){
            isPaused = true;
            stopTimer();

            sessionStartedButtons.setVisibility(View.GONE);
            sessionPausedButtons.setVisibility(View.VISIBLE);
        }

        else
            switchPause.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.shake));
    }

    private void resume(){
        DrawableCompat.setTint(buttonPause.getDrawable(), ContextCompat.getColor(sessionStartedActivity.getApplicationContext(), R.color.grey));
        switchPause.setChecked(false);

        startTimer();

        sessionStartedButtons.setVisibility(View.VISIBLE);
        sessionPausedButtons.setVisibility(View.GONE);
    }

    private void stop(){
        isPaused = false;
        isPauseUnlocked = false;
        DataSession dataSession = sessionStartedActivity.getDataSession();
        sessionStartedActivity = null;  // for garbage collection to take place
        // Change activity here (use a dialog fragment for confirmation)
    }

}
