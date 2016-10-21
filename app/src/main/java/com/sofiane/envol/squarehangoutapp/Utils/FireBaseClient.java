package com.sofiane.envol.squarehangoutapp.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.sofiane.envol.squarehangoutapp.Adapters.ListClubAdapter;
import com.sofiane.envol.squarehangoutapp.Adapters.ListCommentAdapter;
import com.sofiane.envol.squarehangoutapp.Adapters.ListLikeAdapter;
import com.sofiane.envol.squarehangoutapp.Adapters.ListPointAdapter;
import com.sofiane.envol.squarehangoutapp.Adapters.ListRestaurantAdapter;
import com.sofiane.envol.squarehangoutapp.Entities.Booking;
import com.sofiane.envol.squarehangoutapp.Entities.Comments;
import com.sofiane.envol.squarehangoutapp.Entities.Menus;
import com.sofiane.envol.squarehangoutapp.Entities.NightClubs;
import com.sofiane.envol.squarehangoutapp.Entities.Points;
import com.sofiane.envol.squarehangoutapp.Entities.Restaurants;
import com.sofiane.envol.squarehangoutapp.R;

import java.util.ArrayList;

public class FireBaseClient {

    Context c;
    String DB_URL;
    RecyclerView rv;
    Firebase fire;
    ProgressDialog pd;

    ArrayList<Restaurants> restaurants = new ArrayList<>();
    ArrayList<Booking> bookings = new ArrayList<>();
    ArrayList<NightClubs> nightClubs = new ArrayList<>();
    ArrayList<Comments> comments = new ArrayList<>();
    ArrayList<Menus> menus = new ArrayList<>();
    ArrayList<Points> points = new ArrayList<>();

    ListRestaurantAdapter adapter;
    ListLikeAdapter like_adapter;
    ListClubAdapter club_adapter;
    ListCommentAdapter comment_adapter;
    ListPointAdapter point_adapter;

    public FireBaseClient(Context c, String DB_URL, RecyclerView rv) {
        this.c = c;
        this.DB_URL = DB_URL;
        this.rv = rv;

        //INITIALIZE
        Firebase.setAndroidContext(c);
        pd = new ProgressDialog(c);
        pd.setMessage(c.getResources().getString(R.string.loading));
        pd.setIndeterminate(false);
        pd.setCancelable(true);
        pd.show();
        //INSTANTIATE
        fire = new Firebase(DB_URL);
        fire.keepSynced(true);
    }

    /*
        //SAVE
        public void saveOnline(String nom,String image)
        {
            Movie m=new Movie();
            m.setNom(nom) ;
            m.setImage(image);

            fire.child("liste").push().setValue(m);
        }
    */
    //RETRIEVE
    public void findByCuisine(String val) {
        Query queryRef = fire.orderByChild("cuisine").equalTo(val);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                pd.dismiss();
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

                    if (restaurants.size() > 0) {
                        adapter = new ListRestaurantAdapter(c, restaurants);
                        rv.setAdapter(adapter);
                    } else {
                        Toast.makeText(c, "No data", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


    }


    public void find() {

        fire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                pd.dismiss();
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

                    if (restaurants.size() > 0) {
                        adapter = new ListRestaurantAdapter(c, restaurants);
                        rv.setAdapter(adapter);
                    } else {
                        Toast.makeText(c, "No data", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }


    public void findRestaurantByName(String val) {
        Query queryRef = fire.orderByChild("name").equalTo(val);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                pd.dismiss();
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

                    if (restaurants.size() > 0) {
                        adapter = new ListRestaurantAdapter(c, restaurants);
                        rv.setAdapter(adapter);
                    } else {
                        Toast.makeText(c, "No data", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


    }


    public void like() {
        fire.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                pd.dismiss();
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

                    if (restaurants.size() > 0) {
                        like_adapter = new ListLikeAdapter(c, restaurants);
                        rv.setAdapter(like_adapter);


                    } else {
                        Toast.makeText(c, "No data", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }

    public void point() {
        fire.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                pd.dismiss();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Points m = new Points();
                    m.setName(postSnapshot.getValue(Points.class).getName());
                    m.setPoint(postSnapshot.getValue(Points.class).getPoint());
                    points.add(m);

                    if (points.size() > 0) {
                        point_adapter = new ListPointAdapter(c, points);
                        rv.setAdapter(point_adapter);


                    } else {
                        Toast.makeText(c, "No data", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }


    public void getNightClub() {

        fire.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                pd.dismiss();
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

                    if (nightClubs.size() > 0) {
                        club_adapter = new ListClubAdapter(c, nightClubs);
                        rv.setAdapter(club_adapter);
                    } else {
                        Toast.makeText(c, "No data", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }

    public void clear_restaurant() {
        restaurants.clear();
    }


    public void refresh() {
        restaurants.clear();
        adapter = new ListRestaurantAdapter(c, restaurants);
        adapter.notifyDataSetChanged();

    }
}
