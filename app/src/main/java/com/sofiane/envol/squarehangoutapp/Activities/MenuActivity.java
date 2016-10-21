package com.sofiane.envol.squarehangoutapp.Activities;

import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.sofiane.envol.squarehangoutapp.Adapters.ListMenuAdapter;
import com.sofiane.envol.squarehangoutapp.R;
import com.sofiane.envol.squarehangoutapp.Utils.ConnectivityReceiver;
import com.sofiane.envol.squarehangoutapp.Utils.MyApplication;

public class MenuActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private Query queryRef_dessert, queryRef_starter, queryRef_main;
    private ListView listView_dessert, listView_starter, listView_main;
    private ListMenuAdapter mListAdapter_dessert, mListAdapter_starter, mListAdapter_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkConnection();
        listView_dessert = (ListView) findViewById(R.id.list_dessert);
        listView_starter = (ListView) findViewById(R.id.list_starter);
        listView_main = (ListView) findViewById(R.id.list_main);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String id_resto = extras.getString("id_resto");

            Firebase fire_menu = new Firebase("https://hangoutenvol.firebaseio.com/Menu/");
            queryRef_dessert = fire_menu.child(id_resto).orderByChild("menu_type").equalTo("Desserts");
            queryRef_starter = fire_menu.child(id_resto).orderByChild("menu_type").equalTo("Entree");
            queryRef_main = fire_menu.child(id_resto).orderByChild("menu_type").equalTo("Main");
        }
        mListAdapter_dessert = new ListMenuAdapter(queryRef_dessert, MenuActivity.this, R.layout.list_resto);
        listView_dessert.setAdapter(mListAdapter_dessert);
        mListAdapter_dessert.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView_dessert.setSelection(mListAdapter_dessert.getCount() - 1);
            }
        });

        mListAdapter_starter = new ListMenuAdapter(queryRef_starter, MenuActivity.this, R.layout.list_resto);
        listView_starter.setAdapter(mListAdapter_starter);
        mListAdapter_starter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView_starter.setSelection(mListAdapter_starter.getCount() - 1);
            }
        });

        mListAdapter_main = new ListMenuAdapter(queryRef_main, MenuActivity.this, R.layout.list_resto);
        listView_main.setAdapter(mListAdapter_main);
        mListAdapter_main.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView_main.setSelection(mListAdapter_main.getCount() - 1);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mListAdapter_dessert.cleanup();
        mListAdapter_starter.cleanup();
        mListAdapter_main.cleanup();
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
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
