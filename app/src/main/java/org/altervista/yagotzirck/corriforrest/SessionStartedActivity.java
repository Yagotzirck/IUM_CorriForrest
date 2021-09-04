package org.altervista.yagotzirck.corriforrest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Timer;
import java.util.TimerTask;







public class SessionStartedActivity extends AppCompatActivity {

    private DataSession dataSession;

    private Timer timer;
    private boolean isTimerStopped = true;

    ActionBar actionBar;
    int openFragmentId;

    class UpdateStats extends TimerTask {

        public void run() {
            dataSession.update();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_started);


        // calling the action bar
        actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        openFragmentId = R.id.nav_status;




        BottomNavigationView bottomNav = findViewById(R.id.session_bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        dataSession = new DataSession(LoggedUser.getInstance().get());


        getSupportFragmentManager().beginTransaction().replace(R.id.session_fragment_container,
                new SessionStatusFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch(item.getItemId()){
                        case R.id.nav_status:
                            selectedFragment = new SessionStatusFragment();
                            break;

                        case R.id.nav_map:
                            selectedFragment = new SessionMapFragment();
                            break;

                    }

                    openFragmentId = item.getItemId();
                    updateToolbarTitle();

                    getSupportFragmentManager().beginTransaction().replace(R.id.session_fragment_container, selectedFragment).commit();
                    return true;
                }
            };




    public void startTimer(){
        if(isTimerStopped){
            isTimerStopped = false;
            updateToolbarTitle();

            timer = new Timer();
            timer.schedule(new UpdateStats(), 1000, 1000);
        }
    }

    public void stopTimer(){
        timer.cancel();
        isTimerStopped = true;

        updateToolbarTitle();
    }

    public DataSession getDataSession(){
        return dataSession;
    }

    private void updateToolbarTitle(){
        String toolbarTitle = isTimerStopped? getString(R.string.toolbar_sessionPaused) : getString(R.string.toolbar_sessionStarted);

        switch(openFragmentId){
            case R.id.nav_status:
                toolbarTitle += getString(R.string.toolbar_sessionStatus);
                break;

            case R.id.nav_map:
                toolbarTitle += getString(R.string.toolbar_sessionMap);
                break;

            case R.id.nav_goals:
                toolbarTitle += getString(R.string.toolbar_sessionGoals);
                break;
        }

        actionBar.setTitle(toolbarTitle);
    }
}