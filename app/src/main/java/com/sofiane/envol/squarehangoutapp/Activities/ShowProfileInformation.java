package com.sofiane.envol.squarehangoutapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.sofiane.envol.squarehangoutapp.Entities.Users;
import com.sofiane.envol.squarehangoutapp.R;
import com.sofiane.envol.squarehangoutapp.Utils.ConnectivityReceiver;
import com.sofiane.envol.squarehangoutapp.Utils.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class ShowProfileInformation extends AppCompatActivity  implements ConnectivityReceiver.ConnectivityReceiverListener{

    //  private static final String FIREBASE_URL = "https://hangoutenvol.firebaseio.com/";
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private String id;
    private  ProgressDialog dialog;
    private TextView textViewName, textViewEmail, textViewAddress, textViewPhone;
    private static final String FIREBASE_URL = "https://hangoutenvol.firebaseio.com/";
    private Firebase ref_user;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private LinearLayout nameLayout, addressLayout, phoneLayout, emailLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
        id = prefs.getString("Id_User", "No name defined");
        textViewName = (TextView) findViewById(R.id.name);
        textViewEmail = (TextView) findViewById(R.id.email);
        textViewAddress = (TextView) findViewById(R.id.address);
        textViewPhone = (TextView) findViewById(R.id.phone);
        checkConnection();
        nameLayout = (LinearLayout) findViewById(R.id.layout_name);
        emailLayout = (LinearLayout) findViewById(R.id.layout_email);
        addressLayout = (LinearLayout) findViewById(R.id.layout_address);
        phoneLayout = (LinearLayout) findViewById(R.id.layout_phone);

        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        nameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowProfileInformation.this);
                builder.setTitle(textViewName.getText());
                final EditText input_name = new EditText(ShowProfileInformation.this);
                input_name.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
                builder.setView(input_name);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textViewName.setText(input_name.getText());
                        update();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });
        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowProfileInformation.this);
                builder.setTitle(textViewEmail.getText());
                final EditText input_email = new EditText(ShowProfileInformation.this);
                input_email.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
                builder.setView(input_email);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textViewEmail.setText(input_email.getText());
                        update();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowProfileInformation.this);
                builder.setTitle(textViewAddress.getText());
                final EditText input_address = new EditText(ShowProfileInformation.this);
                input_address.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
                builder.setView(input_address);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textViewAddress.setText(input_address.getText());
                        update();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });
        phoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowProfileInformation.this);
                builder.setTitle(textViewPhone.getText());
                final EditText input_phone = new EditText(ShowProfileInformation.this);
                input_phone.setInputType(InputType.TYPE_CLASS_PHONE);
                builder.setView(input_phone);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textViewPhone.setText(input_phone.getText());
                        update();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public void onStart() {
        super.onStart();

        ref_user = new Firebase(FIREBASE_URL).child("Users");
        Query query_user = ref_user.orderByKey().equalTo(id);

        query_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Users post = postSnapshot.getValue(Users.class);
                    textViewName.setText(post.getName());
                    textViewEmail.setText(post.getEmail());
                    textViewAddress.setText(post.getAddress());
                    textViewPhone.setText(post.getPhone());
                }
                dialog.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(this, MainTabActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }

        if (id == R.id.action_issue) {
            Intent intent = new Intent(this, RepportActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_search) {
            // Intent intent = new Intent(this, SearchActivity.class);
            Intent intent = new Intent(this,PointsActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            Toast.makeText(ShowProfileInformation.this, "press", Toast.LENGTH_SHORT).show();
            takeScreenshot();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }


    private void update() {

        Firebase ref = new Firebase("https://hangoutenvol.firebaseio.com/").child("Users").child(id);

        ref.child("name").setValue(textViewName.getText().toString());
        ref.child("email").setValue(textViewEmail.getText().toString());
        ref.child("address").setValue(textViewAddress.getText().toString());
        ref.child("phone").setValue(textViewPhone.getText().toString());
    }


    public void ButtonLogout(View v) {

        Intent intent = new Intent(this, AuthActivity.class);
        LoginManager.getInstance().logOut();
        SharedPreferences.Editor editor = getSharedPreferences("cnx", Context.MODE_PRIVATE).edit();
        editor.putBoolean("connected", false);
        editor.commit();
        startActivity(intent);

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
