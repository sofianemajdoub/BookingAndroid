package com.sofiane.envol.squarehangoutapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;


import com.sofiane.envol.squarehangoutapp.R;
import com.sofiane.envol.squarehangoutapp.Utils.ConnectivityReceiver;
import com.sofiane.envol.squarehangoutapp.Utils.FireBaseClient;
import com.sofiane.envol.squarehangoutapp.Utils.MyApplication;

public class LikeActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    public RecyclerView recyclerView_like;
    private FireBaseClient clientFirebase;
    final static String DB_URL = "https://hangoutenvol.firebaseio.com/like/";
    String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
checkConnection();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainTabActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        clientFirebase.clear_restaurant();
    }

    @Override
    public void onResume() {
        super.onResume();

        recyclerView_like = (RecyclerView) findViewById(R.id.mRecylcer_like);
        recyclerView_like.setLayoutManager(new LinearLayoutManager(LikeActivity.this, LinearLayoutManager.VERTICAL, false));
        SharedPreferences pref = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        id_user = pref.getString("Id_User", "");
        clientFirebase = new FireBaseClient(LikeActivity.this, DB_URL + "" + id_user, recyclerView_like);
        clientFirebase.like();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainTabActivity.class);
        startActivity(intent);
        this.finish();
    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    // Showing the status in Snackbar
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
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

}
