package org.altervista.yagotzirck.corriforrest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle abdToggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(abdToggle);
        abdToggle.syncState();

        int id = getIntent().getIntExtra("navId", R.id.drawer_start_session);
        navigate(id);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
       navigate(item.getItemId());

       drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    private void navigate(int id){

        // toolbar.setTitle() doesn't work when the activity is launched
        // (from onCreate() ), for whatever reason

        switch(id){
            case R.id.drawer_start_session:
                //toolbar.setTitle(R.string.toolbar_startSession);
                getSupportActionBar().setTitle(R.string.toolbar_startSession);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new StartSessionFragment()).commit();
                break;

            case R.id.drawer_session_history:
                getSupportActionBar().setTitle(R.string.toolbar_sessionHistory);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HistoryFragment()).commit();
                break;

            case R.id.drawer_statistics:
                getSupportActionBar().setTitle(R.string.toolbar_statistics);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new StatisticsFragment()).commit();
                break;

            case R.id.drawer_goals:
                //toolbar.setTitle(R.string.toolbar_goals);
                getSupportActionBar().setTitle(R.string.toolbar_goals);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new GoalsFragment()).commit();
                break;

            case R.id.drawer_profile:
                getSupportActionBar().setTitle(R.string.toolbar_profile);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;

            case R.id.drawer_logout:
                LoggedUser.getInstance().set("");
                Intent login = new Intent(this, LoginActivity.class);
                startActivity(login);
                break;
        }
    }
}