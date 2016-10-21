package com.sofiane.envol.squarehangoutapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sofiane.envol.squarehangoutapp.Entities.NightClubs;
import com.sofiane.envol.squarehangoutapp.Entities.Restaurants;
import com.sofiane.envol.squarehangoutapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapSearchActivity extends FragmentActivity implements OnMapReadyCallback {


    private static final int REQUEST_PLACE_PICKER = 1;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLngBounds.Builder mBounds = new LatLngBounds.Builder();
    private static final String FIREBASE_URL = "https://hangoutenvol.firebaseio.com/";

    private AutoCompleteTextView inputText_place;
    private String input_place;
    private List<String> lst_address;

    private MarkerOptions marker_club, marker_restaurant;
    private List<String> lst_places;
    private Firebase ref_restaurant, ref_club;
    private Restaurants restaurant;
    private NightClubs club;
    private ArrayList<Restaurants> restaurants = new ArrayList<>();
    private ArrayList<NightClubs> nightClubs = new ArrayList<>();
    private Query queryRef_restaurant, queryRef_club;

    private void addPointToViewPort(LatLng newPoint) {
        mBounds.include(newPoint);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(newPoint.latitude, newPoint.longitude), 10.0f));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Set up Google Maps
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Set up the API client for Places API
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .build();
        mGoogleApiClient.connect();

        // Set up Firebase
        Firebase.setAndroidContext(this);
        ref_restaurant = new Firebase(FIREBASE_URL).child("Restaurants");
        ref_club = new Firebase(FIREBASE_URL).child("NightClubs");

        lst_address = new ArrayList<String>();
        inputText_place = (AutoCompleteTextView) findViewById(R.id.Input_Place);

        findViewById(R.id.club).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                setClubMarks();
            }
        });
        findViewById(R.id.restau).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                setRestaurantMarks();
            }
        });

        findViewById(R.id.sendback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapSearchActivity.this, MainTabActivity.class);
                startActivity(intent);
            }
        });

        lst_places = new ArrayList<String>();


        ref_restaurant = new Firebase("https://hangoutenvol.firebaseio.com/Restaurants");
        ref_restaurant.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Restaurants restaurants_entity = postSnapshot.getValue(Restaurants.class);
                    lst_places.add(String.valueOf(restaurants_entity.getName()));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        ref_club = new Firebase("https://hangoutenvol.firebaseio.com/NightClubs");
        ref_club.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    NightClubs restaurants_entity = postSnapshot.getValue(NightClubs.class);
                    lst_places.add(String.valueOf(restaurants_entity.getName()));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        ArrayAdapter adapter_address = new ArrayAdapter(MapSearchActivity.this, android.R.layout.simple_list_item_1, lst_places);
        inputText_place.setAdapter(adapter_address);
        inputText_place.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                input_place = inputText_place.getText().toString();
                if (!input_place.equals("")) {

                    findRestaurant(input_place);

                    findClub(input_place);

                }

                inputText_place.setText("");
            }
        });
        inputText_place.setThreshold(1);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                addPointToViewPort(ll);
                mMap.setOnMyLocationChangeListener(null);
            }
        });
    }


    public void setRestaurantMarks() {
        ref_restaurant.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    restaurant = postSnapshot.getValue(Restaurants.class);
                    lst_places.add(String.valueOf(restaurant.getName()) + " - " + String.valueOf(restaurant.getAddress()));
                    marker_restaurant = new MarkerOptions().position(new LatLng(Double.parseDouble(restaurant.getLat()), Double.parseDouble(restaurant.getLng()))).title(restaurant.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker1) {
                            findRestaurant(marker1.getTitle());
                        }
                    });
                    mMap.addMarker(marker_restaurant);
                }
                lst_address.add(String.valueOf(restaurant.getLng()));
                lst_address.add(String.valueOf(restaurant.getLat()));

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void setClubMarks() {

        ref_club.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    club = postSnapshot.getValue(NightClubs.class);
                    lst_places.add(String.valueOf(club.getName()) + " - " + String.valueOf(club.getAddress()));
                    // create marker
                    marker_club = new MarkerOptions().position(new LatLng(Double.parseDouble(club.getLat()), Double.parseDouble(club.getLng()))).title(club.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker1) {
                            findClub(marker1.getTitle());
                        }
                    });
                    mMap.addMarker(marker_club);
                }
                lst_address.add(String.valueOf(club.getLng()));
                lst_address.add(String.valueOf(club.getLat()));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    /**
     * Prompt the user to check out of their location. Called when the "Check Out!" button
     * is clicked.
     */
    public void checkOut(View view) {
        try {
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent, REQUEST_PLACE_PICKER);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    REQUEST_PLACE_PICKER);
        } catch (GooglePlayServicesNotAvailableException e) {

        }
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


    public void findRestaurant(String x) {
        restaurants.clear();
        Query q = ref_restaurant.orderByChild("name").equalTo(x);
        q.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Restaurants m = new Restaurants();
                        m.setName(postSnapshot.getValue(Restaurants.class).getName());
                        m.setImage(postSnapshot.getValue(Restaurants.class).getImage());
                        m.setCuisine(postSnapshot.getValue(Restaurants.class).getCuisine());

                        m.setAddress(postSnapshot.getValue(Restaurants.class).getAddress());
                        m.setClose_time(postSnapshot.getValue(Restaurants.class).getClose_time());
                        m.setOpen_time(postSnapshot.getValue(Restaurants.class).getOpen_time());

                        m.setLat(postSnapshot.getValue(Restaurants.class).getLat());
                        m.setLng(postSnapshot.getValue(Restaurants.class).getLng());
                        m.setNbr_booking(postSnapshot.getValue(Restaurants.class).getNbr_booking());
                        m.setPhone(postSnapshot.getValue(Restaurants.class).getPhone());

                        m.setId(postSnapshot.getKey());
                        restaurants.add(m);

                    }

                    Intent i = new Intent(MapSearchActivity.this, DetailRestaurantActivity.class);
                    ((InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE))
                            .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    i.putExtra("name_resto", restaurants.get(0).getName());
                    i.putExtra("address_resto", restaurants.get(0).getAddress());
                    i.putExtra("cuisine_resto", restaurants.get(0).getCuisine());
                    i.putExtra("close_resto", restaurants.get(0).getClose_time());
                    i.putExtra("open_resto", restaurants.get(0).getOpen_time());
                    i.putExtra("lat_resto", restaurants.get(0).getLat());
                    i.putExtra("lng_resto", restaurants.get(0).getLng());
                    i.putExtra("nbrBooking_resto", restaurants.get(0).getNbr_booking());
                    i.putExtra("phone_resto", restaurants.get(0).getPhone());
                    i.putExtra("image_resto", restaurants.get(0).getImage());
                    i.putExtra("ID_resto", restaurants.get(0).getId());
                    startActivity(i);
                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public void findClub(String x) {
        nightClubs.clear();
        Query q2 = ref_club.orderByChild("name").equalTo(x);
        q2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        NightClubs m = new NightClubs();
                        m.setName(postSnapshot.getValue(NightClubs.class).getName());
                        m.setImage(postSnapshot.getValue(NightClubs.class).getImage());
                        m.setAddress(postSnapshot.getValue(NightClubs.class).getAddress());
                        m.setClose_time(postSnapshot.getValue(NightClubs.class).getClose_time());
                        m.setOpen_time(postSnapshot.getValue(NightClubs.class).getOpen_time());
                        m.setLat(postSnapshot.getValue(NightClubs.class).getLat());
                        m.setLng(postSnapshot.getValue(NightClubs.class).getLng());
                        m.setNbr_booking(postSnapshot.getValue(NightClubs.class).getNbr_booking());
                        m.setPhone(postSnapshot.getValue(NightClubs.class).getPhone());
                        m.setId(postSnapshot.getKey());
                        nightClubs.add(m);

                    }


                    Intent intent = new Intent(MapSearchActivity.this, DetailClubActivity.class);

                    intent.putExtra("name_club", nightClubs.get(0).getName());
                    intent.putExtra("address_club", nightClubs.get(0).getAddress());
                    intent.putExtra("close_club", nightClubs.get(0).getClose_time());
                    intent.putExtra("open_club", nightClubs.get(0).getOpen_time());
                    intent.putExtra("lat_club", nightClubs.get(0).getLat());
                    intent.putExtra("lng_club", nightClubs.get(0).getLng());
                    intent.putExtra("nbrBooking_club", nightClubs.get(0).getNbr_booking());
                    intent.putExtra("phone_club", nightClubs.get(0).getPhone());
                    intent.putExtra("image_club", nightClubs.get(0).getImage());
                    intent.putExtra("ID_club", nightClubs.get(0).getId());
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }

    @Override
    public void onBackPressed() {
    }
}