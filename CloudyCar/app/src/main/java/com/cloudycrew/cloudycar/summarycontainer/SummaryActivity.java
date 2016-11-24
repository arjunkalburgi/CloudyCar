package com.cloudycrew.cloudycar.summarycontainer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.driversummary.DriverSummaryFragment;
import com.cloudycrew.cloudycar.ridersummary.RiderSummaryFragment;
import com.cloudycrew.cloudycar.userprofile.UserProfileActivity;

public class SummaryActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RiderSummaryFragment riderSummaryFragment;
    private DriverSummaryFragment driverSummaryFragment;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.resolveDependencies();
        setContentView(R.layout.activity_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        ((TextView) header.findViewById(R.id.nav_header_name))
                .setText(userController.getCurrentUser().getUsername());
        ((TextView) header.findViewById(R.id.nav_header_email))
                .setText(userController.getCurrentUser().getEmail().getEmail());

        riderSummaryFragment = new RiderSummaryFragment();
        driverSummaryFragment = new DriverSummaryFragment();

        // determine rider or driver for main fragment
        String mode = this.getIntent().getStringExtra("mode");
        if (mode.equals("rider")) {
            setFragment(riderSummaryFragment);
        } else if (mode.equals("driver")) {
            setFragment(driverSummaryFragment);
        }
    }

    private void resolveDependencies() {
        this.userController = getCloudyCarApplication().getUserController();
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
    
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(SummaryActivity.this, UserProfileActivity.class);
            intent.putExtra("username", userController.getCurrentUser().getUsername());
            startActivity(intent);
        } else if (id == R.id.nav_rider) {
            setFragment(riderSummaryFragment);
        } else if (id == R.id.nav_driver) {
            setFragment(driverSummaryFragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.summary_content, fragment)
                .commit();
    }
}
