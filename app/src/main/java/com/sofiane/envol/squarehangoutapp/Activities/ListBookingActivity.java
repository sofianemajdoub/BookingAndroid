package com.sofiane.envol.squarehangoutapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.sofiane.envol.squarehangoutapp.Adapters.BookingListAdapter;
import com.sofiane.envol.squarehangoutapp.Entities.Booking;
import com.sofiane.envol.squarehangoutapp.R;
import com.sofiane.envol.squarehangoutapp.Utils.ConnectivityReceiver;
import com.sofiane.envol.squarehangoutapp.Utils.MyApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ListBookingActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private Query nameQuery;
    private ListView currentlistView;
    private String id_usr;
    private BookingListAdapter currentListAdapter, pastListAdapter;
    private Firebase fire_book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listbooking);
        currentlistView = (ListView) findViewById(R.id.list_curent);
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
    public void onStart() {
        super.onStart();
        SharedPreferences pref = getSharedPreferences("MyPrefsFile", 0);
        id_usr = pref.getString("Id_User", "def");
        fire_book = new Firebase("https://hangoutenvol.firebaseio.com/Booking");
        nameQuery = fire_book.orderByChild("idUser").equalTo(id_usr);


        currentListAdapter = new BookingListAdapter(nameQuery, ListBookingActivity.this, R.layout.list_booking);
        currentlistView.setAdapter(currentListAdapter);
        currentListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                currentlistView.setSelection(currentListAdapter.getCount() - 1);
            }
        });

        currentlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final Booking book = (Booking) parent.getItemAtPosition(position);
                if (!book.getDeleted().equals("yes")) {
                    Intent intent = new Intent(ListBookingActivity.this, DetailBookingActivity.class);
                    intent.putExtra("name", book.getResto_book());
                    intent.putExtra("id_resto", book.getIdPlace());
                    intent.putExtra("id", book.getIdBooking());
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
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

