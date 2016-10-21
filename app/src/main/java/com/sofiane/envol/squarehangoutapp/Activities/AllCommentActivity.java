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
import com.sofiane.envol.squarehangoutapp.Adapters.CommentListAdapter;
import com.sofiane.envol.squarehangoutapp.R;
import com.sofiane.envol.squarehangoutapp.Utils.ConnectivityReceiver;
import com.sofiane.envol.squarehangoutapp.Utils.MyApplication;

public class AllCommentActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private ListView listViewComment;
    private Firebase Fire_comment;
    private Query queryRef_place;
    private CommentListAdapter mListAdapterComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listViewComment = (ListView) findViewById(R.id.list_all_comment);
        checkConnection();
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String place = extras.getString("place");

            Fire_comment = new Firebase("https://hangoutenvol.firebaseio.com/Comment/");
            queryRef_place = Fire_comment.orderByChild("name_place").equalTo(place);
        }
        mListAdapterComment = new CommentListAdapter(queryRef_place, AllCommentActivity.this, R.layout.list_comment, getBaseContext());
        listViewComment.setAdapter(mListAdapterComment);

        mListAdapterComment.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listViewComment.setSelection(mListAdapterComment.getCount() - 1);
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

