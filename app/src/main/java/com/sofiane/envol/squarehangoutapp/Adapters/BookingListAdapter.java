package com.sofiane.envol.squarehangoutapp.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.sofiane.envol.squarehangoutapp.Entities.Booking;
import com.sofiane.envol.squarehangoutapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BookingListAdapter extends FirebaseListAdapter<Booking> {

    public BookingListAdapter(Query ref, Activity activity, int layout) {
        super(ref, Booking.class, layout, activity);

    }

    @Override
    protected void populateView(View view, Booking m) {
        String author = m.getResto_book();
        TextView nameText = (TextView) view.findViewById(R.id.name_place_book);
        nameText.setText(author);
        ((TextView) view.findViewById(R.id.date_place_book)).setText(m.getDate_book());

        ((TextView) view.findViewById(R.id.time_place_book)).setText(" " + m.getTime_book());
        RelativeLayout r = (RelativeLayout) view.findViewById(R.id.relative);


        DateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
        Date date1 = new Date();
        System.out.println("***" + date1);
        Date date2;
        Firebase mFirebaseRef = new Firebase("https://hangoutenvol.firebaseio.com/Booking").child(m.getIdBooking()).child("deleted");
        try {
            date2 = dateFormat.parse(m.getDate_book());
            System.out.println("---" + date2);
            if (date1.before(date2) || dateFormat.format(date2).equals(dateFormat.format(date1))) {
                System.out.println("GREEN");
                mFirebaseRef.setValue("no");
            } else {
                System.out.println("RED");
                mFirebaseRef.setValue("yes");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (m.getDeleted().equals("yes")) {
            r.setBackgroundColor(Color.parseColor("#F44336"));
        } else {
            r.setBackgroundColor(Color.parseColor("#8BC34A"));
        }

    }

}
