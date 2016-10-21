package com.sofiane.envol.squarehangoutapp.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;
import com.sofiane.envol.squarehangoutapp.Entities.Stat;
import com.sofiane.envol.squarehangoutapp.R;
import com.sofiane.envol.squarehangoutapp.Utils.ConnectivityReceiver;
import com.sofiane.envol.squarehangoutapp.Utils.MyApplication;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CreateBookingActivity extends AppCompatActivity implements
        ConnectivityReceiver.ConnectivityReceiverListener,
        View.OnClickListener,
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener,
        NumberPicker.OnValueChangeListener {
    private Button dateButton, timeButton, persButton, validateButton;
    private String id_resto,
            name_value,
            open_value,
            close_value,
            nbr_value,
            type_value;
    private long diffHours;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_booking);
        checkConnection();

        dateButton = (Button) findViewById(R.id.date_button);
        timeButton = (Button) findViewById(R.id.time_button);
        persButton = (Button) findViewById(R.id.pers_button);
        validateButton = (Button) findViewById(R.id.valid_button);

        //GET Data from DetailRestaurantActivity or DetailClubActivity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type_value = bundle.getString("type");
            id_resto = bundle.getString("id_resto");
            name_value = bundle.getString("name_resto");
            open_value = bundle.getString("open_time");
            close_value = bundle.getString("close_time");
            nbr_value = bundle.getString("nbrBooking_resto");

        }
        //    Toast.makeText(CreateBookingActivity.this, "* "+id_resto, Toast.LENGTH_SHORT).show();
        // Show a datepicker when the dateButton is clicked
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        CreateBookingActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                Calendar date = Calendar.getInstance();
                dpd.setMinDate(date);
                dpd.show(getFragmentManager(), "Datepickerdialog");


            }
        });

        // Show a timepicker when the timeButton is clicked
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date d1, d2;
                String dateStart;
                SimpleDateFormat format = new SimpleDateFormat("HH:00");
                Calendar now = Calendar.getInstance();
                if (dateButton.getText().toString().equals("")) {
                    Toast.makeText(CreateBookingActivity.this, "Please , choose the booking date before !", Toast.LENGTH_SHORT).show();
                } else {

                    if (dateButton.getText().toString().equals("" + now.get(Calendar.DAY_OF_MONTH) + "/" +
                            (now.get(Calendar.MONTH) + 1) + "/" +
                            now.get(Calendar.YEAR))) {
                        now.add(Calendar.HOUR_OF_DAY, 1);
                        dateStart = format.format(now.getTime());
                    } else {
                        dateStart = open_value;
                    }
                    String dateStop = close_value;
                    d1 = null;
                    d2 = null;
                    try {
                        Date max = format.parse("24:00");
                        Date min = format.parse("00:00");
                        d1 = format.parse(dateStart);
                        d2 = format.parse(dateStop);
                        diffHours = TimeUnit.MILLISECONDS.toHours((max.getTime() - d1.getTime()) + (d2.getTime() - min.getTime()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    List<String> dates = new ArrayList<String>();
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(d1);

                    for (int i = -1; i < diffHours; i++) {

                        Date result = calendar.getTime();
                        Format formatter = new SimpleDateFormat("HH:mm");
                        String s = formatter.format(result);
                        dates.add(s);
                        calendar.add(Calendar.HOUR, 1);
                    }


                    final AlertDialog alertDialog = new AlertDialog.Builder(CreateBookingActivity.this).create();
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.custom_time, null);
                    alertDialog.setView(convertView);
                    alertDialog.setTitle("List");
                    ListView lv = (ListView) convertView.findViewById(R.id.listView1);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateBookingActivity.this, android.R.layout.simple_list_item_1, dates);
                    lv.setAdapter(adapter);
                    alertDialog.show();

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {
                            timeButton.setText(((TextView) v).getText().toString());
                            //    Toast.makeText(CreateBookingActivity.this, "" + ((TextView) v).getText().toString(), Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }

                    });

                }

            }
        });

        persButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dateButton.getText().toString().equals("")) {
                    Toast.makeText(CreateBookingActivity.this, "Please , choose the booking date before !", Toast.LENGTH_SHORT).show();
                } else {
                    String date_book = dateButton.getText().toString();
                    String[] parts = date_book.split("/");
                    String day = parts[0];
                    String month = parts[1];
                    String year = parts[2];
                    final Firebase ref = new Firebase("https://hangoutenvol.firebaseio.com/Stat/").child(id_resto).child(year).child(month);
                    ref.child(day).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Stat post = snapshot.getValue(Stat.class);
                                showSpinner(Integer.parseInt(post.getSeats()));
                            } else {
                                showSpinner(Integer.parseInt(nbr_value));
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            System.out.println("The read failed: " + firebaseError.getMessage());
                        }
                    });
                }
            }
        });
        validateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ((!dateButton.getText().toString().equals("")) && (!timeButton.getText().toString().equals("")) && (!persButton.getText().toString().equals(""))) {
                    SharedPreferences pref = getSharedPreferences("MyPrefsFile", 0);
                    String id_usr = pref.getString("Id_User", "def");
                    String name_user = pref.getString("Name_User", "def");

                    Firebase mFirebaseRef = new Firebase("https://hangoutenvol.firebaseio.com/Booking");

                    Map<String, String> book = new HashMap<String, String>();
                    book.put("date_book", dateButton.getText().toString());
                    book.put("deleted", "no");
                    book.put("info", "");
                    book.put("name_user", name_user);
                    book.put("resto_book", name_value);
                    book.put("seats", persButton.getText().toString());
                    book.put("time_book", timeButton.getText().toString());
                    book.put("idPlace", id_resto);
                    book.put("idUser", id_usr);

                    book.put("open", open_value);
                    book.put("close", close_value);

                    Firebase x = mFirebaseRef.push();
                    book.put("idBooking", x.getKey());
                    x.setValue(book);

                    addBookingStat(Integer.parseInt(persButton.getText().toString()));
                    Intent intent = new Intent(CreateBookingActivity.this, DetailBookingActivity.class);
                    intent.putExtra("name", name_value);
                    intent.putExtra("id_resto", id_resto);
                    intent.putExtra("id", x.getKey());

                    startActivity(intent);
                } else {
                    new AlertDialog.Builder(CreateBookingActivity.this)
                            .setTitle("Error !")
                            .setMessage("Please , complete the booking formula")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        Log.i("value is", "" + newVal);

    }

    public void showSpinner(int max) {

        final Dialog d = new Dialog(CreateBookingActivity.this);
        // d.setTitle("Number of personnes");
        d.setContentView(R.layout.personne_dialog);
        Button b1 = (Button) d.findViewById(R.id.buttonSet);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(max);
        np.setMinValue(1);
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                persButton.setText(String.valueOf(np.getValue()));
                d.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");


        if (dpd != null) dpd.setOnDateSetListener(this);
        MyApplication.getInstance().setConnectivityListener(this);
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String date = "" + dayOfMonth + "/" + (++monthOfYear) + "/" + year;
        dateButton.setText(date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        String time = "" + hourString + ":" + minuteString + "";
        timeButton.setText(time);
    }

    public void addBookingStat(final int seat) {
        String date = dateButton.getText().toString();
        String[] parts = date.split("/");
        String day = parts[0];
        String month = parts[1];
        String year = parts[2];


        final Firebase validateRef = new Firebase("https://hangoutenvol.firebaseio.com/Stat/" + id_resto + "/" + year + "/" + month + "/" + day );

        validateRef.child("seats").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    mutableData.setValue((Integer.parseInt(nbr_value)-seat));

                } else {
                    long size = (Long) mutableData.getValue() -seat;
                    mutableData.setValue(size);
                }
                return Transaction.success(mutableData);
            }

            public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                if (firebaseError != null) {
                    //           Toast.makeText(CreateBookingActivity.this, "Firebase counter increment failed.", Toast.LENGTH_SHORT).show();
                } else {
                    //            Toast.makeText(CreateBookingActivity.this, "Firebase counter increment succeeded.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        final Firebase upBookref = new Firebase("https://hangoutenvol.firebaseio.com/Stat/" + id_resto + "/" + year + "/" + month + "/" + day + "/sum_booking");
        upBookref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    mutableData.setValue(1);
                } else {
                    long size = (Long) mutableData.getValue() + 1;
                    mutableData.setValue(size);
                }
                return Transaction.success(mutableData);
            }

            public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                if (firebaseError != null) {
                    //           Toast.makeText(CreateBookingActivity.this, "Firebase counter increment failed.", Toast.LENGTH_SHORT).show();
                } else {
                    //            Toast.makeText(CreateBookingActivity.this, "Firebase counter increment succeeded.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final Firebase upSeatref = new Firebase("https://hangoutenvol.firebaseio.com/Stat/" + id_resto + "/" + year + "/" + month + "/" + day + "/sum_seats");
        upSeatref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    mutableData.setValue(seat);
                } else {
                    long size = (Long) mutableData.getValue() + seat;
                    mutableData.setValue(size);
                }
                return Transaction.success(mutableData);
            }

            public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                if (firebaseError != null) {
                    //          Toast.makeText(CreateBookingActivity.this, "Firebase counter increment failed.", Toast.LENGTH_SHORT).show();
                } else {
                    //        Toast.makeText(CreateBookingActivity.this, "Firebase counter increment succeeded.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Firebase valdref = new Firebase("https://hangoutenvol.firebaseio.com/Stat/" + id_resto + "/" + year + "/" + month + "/" + day + "/validate");
        valdref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    mutableData.setValue(0);
                } else {
                    long size = (Long) mutableData.getValue() + 0;
                    mutableData.setValue(size);
                }
                return Transaction.success(mutableData);
            }

            public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                if (firebaseError != null) {
                    //      Toast.makeText(CreateBookingActivity.this, "Firebase counter increment failed.", Toast.LENGTH_SHORT).show();
                } else {
                    //     Toast.makeText(CreateBookingActivity.this, "Firebase counter increment succeeded.", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
