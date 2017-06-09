package com.hubli.imperium.imaperiumdiary.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hubli.imperium.imaperiumdiary.Attendance.StudentParent.AttendanceDisplay;
import com.hubli.imperium.imaperiumdiary.Attendance.StudentParent.AttendanceSubjectWise;
import com.hubli.imperium.imaperiumdiary.Attendance.Teacher.ClassSelector;
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Events.Events;
import com.hubli.imperium.imaperiumdiary.Login.Login;
import com.hubli.imperium.imaperiumdiary.Profile.Profile;
import com.hubli.imperium.imaperiumdiary.ProgressReport.SubjectList;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.GenericMethods;
import com.hubli.imperium.imaperiumdiary.Utility.ServerConnect;
import com.hubli.imperium.imaperiumdiary.Utility.URL;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String BACK_STACK_MAIN = "main_tag";
    private Toolbar toolbar;
    private SPData spData;
    private ImageView propic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar  = (Toolbar) findViewById(R.id.toolbar);
        spData = new SPData();
        setSupportActionBar(toolbar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                initateMainTabAdaptor();
            }
        }).start();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(spData.getIdentification() == SPData.TEACHER){
            Menu m = navigationView.getMenu();
            m.findItem(R.id.nav_progress).setVisible(false);
            navViewSet(spData.getUserData(SPData.EMAIL),navigationView);
        }else{
            navViewSet("Class:"+spData.getUserData(SPData.CLASS)+", "+spData.getUserData(SPData.DIVISION), navigationView);
        }

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void navViewSet(String s, NavigationView navigationView) {
        View holder = navigationView.getHeaderView(0);
        propic = (ImageView) holder.findViewById(R.id.propic);
        Picasso.with(getApplicationContext())
                .load(GenericMethods.getThumbNailURL(URL.PROPIC_BASE_URL+spData.getUserData(SPData.PROPIC_URL)))
                .placeholder(R.drawable.defaultpropic)
                .error(R.drawable.defaultpropic)
                .networkPolicy(ServerConnect.checkInternetConenction(this) ?
                        NetworkPolicy.NO_CACHE : NetworkPolicy.OFFLINE)
                .into(propic);
        TextView name = (TextView) holder.findViewById(R.id.title);
        TextView rollnum = (TextView) holder.findViewById(R.id.rollnum);
        name.setText(spData.getUserData(SPData.FIRST_NAME)+" "+spData.getUserData(SPData.LAST_NAME));
        rollnum.setText(s);
    }

    private void initateMainTabAdaptor(){
        MainTabAdaptor tabAdaptor = new MainTabAdaptor();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_con,tabAdaptor);
        transaction.addToBackStack(BACK_STACK_MAIN);
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Picasso.with(getApplicationContext())
                .load(GenericMethods.getThumbNailURL(URL.PROPIC_BASE_URL+spData.getUserData(SPData.PROPIC_URL)))
                .placeholder(R.drawable.defaultpropic)
                .error(R.drawable.defaultpropic)
                .networkPolicy(ServerConnect.checkInternetConenction(this)?
                        NetworkPolicy.NO_CACHE:NetworkPolicy.OFFLINE)
                .into(propic);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {

            case R.id.nav_profile:
                startActivity(new Intent(getApplicationContext(), Profile.class));
                break;

            case R.id.nav_attendance:
                if(spData.getIdentification() == SPData.TEACHER) {
                    replaceFragment(new ClassSelector());
                }else {
                    if (spData.isSchool()) {
                        replaceFragment(new AttendanceDisplay());
                    } else {
                        replaceFragment(new AttendanceSubjectWise());
                    }
                }
                break;

            case R.id.nav_progress:
                replaceFragment(new SubjectList());
                break;
            case R.id.nav_event:
                replaceFragment(new Events());
                break;

            case R.id.nav_log_out:
                spData.clearData();
                startActivity(new Intent(getApplicationContext(), Login.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_con, fragment);
        transaction.addToBackStack(BACK_STACK_MAIN);
        transaction.commit();
    }
}
