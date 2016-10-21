package com.sofiane.envol.squarehangoutapp.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sofiane.envol.squarehangoutapp.Adapters.ListRestaurantAdapter;
import com.sofiane.envol.squarehangoutapp.Fragments.CommentFragment;
import com.sofiane.envol.squarehangoutapp.R;
import com.sofiane.envol.squarehangoutapp.Utils.ConnectivityReceiver;
import com.sofiane.envol.squarehangoutapp.Utils.MyApplication;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DetailClubActivity extends AppCompatActivity implements OnMapReadyCallback   {


    private static final int REQUEST_PLACE_PICKER = 1;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLngBounds.Builder mBounds = new LatLngBounds.Builder();
    private String lat, lng, id_value, tel_value, nbr_value;
    private String name_value, open_value, close_value, address_value, image_value;
    private RatingBar rb;
    private FABToolbarLayout layout;
    private ImageView like_btn, book_btn, call_btn, close_btn;
    private View fab;
    public Firebase likeRef;
    private ValueEventListener mConnectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_club);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detailclub);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map_detail_club);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .build();
        mGoogleApiClient.connect();


        ImageView img = (ImageView) findViewById(R.id.img_header_club);
        TextView name = (TextView) findViewById(R.id.name_club_detail);
        TextView address = (TextView) findViewById(R.id.address_club_detail);
        TextView time = (TextView) findViewById(R.id.time_club_detail);


        SharedPreferences pref = getSharedPreferences("cnx", 0);
        final boolean cnx = pref.getBoolean("connected", false);

        layout = (FABToolbarLayout) findViewById(R.id.fabtoolbar);
        like_btn = (ImageView) findViewById(R.id.like);
        book_btn = (ImageView) findViewById(R.id.book);
        call_btn = (ImageView) findViewById(R.id.call);
        close_btn = (ImageView) findViewById(R.id.close);

        fab = findViewById(R.id.fabtoolbar_fab);


        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (like_btn.getTag() == "click_off") {
                    like_btn.setImageDrawable(getResources().getDrawable(R.drawable.heart_outline));
                    likeRef.setValue(null);
                } else {
                    like_btn.setImageDrawable(getResources().getDrawable(R.drawable.heart));
                    Map<String, String> user = new HashMap<String, String>();
                    user.put("name", name_value);
                    user.put("image", image_value);
                    user.put("address", address_value);
                    user.put("id", id_value);

                    user.put("close_time", close_value);
                    user.put("lat", lat);
                    user.put("lng", lng);

                    user.put("nbr_booking", nbr_value);
                    user.put("open_time", open_value);
                    user.put("phone", tel_value);

                    likeRef.setValue(user);
                }
            }
        });

        book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cnx == false) {
                    Intent intent = new Intent(DetailClubActivity.this, AuthActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(DetailClubActivity.this, CreateBookingActivity.class);
                    intent.putExtra("type", "NightClubs");
                    intent.putExtra("id_resto", id_value);
                    intent.putExtra("name_resto", name_value);
                    intent.putExtra("open_time", open_value);
                    intent.putExtra("close_time", close_value);
                    intent.putExtra("nbrBooking_resto", nbr_value);
                    startActivity(intent);
                }


            }
        });
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel_value.toString()));
                startActivity(intent);
            }
        });
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.hide();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.show();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name_value = bundle.getString("name_club");
            address_value = bundle.getString("address_club");
            image_value = bundle.getString("image_club");
            lat = bundle.getString("lat_club");
            lng = bundle.getString("lng_club");
            open_value = bundle.getString("open_club");
            close_value = bundle.getString("close_club");
            tel_value = bundle.getString("phone_club");
            id_value = bundle.getString("ID_club");
            nbr_value = bundle.getString("nbrBooking_club");

            name.setText(name_value);
            setTitle(name_value);
            address.setText(address_value);
            time.setText("" + open_value + " ~ " + close_value);
            Bitmap myBitmapAgain = ListRestaurantAdapter.decodeBase64(image_value);
            Bitmap resizedImage = Bitmap.createScaledBitmap(myBitmapAgain, 349, 270, true);
            img.setImageBitmap(resizedImage);
        }

        rb = (RatingBar) findViewById(R.id.ratting);
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                rating_dialog();
            }
        });


        SharedPreferences pref_comment = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        final String id_user = pref_comment.getString("Id_User", "");
        CommentFragment fragInfo = new CommentFragment();
        Bundle bundle_comment = new Bundle();
        bundle_comment.putString("user", id_user);
        bundle_comment.putString("place", name_value);
        fragInfo.setArguments(bundle_comment);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_club, fragInfo);
        fragmentTransaction.commit();
    }

    public void rating_dialog() {
        SharedPreferences pref = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        final String name_user = pref.getString("Name_User", "");
        final String img = pref.getString("Image_User", "");
        final String id_user = pref.getString("Id_User", "");

        AlertDialog.Builder alert = new AlertDialog.Builder(this);


        alert.setMessage("Auteur de l'avis : " + name_user);

        ImageView img_profil = new ImageView(this);
        Picasso.with(getApplicationContext()).load(img).into(img_profil);
        final EditText comment_put = new EditText(this);

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(img_profil);
        layout.addView(comment_put);

        alert.setView(layout);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final Calendar c = Calendar.getInstance();
                final int Myear = c.get(Calendar.YEAR);
                final int Mmonth = c.get(Calendar.MONTH) + 1;
                final int Mday = c.get(Calendar.DAY_OF_MONTH);
                SharedPreferences pref = getSharedPreferences("MyPrefsFile", 0);
                final String id_usr = pref.getString("Id_User", "def");
                Firebase commentRef = new Firebase("https://hangoutenvol.firebaseio.com/").child("Comment");
                Map<String, String> comment = new HashMap<String, String>();
                comment.put("name_user", name_user);
                comment.put("rating", "" + rb.getRating());
                comment.put("date", Mday + "/" + Mmonth + "/" + Myear);
                comment.put("description", comment_put.getText().toString());
                comment.put("id_user", id_usr);
                comment.put("name_place", name_value);
                comment.put("img_user", img);
                comment.put("id_place", id_value);
                String x = commentRef.push().getKey();
                comment.put("id", x);
                commentRef.child(x).setValue(comment);

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
        mMap.addMarker(marker);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), 10.0f));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                Map<String, Object> checkoutData = new HashMap<>();

            } else if (resultCode == PlacePicker.RESULT_ERROR) {

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void addPointToViewPort(LatLng newPoint) {
        mBounds.include(newPoint);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(newPoint.latitude, newPoint.longitude), 10.0f));
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences pref = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        final String id_user = pref.getString("Id_User", "");
        likeRef = new Firebase("https://hangoutenvol.firebaseio.com/like/").child(id_user).child(id_value);
        likeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    like_btn.setImageDrawable(getResources().getDrawable(R.drawable.heart));
                    like_btn.setTag("click_off");
                } else {
                    like_btn.setImageDrawable(getResources().getDrawable(R.drawable.heart_outline));
                    like_btn.setTag("click_on");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        mConnectedListener = likeRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {

                } else {

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        likeRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainTabActivity.class);
                startActivity(intent);
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainTabActivity.class);
        startActivity(intent);
    }
}
