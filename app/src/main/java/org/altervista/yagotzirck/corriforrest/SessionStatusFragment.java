package org.altervista.yagotzirck.corriforrest;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Timer;
import java.util.TimerTask;



public class SessionStatusFragment extends Fragment {

    private TextView sessionKm;
    private TextView sessionDuration;
    private TextView sessionKcal;
    private TextView sessionAvgSpeed;

    private Timer timer;

    class DisplayStats extends TimerTask {
        private DataSession dataSession;

        public DisplayStats(DataSession ds){ dataSession = ds; }

        public void run() {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    sessionKm.setText(dataSession.getDistance());
                    sessionDuration.setText(dataSession.getDuration());
                    sessionKcal.setText(dataSession.getBurnedCalories());
                    sessionAvgSpeed.setText(dataSession.getAvgSpeed());
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

        startTimer();


    }

    public void startTimer(){
        DataSession dataSession = ((SessionStartedActivity)getActivity()).getDataSession();
        timer = new Timer();
        timer.schedule(new DisplayStats(dataSession), 1000, 1000);

        ((SessionStartedActivity)getActivity()).startTimer();
    }

    public void stopTimer(){
        timer.cancel();
        ((SessionStartedActivity)getActivity()).stopTimer();
    }
}
