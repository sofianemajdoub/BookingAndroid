package com.sofiane.envol.squarehangoutapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sofiane.envol.squarehangoutapp.Adapters.MainPagerAdapter;
import com.sofiane.envol.squarehangoutapp.R;
import com.sofiane.envol.squarehangoutapp.Utils.ConnectivityReceiver;
import com.sofiane.envol.squarehangoutapp.Utils.MyApplication;

public class MainTabActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    private MainPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        fragmentManager = getSupportFragmentManager();
        checkConnection();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        SharedPreferences pref = getSharedPreferences("cnx", 0);
        final boolean cnx = pref.getBoolean("connected", false);

        if (id == R.id.action_profil) {
            if (cnx != false) {
                Intent intent = new Intent(this, ShowProfileInformation.class);
                startActivity(intent);
                this.finish();
            } else {
                Intent intent = new Intent(this, AuthActivity.class);
                startActivity(intent);
            }
            return true;
        }

        if (id == R.id.action_booking) {
            Intent intent = new Intent(this, ListBookingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            this.finish();
            return true;
        }

        if (id == R.id.action_Like) {
            Intent intent = new Intent(this, LikeActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    }
    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }


    private void showSnack(boolean isConnected) {

        if (!isConnected) {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "This is main activity", Snackbar.LENGTH_LONG)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    })
                    .setText(R.string.alert_cnx)
                    .setActionTextColor(Color.WHITE)
                    .show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

}