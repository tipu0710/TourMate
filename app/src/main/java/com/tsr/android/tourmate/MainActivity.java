package com.tsr.android.tourmate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText eventName,destination,budget;
    private Button createEvent,showEventBtn,fromDate,toDate;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;



    private long fromDay,fromMounth,fromYear,toDay,toMounth,toYear,currentDay,currentMounth;
    private String muserName;
    private String mUid= "";
    private boolean status;

    public static final int RC_SIGN_IN = 1;
    private String keyValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        destination= (EditText) findViewById(R.id.destination);
        budget= (EditText) findViewById(R.id.budget);
        fromDate= (Button) findViewById(R.id.from_date);
        toDate= (Button) findViewById(R.id.to_date);
        createEvent = (Button)findViewById(R.id.create_event);
        showEventBtn = (Button)findViewById(R.id.show_event);
        eventName = (EditText) findViewById(R.id.event_name);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        Intent intent =getIntent();
        status = intent.getBooleanExtra("status",false);

        if (status){
            keyValue = intent.getStringExtra("key");
            mUid = intent.getStringExtra("uid");
            /*fromDay= intent.getIntExtra("fromDay",-1);
            fromMounth= intent.getIntExtra("fromMounth",-1);
            fromYear= intent.getIntExtra("fromYear",-1);
            toDay= intent.getIntExtra("toDay",-1);
            toMounth= intent.getIntExtra("toMounth",-1);
            toYear= intent.getIntExtra("toYear",-1);*/
            mDatabaseReference = mFirebaseDatabase.getReference().child(mUid);
            Toast.makeText(this, mUid, Toast.LENGTH_SHORT).show();
            mDatabaseReference.child(keyValue).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String s = (String) dataSnapshot.child("eventName").getValue();
                    eventName.setText(s);
                    s=(String) dataSnapshot.child("budget").getValue();
                    budget.setText(s);
                    s= (String) dataSnapshot.child("destination").getValue();
                    destination.setText(s);
                    HashMap<Long,Object> map = new HashMap<>();
                    map = (HashMap<Long, Object>) dataSnapshot.child("fromDateFinder").getValue();
                    fromDay= (long) map.get("day");
                    fromMounth= (long) map.get("mounth");
                    fromYear= (long) map.get("year");

                    map = (HashMap<Long, Object>) dataSnapshot.child("toDateFinder").getValue();
                    toDay= (long) map.get("day");
                    toMounth= (long) map.get("mounth");
                    toYear= (long) map.get("year");

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            createEvent.setText("Update");
            showEventBtn.setVisibility(View.GONE);
            fromDate.setText(""+String.valueOf(fromDay)+"/"+String.valueOf(fromMounth)+"/"+String.valueOf(fromYear));

            toDate.setText(""+String.valueOf(toDay)+"/"+String.valueOf(toMounth)+"/"+String.valueOf(toYear));


            Calendar calendar1 = Calendar.getInstance(Locale.getDefault());
            if (fromYear>=calendar1.get(Calendar.YEAR)){
                if (fromMounth==(calendar1.get(Calendar.MONTH)+1)){
                    if (fromDay>=calendar1.get(Calendar.DAY_OF_MONTH)){
                        fromDate.setEnabled(true);
                    }else {
                        fromDate.setEnabled(false);
                    }
                }else if (fromMounth>(calendar1.get(Calendar.MONTH)+1)){
                    fromDate.setEnabled(true);
                }else {
                    fromDate.setEnabled(false);
                }
            }else {
                fromDate.setEnabled(false);
            }

            if (toYear>=calendar1.get(Calendar.YEAR)){
                if (toMounth==(calendar1.get(Calendar.MONTH)+1)){
                    if (toDay>=calendar1.get(Calendar.DAY_OF_MONTH)){
                        Toast.makeText(MainActivity.this, ""+toYear, Toast.LENGTH_SHORT).show();
                        toDate.setEnabled(true);
                    }else {
                        toDate.setEnabled(false);
                    }
                }else if (toMounth>(calendar1.get(Calendar.MONTH)+1)){
                    toDate.setEnabled(true);
                }else {
                    toDate.setEnabled(false);
                }
            }else {
                toDate.setEnabled(false);
            }
        }


        fromDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance(Locale.getDefault());

                    int mYear = calendar.get(Calendar.YEAR);
                    fromYear= mYear;
                    int mMonth = calendar.get(Calendar.MONTH);
                    fromMounth= mMonth;
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                    fromDay = mDay;
                    DatePickerDialog dpd = new DatePickerDialog(MainActivity.this,dateListener,mYear,mMonth,mDay);
                    dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    dpd.show();

                }

                private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fromYear = year;
                        fromMounth = month+1;
                        fromDay = dayOfMonth;
                        fromDate.setText(""+dayOfMonth+"/"+fromMounth+"/"+year);
                    }
                };
            });





        toDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Calendar calendar = Calendar.getInstance(Locale.getDefault());

                    int mYear = calendar.get(Calendar.YEAR);
                    toYear= mYear;
                    int mMonth = calendar.get(Calendar.MONTH);
                    toMounth= mMonth;
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                    toDay = mDay;
                    DatePickerDialog dpd = new DatePickerDialog(MainActivity.this,dateListener,mYear,mMonth,mDay);
                    dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    dpd.show();

                }

                private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        toYear = year;
                        toMounth = month+1;
                        toDay = dayOfMonth;
                        toDate.setText(""+dayOfMonth+"/"+toMounth+"/"+year);
                    }
                };
            });



        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null){
                    muserName = user.getDisplayName();
                    mUid = user.getUid();

                }else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(
                                            AuthUI.EMAIL_PROVIDER,
                                            AuthUI.GOOGLE_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };





        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (destination.getText().toString().isEmpty()){
                    destination.setError(getString(R.string.error_msg));
                }else if (budget.getText().toString().isEmpty()) {
                    budget.setError(getString(R.string.error_msg));
                }else if (eventName.getText().toString().isEmpty()){
                    eventName.setError(getString(R.string.error_msg));
                }else if (currentDay==toDay && currentMounth == toMounth){
                    toDate.setError("Wrong To Date");
                }else if (fromMounth>= toMounth && fromDay>=toDay){
                    toDate.setError("Wrong To Date");
                }
                else {
                    DateFinder fromDateFinder = new DateFinder(fromYear,fromMounth,fromDay);
                    DateFinder toDateFinder = new DateFinder(toYear,toMounth,toDay);
                    EventList eventList = new EventList(eventName.getText().toString(),destination.getText().toString(),budget.getText().toString(),fromDateFinder,toDateFinder);
                    if (status){
                        mDatabaseReference = mFirebaseDatabase.getReference().child(mUid);
                        mDatabaseReference.child(keyValue).setValue(eventList);
                        status = false;
                        startActivity(new Intent(MainActivity.this,ShowEvent.class));
                    }else {
                        mDatabaseReference = mFirebaseDatabase.getReference().child(mUid);
                        mDatabaseReference.push().setValue(eventList);
                    }


                    Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            if(resultCode==RESULT_OK){
                Toast.makeText(this,"Signed in!",Toast.LENGTH_SHORT).show();
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this,"Signed in canceled",Toast.LENGTH_SHORT).show();
                finish();
            }}

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        /*LocalBroadcastManager.getInstance(this).unregisterReceiver(keyReceiver);*/
    }



    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        /*IntentFilter intentFilter = new IntentFilter(ShowEvent.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(keyReceiver,intentFilter);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showEvent(View view) {
        Intent intent = new Intent(this,ShowEvent.class);
        intent.putExtra("uid",mUid);
        startActivity(intent);
    }


    /*private BroadcastReceiver keyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(MainActivity.this, "Called", Toast.LENGTH_SHORT).show();
            keyValue = intent.getStringExtra("key");
            status = intent.getBooleanExtra("status",false);
            fromDay= intent.getIntExtra("fromDay",-1);
            fromMounth= intent.getIntExtra("fromMounth",-1);
            fromYear= intent.getIntExtra("fromYear",-1);
            toDay= intent.getIntExtra("toDay",-1);
            toMounth= intent.getIntExtra("toMounth",-1);
            toYear= intent.getIntExtra("toYear",-1);
        }
    };*/

}

