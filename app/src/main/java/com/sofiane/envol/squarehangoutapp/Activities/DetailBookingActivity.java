package com.sofiane.envol.squarehangoutapp.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.sofiane.envol.squarehangoutapp.Adapters.ListRestaurantAdapter;
import com.sofiane.envol.squarehangoutapp.Entities.Booking;
import com.sofiane.envol.squarehangoutapp.Entities.Restaurants;
import com.sofiane.envol.squarehangoutapp.Entities.Stat;
import com.sofiane.envol.squarehangoutapp.R;
import com.sofiane.envol.squarehangoutapp.Utils.ConnectivityReceiver;
import com.sofiane.envol.squarehangoutapp.Utils.MyApplication;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class DetailBookingActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener,
        NumberPicker.OnValueChangeListener {
    private Firebase ref_DetailBooking;
    private String id_usr;
    private ProgressDialog dialog;
    TextView id_txt, name_txt, date_txt, time_txt, seats_txt, name_user_txt, special_info;

    private RelativeLayout placeLayout, dateLayout, nbrLayout, infoLayout, timeLayout;
    private Booking booking_entity;
    private ImageView qrCodeImageview;
    private String QRcode;
    public final static int WIDTH = 500;
    private Bitmap bitmap = null;
    public String id_rest, name, id;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private long diffHours;
    private     Dialog d ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();


        checkConnection();
        if (bundle != null) {

            id_rest = bundle.getString("id_resto");
            name = bundle.getString("name");
        }


        SharedPreferences pref = getSharedPreferences("MyPrefsFile", 0);
        id_usr = pref.getString("Id_User", "def");

        dialog = new ProgressDialog(DetailBookingActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(getResources().getString(R.string.loading));
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.show();

        qrCodeImageview = (ImageView) findViewById(R.id.img_qr_code_image);
        qrCodeImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taptosave();
            }
        });

        placeLayout = (RelativeLayout) findViewById(R.id.place_layout);
        placeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        dateLayout = (RelativeLayout) findViewById(R.id.date_layout);
        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        DetailBookingActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                Calendar date = Calendar.getInstance();
                dpd.setMinDate(date);
                dpd.show(getFragmentManager(), "Datepickerdialog");

            }
        });

        timeLayout = (RelativeLayout) findViewById(R.id.time_layout);
        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final List<String> dates = new ArrayList<String>();
            Firebase    ref_time = new Firebase("https://hangoutenvol.firebaseio.com/Booking").child(id);

                ref_DetailBooking.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        booking_entity = snapshot.getValue(Booking.class);


                        Date d1, d2;
                        String dateStart;
                        SimpleDateFormat format = new SimpleDateFormat("HH:00");
                        Calendar now = Calendar.getInstance();


                        if (date_txt.getText().toString().equals("" + now.get(Calendar.DAY_OF_MONTH) + "/" +
                                (now.get(Calendar.MONTH) + 1) + "/" +
                                now.get(Calendar.YEAR))) {
                            now.add(Calendar.HOUR_OF_DAY, 1);
                            dateStart = format.format(now.getTime());
                        } else {
                            dateStart = booking_entity.getOpen();
                        }
                        String dateStop = booking_entity.getClose();
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

                        Calendar calendar = new GregorianCalendar();
                        calendar.setTime(d1);

                        for (int i = -1; i < diffHours; i++) {

                            Date result = calendar.getTime();
                            Format formatter = new SimpleDateFormat("HH:mm");
                            String s = formatter.format(result);
                            dates.add(s);
                            calendar.add(Calendar.HOUR, 1);
                        }

                               }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });





                    final AlertDialog alertDialog = new AlertDialog.Builder(DetailBookingActivity.this).create();
                    LayoutInflater inflater = getLayoutInflater();
                    View convertView = (View) inflater.inflate(R.layout.custom_time, null);
                    alertDialog.setView(convertView);
                    alertDialog.setTitle("List");
                    ListView lv = (ListView) convertView.findViewById(R.id.listView1);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(DetailBookingActivity.this, android.R.layout.simple_list_item_1, dates);
                    lv.setAdapter(adapter);
                    alertDialog.show();

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {
                            time_txt.setText(((TextView) v).getText().toString());
                            update();
                            alertDialog.dismiss();
                        }

                    });

                }
        });

        nbrLayout = (RelativeLayout) findViewById(R.id.seat_layout);
        nbrLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                d= new Dialog(DetailBookingActivity.this);
                d.setContentView(R.layout.personne_dialog);

                    String date_book = date_txt.getText().toString();
                    String[] parts = date_book.split("/");
                    String day = parts[0];
                    String month = parts[1];
                    String year = parts[2];
                    final Firebase ref = new Firebase("https://hangoutenvol.firebaseio.com/Stat/").child(id_rest).child(year).child(month);
                    ref.child(day).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Stat post = snapshot.getValue(Stat.class);
                                showSpinner(Integer.parseInt(post.getSeats())+Integer.parseInt(seats_txt.getText().toString()));

                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            System.out.println("The read failed: " + firebaseError.getMessage());
                        }
                    });
                d.show();
                }


        });
        infoLayout = (RelativeLayout) findViewById(R.id.info_layout);
        infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailBookingActivity.this);
                builder.setTitle(special_info.getText());
                final EditText input_info = new EditText(DetailBookingActivity.this);
                input_info.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
                builder.setView(input_info);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        special_info.setText(special_info.getText());
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
    }


    private void update() {

        ref_DetailBooking = new Firebase("https://hangoutenvol.firebaseio.com/Booking").child(id);

        ref_DetailBooking.child("date_book").setValue(date_txt.getText().toString());
        ref_DetailBooking.child("time_book").setValue(time_txt.getText().toString());
        ref_DetailBooking.child("seats").setValue(seats_txt.getText().toString());
        ref_DetailBooking.child("info").setValue(special_info.getText().toString());

    }

    private void getID(final String id, final String name, final String date, final String time, final String seats) {
        dialog.show();
        Thread t = new Thread(new Runnable() {
            public void run() {
                QRcode = id + "~#" + name + "~#" + date + "~#" + time + "~#" + seats + "~#" + id_usr + "~#" + id_rest;
                System.out.println("*_* " + QRcode);
                try {
                    synchronized (this) {

                        wait(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    bitmap = encodeAsBitmap(QRcode);
                                    qrCodeImageview.setImageBitmap(bitmap);
                                    dialog.dismiss();

                                } catch (WriterException e) {
                                    e.printStackTrace();
                                }
                            } // end of run method
                        });


                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            name = null;
            id = null;
        } else {
            name = extras.getString("name");
            id = extras.getString("id");
        }
        ref_DetailBooking = new Firebase("https://hangoutenvol.firebaseio.com/Booking").child(id);
        System.out.println("**" + id);


        ref_DetailBooking.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                booking_entity = snapshot.getValue(Booking.class);
                id_txt = (TextView) findViewById(R.id.id_book_txt);
                name_txt = (TextView) findViewById(R.id.resto_book_txt);
                date_txt = (TextView) findViewById(R.id.date_book_txt);
                time_txt = (TextView) findViewById(R.id.time_book_txt);
                seats_txt = (TextView) findViewById(R.id.seats_book_txt);
                name_user_txt = (TextView) findViewById(R.id.name_user_txt);
                special_info = (TextView) findViewById(R.id.info_book_txt);

                name_txt.setText(booking_entity.getResto_book());
                date_txt.setText(booking_entity.getDate_book());
                time_txt.setText(booking_entity.getTime_book());
                seats_txt.setText(booking_entity.getSeats());
                name_user_txt.setText(booking_entity.getName_user());
                special_info.setText(booking_entity.getInfo());

                getID(id, booking_entity.getResto_book(), booking_entity.getDate_book(), booking_entity.getTime_book(), booking_entity.getSeats());
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


    // this is method call from on create and return bitmap image of QRCode.
    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? getResources().getColor(R.color.colorPrimary) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
        return bitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainTabActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            case R.id.delete:

                new AlertDialog.Builder(this)
                        .setTitle(R.string.remove)
                        .setMessage(R.string.remove_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Delete_booking();

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            case R.id.google_calendar:
                setGoogleCalandar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainTabActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void setGoogleCalandar() {
        Calendar calendar = Calendar.getInstance();
        String d = booking_entity.getDate_book();
        String t = booking_entity.getTime_book();

        String[] parts = d.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        String[] time = t.split(":");
        int hour = Integer.parseInt(time[0]);
        int minut = Integer.parseInt(time[1]);

        // Here we set a start time of Tuesday the 17th, 6pm
        calendar.set(year, month, day, hour, minut, 0);
        calendar.setTimeZone(TimeZone.getDefault());

        long start = calendar.getTimeInMillis();
        // add three hours in milliseconds to get end time of 9pm
        long end = calendar.getTimeInMillis() + 2 * 60 * 60 * 1000;

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .setType("vnd.android.cursor.item/event")
                .putExtra(CalendarContract.Events.TITLE, "Booking for " + booking_entity.getResto_book() + "")
                .putExtra(CalendarContract.Events.DTSTART, calendar.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end)
                .putExtra(CalendarContract.Events.HAS_ALARM, 1)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        startActivity(intent);
    }

    public void Delete_booking() {

        Firebase mFirebaseRef = new Firebase("https://hangoutenvol.firebaseio.com/Booking").child(id).child("deleted");
        mFirebaseRef.setValue("yes");

        String[] parts = date_txt.getText().toString().split("/");
        String day = parts[0];
        String month = parts[1];
        String year = parts[2];

        final Firebase downBookref = new Firebase("https://hangoutenvol.firebaseio.com/Stat/" + id_rest + "/" + year + "/" + month + "/" + day + "/sum_booking");
        downBookref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    mutableData.setValue(0);
                } else {
                    long size = (Long) mutableData.getValue() - 1;
                    mutableData.setValue(size);
                }
                return Transaction.success(mutableData);
            }

            public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                if (firebaseError != null) {
                    //             Toast.makeText(DetailBookingActivity.this, "Firebase counter increment failed.", Toast.LENGTH_SHORT).show();
                } else {
                    //           Toast.makeText(DetailBookingActivity.this, "Firebase counter increment succeeded.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Firebase downSeatref = new Firebase("https://hangoutenvol.firebaseio.com/Stat/" + id_rest + "/" + year + "/" + month + "/" + day + "/sum_seats");
        downSeatref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    mutableData.setValue(0);
                } else {
                    long size = (Long) mutableData.getValue() - Integer.parseInt(seats_txt.getText().toString());
                    mutableData.setValue(size);
                }
                return Transaction.success(mutableData);
            }

            public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                if (firebaseError != null) {
                    //             Toast.makeText(DetailBookingActivity.this, "Firebase counter increment failed.", Toast.LENGTH_SHORT).show();
                } else {
                    //           Toast.makeText(DetailBookingActivity.this, "Firebase counter increment succeeded.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Intent intent = new Intent(this, ListBookingActivity.class);
        startActivity(intent);
    }

    public void taptosave() {
        qrCodeImageview.buildDrawingCache();
        Bitmap bm = qrCodeImageview.getDrawingCache();

        OutputStream fOut = null;
        Uri outputFileUri;
        try {
            File root = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "SquareHangout" + File.separator);

            root.mkdirs();
            File sdImageMainDirectory = new File(root, booking_entity.getResto_book() + "_" + booking_entity.getName_user() + ".jpg");
            outputFileUri = Uri.fromFile(sdImageMainDirectory);
            fOut = new FileOutputStream(sdImageMainDirectory);
            //        Toast.makeText(this, "" + root,
            //              Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //    Toast.makeText(this, "Error occured. Please try again later.",
            //           Toast.LENGTH_SHORT).show();
        }

        try {
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
        }

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
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        Log.i("value is", "" + newVal);

    }

    public void showSpinner(final int max) {


        Button b1 = (Button) d.findViewById(R.id.buttonSet);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(max);
        np.setMinValue(1);
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(this);
        String date = date_txt.getText().toString();
        String[] parts = date.split("/");
        final String day = parts[0];
        final String month = parts[1];
        final String year = parts[2];
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seats_txt.setText(String.valueOf(np.getValue()));

                final Firebase seat_Ref = new Firebase("https://hangoutenvol.firebaseio.com/Stat/" + id_rest + "/" + year + "/" + month + "/" + day );
                seat_Ref.child("seats").setValue(max-np.getValue());

                update();
                d.dismiss();
            }
        });

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String date = "" + dayOfMonth + "/" + (++monthOfYear) + "/" + year;

        date_txt.setText(date);
        update();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        String time = "" + hourString + ":" + minuteString + "";
      //  timeButton.setText(time);
    }
    @Override
    protected void onResume() {


        super.onResume();
        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");


        if (dpd != null) dpd.setOnDateSetListener(this);
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
